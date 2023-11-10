package supie.common.dict.service;

import supie.common.core.base.service.IBaseService;
import supie.common.dict.model.GlobalDict;
import supie.common.dict.model.GlobalDictItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局字典数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface GlobalDictService extends IBaseService<GlobalDict, Long> {

    /**
     * 保存全局字典对象。
     *
     * @param globalDict 全局字典对象。
     * @return 保存后的字典对象。
     */
    GlobalDict saveNew(GlobalDict globalDict);

    /**
     * 更新全局字典对象。
     *
     * @param globalDict         更新的全局字典对象。
     * @param originalGlobalDict 原有的全局字典对象。
     * @return 更新成功返回true，否则false。
     */
    boolean update(GlobalDict globalDict, GlobalDict originalGlobalDict);

    /**
     * 删除全局字典对象，以及其关联的字典项目数据。
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
    List<GlobalDict> getGlobalDictList(GlobalDict filter, String orderBy);

    /**
     * 判断字典编码是否存在。
     *
     * @param dictCode 字典编码。
     * @return true表示存在，否则false。
     */
    boolean existDictCode(String dictCode);

    /**
     * 判断指定字典编码的字典项目是否存在。
     * 该方法通常会在业务主表中调用，为了提升整体运行时效率，该方法会从缓存中获取，如果缓存为空，
     * 会从数据库读取指定编码的字典数据，并同步到缓存。
     *
     * @param dictCode 字典编码。
     * @param itemId   字典项目Id。
     * @return true表示存在，否则false。
     */
    boolean existDictItemFromCache(String dictCode, Serializable itemId);

    /**
     * 从缓存中获取指定编码的字典项目列表。
     * 该方法通常会在业务主表中调用，为了提升整体运行时效率，该方法会从缓存中获取，如果缓存为空，
     * 会从数据库读取指定编码的字典数据，并同步到缓存。
     *
     * @param dictCode 字典编码。
     * @param itemIds  字典项目Id集合。
     * @return 查询结果列表。
     */
    List<GlobalDictItem> getGlobalDictItemListFromCache(String dictCode, Set<Serializable> itemIds);

    /**
     * 从缓存中获取指定编码的字典项目列表。返回的结果Map中，键是itemId，值是itemName。
     * 该方法通常会在业务主表中调用，为了提升整体运行时效率，该方法会从缓存中获取，如果缓存为空，
     * 会从数据库读取指定编码的字典数据，并同步到缓存。
     *
     * @param dictCode 字典编码。
     * @param itemIds  字典项目Id集合。
     * @return 查询结果列表。
     */
    Map<Serializable, String> getGlobalDictItemDictMapFromCache(String dictCode, Set<Serializable> itemIds);

    /**
     * 强制同步指定字典编码的全部字典项目到缓存。
     *
     * @param dictCode 字典编码。
     */
    void reloadCachedData(String dictCode);

    /**
     * 从缓存中移除指定字典编码的数据。
     *
     * @param dictCode 字典编码。
     */
    void removeCache(String dictCode);
}
