package supie.common.core.base.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.cache.DictionaryCache;
import supie.common.core.object.TokenData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * 带有缓存功能的字典Service基类，需要留意的是，由于缓存基于Key/Value方式存储，
 * 目前仅支持基于主键字段的缓存查找，其他条件的查找仍然从数据源获取。
 *
 * @param <M> Model实体对象的类型。
 * @param <K> Model对象主键的类型。
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public abstract class BaseDictService<M, K extends Serializable>
        extends BaseService<M, K> implements IBaseDictService<M, K> {

    /**
     * 缓存池对象。
     */
    protected DictionaryCache<K, M> dictionaryCache;

    /**
     * 构造函数使用缺省缓存池对象。
     */
    protected BaseDictService() {
        super();
    }

    /**
     * 重新加载数据库中所有当前表数据到系统内存。
     *
     * @param force true则强制刷新，如果false，当缓存中存在数据时不刷新。
     */
    @Override
    public void reloadCachedData(boolean force) {
        // 在非强制刷新情况下。
        // 先行判断缓存中是否存在数据，如果有就不加载了。
        if (!force && dictionaryCache.getCount() > 0) {
            return;
        }
        List<M> allList = super.getAllList();
        dictionaryCache.reload(allList, force);
    }

    /**
     * 保存新增对象。
     *
     * @param data 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public M saveNew(M data) {
        // 清空全部缓存
        dictionaryCache.invalidateAll();
        if (deletedFlagFieldName != null) {
            ReflectUtil.setFieldValue(data, deletedFlagFieldName, GlobalDeletedFlag.NORMAL);
        }
        if (tenantIdField != null) {
            ReflectUtil.setFieldValue(data, tenantIdField, TokenData.takeFromRequest().getTenantId());
        }
        mapper().insert(data);
        return data;
    }

    /**
     * 更新数据对象。
     *
     * @param data         更新的对象。
     * @param originalData 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(M data, M originalData) {
        dictionaryCache.invalidateAll();
        if (tenantIdField != null) {
            ReflectUtil.setFieldValue(data, tenantIdField, TokenData.takeFromRequest().getTenantId());
        }
        return mapper().updateById(data) == 1;
    }

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(K id) {
        dictionaryCache.invalidateAll();
        return mapper().deleteById(id) == 1;
    }

    /**
     * 直接从缓存池中获取主键Id关联的数据。如果缓存中不存在，再从数据库中取出并回写到缓存。
     *
     * @param id 主键Id。
     * @return 主键关联的数据，不存在返回null。
     */
    @SuppressWarnings("unchecked")
    @Override
    public M getById(Serializable id) {
        M data = dictionaryCache.get((K) id);
        if (data != null) {
            return data;
        }
        if (dictionaryCache.getCount() != 0) {
            return data;
        }
        this.reloadCachedData(true);
        return dictionaryCache.get((K) id);
    }

    /**
     * 直接从缓存池中获取所有数据。
     *
     * @return 返回所有数据。
     */
    @Override
    public List<M> getAllListFromCache() {
        List<M> resultList = dictionaryCache.getAll();
        if (CollUtil.isNotEmpty(resultList)) {
            return resultList;
        }
        this.reloadCachedData(true);
        return dictionaryCache.getAll();
    }

    /**
     * 直接从缓存池中返回符合主键 in (idValues) 条件的所有数据。
     * 对于缓存中不存在的数据，从数据库中获取并回写入缓存。
     *
     * @param idValues 主键值列表。
     * @return 检索后的数据列表。
     */
    @Override
    public List<M> getInList(Set<K> idValues) {
        List<M> resultList = dictionaryCache.getInList(idValues);
        // 如果从缓存中获取与请求的id完全相同就直接返回。
        if (resultList.size() == idValues.size()) {
            return resultList;
        }
        // 如果此时缓存中存在数据，说明有部分id是不存在的。也可以直接返回了。
        if (dictionaryCache.getCount() != 0) {
            return resultList;
        }
        // 执行到这里，说明缓存是空的，所有需要重新加载并再次从缓存中读取并返回。
        this.reloadCachedData(true);
        return dictionaryCache.getInList(idValues);
    }

    @Override
    public List<M> getListByParentId(K parentId) {
        List<M> resultList = dictionaryCache.getListByParentId(parentId);
        // 如果包含数据就直接返回了
        if (CollUtil.isNotEmpty(resultList)) {
            return resultList;
        }
        // 如果缓存中存在该字典数据，说明该parentId下子对象列表为空，也可以直接返回了。
        if (this.getCachedCount() != 0) {
            return resultList;
        }
        // 执行到这里就需要重新加载全部缓存了。
        this.reloadCachedData(true);
        return dictionaryCache.getListByParentId(parentId);
    }

    /**
     * 返回符合 inFilterField in (inFilterValues) 条件的所有数据。属性property是主键，则从缓存中读取。
     *
     * @param inFilterField  参与(In-list)过滤的Java字段。
     * @param inFilterValues 参与(In-list)过滤的Java字段值集合。
     * @return 检索后的数据列表。
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<M> getInList(String inFilterField, Set<T> inFilterValues) {
        if (inFilterField.equals(this.idFieldName)) {
            return this.getInList((Set<K>) inFilterValues);
        }
        return super.getInList(inFilterField, inFilterValues);
    }

    /**
     * 判断参数值列表中的所有数据，是否全部存在。另外，keyName字段在数据表中必须是唯一键值，否则返回结果会出现误判。
     *
     * @param inFilterField  待校验的数据字段，这里使用Java对象中的属性，如courseId，而不是数据字段名course_id。
     * @param inFilterValues 数据值集合。
     * @return 全部存在返回true，否则false。
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> boolean existUniqueKeyList(String inFilterField, Set<T> inFilterValues) {
        if (CollUtil.isEmpty(inFilterValues)) {
            return true;
        }
        if (inFilterField.equals(this.idFieldName)) {
            List<M> dataList = this.getInList((Set<K>) inFilterValues);
            return dataList.size() == inFilterValues.size();
        }
        String columnName = this.safeMapToColumnName(inFilterField);
        QueryWrapper<M> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(columnName, inFilterValues);
        return mapper().selectCount(queryWrapper) == inFilterValues.size();
    }

    /**
     * 获取缓存中的数据数量。
     *
     * @return 缓存中的数据总量。
     */
    @Override
    public int getCachedCount() {
        return dictionaryCache.getCount();
    }
}
