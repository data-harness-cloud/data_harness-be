package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 自定义动态路由数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface CustomizeRouteService extends IBaseService<CustomizeRoute, Long> {

    /**
     * 保存新增对象。
     *
     * @param customizeRoute 新增对象。
     * @return 返回新增对象。
     */
    CustomizeRoute saveNew(CustomizeRoute customizeRoute);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param customizeRouteList 新增对象列表。
     */
    void saveNewBatch(List<CustomizeRoute> customizeRouteList);

    /**
     * 更新数据对象。
     *
     * @param customizeRoute         更新的对象。
     * @param originalCustomizeRoute 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(CustomizeRoute customizeRoute, CustomizeRoute originalCustomizeRoute);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getCustomizeRouteListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<CustomizeRoute> getCustomizeRouteList(CustomizeRoute filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getCustomizeRouteList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<CustomizeRoute> getCustomizeRouteListWithRelation(CustomizeRoute filter, String orderBy);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<CustomizeRoute> getGroupedCustomizeRouteListWithRelation(
            CustomizeRoute filter, String groupSelect, String groupBy, String orderBy);

    /**
     * 上线 API
     *
     * @param originalCustomizeRoute 原始自定义路线
     * @author 王立宏
     * @date 2023/11/16 05:06
     */
    void registerApi(CustomizeRoute originalCustomizeRoute);

    /**
     * 下线 API
     *
     * @param originalCustomizeRoute 原始自定义路线
     * @author 王立宏
     * @date 2023/11/16 05:07
     */
    void unregisterApi(CustomizeRoute originalCustomizeRoute);

    /**
     * 列出不与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。通常用于查看添加新 [自定义动态路由] 对象的候选列表。
     *
     * @param externalAppId 主表关联字段。
     * @param customizeRouteFilter [自定义动态路由] 过滤对象。
     * @param orderBy 排序参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     * @author 王立宏
     * @date 2023/11/20 10:28
     */
    List<CustomizeRoute> getNotInCustomizeRouteListByExternalAppId(Long externalAppId, CustomizeRoute customizeRouteFilter, String orderBy);

    /**
     * 列出与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。
     *
     * @param externalAppId        主表关联字段。
     * @param customizeRouteFilter 自定义路由过滤器。
     * @param orderBy              排序方式。
     * @return 应答结果对象，返回符合条件的数据列表。
     * @return 列表<自定义路由>
     * @author 王立宏
     * @date 2023/11/20 10:34
     */
    List<CustomizeRoute> getCustomizeRouteListByExternalAppId(Long externalAppId, CustomizeRoute customizeRouteFilter, String orderBy);
}
