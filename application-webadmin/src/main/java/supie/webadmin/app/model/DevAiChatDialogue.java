package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DevAiChatDialogueVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * DevAiChatDialogue实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_dev_ai_chat_dialogue")
public class DevAiChatDialogue extends BaseModel {

    /**
     * 主表控制台id。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 数据所属人。
     */
    @UserFilterColumn
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @DeptFilterColumn
    private Long dataDeptId;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 对话名称。
     */
    private String dialogueName;

    /**
     * 对话问题。
     */
    private String dialogueQuestion;

    /**
     * 相应答案。
     */
    private String dialogueAnswer;

    /**
     * 问答类型：对话、工具调用、代码执行。
     */
    private String dialogueType;

    /**
     * 数据应用类型：数据探源、生成图表、归因总结等等。
     */
    private String dialogueDataType;

    /**
     * 问答角色。
     */
    private String dialogueRole;

    /**
     * 问答预设提示语。
     */
    private String dialoguePrompt;

    /**
     * 对话标识ID。
     */
    private String dialogueStrId;

    /**
     * 对话标识ID列表(用于查询时作为 dialogueStrId 查询条件)。
     */
    @TableField(exist = false)
    private List<String> dialogueStrIdList;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String updateTimeEnd;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String createTimeEnd;

    /**
     * dialogue_name / dialogue_question / dialogue_answer LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @RelationDict(
            masterIdField = "createUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> createUserIdDictMap;

    @RelationDict(
            masterIdField = "dataUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> dataUserIdDictMap;

    @RelationDict(
            masterIdField = "dataDeptId",
            slaveModelClass = SysDept.class,
            slaveIdField = "deptId",
            slaveNameField = "deptName")
    @TableField(exist = false)
    private Map<String, Object> dataDeptIdDictMap;

    @Mapper
    public interface DevAiChatDialogueModelMapper extends BaseModelMapper<DevAiChatDialogueVo, DevAiChatDialogue> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param devAiChatDialogueVo 域对象。
         * @return 实体对象。
         */
        @Override
        DevAiChatDialogue toModel(DevAiChatDialogueVo devAiChatDialogueVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param devAiChatDialogue 实体对象。
         * @return 域对象。
         */
        @Override
        DevAiChatDialogueVo fromModel(DevAiChatDialogue devAiChatDialogue);
    }
    public static final DevAiChatDialogueModelMapper INSTANCE = Mappers.getMapper(DevAiChatDialogueModelMapper.class);
}
