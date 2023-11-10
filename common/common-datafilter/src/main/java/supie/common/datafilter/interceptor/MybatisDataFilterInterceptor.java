package supie.common.datafilter.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.annotation.*;
import supie.common.core.cache.CacheConfig;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.exception.NoDataPermException;
import supie.common.core.object.GlobalThreadLocal;
import supie.common.core.object.TokenData;
import supie.common.core.util.ApplicationContextHolder;
import supie.common.core.util.ContextUtil;
import supie.common.core.util.MyModelUtil;
import supie.common.core.util.RedisKeyUtil;
import supie.common.core.constant.DataPermRuleType;
import supie.common.datafilter.config.DataFilterProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.*;

/**
 * Mybatis拦截器。目前用于数据权限的统一拦截和注入处理。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Slf4j
@Component
public class MybatisDataFilterInterceptor implements Interceptor {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DataFilterProperties properties;
    @Resource(name = "caffeineCacheManager")
    private CacheManager cacheManager;

    /**
     * 对象缓存。由于Set是排序后的，因此在查找排除方法名称时效率更高。
     * 在应用服务启动的监听器中(LoadDataPermMapperListener)，会调用当前对象的(loadMappersWithDataPerm)方法，加载缓存。
     */
    private final Map<String, ModelDataPermInfo> cachedDataPermMap = MapUtil.newHashMap();
    /**
     * 租户租户对象缓存。
     */
    private final Map<String, ModelTenantInfo> cachedTenantMap = MapUtil.newHashMap();

    /**
     * 预先加载与数据过滤相关的数据到缓存，该函数会在(LoadDataFilterInfoListener)监听器中调用。
     */
    public void loadInfoWithDataFilter() {
        @SuppressWarnings("all")
        Map<String, BaseDaoMapper> mapperMap =
                ApplicationContextHolder.getApplicationContext().getBeansOfType(BaseDaoMapper.class);
        for (BaseDaoMapper<?> mapperProxy : mapperMap.values()) {
            // 优先处理jdk的代理
            Object proxy = ReflectUtil.getFieldValue(mapperProxy, "h");
            // 如果不是jdk的代理，再看看cjlib的代理。
            if (proxy == null) {
                proxy = ReflectUtil.getFieldValue(mapperProxy, "CGLIB$CALLBACK_0");
            }
            Class<?> mapperClass = (Class<?>) ReflectUtil.getFieldValue(proxy, "mapperInterface");
            if (BooleanUtil.isTrue(properties.getEnabledTenantFilter())) {
                loadTenantFilterData(mapperClass);
            }
            if (BooleanUtil.isTrue(properties.getEnabledDataPermFilter())) {
                EnableDataPerm rule = mapperClass.getAnnotation(EnableDataPerm.class);
                if (rule != null) {
                    loadDataPermFilterRules(mapperClass, rule);
                }
            }
        }
    }

    private void loadTenantFilterData(Class<?> mapperClass) {
        Class<?> modelClass = (Class<?>) ((ParameterizedType)
                mapperClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        Field[] fields = ReflectUtil.getFields(modelClass);
        for (Field field : fields) {
            if (field.getAnnotation(TenantFilterColumn.class) != null) {
                ModelTenantInfo tenantInfo = new ModelTenantInfo();
                tenantInfo.setModelName(modelClass.getSimpleName());
                tenantInfo.setTableName(modelClass.getAnnotation(TableName.class).value());
                tenantInfo.setFieldName(field.getName());
                tenantInfo.setColumnName(MyModelUtil.mapToColumnName(field, modelClass));
                // 判断当前dao中是否包括不需要自动注入租户Id过滤的方法。
                DisableTenantFilter disableTenantFilter = mapperClass.getAnnotation(DisableTenantFilter.class);
                if (disableTenantFilter != null) {
                    // 这里开始获取当前Mapper已经声明的的SqlId中，有哪些是需要排除在外的。
                    // 排除在外的将不进行数据过滤。
                    Set<String> excludeMethodNameSet = new HashSet<>();
                    for (String excludeName : disableTenantFilter.includeMethodName()) {
                        excludeMethodNameSet.add(excludeName);
                        // 这里是给pagehelper中，分页查询先获取数据总量的查询。
                        excludeMethodNameSet.add(excludeName + "_COUNT");
                    }
                    tenantInfo.setExcludeMethodNameSet(excludeMethodNameSet);
                }
                cachedTenantMap.put(mapperClass.getName(), tenantInfo);
                break;
            }
        }
    }

    private void loadDataPermFilterRules(Class<?> mapperClass, EnableDataPerm rule) {
        String sysDataPermMapperName = "SysDataPermMapper";
        // 由于给数据权限Mapper添加@EnableDataPerm，将会导致无限递归，因此这里检测到之后，
        // 会在系统启动加载监听器的时候，及时抛出异常。
        if (StrUtil.equals(sysDataPermMapperName, mapperClass.getSimpleName())) {
            throw new IllegalStateException("Add @EnableDataPerm annotation to SysDataPermMapper is ILLEGAL!");
        }
        // 这里开始获取当前Mapper已经声明的的SqlId中，有哪些是需要排除在外的。
        // 排除在外的将不进行数据过滤。
        Set<String> excludeMethodNameSet = null;
        String[] excludes = rule.excluseMethodName();
        if (excludes.length > 0) {
            excludeMethodNameSet = new HashSet<>();
            for (String excludeName : excludes) {
                excludeMethodNameSet.add(excludeName);
                // 这里是给pagehelper中，分页查询先获取数据总量的查询。
                excludeMethodNameSet.add(excludeName + "_COUNT");
            }
        }
        // 获取Mapper关联的主表信息，包括表名，user过滤字段名和dept过滤字段名。
        Class<?> modelClazz = (Class<?>)
                ((ParameterizedType) mapperClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        Field[] fields = ReflectUtil.getFields(modelClazz);
        Field userFilterField = null;
        Field deptFilterField = null;
        for (Field field : fields) {
            if (null != field.getAnnotation(UserFilterColumn.class)) {
                userFilterField = field;
            }
            if (null != field.getAnnotation(DeptFilterColumn.class)) {
                deptFilterField = field;
            }
            if (userFilterField != null && deptFilterField != null) {
                break;
            }
        }
        // 通过注解解析与Mapper关联的Model，并获取与数据权限关联的信息，并将结果缓存。
        ModelDataPermInfo info = new ModelDataPermInfo();
        info.setMainTableName(MyModelUtil.mapToTableName(modelClazz));
        info.setMustIncludeUserRule(rule.mustIncludeUserRule());
        info.setExcludeMethodNameSet(excludeMethodNameSet);
        if (userFilterField != null) {
            info.setUserFilterColumn(MyModelUtil.mapToColumnName(userFilterField, modelClazz));
        }
        if (deptFilterField != null) {
            info.setDeptFilterColumn(MyModelUtil.mapToColumnName(deptFilterField, modelClazz));
        }
        cachedDataPermMap.put(mapperClass.getName(), info);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 判断当前线程本地存储中，业务操作是否禁用了数据权限过滤，如果禁用，则不进行后续的数据过滤处理了。
        if (!GlobalThreadLocal.enabledDataFilter()
                && BooleanUtil.isFalse(properties.getEnabledTenantFilter())) {
            return invocation.proceed();
        }
        // 只有在HttpServletRequest场景下，该拦截器才起作用，对于系统级别的预加载数据不会应用数据权限。
        if (!ContextUtil.hasRequestContext()) {
            return invocation.proceed();
        }
        // 没有登录的用户，不会参与租户过滤，如果需要过滤的，自己在代码中手动实现
        // 通常对于无需登录的白名单url，也无需过滤了。
        // 另外就是登录接口中，获取菜单列表的接口，由于尚未登录，没有TokenData，所以这个接口我们手动加入了该条件。
        if (TokenData.takeFromRequest() == null) {
            return invocation.proceed();
        }
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate =
                (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
        // 通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
        MappedStatement mappedStatement =
                (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
        SqlCommandType commandType = mappedStatement.getSqlCommandType();
        // 对于INSERT语句，我们不进行任何数据过滤。
        if (commandType == SqlCommandType.INSERT) {
            return invocation.proceed();
        }
        String sqlId = mappedStatement.getId();
        int pos = StrUtil.lastIndexOfIgnoreCase(sqlId, ".");
        String className = StrUtil.sub(sqlId, 0, pos);
        String methodName = StrUtil.subSuf(sqlId, pos + 1);
        // 先进行租户过滤条件的处理，再将解析并处理后的SQL Statement交给下一步的数据权限过滤去处理。
        // 这样做的目的主要是为了减少一次SQL解析的过程，因为这是高频操作，所以要尽量去优化。
        Statement statement = null;
        if (BooleanUtil.isTrue(properties.getEnabledTenantFilter())) {
            statement = this.processTenantFilter(className, methodName, delegate.getBoundSql(), commandType);
        }
        // 处理数据权限过滤。
        if (GlobalThreadLocal.enabledDataFilter()
                && BooleanUtil.isTrue(properties.getEnabledDataPermFilter())) {
            this.processDataPermFilter(className, methodName, delegate.getBoundSql(), commandType, statement, sqlId);
        }
        return invocation.proceed();
    }

    private Statement processTenantFilter(
            String className, String methodName, BoundSql boundSql, SqlCommandType commandType) throws JSQLParserException {
        ModelTenantInfo info = cachedTenantMap.get(className);
        if (info == null || CollUtil.contains(info.getExcludeMethodNameSet(), methodName)) {
            return null;
        }
        String sql = boundSql.getSql();
        Statement statement = CCJSqlParserUtil.parse(sql);
        StringBuilder filterBuilder = new StringBuilder(64);
        filterBuilder.append(info.tableName).append(".")
                .append(info.columnName)
                .append("=")
                .append(TokenData.takeFromRequest().getTenantId());
        String dataFilter = filterBuilder.toString();
        if (commandType == SqlCommandType.UPDATE) {
            Update update = (Update) statement;
            this.buildWhereClause(update, dataFilter);
        } else if (commandType == SqlCommandType.DELETE) {
            Delete delete = (Delete) statement;
            this.buildWhereClause(delete, dataFilter);
        } else {
            Select select = (Select) statement;
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            FromItem fromItem = selectBody.getFromItem();
            if (fromItem != null) {
                PlainSelect subSelect = null;
                if (fromItem instanceof SubSelect) {
                    subSelect = (PlainSelect) ((SubSelect) fromItem).getSelectBody();
                }
                if (subSelect != null) {
                    dataFilter = replaceTableAlias(info.getTableName(), subSelect, dataFilter);
                    buildWhereClause(subSelect, dataFilter);
                } else {
                    dataFilter = replaceTableAlias(info.getTableName(), selectBody, dataFilter);
                    buildWhereClause(selectBody, dataFilter);
                }
            }
        }
        log.info("Tenant Filter Where Clause [{}]", dataFilter);
        ReflectUtil.setFieldValue(boundSql, "sql", statement.toString());
        return statement;
    }

    private void processDataPermFilter(
            String className, String methodName, BoundSql boundSql, SqlCommandType commandType, Statement statement, String sqlId)
            throws JSQLParserException {
        // 判断当前线程本地存储中，业务操作是否禁用了数据权限过滤，如果禁用，则不进行后续的数据过滤处理了。
        // 数据过滤权限中，INSERT不过滤。如果是管理员则不参与数据权限的数据过滤，显示全部数据。
        TokenData tokenData = TokenData.takeFromRequest();
        if (Boolean.TRUE.equals(tokenData.getIsAdmin())) {
            return;
        }
        ModelDataPermInfo info = cachedDataPermMap.get(className);
        // 再次查找当前方法是否为排除方法，如果不是，就参与数据权限注入过滤。
        if (info == null || CollUtil.contains(info.getExcludeMethodNameSet(), methodName)) {
            return;
        }
        String dataPermSessionKey = RedisKeyUtil.makeSessionDataPermIdKey(tokenData.getSessionId());
        Object cachedData = this.getCachedData(dataPermSessionKey);
        if (cachedData == null) {
            throw new NoDataPermException(StrFormatter.format(
                    "No Related DataPerm found for SQL_ID [{}] from Cache.", sqlId));
        }
        JSONObject allMenuDataPermMap = cachedData instanceof JSONObject
                ? (JSONObject) cachedData : JSON.parseObject(cachedData.toString());
        JSONObject menuDataPermMap = this.getAndVerifyMenuDataPerm(allMenuDataPermMap, sqlId);
        Map<Integer, String> dataPermMap = new HashMap<>(8);
        for (Map.Entry<String, Object> entry : menuDataPermMap.entrySet()) {
            dataPermMap.put(Integer.valueOf(entry.getKey()), entry.getValue().toString());
        }
        if (MapUtil.isEmpty(dataPermMap)) {
            throw new NoDataPermException(StrFormatter.format(
                    "No Related DataPerm found for SQL_ID [{}].", sqlId));
        }
        if (dataPermMap.containsKey(DataPermRuleType.TYPE_ALL)) {
            return;
        }
        // 如果当前过滤注解中mustIncludeUserRule参数为true，同时当前用户的数据权限中，不包含TYPE_USER_ONLY，
        // 这里就需要自动添加该数据权限。
        if (info.getMustIncludeUserRule()
                && !dataPermMap.containsKey(DataPermRuleType.TYPE_USER_ONLY)) {
            dataPermMap.put(DataPermRuleType.TYPE_USER_ONLY, null);
        }
        this.processDataPerm(info, dataPermMap, boundSql, commandType, statement);
    }

    private JSONObject getAndVerifyMenuDataPerm(JSONObject allMenuDataPermMap, String sqlId) {
        String menuId = ContextUtil.getHttpRequest().getHeader(ApplicationConstant.HTTP_HEADER_MENU_ID);
        if (menuId == null) {
            menuId = ContextUtil.getHttpRequest().getParameter(ApplicationConstant.HTTP_HEADER_MENU_ID);
        }
        if (BooleanUtil.isFalse(properties.getEnableMenuPermVerify()) && menuId == null) {
            menuId = ApplicationConstant.DATA_PERM_ALL_MENU_ID;
        }
        Assert.notNull(menuId);
        JSONObject menuDataPermMap = allMenuDataPermMap.getJSONObject(menuId);
        if (menuDataPermMap == null) {
            menuDataPermMap = allMenuDataPermMap.getJSONObject(ApplicationConstant.DATA_PERM_ALL_MENU_ID);
        }
        if (menuDataPermMap == null) {
            throw new NoDataPermException(StrFormatter.format(
                    "No Related DataPerm found for menuId [{}] and SQL_ID [{}].", menuId, sqlId));
        }
        if (BooleanUtil.isTrue(properties.getEnableMenuPermVerify())) {
            String url = ContextUtil.getHttpRequest().getHeader(ApplicationConstant.HTTP_HEADER_ORIGINAL_REQUEST_URL);
            if (StrUtil.isBlank(url)) {
                url = ContextUtil.getHttpRequest().getRequestURI();
            }
            Assert.notNull(url);
            if (!this.verifyMenuPerm(menuId, url, sqlId) && !this.verifyMenuPerm(null, url, sqlId)) {
                String msg = StrFormatter.format("Mismatched DataPerm " +
                        "for menuId [{}] and url [{}] and SQL_ID [{}].", menuId, url, sqlId);
                throw new NoDataPermException(msg);
            }
        }
        return menuDataPermMap;
    }

    private Object getCachedData(String dataPermSessionKey) {
        Object cachedData;
        Cache cache = cacheManager.getCache(CacheConfig.CacheEnum.DATA_PERMISSION_CACHE.name());
        org.springframework.util.Assert.notNull(cache, "Cache [DATA_PERMISSION_CACHE] can't be null.");
        Cache.ValueWrapper wrapper = cache.get(dataPermSessionKey);
        if (wrapper == null) {
            cachedData = redissonClient.getBucket(dataPermSessionKey).get();
            if (cachedData != null) {
                cache.put(dataPermSessionKey, JSON.parseObject(cachedData.toString()));
            }
        } else {
            cachedData = wrapper.get();
        }
        return cachedData;
    }

    @SuppressWarnings("unchecked")
    private boolean verifyMenuPerm(String menuId, String url, String sqlId) {
        String sessionId = TokenData.takeFromRequest().getSessionId();
        String menuPermSessionKey;
        if (menuId != null) {
            menuPermSessionKey = RedisKeyUtil.makeSessionMenuPermKey(sessionId, menuId);
        } else {
            menuPermSessionKey = RedisKeyUtil.makeSessionWhiteListPermKey(sessionId);
        }
        Cache cache = cacheManager.getCache(CacheConfig.CacheEnum.MENU_PERM_CACHE.name());
        org.springframework.util.Assert.notNull(cache, "Cache [MENU_PERM_CACHE] can't be null!");
        Cache.ValueWrapper wrapper = cache.get(menuPermSessionKey);
        if (wrapper != null) {
            Object cachedData = wrapper.get();
            if (cachedData != null) {
                return ((Set<String>) cachedData).contains(url);
            }
        }
        RBucket<String> bucket = redissonClient.getBucket(menuPermSessionKey);
        if (!bucket.isExists()) {
            String msg;
            if (menuId == null) {
                msg = StrFormatter.format("No Related MenuPerm found " +
                        "in Redis Cache for WHITE_LIST and SQL_ID [{}] with sessionId [{}].", sqlId, sessionId);
            } else {
                msg = StrFormatter.format("No Related MenuPerm found " +
                        "in Redis Cache for menuId [{}] and SQL_ID [{}] with sessionId [{}].", menuId, sqlId, sessionId);
            }
            throw new NoDataPermException(msg);
        }
        Set<String> cachedMenuPermSet = new HashSet<>(JSONArray.parseArray(bucket.get(), String.class));
        cache.put(menuPermSessionKey, cachedMenuPermSet);
        return cachedMenuPermSet.contains(url);
    }

    private void processDataPerm(
            ModelDataPermInfo info,
            Map<Integer, String> dataPermMap,
            BoundSql boundSql,
            SqlCommandType commandType,
            Statement statement) throws JSQLParserException {
        List<String> criteriaList = new LinkedList<>();
        for (Map.Entry<Integer, String> entry : dataPermMap.entrySet()) {
            String filterClause = processDataPermRule(info, entry.getKey(), entry.getValue());
            if (StrUtil.isNotBlank(filterClause)) {
                criteriaList.add(filterClause);
            }
        }
        if (CollUtil.isEmpty(criteriaList)) {
            return;
        }
        StringBuilder filterBuilder = new StringBuilder(128);
        filterBuilder.append("(");
        filterBuilder.append(StrUtil.join(" OR ", criteriaList));
        filterBuilder.append(")");
        String dataFilter = filterBuilder.toString();
        if (statement == null) {
            String sql = boundSql.getSql();
            statement = CCJSqlParserUtil.parse(sql);
        }
        if (commandType == SqlCommandType.UPDATE) {
            Update update = (Update) statement;
            this.buildWhereClause(update, dataFilter);
        } else if (commandType == SqlCommandType.DELETE) {
            Delete delete = (Delete) statement;
            this.buildWhereClause(delete, dataFilter);
        } else {
            this.processSelect(statement, info, dataFilter);
        }
        log.info("DataPerm Filter Where Clause [{}]", dataFilter);
        ReflectUtil.setFieldValue(boundSql, "sql", statement.toString());
    }

    private void processSelect(Statement statement, ModelDataPermInfo info, String dataFilter)
            throws JSQLParserException {
        Select select = (Select) statement;
        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        FromItem fromItem = selectBody.getFromItem();
        if (fromItem == null) {
            return;
        }
        PlainSelect subSelect = null;
        if (fromItem instanceof SubSelect) {
            subSelect = (PlainSelect) ((SubSelect) fromItem).getSelectBody();
        }
        if (subSelect != null) {
            dataFilter = replaceTableAlias(info.getMainTableName(), subSelect, dataFilter);
            buildWhereClause(subSelect, dataFilter);
        } else {
            dataFilter = replaceTableAlias(info.getMainTableName(), selectBody, dataFilter);
            buildWhereClause(selectBody, dataFilter);
        }
    }

    private String processDataPermRule(ModelDataPermInfo info, Integer ruleType, String dataIds) {
        TokenData tokenData = TokenData.takeFromRequest();
        StringBuilder filter = new StringBuilder(128);
        String tableName = info.getMainTableName();
        if (ruleType != DataPermRuleType.TYPE_USER_ONLY
                && ruleType != DataPermRuleType.TYPE_DEPT_AND_CHILD_DEPT_USERS
                && ruleType != DataPermRuleType.TYPE_DEPT_USERS) {
            return this.processDeptDataPermRule(info, ruleType, dataIds);
        }
        if (StrUtil.isBlank(info.getUserFilterColumn())) {
            log.warn("No UserFilterColumn for table [{}] but USER_FILTER_DATA_PERM exists !!!", tableName);
            return filter.toString();
        }
        if (BooleanUtil.isTrue(properties.getAddTableNamePrefix())) {
            filter.append(info.getMainTableName()).append(".");
        }
        if (ruleType == DataPermRuleType.TYPE_USER_ONLY) {
            filter.append(info.getUserFilterColumn())
                    .append(" = ")
                    .append(tokenData.getUserId());
        } else {
            filter.append(info.getUserFilterColumn())
                    .append(" IN (")
                    .append(dataIds)
                    .append(") ");
        }
        return filter.toString();
    }

    private String processDeptDataPermRule(ModelDataPermInfo info, Integer ruleType, String deptIds) {
        StringBuilder filter = new StringBuilder(128);
        String tableName = info.getMainTableName();
        if (StrUtil.isBlank(info.getDeptFilterColumn())) {
            log.warn("No DeptFilterColumn for table [{}] but DEPT_FILTER_DATA_PERM exists !!!", tableName);
            return filter.toString();
        }
        TokenData tokenData = TokenData.takeFromRequest();
        if (ruleType == DataPermRuleType.TYPE_DEPT_ONLY) {
            if (BooleanUtil.isTrue(properties.getAddTableNamePrefix())) {
                filter.append(info.getMainTableName()).append(".");
            }
            filter.append(info.getDeptFilterColumn())
                    .append(" = ")
                    .append(tokenData.getDeptId());
        } else if (ruleType == DataPermRuleType.TYPE_DEPT_AND_CHILD_DEPT) {
            filter.append(" EXISTS ")
                    .append("(SELECT 1 FROM ")
                    .append(properties.getDeptRelationTablePrefix())
                    .append("sys_dept_relation WHERE ")
                    .append(properties.getDeptRelationTablePrefix())
                    .append("sys_dept_relation.parent_dept_id = ")
                    .append(tokenData.getDeptId())
                    .append(" AND ");
            if (BooleanUtil.isTrue(properties.getAddTableNamePrefix())) {
                filter.append(info.getMainTableName()).append(".");
            }
            filter.append(info.getDeptFilterColumn())
                    .append(" = ")
                    .append(properties.getDeptRelationTablePrefix())
                    .append("sys_dept_relation.dept_id) ");
        } else if (ruleType == DataPermRuleType.TYPE_MULTI_DEPT_AND_CHILD_DEPT) {
            filter.append(" EXISTS ")
                    .append("(SELECT 1 FROM ")
                    .append(properties.getDeptRelationTablePrefix())
                    .append("sys_dept_relation WHERE ")
                    .append(properties.getDeptRelationTablePrefix())
                    .append("sys_dept_relation.parent_dept_id IN (")
                    .append(deptIds)
                    .append(") AND ");
            if (BooleanUtil.isTrue(properties.getAddTableNamePrefix())) {
                filter.append(info.getMainTableName()).append(".");
            }
            filter.append(info.getDeptFilterColumn())
                    .append(" = ")
                    .append(properties.getDeptRelationTablePrefix())
                    .append("sys_dept_relation.dept_id) ");
        } else if (ruleType == DataPermRuleType.TYPE_CUSTOM_DEPT_LIST) {
            if (BooleanUtil.isTrue(properties.getAddTableNamePrefix())) {
                filter.append(info.getMainTableName()).append(".");
            }
            filter.append(info.getDeptFilterColumn())
                    .append(" IN (")
                    .append(deptIds)
                    .append(") ");
        }
        return filter.toString();
    }
    
    private String replaceTableAlias(String tableName, PlainSelect select, String dataFilter) {
        if (select.getFromItem().getAlias() == null) {
            return dataFilter;
        }
        return dataFilter.replaceAll(tableName, select.getFromItem().getAlias().getName());
    }

    private void buildWhereClause(Update update, String dataFilter) throws JSQLParserException {
        if (update.getWhere() == null) {
            update.setWhere(CCJSqlParserUtil.parseCondExpression(dataFilter));
        } else {
            AndExpression and = new AndExpression(
                    CCJSqlParserUtil.parseCondExpression(dataFilter), update.getWhere());
            update.setWhere(and);
        }
    }

    private void buildWhereClause(Delete delete, String dataFilter) throws JSQLParserException {
        if (delete.getWhere() == null) {
            delete.setWhere(CCJSqlParserUtil.parseCondExpression(dataFilter));
        } else {
            AndExpression and = new AndExpression(
                    CCJSqlParserUtil.parseCondExpression(dataFilter), delete.getWhere());
            delete.setWhere(and);
        }
    }

    private void buildWhereClause(PlainSelect select, String dataFilter) throws JSQLParserException {
        if (select.getWhere() == null) {
            select.setWhere(CCJSqlParserUtil.parseCondExpression(dataFilter));
        } else {
            AndExpression and = new AndExpression(
                    CCJSqlParserUtil.parseCondExpression(dataFilter), select.getWhere());
            select.setWhere(and);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 这里需要空注解，否则sonar会不happy。
    }

    @Data
    private static final class ModelDataPermInfo {
        private Set<String> excludeMethodNameSet;
        private String userFilterColumn;
        private String deptFilterColumn;
        private String mainTableName;
        private Boolean mustIncludeUserRule;
    }

    @Data
    private static final class ModelTenantInfo {
        private Set<String> excludeMethodNameSet;
        private String modelName;
        private String tableName;
        private String fieldName;
        private String columnName;
    }
}
