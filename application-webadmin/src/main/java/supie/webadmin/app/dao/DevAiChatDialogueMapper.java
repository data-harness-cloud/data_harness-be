package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.DevAiChatDialogue;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据开发-AI模块-对话记录数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface DevAiChatDialogueMapper extends BaseDaoMapper<DevAiChatDialogue> {

    /**
     * 批量插入对象列表。
     *
     * @param devAiChatDialogueList 新增对象列表。
     */
    void insertList(List<DevAiChatDialogue> devAiChatDialogueList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param devAiChatDialogueFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<DevAiChatDialogue> getDevAiChatDialogueList(
            @Param("devAiChatDialogueFilter") DevAiChatDialogue devAiChatDialogueFilter, @Param("orderBy") String orderBy);

    /**
     * 查询对话历史记录
     * @param devAiChatDialogueFilter 过滤条件
     * @return 对象列表
     */
    List<DevAiChatDialogue> queryConversationHistory(
            @Param("devAiChatDialogueFilter") DevAiChatDialogue devAiChatDialogueFilter);
}
