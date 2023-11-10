package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DevLiteflowRulerDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowRulerDto对象")
@Data
public class DevLiteflowRulerDto {

    /**
     * 编号。
     */
    @ApiModelProperty(value = "编号", required = true)
    @NotNull(message = "数据验证失败，编号不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 字符编号。
     */
    @ApiModelProperty(value = "字符编号")
    private String strId;

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
     * 应用名称。
     */
    @ApiModelProperty(value = "应用名称")
    private String applicationName;

    /**
     * 流程chain名称。
     */
    @ApiModelProperty(value = "流程chain名称")
    private String chainName;

    /**
     * 流程chain描述。
     */
    @ApiModelProperty(value = "流程chain描述")
    private String chainDesc;

    /**
     * 规则表达式。
     */
    @ApiModelProperty(value = "规则表达式")
    private String elData;

    /**
     * 流程结构JSON数据。
     */
    @ApiModelProperty(value = "流程结构JSON数据")
    private String chainStructureJson;

    /**
     * 上线类型（0：开发中。1：发布上线）。
     */
    @ApiModelProperty(value = "上线类型（0：开发中。1：发布上线）")
    private Integer onlineType;

    /**
     * 上一个版本的ID。
     */
    @ApiModelProperty(value = "上一个版本的ID")
    private Long previousVersionId;

    /**
     * 过程ID。
     */
    @ApiModelProperty(value = "过程ID")
    private Long processId;

    /**
     * 所属项目ID。
     */
    @ApiModelProperty(value = "所属项目ID")
    private Long projectId;

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
     * application_name / chain_name LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
