package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.app.model.ProjectMember;
import supie.webadmin.upms.model.constant.SysUserType;
import supie.webadmin.upms.model.constant.SysUserStatus;
import supie.common.core.upload.UploadStoreTypeEnum;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.upms.vo.SysUserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.List;

/**
 * SysUser实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_sys_user")
public class SysUser extends BaseModel {

    /**
     * 用户Id。
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 登录用户名。
     */
    private String loginName;

    /**
     * 用户密码。
     */
    private String password;

    /**
     * 用户显示名称。
     */
    private String showName;

    /**
     * 用户部门Id。
     */
    private Long deptId;

    /**
     * 用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)。
     */
    private Integer userType;

    /**
     * 用户头像的Url。
     */
    @UploadFlagColumn(storeType = UploadStoreTypeEnum.LOCAL_SYSTEM)
    private String headImageUrl;

    /**
     * 用户状态(0: 正常 1: 锁定)。
     */
    private Integer userStatus;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String createTimeEnd;

    /**
     * 多对多用户部门岗位数据集合。
     */
    @RelationManyToMany(
            relationMasterIdField = "userId",
            relationModelClass = SysUserPost.class)
    @TableField(exist = false)
    private List<SysUserPost> sysUserPostList;

    /**
     * 多对多用户角色数据集合。
     */
    @RelationManyToMany(
            relationMasterIdField = "userId",
            relationModelClass = SysUserRole.class)
    @TableField(exist = false)
    private List<SysUserRole> sysUserRoleList;

    /**
     * 多对多用户数据权限数据集合。
     */
    @RelationManyToMany(
            relationMasterIdField = "userId",
            relationModelClass = SysDataPermUser.class)
    @TableField(exist = false)
    private List<SysDataPermUser> sysDataPermUserList;

    /**
     * userId 的多对多关联表数据对象。
     */
    @TableField(exist = false)
    private ProjectMember projectMember;

    @RelationDict(
            masterIdField = "deptId",
            slaveModelClass = SysDept.class,
            slaveIdField = "deptId",
            slaveNameField = "deptName")
    @TableField(exist = false)
    private Map<String, Object> deptIdDictMap;

    @RelationConstDict(
            masterIdField = "userType",
            constantDictClass = SysUserType.class)
    @TableField(exist = false)
    private Map<String, Object> userTypeDictMap;

    @RelationConstDict(
            masterIdField = "userStatus",
            constantDictClass = SysUserStatus.class)
    @TableField(exist = false)
    private Map<String, Object> userStatusDictMap;

    @Mapper
    public interface SysUserModelMapper extends BaseModelMapper<SysUserVo, SysUser> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param sysUserVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "projectMember", expression = "java(mapToBean(sysUserVo.getProjectMember(), supie.webadmin.app.model.ProjectMember.class))")
        @Mapping(target = "sysUserRoleList", expression = "java(mapToBean(sysUserVo.getSysUserRoleList(), supie.webadmin.upms.model.SysUserRole.class))")
        @Mapping(target = "sysUserPostList", expression = "java(mapToBean(sysUserVo.getSysUserPostList(), supie.webadmin.upms.model.SysUserPost.class))")
        @Mapping(target = "sysDataPermUserList", expression = "java(mapToBean(sysUserVo.getSysDataPermUserList(), supie.webadmin.upms.model.SysDataPermUser.class))")
        @Override
        SysUser toModel(SysUserVo sysUserVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysUser 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "projectMember", expression = "java(beanToMap(sysUser.getProjectMember(), false))")
        @Mapping(target = "sysUserRoleList", expression = "java(beanToMap(sysUser.getSysUserRoleList(), false))")
        @Mapping(target = "sysUserPostList", expression = "java(beanToMap(sysUser.getSysUserPostList(), false))")
        @Mapping(target = "sysDataPermUserList", expression = "java(beanToMap(sysUser.getSysDataPermUserList(), false))")
        @Override
        SysUserVo fromModel(SysUser sysUser);
    }
    public static final SysUserModelMapper INSTANCE = Mappers.getMapper(SysUserModelMapper.class);
}
