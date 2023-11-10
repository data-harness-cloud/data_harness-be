package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * PlanningProcessDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("PlanningProcessDto对象")
@Data
public class PlanningProcessDto {

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
     * 分层关联项目id。
     */
    @ApiModelProperty(value = "分层关联项目id")
    private Long projectId;

    /**
     * 关联分类id。
     */
    @ApiModelProperty(value = "关联分类id")
    private Long classificationId;

    /**
     * 关联主题域id。
     */
    @ApiModelProperty(value = "关联主题域id")
    private Long processThemeId;

    /**
     * 业务过程名称。
     */
    @ApiModelProperty(value = "业务过程名称")
    private String processName;

    /**
     * 业务过程代码。
     */
    @ApiModelProperty(value = "业务过程代码")
    private String processCode;

    /**
     * 业务过程状态。
     */
    @ApiModelProperty(value = "业务过程状态")
    private String processStatus;

    /**
     * 业务过程描述。
     */
    @ApiModelProperty(value = "业务过程描述")
    private String processDescription;

    /**
     * 父过程id。
     */
    @ApiModelProperty(value = "父过程id")
    private Long processParentId;

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
     * process_name / process_code / process_status / process_description LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
