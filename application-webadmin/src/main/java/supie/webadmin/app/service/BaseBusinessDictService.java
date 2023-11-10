package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 基础业务字典数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface BaseBusinessDictService extends IBaseService<BaseBusinessDict, Long> {

    /**
     * 保存新增对象。
     *
     * @param baseBusinessDict 新增对象。
     * @return 返回新增对象。
     */
    BaseBusinessDict saveNew(BaseBusinessDict baseBusinessDict);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param baseBusinessDictList 新增对象列表。
     */
    void saveNewBatch(List<BaseBusinessDict> baseBusinessDictList);

    /**
     * 更新数据对象。
     *
     * @param baseBusinessDict         更新的对象。
     * @param originalBaseBusinessDict 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(BaseBusinessDict baseBusinessDict, BaseBusinessDict originalBaseBusinessDict);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getBaseBusinessDictListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<BaseBusinessDict> getBaseBusinessDictList(BaseBusinessDict filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getBaseBusinessDictList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<BaseBusinessDict> getBaseBusinessDictListWithRelation(BaseBusinessDict filter, String orderBy);

    /**
     * 判断指定对象是否包含下级对象。
     *
     * @param id 主键Id。
     * @return 存在返回true，否则false。
     */
    boolean hasChildren(Long id);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<BaseBusinessDict> getGroupedBaseBusinessDictListWithRelation(
            BaseBusinessDict filter, String groupSelect, String groupBy, String orderBy);
}
