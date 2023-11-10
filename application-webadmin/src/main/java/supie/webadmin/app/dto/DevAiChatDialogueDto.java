package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DevAiChatDialogueDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevAiChatDialogueDto对象")
@Data
public class DevAiChatDialogueDto {

    /**
     * 主表控制台id。
     */
    @ApiModelProperty(value = "主表控制台id", required = true)
    @NotNull(message = "数据验证失败，主表控制台id不能为空！", groups = {UpdateGroup.class})
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
    @ApiModelProperty(value = "对话标识ID", required = true)
    @NotBlank(message = "数据验证失败，对话标识ID不能为空！")
    private String dialogueStrId;

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
     * dialogue_name / dialogue_question / dialogue_answer LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
