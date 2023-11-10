package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 数据规划-模型设计-模型字段表数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface ModelDesginFieldService extends IBaseService<ModelDesginField, Long> {

    /**
     * 保存新增对象。
     *
     * @param modelDesginField 新增对象。
     * @return 返回新增对象。
     */
    ModelDesginField saveNew(ModelDesginField modelDesginField);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param modelDesginFieldList 新增对象列表。
     */
    void saveNewBatch(List<ModelDesginField> modelDesginFieldList);

    /**
     * 更新数据对象。
     *
     * @param modelDesginField         更新的对象。
     * @param originalModelDesginField 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(ModelDesginField modelDesginField, ModelDesginField originalModelDesginField);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 当前服务的支持表为从表，根据主表的关联Id，删除一对多的从表数据。
     *
     * @param modelId 从表关联字段。
     * @return 删除数量。
     */
    int removeByModelId(Long modelId);

    /**
     * 批量更新一对多从表的数据。
     *
     * @param modelId 从表关联字段。
     * @param dataList 本次批量更新的一对多从表数据。
     */
    void updateBatchByModelId(Long modelId, List<ModelDesginField> dataList);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getModelDesginFieldListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ModelDesginField> getModelDesginFieldList(ModelDesginField filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getModelDesginFieldList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ModelDesginField> getModelDesginFieldListWithRelation(ModelDesginField filter, String orderBy);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<ModelDesginField> getGroupedModelDesginFieldListWithRelation(
            ModelDesginField filter, String groupSelect, String groupBy, String orderBy);

    /**
     * 在多对多关系中，当前Service的数据表为从表，返回不与指定主表主键Id存在对多对关系的列表。
     *
     * @param indexId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ModelDesginField> getNotInModelDesginFieldListByIndexId(Long indexId, ModelDesginField filter, String orderBy);

    /**
     * 在多对多关系中，当前Service的数据表为从表，返回与指定主表主键Id存在对多对关系的列表。
     *
     * @param indexId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param definitionIndexModelFieldRelationFilter 多对多关联表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ModelDesginField> getModelDesginFieldListByIndexId(
            Long indexId, ModelDesginField filter, DefinitionIndexModelFieldRelation definitionIndexModelFieldRelationFilter, String orderBy);
}
