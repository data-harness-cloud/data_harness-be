package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.object.CallResult;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 数据项目—项目管理主表数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface ProjectMainService extends IBaseService<ProjectMain, Long> {

    /**
     * 保存新增对象。
     *
     * @param projectMain 新增对象。
     * @return 返回新增对象。
     */
    ProjectMain saveNew(ProjectMain projectMain);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param projectMainList 新增对象列表。
     */
    void saveNewBatch(List<ProjectMain> projectMainList);

    /**
     * 更新数据对象。
     *
     * @param projectMain         更新的对象。
     * @param originalProjectMain 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(ProjectMain projectMain, ProjectMain originalProjectMain);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getProjectMainListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ProjectMain> getProjectMainList(ProjectMain filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getProjectMainList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param remoteHostFilter 一对多从表过滤对象。
     * @param seatunnelConfigFilter 一对多从表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ProjectMain> getProjectMainListWithRelation(ProjectMain filter, RemoteHost remoteHostFilter, SeatunnelConfig seatunnelConfigFilter, String orderBy);

    /**
     * 判断从表数据是否存在，如果存在就不能删除主对象，否则可以删除主对象。
     * 适用于主表对从表不是强制级联删除的场景。
     *
     * @param projectMain 主表对象。
     * @return 没有关联数据返回true，否则false，同时返回具体的提示信息。
     */
    CallResult verifyRelatedDataBeforeDelete(ProjectMain projectMain);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<ProjectMain> getGroupedProjectMainListWithRelation(
            ProjectMain filter, String groupSelect, String groupBy, String orderBy);

    /**
     * 批量添加多对多关联关系。
     *
     * @param projectMemberList 多对多关联表对象集合。
     * @param memberProjectId 主表Id。
     */
    void addProjectMemberList(List<ProjectMember> projectMemberList, Long memberProjectId);

    /**
     * 更新中间表数据。
     *
     * @param projectMember 中间表对象。
     * @return 更新成功与否。
     */
    boolean updateProjectMember(ProjectMember projectMember);

    /**
     * 获取中间表数据。
     *
     * @param memberProjectId 主表Id。
     * @param memberUserId 从表Id。
     * @return 中间表对象。
     */
    ProjectMember getProjectMember(Long memberProjectId, Long memberUserId);

    /**
     * 移除单条多对多关系。
     *
     * @param memberProjectId 主表Id。
     * @param memberUserId 从表Id。
     * @return 成功返回true，否则false。
     */
    boolean removeProjectMember(Long memberProjectId, Long memberUserId);

    /**
     * 批量添加多对多关联关系。
     *
     * @param projectDatasourceRelationList 多对多关联表对象集合。
     * @param projectId 主表Id。
     */
    void addProjectDatasourceRelationList(List<ProjectDatasourceRelation> projectDatasourceRelationList, Long projectId);

    /**
     * 更新中间表数据。
     *
     * @param projectDatasourceRelation 中间表对象。
     * @return 更新成功与否。
     */
    boolean updateProjectDatasourceRelation(ProjectDatasourceRelation projectDatasourceRelation);

    /**
     * 获取中间表数据。
     *
     * @param projectId 主表Id。
     * @param datasourceId 从表Id。
     * @return 中间表对象。
     */
    ProjectDatasourceRelation getProjectDatasourceRelation(Long projectId, Long datasourceId);

    /**
     * 移除单条多对多关系。
     *
     * @param projectId 主表Id。
     * @param datasourceId 从表Id。
     * @return 成功返回true，否则false。
     */
    boolean removeProjectDatasourceRelation(Long projectId, Long datasourceId);

    /**
     * 新增项目。
     *
     * @param projectMain 项目主表对象。
     * @param memberUserIdList 项目成员用户Id列表。
     * @return 新增结果。
     */
    Long addProject(ProjectMain projectMain, List<Long> memberUserIdList) throws Exception;
}
