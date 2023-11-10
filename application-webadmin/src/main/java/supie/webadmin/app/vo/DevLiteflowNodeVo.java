package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * DevLiteflowNodeVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowNodeVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DevLiteflowNodeVo extends BaseVo {

    /**
     * 主键id。
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 规则链ID（EL-ID）。
     */
    @ApiModelProperty(value = "规则链ID（EL-ID）")
    private Long rulerId;

    /**
     * 组件ID。
     */
    @ApiModelProperty(value = "组件ID")
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
     * createUserId 字典关联数据。
     */
    @ApiModelProperty(value = "createUserId 字典关联数据")
    private Map<String, Object> createUserIdDictMap;

    /**
     * dataUserId 字典关联数据。
     */
    @ApiModelProperty(value = "dataUserId 字典关联数据")
    private Map<String, Object> dataUserIdDictMap;

    /**
     * dataDeptId 字典关联数据。
     */
    @ApiModelProperty(value = "dataDeptId 字典关联数据")
    private Map<String, Object> dataDeptIdDictMap;
}
