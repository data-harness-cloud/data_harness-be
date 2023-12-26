package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import supie.common.core.util.ApplicationContextHolder;
import supie.webadmin.app.controller.dynamicRoutingAPI.MyDynamicController;
import supie.webadmin.app.service.*;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.model.*;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.MyRelationParam;
import supie.common.core.base.service.BaseService;
import supie.common.core.util.MyModelUtil;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自定义动态路由数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("customizeRouteService")
public class CustomizeRouteServiceImpl extends BaseService<CustomizeRoute, Long> implements CustomizeRouteService, ApplicationRunner {

    private static RequestMappingHandlerMapping requestMappingHandlerMapping = null;

    @Autowired
    private CustomizeRouteMapper customizeRouteMapper;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<CustomizeRoute> mapper() {
        return customizeRouteMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param customizeRoute 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomizeRoute saveNew(CustomizeRoute customizeRoute) {
        customizeRouteMapper.insert(this.buildDefaultValue(customizeRoute));
        return customizeRoute;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param customizeRouteList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<CustomizeRoute> customizeRouteList) {
        if (CollUtil.isNotEmpty(customizeRouteList)) {
            customizeRouteList.forEach(this::buildDefaultValue);
            customizeRouteMapper.insertList(customizeRouteList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param customizeRoute         更新的对象。
     * @param originalCustomizeRoute 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(CustomizeRoute customizeRoute, CustomizeRoute originalCustomizeRoute) {
        // 如果就数据已经处于上线状态的，则先下线，修改成功后再上线。
        if (originalCustomizeRoute.getState() == null || originalCustomizeRoute.getState() == 1) {
            // 下线路由
            unregisterApi(originalCustomizeRoute);
        }
        unregisterDynamicRouteFromRedis(originalCustomizeRoute.getUrl());
        MyModelUtil.fillCommonsForUpdate(customizeRoute, originalCustomizeRoute);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<CustomizeRoute> uw = this.createUpdateQueryForNullValue(customizeRoute, customizeRoute.getId());
        boolean updateResult = customizeRouteMapper.update(customizeRoute, uw) == 1;
        // 修改成功，如果新数据为上线状态则上线路由
        if (updateResult && customizeRoute.getState() != null && customizeRoute.getState() == 1) {
            // 上线该路由
            registerApi(customizeRoute);
        }
        return updateResult;
    }

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long id) {
        return customizeRouteMapper.deleteById(id) == 1;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getCustomizeRouteListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<CustomizeRoute> getCustomizeRouteList(CustomizeRoute filter, String orderBy) {
        return customizeRouteMapper.getCustomizeRouteList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getCustomizeRouteList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<CustomizeRoute> getCustomizeRouteListWithRelation(CustomizeRoute filter, String orderBy) {
        List<CustomizeRoute> resultList = customizeRouteMapper.getCustomizeRouteList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    @Override
    public List<CustomizeRoute> getGroupedCustomizeRouteListWithRelation(
            CustomizeRoute filter, String groupSelect, String groupBy, String orderBy) {
        List<CustomizeRoute> resultList =
                customizeRouteMapper.getGroupedCustomizeRouteList(filter, groupSelect, groupBy, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        // NOTE: 这里只是包含了关联数据，聚合计算数据没有包含。
        // 主要原因是，由于聚合字段通常被视为普通字段使用，不会在group by的从句中出现，语义上也不会在此关联。
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    private CustomizeRoute buildDefaultValue(CustomizeRoute customizeRoute) {
        if (customizeRoute.getId() == null) {
            customizeRoute.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(customizeRoute);
        customizeRoute.setIsDelete(GlobalDeletedFlag.NORMAL);
        return customizeRoute;
    }

    /**
     * 从 Redis 注销动态路由信息
     *
     * @param originalUrl 原始路由
     * @author 王立宏
     * @date 2023/11/22 04:12
     */
    @Override
    public void unregisterDynamicRouteFromRedis(String originalUrl) {
        redissonClient.getBucket("CustomizeRoute:" + originalUrl).delete();
        redissonClient.getBucket("CustomizeRouteVerificationInfo:" + originalUrl).delete();
    }

    /**
     * 程序启动成功后注册数据库中状态为上线的路径
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动完成，开始注册动态路由！");
        if (requestMappingHandlerMapping == null) {
            requestMappingHandlerMapping = ApplicationContextHolder.getBean("requestMappingHandlerMapping");
        }
        List<CustomizeRoute> customizeRouteList = customizeRouteMapper.queryRegisterApi();
        String path;
        RequestMethod requestMethod = RequestMethod.POST;
        MyDynamicController myDynamicController = ApplicationContextHolder.getBean(MyDynamicController.class);
        Method method = MyDynamicController.class.getDeclaredMethod("executeSql", HttpServletRequest.class);
        for (CustomizeRoute customizeRoute : customizeRouteList) {
            path = customizeRoute.getUrl();
            if (customizeRoute.getRequestType() == 1) {
                requestMethod = RequestMethod.GET;
            } else if (customizeRoute.getRequestType() == 2) {
                requestMethod = RequestMethod.POST;
            }
            RequestMappingInfo mappingInfo = RequestMappingInfo
                    .paths(path)
                    .methods(requestMethod)
                    .build();
            // 反射获取ExampleController中的hello方法，用于执行实际逻辑
            requestMappingHandlerMapping.registerMapping(mappingInfo, myDynamicController, method);
        }
    }

    /**
     * 上线 API
     *
     * @param originalCustomizeRoute 原始自定义路线
     * @author 王立宏
     * @date 2023/11/16 05:06
     */
    @Override
    public void registerApi(CustomizeRoute originalCustomizeRoute) {
        if (requestMappingHandlerMapping == null) {
            requestMappingHandlerMapping = ApplicationContextHolder.getBean("requestMappingHandlerMapping");
        }
        String path = originalCustomizeRoute.getUrl();
        RequestMethod requestMethod = RequestMethod.POST;
        if (originalCustomizeRoute.getRequestType() == 1) {
            requestMethod = RequestMethod.GET;
        } else if (originalCustomizeRoute.getRequestType() == 2) {
            requestMethod = RequestMethod.POST;
        }
        MyDynamicController myDynamicController = ApplicationContextHolder.getBean(MyDynamicController.class);
        RequestMappingInfo mappingInfo = RequestMappingInfo
                .paths(path)
                .methods(requestMethod)
                .build();
        try {
            // 反射获取ExampleController中的hello方法，用于执行实际逻辑
            Method method = MyDynamicController.class.getDeclaredMethod("executeSql", HttpServletRequest.class);
            requestMappingHandlerMapping.registerMapping(mappingInfo, myDynamicController, method);
            // 修改数据库中显示的状态
            originalCustomizeRoute.setState(1);
            MyModelUtil.fillCommonsForUpdate(originalCustomizeRoute, originalCustomizeRoute);
            UpdateWrapper<CustomizeRoute> uw = this.createUpdateQueryForNullValue(originalCustomizeRoute, originalCustomizeRoute.getId());
            customizeRouteMapper.update(originalCustomizeRoute, uw);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下线 API
     *
     * @param originalCustomizeRoute 原始自定义路线
     * @author 王立宏
     * @date 2023/11/16 05:07
     */
    @Override
    public void unregisterApi(CustomizeRoute originalCustomizeRoute) {
        if (requestMappingHandlerMapping == null) {
            requestMappingHandlerMapping = ApplicationContextHolder.getBean("requestMappingHandlerMapping");
        }
        String path = originalCustomizeRoute.getUrl();
        log.warn("删除路由[" + path + "]");
        RequestMappingInfo requestMappingInfo = requestMappingHandlerMapping.getHandlerMethods().keySet().stream()
                .filter(mapping -> mapping.getPatternsCondition().getPatterns().contains(path))
                .findFirst()
                .orElse(null);
        // 删除指定路径的映射
        requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
        // 修改数据库中显示的状态
        originalCustomizeRoute.setState(-1);
        MyModelUtil.fillCommonsForUpdate(originalCustomizeRoute, originalCustomizeRoute);
        UpdateWrapper<CustomizeRoute> uw = this.createUpdateQueryForNullValue(originalCustomizeRoute, originalCustomizeRoute.getId());
        customizeRouteMapper.update(originalCustomizeRoute, uw);
        unregisterDynamicRouteFromRedis(originalCustomizeRoute.getUrl());
    }

    /**
     * TODO 列出不与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。通常用于查看添加新 [自定义动态路由] 对象的候选列表。
     *
     * @param externalAppId        主表关联字段。
     * @param customizeRouteFilter [自定义动态路由] 过滤对象。
     * @param orderBy              排序参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     * @author 王立宏
     * @date 2023/11/20 10:28
     */
    @Override
    public List<CustomizeRoute> getNotInCustomizeRouteListByExternalAppId(Long externalAppId, CustomizeRoute customizeRouteFilter, String orderBy) {
        throw new RuntimeException("该功能待完成[急需请与管理员联系]...");
    }

    /**
     * 列出与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。
     *
     * @param externalAppId        主表关联字段。
     * @param customizeRouteFilter 自定义路由过滤器。
     * @param orderBy              排序方式。
     * @return 应答结果对象，返回符合条件的数据列表。
     * @return 列表<自定义路由>
     * @author 王立宏
     * @date 2023/11/20 10:34
     */
    @Override
    public List<CustomizeRoute> getCustomizeRouteListByExternalAppId(Long externalAppId, CustomizeRoute customizeRouteFilter, String orderBy) {
        return customizeRouteMapper.getCustomizeRouteListByExternalAppId(externalAppId, customizeRouteFilter, orderBy);
    }

    /**
     * 查询externalAppId关联的CustomizeRoute
     *
     * @param externalAppId 外部应用 ID
     * @return
     * @author 王立宏
     * @date 2023/11/22 04:31
     */
    @Override
    public List<CustomizeRoute> queryAssociatedCustomizeRoute(Long externalAppId) {
        return customizeRouteMapper.queryAssociatedCustomizeRoute(externalAppId);
    }

    /**
     * 通过 外部应用与自定义路由关联信息 查询自定义路由信息
     *
     * @param externalAppCustomizeRoute 外部应用自定义路由
     * @return 自定义路由信息集
     * @author 王立宏
     * @date 2023/11/22 05:23
     */
    @Override
    public List<CustomizeRoute> queryCustomizeRouteByExternalAppCustomizeRoute(ExternalAppCustomizeRoute externalAppCustomizeRoute) {
        return customizeRouteMapper.queryCustomizeRouteByExternalAppCustomizeRoute(externalAppCustomizeRoute);
    }
}
