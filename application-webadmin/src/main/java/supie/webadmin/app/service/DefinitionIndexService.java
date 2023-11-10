package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 业务定义-指标管理数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface DefinitionIndexService extends IBaseService<DefinitionIndex, Long> {

    /**
     * 保存新增对象。
     *
     * @param definitionIndex 新增对象。
     * @return 返回新增对象。
     */
    DefinitionIndex saveNew(DefinitionIndex definitionIndex);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param definitionIndexList 新增对象列表。
     */
    void saveNewBatch(List<DefinitionIndex> definitionIndexList);

    /**
     * 更新数据对象。
     *
     * @param definitionIndex         更新的对象。
     * @param originalDefinitionIndex 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(DefinitionIndex definitionIndex, DefinitionIndex originalDefinitionIndex);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getDefinitionIndexListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<DefinitionIndex> getDefinitionIndexList(DefinitionIndex filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getDefinitionIndexList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<DefinitionIndex> getDefinitionIndexListWithRelation(DefinitionIndex filter, String orderBy);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<DefinitionIndex> getGroupedDefinitionIndexListWithRelation(
            DefinitionIndex filter, String groupSelect, String groupBy, String orderBy);

    /**
     * 批量添加多对多关联关系。
     *
     * @param definitionIndexModelFieldRelationList 多对多关联表对象集合。
     * @param indexId 主表Id。
     */
    void addDefinitionIndexModelFieldRelationList(List<DefinitionIndexModelFieldRelation> definitionIndexModelFieldRelationList, Long indexId);

    /**
     * 更新中间表数据。
     *
     * @param definitionIndexModelFieldRelation 中间表对象。
     * @return 更新成功与否。
     */
    boolean updateDefinitionIndexModelFieldRelation(DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation);

    /**
     * 获取中间表数据。
     *
     * @param indexId 主表Id。
     * @param modelFieldId 从表Id。
     * @return 中间表对象。
     */
    DefinitionIndexModelFieldRelation getDefinitionIndexModelFieldRelation(Long indexId, Long modelFieldId);

    /**
     * 移除单条多对多关系。
     *
     * @param indexId 主表Id。
     * @param modelFieldId 从表Id。
     * @return 成功返回true，否则false。
     */
    boolean removeDefinitionIndexModelFieldRelation(Long indexId, Long modelFieldId);
}
