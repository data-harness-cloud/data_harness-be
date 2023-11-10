package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ProjectMember;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据项目-项目成员关联表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ProjectMemberMapper extends BaseDaoMapper<ProjectMember> {

    /**
     * 批量插入对象列表。
     *
     * @param projectMemberList 新增对象列表。
     */
    void insertList(List<ProjectMember> projectMemberList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param projectMemberFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ProjectMember> getProjectMemberList(
            @Param("projectMemberFilter") ProjectMember projectMemberFilter, @Param("orderBy") String orderBy);
}
