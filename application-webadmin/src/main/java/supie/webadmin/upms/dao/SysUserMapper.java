package supie.webadmin.upms.dao;

import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.upms.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 用户管理数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface SysUserMapper extends BaseDaoMapper<SysUser> {

    /**
     * 批量插入对象列表。
     *
     * @param sysUserList 新增对象列表。
     */
    void insertList(List<SysUser> sysUserList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param sysUserFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<SysUser> getSysUserList(
            @Param("sysUserFilter") SysUser sysUserFilter, @Param("orderBy") String orderBy);

    /**
     * 根据关联主表Id，获取关联从表数据列表。
     *
     * @param memberProjectId 关联主表Id。
     * @param sysUserFilter 从表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 从表数据列表。
     */
    List<SysUser> getSysUserListByMemberProjectId(
            @Param("memberProjectId") Long memberProjectId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据关联主表Id，获取关联从表中没有和主表建立关联关系的数据列表。
     *
     * @param memberProjectId 关联主表Id。
     * @param sysUserFilter 过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 与主表没有建立关联的从表数据列表。
     */
    List<SysUser> getNotInSysUserListByMemberProjectId(
            @Param("memberProjectId") Long memberProjectId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据部门Id集合，获取关联的用户列表。
     *
     * @param deptIds       关联的部门Id集合。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和部门Id集合关联的用户列表。
     */
    List<SysUser> getSysUserListByDeptIds(
            @Param("deptIds") Set<Long> deptIds,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据登录名集合，获取关联的用户列表。
     * @param loginNames    登录名集合。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和登录名集合关联的用户列表。
     */
    List<SysUser> getSysUserListByLoginNames(
            @Param("loginNames") List<String> loginNames,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据角色Id，获取关联的用户列表。
     *
     * @param roleId        关联的角色Id。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和角色Id关联的用户列表。
     */
    List<SysUser> getSysUserListByRoleId(
            @Param("roleId") Long roleId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据角色Id集合，获取去重后的用户Id列表。
     *
     * @param roleIds       关联的角色Id集合。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和角色Id集合关联的去重后的用户Id列表。
     */
    List<Long> getUserIdListByRoleIds(
            @Param("roleIds") Set<Long> roleIds,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据角色Id，获取和当前角色Id没有建立多对多关联关系的用户列表。
     *
     * @param roleId        关联的角色Id。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy order by从句的参数。
     * @return 和RoleId没有建立关联关系的用户列表。
     */
    List<SysUser> getNotInSysUserListByRoleId(
            @Param("roleId") Long roleId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据数据权限Id，获取关联的用户列表。
     *
     * @param dataPermId    关联的数据权限Id。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy order by从句的参数。
     * @return 和DataPermId关联的用户列表。
     */
    List<SysUser> getSysUserListByDataPermId(
            @Param("dataPermId") Long dataPermId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据数据权限Id，获取和当前数据权限Id没有建立多对多关联关系的用户列表。
     *
     * @param dataPermId    关联的数据权限Id。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy order by从句的参数。
     * @return 和DataPermId没有建立关联关系的用户列表。
     */
    List<SysUser> getNotInSysUserListByDataPermId(
            @Param("dataPermId") Long dataPermId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据部门岗位Id集合，获取关联的去重后的用户Id列表。
     *
     * @param deptPostIds   关联的部门岗位Id集合。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和部门岗位Id集合关联的去重后的用户Id列表。
     */
    List<Long> getUserIdListByDeptPostIds(
            @Param("deptPostIds") Set<Long> deptPostIds,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据部门岗位Id，获取关联的用户列表。
     *
     * @param deptPostId    关联的部门岗位Id。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和部门岗位Id关联的用户列表。
     */
    List<SysUser> getSysUserListByDeptPostId(
            @Param("deptPostId") Long deptPostId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据部门岗位Id，获取和当前部门岗位Id没有建立多对多关联关系的用户列表。
     *
     * @param deptPostId    关联的部门岗位Id。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和deptPostId没有建立关联关系的用户列表。
     */
    List<SysUser> getNotInSysUserListByDeptPostId(
            @Param("deptPostId") Long deptPostId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据岗位Id集合，获取关联的去重后的用户Id列表。
     *
     * @param postIds       关联的岗位Id集合。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和岗位Id集合关联的去重后的用户Id列表。
     */
    List<Long> getUserIdListByPostIds(
            @Param("postIds") Set<Long> postIds,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据岗位Id，获取关联的用户列表。
     *
     * @param postId        关联的岗位Id。
     * @param sysUserFilter 用户过滤条件对象。
     * @param orderBy       order by从句的参数。
     * @return 和岗位Id关联的用户列表。
     */
    List<SysUser> getSysUserListByPostId(
            @Param("postId") Long postId,
            @Param("sysUserFilter") SysUser sysUserFilter,
            @Param("orderBy") String orderBy);

    /**
     * 查询用户的权限资源地址列表。同时返回详细的分配路径。
     *
     * @param userId 用户Id。
     * @param url    url过滤条件。
     * @return 包含从用户到权限资源的完整权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysPermListWithDetail(
            @Param("userId") Long userId, @Param("url") String url);

    /**
     * 查询用户的权限字列表。同时返回详细的分配路径。
     *
     * @param userId   用户Id。
     * @param permCode 权限字名称过滤条件。
     * @return 包含从用户到权限字的权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysPermCodeListWithDetail(
            @Param("userId") Long userId, @Param("permCode") String permCode);

    /**
     * 查询用户的菜单列表。同时返回详细的分配路径。
     *
     * @param userId   用户Id。
     * @param menuName 菜单名称过滤条件。
     * @return 包含从用户到菜单的权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysMenuListWithDetail(
            @Param("userId") Long userId, @Param("menuName") String menuName);
}
