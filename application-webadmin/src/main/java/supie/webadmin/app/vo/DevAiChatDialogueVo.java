package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * DevAiChatDialogueVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevAiChatDialogueVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DevAiChatDialogueVo extends BaseVo {

    /**
     * 主表控制台id。
     */
    @ApiModelProperty(value = "主表控制台id")
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
     * 对话名称。
     */
    @ApiModelProperty(value = "对话名称")
    private String dialogueName;

    /**
     * 对话问题。
     */
    @ApiModelProperty(value = "对话问题")
    private String dialogueQuestion;

    /**
     * 相应答案。
     */
    @ApiModelProperty(value = "相应答案")
    private String dialogueAnswer;

    /**
     * 问答类型：对话、工具调用、代码执行。
     */
    @ApiModelProperty(value = "问答类型：对话、工具调用、代码执行")
    private String dialogueType;

    /**
     * 数据应用类型：数据探源、生成图表、归因总结等等。
     */
    @ApiModelProperty(value = "数据应用类型：数据探源、生成图表、归因总结等等")
    private String dialogueDataType;

    /**
     * 问答角色。
     */
    @ApiModelProperty(value = "问答角色")
    private String dialogueRole;

    /**
     * 问答预设提示语。
     */
    @ApiModelProperty(value = "问答预设提示语")
    private String dialoguePrompt;

    /**
     * 对话标识ID。
     */
    @ApiModelProperty(value = "对话标识ID")
    private String dialogueStrId;

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
