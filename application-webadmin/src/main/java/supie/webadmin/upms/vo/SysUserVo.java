package supie.webadmin.upms.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * SysUserVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("SysUserVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserVo extends BaseVo {

    /**
     * 用户Id。
     */
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**
     * 登录用户名。
     */
    @ApiModelProperty(value = "登录用户名")
    private String loginName;

    /**
     * 用户显示名称。
     */
    @ApiModelProperty(value = "用户显示名称")
    private String showName;

    /**
     * 用户部门Id。
     */
    @ApiModelProperty(value = "用户部门Id")
    private Long deptId;

    /**
     * 用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)。
     */
    @ApiModelProperty(value = "用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)")
    private Integer userType;

    /**
     * 用户头像的Url。
     */
    @ApiModelProperty(value = "用户头像的Url")
    private String headImageUrl;

    /**
     * 用户状态(0: 正常 1: 锁定)。
     */
    @ApiModelProperty(value = "用户状态(0: 正常 1: 锁定)")
    private Integer userStatus;

    /**
     * 多对多用户岗位数据集合。
     */
    @ApiModelProperty(value = "多对多用户岗位数据集合")
    private List<Map<String, Object>> sysUserPostList;

    /**
     * 多对多用户角色数据集合。
     */
    @ApiModelProperty(value = "多对多用户角色数据集合")
    private List<Map<String, Object>> sysUserRoleList;

    /**
     * 多对多用户数据权限数据集合。
     */
    @ApiModelProperty(value = "多对多用户数据权限数据集合")
    private List<Map<String, Object>> sysDataPermUserList;

    /**
     * userId 的多对多关联表数据对象，数据对应类型为ProjectMemberVo。
     */
    @ApiModelProperty(value = "userId 的多对多关联表数据对象，数据对应类型为ProjectMemberVo")
    private Map<String, Object> projectMember;

    /**
     * deptId 字典关联数据。
     */
    @ApiModelProperty(value = "deptId 字典关联数据")
    private Map<String, Object> deptIdDictMap;

    /**
     * userType 常量字典关联数据。
     */
    @ApiModelProperty(value = "userType 常量字典关联数据")
    private Map<String, Object> userTypeDictMap;

    /**
     * userStatus 常量字典关联数据。
     */
    @ApiModelProperty(value = "userStatus 常量字典关联数据")
    private Map<String, Object> userStatusDictMap;
}
