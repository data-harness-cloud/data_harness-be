package supie.webadmin.app.service;

import com.alibaba.fastjson.JSONObject;
import supie.webadmin.app.model.*;
import supie.common.core.base.service.IBaseService;
import supie.webadmin.app.service.databasemanagement.Strategy;

import java.math.BigDecimal;
import java.util.*;

/**
 * 数据规划-模型设计-模型逻辑表数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface ModelLogicalMainService extends IBaseService<ModelLogicalMain, Long> {

    /**
     * 保存新增对象。
     *
     * @param modelLogicalMain 新增对象。
     * @return 返回新增对象。
     */
    ModelLogicalMain saveNew(ModelLogicalMain modelLogicalMain);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param modelLogicalMainList 新增对象列表。
     */
    void saveNewBatch(List<ModelLogicalMain> modelLogicalMainList);

    /**
     * 保存新增主表对象及关联对象。
     *
     * @param modelLogicalMain 新增主表对象。
     * @param relationData 全部关联从表数据。
     * @return 返回新增主表对象。
     */
    ModelLogicalMain saveNewWithRelation(ModelLogicalMain modelLogicalMain, JSONObject relationData);

    /**
     * 更新数据对象。
     *
     * @param modelLogicalMain         更新的对象。
     * @param originalModelLogicalMain 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(ModelLogicalMain modelLogicalMain, ModelLogicalMain originalModelLogicalMain);

    /**
     * 更新主表对象及关联对象。
     *
     * @param modelLogicalMain 主表对象新数据。
     * @param originalModelLogicalMain 主表对象源数据。
     * @param relationData 全部关联从表数据。
     * @return 修改成功返回true，否则false。
     */
    boolean updateWithRelation(ModelLogicalMain modelLogicalMain, ModelLogicalMain originalModelLogicalMain, JSONObject relationData);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getModelLogicalMainListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ModelLogicalMain> getModelLogicalMainList(ModelLogicalMain filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getModelLogicalMainList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ModelLogicalMain> getModelLogicalMainListWithRelation(ModelLogicalMain filter, String orderBy);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<ModelLogicalMain> getGroupedModelLogicalMainListWithRelation(
            ModelLogicalMain filter, String groupSelect, String groupBy, String orderBy);

    /**
     * 根据ID生成建表SQL
     *
     * @param modelLogicalMain 主键ID
     * @return SQL
     * @author 王立宏
     * @date 2023/10/25 06:07
     */
    Map<String, String> buildSql(ModelLogicalMain modelLogicalMain);

    /**
     * 获取 ModelLogicalMain 的数据库
     * @param planningWarehouseLayerId
     * @return
     */
    Strategy getModelLogicalMainStrategy(Long planningWarehouseLayerId);

    Map<String, Object> statisticsTableType(String projectId);

    Map<String, BigDecimal> houseLayerNameNumber(Long projectId);

}
