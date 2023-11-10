package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import supie.webadmin.upms.model.SysUserData;

import java.util.Date;
import java.util.Map;

/**
 * ProjectMemberVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectMemberVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectMemberVo extends BaseVo {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号")
    private Long id;

    /**
     * 数据所属人。
     */
    @ApiModelProperty(value = "数据所属人")
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @ApiModelProperty(value = "数据所属部门")
    private Long dataDeptId;

    /**
     * 成员关联用户表数据。
     */
    @ApiModelProperty(value = "成员关联用户表数据")
    private SysUserData memberUser;

    /**
     * 成员关联用户表id。
     */
    @ApiModelProperty(value = "成员关联用户表id")
    private Long memberUserId;

    /**
     * 关联项目id。
     */
    @ApiModelProperty(value = "关联项目id")
    private Long memberProjectId;
}
