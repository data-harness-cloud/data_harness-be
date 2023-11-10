package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * ProjectEngineDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectEngineDto对象")
@Data
public class ProjectEngineDto {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号", required = true)
    @NotNull(message = "数据验证失败，租户号不能为空！", groups = {UpdateGroup.class})
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
     * 引擎名称。
     */
    @ApiModelProperty(value = "引擎名称")
    private String engineName;

    /**
     * 引擎类型。
     */
    @ApiModelProperty(value = "引擎类型")
    private String engineType;

    /**
     * 引擎地址。
     */
    @ApiModelProperty(value = "引擎地址")
    private String engineHost;

    /**
     * 引擎端口。
     */
    @ApiModelProperty(value = "引擎端口")
    private String enginePort;

    /**
     * 引擎用户名。
     */
    @ApiModelProperty(value = "引擎用户名")
    private String engineUsername;

    /**
     * 引擎密码。
     */
    @ApiModelProperty(value = "引擎密码")
    private String enginePassword;

    /**
     * 引擎位置。
     */
    @ApiModelProperty(value = "引擎位置")
    private String enginePath;

    /**
     * 引擎配置i文件。
     */
    @ApiModelProperty(value = "引擎配置i文件")
    private String engineConfig;

    /**
     * 引擎状态。
     */
    @ApiModelProperty(value = "引擎状态")
    private String engineStatus;

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
     * engine_name / engine_type / engine_username LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
