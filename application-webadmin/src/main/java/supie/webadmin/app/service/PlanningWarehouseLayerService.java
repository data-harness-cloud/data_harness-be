package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.object.CallResult;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 数据规划-数据仓库分层表数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface PlanningWarehouseLayerService extends IBaseService<PlanningWarehouseLayer, Long> {

    /**
     * 保存新增对象。
     *
     * @param planningWarehouseLayer 新增对象。
     * @return 返回新增对象。
     */
    PlanningWarehouseLayer saveNew(PlanningWarehouseLayer planningWarehouseLayer);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param planningWarehouseLayerList 新增对象列表。
     */
    void saveNewBatch(List<PlanningWarehouseLayer> planningWarehouseLayerList);

    /**
     * 更新数据对象。
     *
     * @param planningWarehouseLayer         更新的对象。
     * @param originalPlanningWarehouseLayer 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(PlanningWarehouseLayer planningWarehouseLayer, PlanningWarehouseLayer originalPlanningWarehouseLayer);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getPlanningWarehouseLayerListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<PlanningWarehouseLayer> getPlanningWarehouseLayerList(PlanningWarehouseLayer filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getPlanningWarehouseLayerList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param modelPhysicsScriptFilter 一对多从表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<PlanningWarehouseLayer> getPlanningWarehouseLayerListWithRelation(PlanningWarehouseLayer filter, ModelPhysicsScript modelPhysicsScriptFilter, String orderBy);

    /**
     * 判断从表数据是否存在，如果存在就不能删除主对象，否则可以删除主对象。
     * 适用于主表对从表不是强制级联删除的场景。
     *
     * @param planningWarehouseLayer 主表对象。
     * @return 没有关联数据返回true，否则false，同时返回具体的提示信息。
     */
    CallResult verifyRelatedDataBeforeDelete(PlanningWarehouseLayer planningWarehouseLayer);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<PlanningWarehouseLayer> getGroupedPlanningWarehouseLayerListWithRelation(
            PlanningWarehouseLayer filter, String groupSelect, String groupBy, String orderBy);

    void createDatabaseByProjectEngine(ProjectEngine projectEngine, String... databaseNameList);

    /**
     * 根据项目ID删除数据。
     *
     * @param projectId 项目ID。
     */
    boolean removeByProjectId(Long projectId);
}
