package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ExternalAppCustomizeRoute;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 外部APP与动态路由关联表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ExternalAppCustomizeRouteMapper extends BaseDaoMapper<ExternalAppCustomizeRoute> {

    /**
     * 批量插入对象列表。
     *
     * @param externalAppCustomizeRouteList 新增对象列表。
     */
    void insertList(List<ExternalAppCustomizeRoute> externalAppCustomizeRouteList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param externalAppCustomizeRouteFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ExternalAppCustomizeRoute> getExternalAppCustomizeRouteList(
            @Param("externalAppCustomizeRouteFilter") ExternalAppCustomizeRoute externalAppCustomizeRouteFilter, @Param("orderBy") String orderBy);
}
