package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * ProjectMainDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectMainDto对象")
@Data
public class ProjectMainDto {

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
     * 项目名称。
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目描述。
     */
    @ApiModelProperty(value = "项目描述")
    private String projectDescription;

    /**
     * 项目存算引擎。
     */
    @ApiModelProperty(value = "项目存算引擎")
    private Long projectEngineId;

    /**
     * 项目状态。
     */
    @ApiModelProperty(value = "项目状态")
    private Integer projectCurrentsStatus;

    /**
     * 项目组名称。
     */
    @ApiModelProperty(value = "项目组名称")
    private String projectGroupName;

    /**
     * 项目负责人。
     */
    @ApiModelProperty(value = "项目负责人")
    private Long projectHeaderId;

    /**
     * 项目流程状态。
     */
    @ApiModelProperty(value = "项目流程状态")
    private Long projectFlowStatus;

    /**
     * 项目审批状态字段。
     */
    @ApiModelProperty(value = "项目审批状态字段")
    private Long projectFlowApproveStatus;

    /**
     * 项目英文名称。
     */
    @ApiModelProperty(value = "项目英文名称", required = true)
    @NotBlank(message = "数据验证失败，项目英文名称不能为空！")
    private String projectCode;

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
     * project_name / project_description / project_group_name LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
