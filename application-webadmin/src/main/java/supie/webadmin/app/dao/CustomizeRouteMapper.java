package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.CustomizeRoute;
import org.apache.ibatis.annotations.Param;

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

}
