package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DevLiteflowNodeDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowNodeDto对象")
@Data
public class DevLiteflowNodeDto {

    /**
     * 主键id。
     */
    @ApiModelProperty(value = "主键id", required = true)
    @NotNull(message = "数据验证失败，主键id不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 规则链ID（EL-ID）。
     */
    @ApiModelProperty(value = "规则链ID（EL-ID）", required = true)
    @NotNull(message = "数据验证失败，规则链ID（EL-ID）不能为空！", groups = {UpdateGroup.class})
    private Long rulerId;

    /**
     * 组件ID。
     */
    @ApiModelProperty(value = "组件ID", required = true)
    @NotBlank(message = "数据验证失败，组件ID不能为空！")
    private String nodeId;

    /**
     * 组件标签。
     */
    @ApiModelProperty(value = "组件标签")
    private String nodeTag;

    /**
     * 字段值数据（JSON数据）。
     */
    @ApiModelProperty(value = "字段值数据（JSON数据）")
    private String fieldJsonData;

    /**
     * 上一次执行结果信息
     */
    @ApiModelProperty(value = "上一次执行结果信息")
    private String executionMessage;

    /**
     * 节点名称。
     */
    @ApiModelProperty(value = "节点名称")
    private String name;

    /**
     * 节点状态（1：启用。0：停用）。
     */
    @ApiModelProperty(value = "节点状态（1：启用。0：停用）")
    private Integer status;

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
     * node_tag LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
