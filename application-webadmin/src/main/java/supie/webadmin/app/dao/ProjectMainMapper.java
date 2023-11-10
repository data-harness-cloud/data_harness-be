package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ProjectMain;
import supie.webadmin.app.model.RemoteHost;
import supie.webadmin.app.model.SeatunnelConfig;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据项目—项目管理主表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ProjectMainMapper extends BaseDaoMapper<ProjectMain> {

    /**
     * 批量插入对象列表。
     *
     * @param projectMainList 新增对象列表。
     */
    void insertList(List<ProjectMain> projectMainList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param projectMainFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<ProjectMain> getGroupedProjectMainList(
            @Param("projectMainFilter") ProjectMain projectMainFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param projectMainFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ProjectMain> getProjectMainList(
            @Param("projectMainFilter") ProjectMain projectMainFilter, @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。同时支持基于一对一从表字段的过滤条件。
     *
     * @param projectMainFilter 主表过滤对象。
     * @param remoteHostFilter 一对多从表过滤对象。
     * @param seatunnelConfigFilter 一对多从表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ProjectMain> getProjectMainListEx(
            @Param("projectMainFilter") ProjectMain projectMainFilter,
            @Param("remoteHostFilter") RemoteHost remoteHostFilter,
            @Param("seatunnelConfigFilter") SeatunnelConfig seatunnelConfigFilter,
            @Param("orderBy") String orderBy);
}
