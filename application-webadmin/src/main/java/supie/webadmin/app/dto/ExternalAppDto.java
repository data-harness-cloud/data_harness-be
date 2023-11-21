package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * ExternalAppDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ExternalAppDto对象")
@Data
public class ExternalAppDto {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "数据验证失败，主键ID不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 应用名称。
     */
    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     * App描述。
     */
    @ApiModelProperty(value = "App描述")
    private String appDescribe;

    /**
     * AppKey。
     */
    @ApiModelProperty(value = "AppKey")
    private String appKey;

    /**
     * 认证方式（1：key认证。2：无认证）。
     */
    @ApiModelProperty(value = "认证方式（1：key认证。2：无认证）", required = true)
    @NotNull(message = "数据验证失败，认证方式（1：key认证。2：无认证）不能为空！")
    private Integer authenticationMethod;

    /**
     * 过程ID。
     */
    @ApiModelProperty(value = "过程ID")
    private Long processId;

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
     * app_name / app_describe / app_key LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
