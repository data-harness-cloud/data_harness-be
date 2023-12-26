package supie.webadmin.app.controller;

import cn.hutool.core.util.StrUtil;
import org.quartz.SchedulerException;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.common.quartz.controller.QuartzJobController;
import supie.webadmin.app.service.impl.SchedulingTasksServiceImpl;
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
 * 数据开发-任务调度-任务表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据开发-任务调度-任务表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/schedulingTasks")
public class SchedulingTasksController extends QuartzJobController<SchedulingTasks, SchedulingTasksDto, SchedulingTasksVo> {

    @Autowired
    private SchedulingTasksService schedulingTasksService;

    /**
     * 新增数据开发-任务调度-任务表数据。
     *
     * @param schedulingTasksDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "schedulingTasksDto.id",
            "schedulingTasksDto.searchString",
            "schedulingTasksDto.creatTimeStart",
            "schedulingTasksDto.creatTimeEnd",
            "schedulingTasksDto.updateTimeStart",
            "schedulingTasksDto.updateTimeEnd",
            "schedulingTasksDto.runNumberStart",
            "schedulingTasksDto.runNumberEnd",
            "schedulingTasksDto.errorNumberStart",
            "schedulingTasksDto.errorNumberEnd",
            "schedulingTasksDto.successNumberStart",
            "schedulingTasksDto.successNumberEnd",
            "schedulingTasksDto.startTimeStart",
            "schedulingTasksDto.startTimeEnd",
            "schedulingTasksDto.endTimeStart",
            "schedulingTasksDto.endTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody SchedulingTasksDto schedulingTasksDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(schedulingTasksDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SchedulingTasks schedulingTasks = MyModelUtil.copyTo(schedulingTasksDto, SchedulingTasks.class);
        // 验证关联Id的数据合法性
        CallResult callResult = schedulingTasksService.verifyRelatedData(schedulingTasks, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        schedulingTasks = schedulingTasksService.saveNew(schedulingTasks);
        return ResponseResult.success(schedulingTasks.getId());
    }

    /**
     * 添加Job任务对象。
     *
     * @param modelJobParam Job的参数对象。
     * @return 添加结果应答对象。
     */
    @Override
    public ResponseResult<Void> addJob(SchedulingTasksDto modelJobParam) {
        if (StrUtil.isBlankIfStr(modelJobParam.getTaskClassName())) {
            modelJobParam.setTaskClassName(SchedulingTasksServiceImpl.TASK_CLASS.getCanonicalName());
        }
        if (StrUtil.isBlankIfStr(modelJobParam.getTaskGroup())) {
            modelJobParam.setTaskGroup(SchedulingTasksServiceImpl.TASK_CLASS.getSimpleName());
        }
        return super.addJob(modelJobParam);
    }

    /**
     * 更新数据开发-任务调度-任务表数据。
     *
     * @param schedulingTasksDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "schedulingTasksDto.searchString",
            "schedulingTasksDto.creatTimeStart",
            "schedulingTasksDto.creatTimeEnd",
            "schedulingTasksDto.updateTimeStart",
            "schedulingTasksDto.updateTimeEnd",
            "schedulingTasksDto.runNumberStart",
            "schedulingTasksDto.runNumberEnd",
            "schedulingTasksDto.errorNumberStart",
            "schedulingTasksDto.errorNumberEnd",
            "schedulingTasksDto.successNumberStart",
            "schedulingTasksDto.successNumberEnd",
            "schedulingTasksDto.startTimeStart",
            "schedulingTasksDto.startTimeEnd",
            "schedulingTasksDto.endTimeStart",
            "schedulingTasksDto.endTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody SchedulingTasksDto schedulingTasksDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(schedulingTasksDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SchedulingTasks schedulingTasks = MyModelUtil.copyTo(schedulingTasksDto, SchedulingTasks.class);
        SchedulingTasks originalSchedulingTasks = schedulingTasksService.getById(schedulingTasks.getId());
        if (originalSchedulingTasks == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = schedulingTasksService.verifyRelatedData(schedulingTasks, originalSchedulingTasks);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!schedulingTasksService.update(schedulingTasks, originalSchedulingTasks)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据开发-任务调度-任务表数据。
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
     * 列出符合过滤条件的数据开发-任务调度-任务表列表。
     *
     * @param schedulingTasksDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<SchedulingTasksVo>> list(
            @MyRequestBody SchedulingTasksDto schedulingTasksDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SchedulingTasks schedulingTasksFilter = MyModelUtil.copyTo(schedulingTasksDtoFilter, SchedulingTasks.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SchedulingTasks.class);
        List<SchedulingTasks> schedulingTasksList =
                schedulingTasksService.getSchedulingTasksListWithRelation(schedulingTasksFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(schedulingTasksList, SchedulingTasks.INSTANCE));
    }

    /**
     * 查看指定数据开发-任务调度-任务表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<SchedulingTasksVo> view(@RequestParam Long id) {
        SchedulingTasks schedulingTasks = schedulingTasksService.getByIdWithRelation(id, MyRelationParam.full());
        if (schedulingTasks == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SchedulingTasksVo schedulingTasksVo = SchedulingTasks.INSTANCE.fromModel(schedulingTasks);
        return ResponseResult.success(schedulingTasksVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        SchedulingTasks originalSchedulingTasks = schedulingTasksService.getById(id);
        if (originalSchedulingTasks == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!schedulingTasksService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        try {
            super.deleteJob(MyModelUtil.copyTo(originalSchedulingTasks, SchedulingTasksDto.class));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.success();
    }
}
