package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.CustomizeRoute;
import org.apache.ibatis.annotations.Param;
import supie.webadmin.app.model.ExternalAppCustomizeRoute;

import java.util.*;

/**
 * 自定义动态路由数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface CustomizeRouteMapper extends BaseDaoMapper<CustomizeRoute> {

    /**
     * 批量插入对象列表。
     *
     * @param customizeRouteList 新增对象列表。
     */
    void insertList(List<CustomizeRoute> customizeRouteList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param customizeRouteFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<CustomizeRoute> getGroupedCustomizeRouteList(
            @Param("customizeRouteFilter") CustomizeRoute customizeRouteFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param customizeRouteFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<CustomizeRoute> getCustomizeRouteList(
            @Param("customizeRouteFilter") CustomizeRoute customizeRouteFilter, @Param("orderBy") String orderBy);

    /**
     * 查询需要注册的路径
     * @return
     */
    List<CustomizeRoute> queryRegisterApi();

    /**
     * 列出与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。
     *
     * @param externalAppId        主表关联字段。
     * @param customizeRouteFilter 自定义路由过滤器。
     * @param orderBy              排序方式。
     * @return 应答结果对象，返回符合条件的数据列表。
     * @author 王立宏
     * @date 2023/11/21 03:55
     */
    List<CustomizeRoute> getCustomizeRouteListByExternalAppId(
            @Param("externalAppId") Long externalAppId,
            @Param("customizeRouteFilter") CustomizeRoute customizeRouteFilter,
            @Param("orderBy") String orderBy);

    /**
     * 查询externalAppId关联的CustomizeRoute
     *
     * @param externalAppId 外部应用 ID
     * @return 列表<自定义路由>
     * @author 王立宏
     * @date 2023/11/22 04:32
     */
    List<CustomizeRoute> queryAssociatedCustomizeRoute(@Param("externalAppId") Long externalAppId);

    /**
     * 通过 外部应用与自定义路由关联信息 查询自定义路由信息
     *
     * @param externalAppCustomizeRouteFilter 外部应用自定义路由
     * @return 自定义路由信息集
     * @author 王立宏
     * @date 2023/11/22 05:25
     */
    List<CustomizeRoute> queryCustomizeRouteByExternalAppCustomizeRoute(
            @Param("externalAppCustomizeRouteFilter") ExternalAppCustomizeRoute externalAppCustomizeRouteFilter);

}
