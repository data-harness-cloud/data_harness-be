package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 业务定义-数据维度数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface DefinitionDimensionService extends IBaseService<DefinitionDimension, Long> {

    /**
     * 保存新增对象。
     *
     * @param definitionDimension 新增对象。
     * @return 返回新增对象。
     */
    DefinitionDimension saveNew(DefinitionDimension definitionDimension);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param definitionDimensionList 新增对象列表。
     */
    void saveNewBatch(List<DefinitionDimension> definitionDimensionList);

    /**
     * 更新数据对象。
     *
     * @param definitionDimension         更新的对象。
     * @param originalDefinitionDimension 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(DefinitionDimension definitionDimension, DefinitionDimension originalDefinitionDimension);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getDefinitionDimensionListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<DefinitionDimension> getDefinitionDimensionList(DefinitionDimension filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getDefinitionDimensionList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<DefinitionDimension> getDefinitionDimensionListWithRelation(DefinitionDimension filter, String orderBy);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<DefinitionDimension> getGroupedDefinitionDimensionListWithRelation(
            DefinitionDimension filter, String groupSelect, String groupBy, String orderBy);
}
