package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * RemoteHostDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("RemoteHostDto对象")
@Data
public class RemoteHostDto {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "数据验证失败，主键ID不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 显示名称。
     */
    @ApiModelProperty(value = "显示名称")
    private String showName;

    /**
     * SSH-IP地址。
     */
    @ApiModelProperty(value = "SSH-IP地址")
    private String hostIp;

    /**
     * 端口。
     */
    @ApiModelProperty(value = "端口")
    private String hostPort;

    /**
     * ssh-key文件存储路径。
     */
    @ApiModelProperty(value = "ssh-key文件存储路径")
    private String hostKeyFilePath;

    /**
     * 登录名。
     */
    @ApiModelProperty(value = "登录名")
    private String loginName;

    /**
     * 密码。
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 数据所属人ID。
     */
    @ApiModelProperty(value = "数据所属人ID")
    private Long dataUserId;

    /**
     * 数据所属部门ID。
     */
    @ApiModelProperty(value = "数据所属部门ID")
    private Long dataDeptId;

    /**
     * 项目关联id。
     */
    @ApiModelProperty(value = "项目关联id")
    private Long projectId;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值(>=)")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值(<=)")
    private String createTimeEnd;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤起始值(>=)")
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤结束值(<=)")
    private String updateTimeEnd;

    /**
     * show_name / login_name LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
