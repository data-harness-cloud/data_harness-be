package supie.common.dict.service;

import supie.common.core.base.service.IBaseService;
import supie.common.dict.model.GlobalDictItem;

import java.io.Serializable;
import java.util.List;

/**
 * 全局字典项目数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface GlobalDictItemService extends IBaseService<GlobalDictItem, Long> {

    /**
     * 保存新增的全局字典项目。
     *
     * @param globalDictItem 新字典项目对象。
     * @return 保存后的对象。
     */
    GlobalDictItem saveNew(GlobalDictItem globalDictItem);

    /**
     * 更新全局字典项目对象。
     *
     * @param globalDictItem         更新的全局字典项目对象。
     * @param originalGlobalDictItem 原有的全局字典项目对象。
     * @return 更新成功返回true，否则false。
     */
    boolean update(GlobalDictItem globalDictItem, GlobalDictItem originalGlobalDictItem);

    /**
     * 更新字典条目的编码。
     *
     * @param oldCode 原有编码。
     * @param newCode 新编码。
     */
    void updateNewCode(String oldCode, String newCode);

    /**
     * 更新字典条目的状态。
     *
     * @param globalDictItem 字典项目对象。
     * @param status         状态值。
     */
    void updateStatus(GlobalDictItem globalDictItem, Integer status);

    /**
     * 删除指定字典项目。
     *
     * @param globalDictItem 待删除字典项目。
     * @return 成功返回true，否则false。
     */
    boolean remove(GlobalDictItem globalDictItem);

    /**
     * 判断指定的编码和项目Id是否存在。
     *
     * @param dictCode 字典编码。
     * @param itemId   项目Id。
     * @return true存在，否则false。
     */
    boolean existDictCodeAndItemId(String dictCode, Serializable itemId);

    /**
     * 根据字典编码和项目Id获取指定字段项目对象。
     *
     * @param dictCode 字典编码。
     * @param itemId   项目Id。
     * @return 字典项目对象。
     */
    GlobalDictItem getGlobalDictItemByDictCodeAndItemId(String dictCode, Serializable itemId);

    /**
     * 查询数据字典项目列表。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序字符串，如果为空，则按照showOrder升序排序。
     * @return 查询结果列表。
     */
    List<GlobalDictItem> getGlobalDictItemList(GlobalDictItem filter, String orderBy);

    /**
     * 查询指定字典编码的数据字典项目列表。查询结果按照showOrder升序排序。
     *
     * @param dictCode 过滤对象。
     * @return 查询结果列表。
     */
    List<GlobalDictItem> getGlobalDictItemListByDictCode(String dictCode);
}
