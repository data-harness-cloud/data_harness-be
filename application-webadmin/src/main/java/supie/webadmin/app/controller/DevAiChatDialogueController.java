package supie.webadmin.app.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.app.vo.*;
import supie.webadmin.app.dto.*;
import supie.webadmin.app.model.*;
import supie.webadmin.app.service.*;
import supie.common.core.object.*;
import supie.common.core.util.*;
import supie.common.core.constant.*;
import supie.common.core.annotation.MyRequestBody;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 数据开发-AI模块-对话记录操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据开发-AI模块-对话记录管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/devAiChatDialogue")
public class DevAiChatDialogueController {

    @Autowired
    private DevAiChatDialogueService devAiChatDialogueService;

    /**
     * 新增数据开发-AI模块-对话记录数据。
     *
     * @param devAiChatDialogueDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devAiChatDialogueDto.id",
            "devAiChatDialogueDto.searchString",
            "devAiChatDialogueDto.updateTimeStart",
            "devAiChatDialogueDto.updateTimeEnd",
            "devAiChatDialogueDto.createTimeStart",
            "devAiChatDialogueDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DevAiChatDialogueDto devAiChatDialogueDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devAiChatDialogueDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevAiChatDialogue devAiChatDialogue = MyModelUtil.copyTo(devAiChatDialogueDto, DevAiChatDialogue.class);
        // 验证关联Id的数据合法性
        CallResult callResult = devAiChatDialogueService.verifyRelatedData(devAiChatDialogue, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        devAiChatDialogue = devAiChatDialogueService.saveNew(devAiChatDialogue);
        return ResponseResult.success(devAiChatDialogue.getId());
    }

    /**
     * 更新数据开发-AI模块-对话记录数据。
     *
     * @param devAiChatDialogueDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devAiChatDialogueDto.searchString",
            "devAiChatDialogueDto.updateTimeStart",
            "devAiChatDialogueDto.updateTimeEnd",
            "devAiChatDialogueDto.createTimeStart",
            "devAiChatDialogueDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DevAiChatDialogueDto devAiChatDialogueDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devAiChatDialogueDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevAiChatDialogue devAiChatDialogue = MyModelUtil.copyTo(devAiChatDialogueDto, DevAiChatDialogue.class);
        DevAiChatDialogue originalDevAiChatDialogue = devAiChatDialogueService.getById(devAiChatDialogue.getId());
        if (originalDevAiChatDialogue == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = devAiChatDialogueService.verifyRelatedData(devAiChatDialogue, originalDevAiChatDialogue);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!devAiChatDialogueService.update(devAiChatDialogue, originalDevAiChatDialogue)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据开发-AI模块-对话记录数据。
     *
     * @param id 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long id) {
        if (MyCommonUtil.existBlankArgument(id)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(id);
    }

    /**
     * 列出符合过滤条件的数据开发-AI模块-对话记录列表。
     *
     * @param devAiChatDialogueDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DevAiChatDialogueVo>> list(
            @MyRequestBody DevAiChatDialogueDto devAiChatDialogueDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DevAiChatDialogue devAiChatDialogueFilter = MyModelUtil.copyTo(devAiChatDialogueDtoFilter, DevAiChatDialogue.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DevAiChatDialogue.class);
        List<DevAiChatDialogue> devAiChatDialogueList =
                devAiChatDialogueService.getDevAiChatDialogueListWithRelation(devAiChatDialogueFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(devAiChatDialogueList, DevAiChatDialogue.INSTANCE));
    }

    /**
     * 查看指定数据开发-AI模块-对话记录对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DevAiChatDialogueVo> view(@RequestParam Long id) {
        DevAiChatDialogue devAiChatDialogue = devAiChatDialogueService.getByIdWithRelation(id, MyRelationParam.full());
        if (devAiChatDialogue == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DevAiChatDialogueVo devAiChatDialogueVo = DevAiChatDialogue.INSTANCE.fromModel(devAiChatDialogue);
        return ResponseResult.success(devAiChatDialogueVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DevAiChatDialogue originalDevAiChatDialogue = devAiChatDialogueService.getById(id);
        if (originalDevAiChatDialogue == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!devAiChatDialogueService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 查询对话历史记录
     * @param devAiChatDialogueDtoFilter 过滤对象。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "查询对话历史记录", notes = "查询对话历史记录")
    @PostMapping("/queryConversationHistory")
    public ResponseResult<MyPageData<DevAiChatDialogueVo>> queryConversationHistory(
            @MyRequestBody DevAiChatDialogueDto devAiChatDialogueDtoFilter, @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DevAiChatDialogue devAiChatDialogueFilter = MyModelUtil.copyTo(devAiChatDialogueDtoFilter, DevAiChatDialogue.class);
        List<DevAiChatDialogue> devAiChatDialogueList =
                devAiChatDialogueService.queryConversationHistory(devAiChatDialogueFilter);
        return ResponseResult.success(MyPageUtil.makeResponseData(devAiChatDialogueList, DevAiChatDialogue.INSTANCE));
    }

    /**
     * 根据 对话记录id 查询对话记录
     * @param dialogueStrId 对话记录id
     * @return
     */
    @ApiOperation(value = "根据 对话记录id 查询对话记录", notes = "根据 对话记录id 查询对话记录")
    @PostMapping("/queryConversationHistoryByDialogueStrId")
    public ResponseResult<List<DevAiChatDialogueVo>> queryConversationHistoryByDialogueStrId(@MyRequestBody String dialogueStrId) {
        if (StrUtil.isBlank(dialogueStrId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        List<DevAiChatDialogue> devAiChatDialogueList =
                devAiChatDialogueService.queryConversationHistoryByDialogueStrId(dialogueStrId);
        List<DevAiChatDialogueVo> devAiChatDialogueVoList = new LinkedList<>();
        if (devAiChatDialogueList != null && devAiChatDialogueList.size() > 0) {
            for (DevAiChatDialogue devAiChatDialogue : devAiChatDialogueList) {
                devAiChatDialogueVoList.add(DevAiChatDialogue.INSTANCE.fromModel(devAiChatDialogue));
            }
        }
        return ResponseResult.success(devAiChatDialogueVoList);
    }

}
