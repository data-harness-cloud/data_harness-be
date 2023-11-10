package supie.common.dict.service;

import supie.common.core.base.service.IBaseService;
import supie.common.dict.model.TenantGlobalDict;
import supie.common.dict.model.TenantGlobalDictItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 租户全局字典数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface TenantGlobalDictService extends IBaseService<TenantGlobalDict, Long> {

    /**
     * 保存租户全局字典对象。
     *
     * @param tenantGlobalDict 全局租户字典对象。
     * @param tenantIdSet      租户Id集合。
     * @return 保存后的字典对象。
     */
    TenantGlobalDict saveNew(TenantGlobalDict tenantGlobalDict, Set<Long> tenantIdSet);

    /**
     * 更新租户全局字典对象。
     *
     * @param tenantGlobalDict         更新的租户全局字典对象。
     * @param originalTenantGlobalDict 原有的租户全局字典对象。
     * @return 更新成功返回true，否则false。
     */
    boolean update(TenantGlobalDict tenantGlobalDict, TenantGlobalDict originalTenantGlobalDict);

    /**
     * 删除租户全局字典对象，以及其关联的字典项目数据。
     *
     * @param dictId 全局字典Id。
     * @return 是否删除成功。
     */
    boolean remove(Long dictId);

    /**
     * 获取全局字典列表。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序条件。
     * @return 查询结果集列表。
     */
    List<TenantGlobalDict> getGlobalDictList(TenantGlobalDict filter, String orderBy);

    /**
     * 判断租户字典编码是否存在。
     *
     * @param dictCode 字典编码。
     * @return true表示存在，否则false。
     */
    boolean existDictCode(String dictCode);

    /**
     * 根据字典编码获取全局字典编码对象。
     *
     * @param dictCode 字典编码。
     * @return 查询后的字典对象。
     */
    TenantGlobalDict getTenantGlobalDictByDictCode(String dictCode);

    /**
     * 从缓存中中获取指定字典数据。如果缓存中不存在，会从数据库读取并同步到缓存。
     *
     * @param dictCode 字典编码。
     * @return 查询到的字段对象。
     */
    TenantGlobalDict getTenantGlobalDictFromCache(String dictCode);

    /**
     * 从缓存中获取指定编码的字典项目列表。
     * 如果是租户非公用字典，会仅仅返回该租户的字典数据列表。
     * 该方法通常会在业务主表中调用，为了提升整体运行时效率，该方法会从缓存中获取，如果缓存为空，
     * 会从数据库读取指定编码的字典数据，并同步到缓存。
     *
     * @param tenantGlobalDict 编码字典对象。
     * @param itemIds          字典项目Id集合。
     * @return 查询结果列表。
     */
    List<TenantGlobalDictItem> getGlobalDictItemListFromCache(TenantGlobalDict tenantGlobalDict, Set<Serializable> itemIds);

    /**
     * 从缓存中获取指定编码的字典项目列表。返回的结果Map中，键是itemId，值是itemName。
     * 如果是租户非公用字典，会仅仅返回该租户的字典数据列表。
     * 该方法通常会在业务主表中调用，为了提升整体运行时效率，该方法会从缓存中获取，如果缓存为空，
     * 会从数据库读取指定编码的字典数据，并同步到缓存。
     *
     * @param tenantGlobalDict 编码字典对象。
     * @param itemIds          字典项目Id集合。
     * @return 查询结果列表。
     */
    Map<Serializable, String> getGlobalDictItemDictMapFromCache(TenantGlobalDict tenantGlobalDict, Set<Serializable> itemIds);

    /**
     * 强制同步指定所有租户通用字典编码的全部字典项目到缓存。
     * 如果是租户非公用字典，会仅仅返回该租户的字典数据列表。
     *
     * @param tenantGlobalDict 编码字典对象。
     */
    void reloadCachedData(TenantGlobalDict tenantGlobalDict);

    /**
     * 重置所有非公用租户编码字典的数据到缓存。
     * 该方法会将指定编码字典中，所有租户的缓存全部重新加载。一般用于系统故障，或大促活动的数据预热。
     *
     * @param tenantGlobalDict 非公用编码字典对象。
     */
    void reloadAllTenantCachedData(TenantGlobalDict tenantGlobalDict);

    /**
     * 从缓存中移除指定字典编码的数据。
     * 该方法的实现内部会判断是否为公用字典，还是租户可修改的非公用字典。
     *
     * @param tenantGlobalDict 字典编码。
     */
    void removeCache(TenantGlobalDict tenantGlobalDict);
}
