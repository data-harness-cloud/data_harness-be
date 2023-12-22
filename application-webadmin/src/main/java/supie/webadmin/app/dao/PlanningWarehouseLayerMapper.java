package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.PlanningWarehouseLayer;
import supie.webadmin.app.model.ModelPhysicsScript;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据规划-数据仓库分层表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface PlanningWarehouseLayerMapper extends BaseDaoMapper<PlanningWarehouseLayer> {

    /**
     * 批量插入对象列表。
     *
     * @param planningWarehouseLayerList 新增对象列表。
     */
    void insertList(List<PlanningWarehouseLayer> planningWarehouseLayerList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param planningWarehouseLayerFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<PlanningWarehouseLayer> getGroupedPlanningWarehouseLayerList(
            @Param("planningWarehouseLayerFilter") PlanningWarehouseLayer planningWarehouseLayerFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param planningWarehouseLayerFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<PlanningWarehouseLayer> getPlanningWarehouseLayerList(
            @Param("planningWarehouseLayerFilter") PlanningWarehouseLayer planningWarehouseLayerFilter, @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。同时支持基于一对一从表字段的过滤条件。
     *
     * @param planningWarehouseLayerFilter 主表过滤对象。
     * @param modelPhysicsScriptFilter 一对多从表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<PlanningWarehouseLayer> getPlanningWarehouseLayerListEx(
            @Param("planningWarehouseLayerFilter") PlanningWarehouseLayer planningWarehouseLayerFilter,
            @Param("modelPhysicsScriptFilter") ModelPhysicsScript modelPhysicsScriptFilter,
            @Param("orderBy") String orderBy);

    int deleteByProjectId(@Param("projectId") Long projectId);

}
