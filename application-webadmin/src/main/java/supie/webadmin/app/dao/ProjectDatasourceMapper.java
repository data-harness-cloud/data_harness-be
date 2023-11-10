package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ProjectDatasource;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据项目-数据源表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ProjectDatasourceMapper extends BaseDaoMapper<ProjectDatasource> {

    /**
     * 批量插入对象列表。
     *
     * @param projectDatasourceList 新增对象列表。
     */
    void insertList(List<ProjectDatasource> projectDatasourceList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param projectDatasourceFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<ProjectDatasource> getGroupedProjectDatasourceList(
            @Param("projectDatasourceFilter") ProjectDatasource projectDatasourceFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param projectDatasourceFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ProjectDatasource> getProjectDatasourceList(
            @Param("projectDatasourceFilter") ProjectDatasource projectDatasourceFilter, @Param("orderBy") String orderBy);

    /**
     * 根据关联主表Id，获取关联从表数据列表。
     *
     * @param projectId 关联主表Id。
     * @param projectDatasourceFilter 从表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 从表数据列表。
     */
    List<ProjectDatasource> getProjectDatasourceListByProjectId(
            @Param("projectId") Long projectId,
            @Param("projectDatasourceFilter") ProjectDatasource projectDatasourceFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据关联主表Id，获取关联从表中没有和主表建立关联关系的数据列表。
     *
     * @param projectId 关联主表Id。
     * @param projectDatasourceFilter 过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 与主表没有建立关联的从表数据列表。
     */
    List<ProjectDatasource> getNotInProjectDatasourceListByProjectId(
            @Param("projectId") Long projectId,
            @Param("projectDatasourceFilter") ProjectDatasource projectDatasourceFilter,
            @Param("orderBy") String orderBy);
}
