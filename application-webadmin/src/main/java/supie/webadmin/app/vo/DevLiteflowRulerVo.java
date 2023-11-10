package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * DevLiteflowRulerVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowRulerVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DevLiteflowRulerVo extends BaseVo {

    /**
     * 编号。
     */
    @ApiModelProperty(value = "编号")
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
