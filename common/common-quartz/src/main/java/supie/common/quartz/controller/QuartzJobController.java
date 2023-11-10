package supie.common.quartz.controller;

import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import supie.common.quartz.object.QuartzJobData;
import supie.common.quartz.object.QuartzJobParam;
import supie.common.quartz.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import supie.common.core.constant.ErrorCodeEnum;
import supie.common.core.object.ResponseResult;
import supie.common.quartz.utils.QuaryzUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Quartz Job 基本操作接口。
 *
 * @author zw
 * @date 2021-06-19
 */
@Slf4j
//@RestController
//@RequestMapping("/admin/system/job")
public class QuartzJobController<M, MDto, MVo> {

    protected final Class<M> modelClass;
    protected final Class<MDto> modelDtoClass;
    protected final Class<MVo> modelVoClass;

    @Autowired
    private QuartzJobService jobService;

    public QuartzJobController() {
        Class<?> type = getClass();
        while (!(type.getGenericSuperclass() instanceof ParameterizedType)) {
            type = type.getSuperclass();
        }
        modelClass = (Class<M>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0];
        modelDtoClass = (Class<MDto>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[1];
        modelVoClass = (Class<MVo>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[2];
    }

    /**
     * 添加Job任务对象。
     *
     * @param modelJobParam Job的参数对象。
     * @return 添加结果应答对象。
     */
    @ApiOperation("添加Job任务对象")
    @PostMapping("/addJob")
    public ResponseResult<Void> addJob(@RequestBody MDto modelJobParam) {
        QuartzJobParam jobParam = QuaryzUtil.modelDtoToJobParam(modelJobParam);
        try {
            jobService.saveNew(jobParam);
        } catch (Exception e) {
            return ResponseResult.error(ErrorCodeEnum.SERVER_INTERNAL_ERROR, e.getMessage());
        }
        return ResponseResult.success();
    }

    /**
     * 删除Job任务对象。
     *
     * @param modelJobParamDto        Job的参数。
     * @return 删除结果应答对象。
     * @throws SchedulerException 删除异常。
     */
    @ApiOperation("删除Job任务对象")
    @PostMapping("/deleteJob")
    public ResponseResult<Void> deleteJob(
            @RequestBody MDto modelJobParamDto) throws SchedulerException {
        QuartzJobParam jobParam = QuaryzUtil.modelDtoToJobParam(modelJobParamDto);
        jobService.remove(jobParam.getJobName(), jobParam.getJobGroup());
        return ResponseResult.success();
    }

    /**
     * 获取当前Scheduler的所有Job数据列表。
     *
     * @return 当前Scheduler的所有Job数据列表。
     */
    @ApiOperation("获取当前Scheduler的所有Job数据列表")
    @GetMapping("/listJobs")
    public ResponseResult<List<MVo>> getAllJobList() throws SchedulerException {
        List<QuartzJobData> jobDataList = jobService.getAllJobDataList();
        List<MVo> modelList = new LinkedList<>();
        for (QuartzJobData quartzJobData : jobDataList) {
            modelList.add(QuaryzUtil.getModelByJobData(quartzJobData, modelVoClass));
        }
        return ResponseResult.success(modelList);
    }

    /**
     * 触发指定的Job。
     *
     * @param modelJobParam        Job的参数。
     * @return 操作结果应答对象。
     * @throws SchedulerException 操作异常。
     */
    @ApiOperation("触发指定的Job")
    @PostMapping(value = "/triggerJob")
    public ResponseResult<Void> triggerJob(
            @RequestBody MDto modelJobParam) throws SchedulerException {
        QuartzJobParam jobParam = QuaryzUtil.modelDtoToJobParam(modelJobParam);
        jobService.trigger(jobParam.getJobName(), jobParam.getJobGroup());
        return ResponseResult.success();
    }

    /**
     * 暂停指定的Job。
     *
     * @param modelJobParam        Job的参数。
     * @return 操作结果应答对象。
     * @throws SchedulerException 操作异常。
     */
    @ApiOperation("暂停指定的Job")
    @PostMapping(value = "/pauseJob")
    public ResponseResult<Void> pauseJob(
            @RequestBody MDto modelJobParam) throws SchedulerException {
        QuartzJobParam jobParam = QuaryzUtil.modelDtoToJobParam(modelJobParam);
        jobService.pause(jobParam.getJobName(), jobParam.getJobGroup());
        return ResponseResult.success();
    }

    /**
     * 恢复暂停指定的Job。
     *
     * @param modelJobParam        Job的参数。
     * @return 操作结果应答对象。
     * @throws SchedulerException 操作异常。
     */
    @ApiOperation("恢复暂停指定的Job")
    @PostMapping(value = "/resumeJob")
    public ResponseResult<Void> resumeJob(
            @RequestBody MDto modelJobParam) throws SchedulerException {
        QuartzJobParam jobParam = QuaryzUtil.modelDtoToJobParam(modelJobParam);
        jobService.resume(jobParam.getJobName(), jobParam.getJobGroup());
        return ResponseResult.success();
    }

    /**
     * 重新设置指定Job的调度时间。
     *
     * @param modelJobParam        Job的参数。
     * @return 操作结果应答对象。
     * @throws SchedulerException 操作异常。
     */
    @ApiOperation("重新设置指定Job的调度时间")
    @PostMapping(value = "/rescheduleJob")
    public ResponseResult<String> rescheduleJob(
            @RequestBody MDto modelJobParam) throws SchedulerException {
        QuartzJobParam jobParam = QuaryzUtil.modelDtoToJobParam(modelJobParam);
        String resultMsg = jobService.reschedule(jobParam.getJobName(), jobParam.getJobGroup(), jobParam.getCronExpression(), jobParam.getStartTime(), jobParam.getEndTime());
        if ("SUCCESS".equals(resultMsg)) {
            return ResponseResult.success(resultMsg);
        }
        return ResponseResult.error(ErrorCodeEnum.NO_ERROR, resultMsg);
    }
}
