package supie.common.quartz.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import supie.common.quartz.object.QuartzJobData;
import supie.common.quartz.object.QuartzJobParam;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Quartz Job 数据操作接口。
 *
 * @author zw
 * @date 2021-06-19
 */
@Service
public class QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 保存新Job。
     *
     * @param jobParam Job的参数对象。
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public void saveNew(QuartzJobParam jobParam) throws ClassNotFoundException, SchedulerException {
        if (StrUtil.isBlankIfStr(jobParam.getJobClassName())) {
            throw new NullPointerException("job执行类的完全限定名不能为空!");
        }
        Class<? extends QuartzJobBean> clazz =
                (Class<? extends QuartzJobBean>) Class.forName(jobParam.getJobClassName());
        String jobGroup;
        if (StrUtil.isBlankIfStr(jobParam.getJobGroup())) {
            jobGroup = clazz.getSimpleName();
        } else {
            jobGroup = jobParam.getJobGroup();
        }
        // 构建job信息
        JobDetail job = JobBuilder.newJob(clazz)
                .withIdentity(jobParam.getJobName(), jobGroup)
                .withDescription(jobParam.getDescription())
                .storeDurably().build();
        if (MapUtil.isNotEmpty(jobParam.getJobDataMap())) {
            job.getJobDataMap().putAll(jobParam.getJobDataMap());
        }
        TriggerBuilder<Trigger> triggerTriggerBuilder = TriggerBuilder.newTrigger();
        triggerTriggerBuilder.withIdentity(jobParam.getJobName(), jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(jobParam.getCronExpression()));
        if (jobParam.getStartTime() != null) triggerTriggerBuilder.startAt(jobParam.getStartTime());
        if (jobParam.getEndTime() != null) triggerTriggerBuilder.endAt(jobParam.getEndTime());
        // 触发时间点
        Trigger trigger = triggerTriggerBuilder.build();
        // 交由Scheduler安排触发
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 删除Job。
     *
     * @param jobName  Job名称。
     * @param jobGroup Job组名。
     */
    public void remove(String jobName, String jobGroup) throws SchedulerException {
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(JobKey.jobKey(jobName, jobGroup));
        if (triggers.size() > 0) {
            if (!"PAUSED".equals(scheduler.getTriggerState(TriggerKey.triggerKey(jobName, jobGroup)).name())) {
                scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
            }
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
        }
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 获取当前Scheduler的所有Job数据列表。
     *
     * @return 当前Scheduler的所有Job数据列表。
     */
    public List<QuartzJobData> getAllJobDataList() throws SchedulerException {
        List<QuartzJobData> jobInfos = new LinkedList<>();
        List<String> groups = scheduler.getJobGroupNames();
        for (String group : groups) {
            GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupEquals(group);
            Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
            for (JobKey jobKey : jobKeys) {
                QuartzJobData jobData = new QuartzJobData();
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                jobData.setJobName(jobKey.getName());
                jobData.setJobGroup(jobKey.getGroup());
                jobData.setJobClassName(jobDetail.getJobClass().getName());
                jobData.setJobDataMap(jobDetail.getJobDataMap());
                Trigger jobTrigger = scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                if (jobTrigger != null) {
                    Trigger.TriggerState state =
                            scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                    jobData.setTriggerName(jobKey.getName());
                    jobData.setTriggerGroup(jobKey.getGroup());
                    CronTrigger cronTrigger = (CronTrigger) jobTrigger;
                    jobData.setCronExpression(cronTrigger.getCronExpression());
                    jobData.setDescription(jobDetail.getDescription());
                    /*
                    NONE -1 不存在
                    NORMAL 0 正常
                    PAUSED 1 暂停
                    COMPLETE 2 完成
                    ERROR 3 错误
                    BLOCKED 4 阻塞
                     */
                    switch (state) {
                        case NONE:
                            jobData.setState("不存在");
                            break;
                        case NORMAL:
                            jobData.setState("正常");
                            break;
                        case PAUSED:
                            jobData.setState("暂停");
                            break;
                        case COMPLETE:
                            jobData.setState("完成");
                            break;
                        case ERROR:
                            jobData.setState("错误");
                            break;
                        case BLOCKED:
                            jobData.setState("阻塞");
                            break;
                    }
                    if (jobTrigger.getStartTime() != null) jobData.setStartTime(jobTrigger.getStartTime());
                    if (jobTrigger.getEndTime() != null) jobData.setEndTime(jobTrigger.getEndTime());
                } else {
                    jobData.setState("已结束");
                }
                jobInfos.add(jobData);
            }
        }
        return jobInfos;
    }

    /**
     * 触发Job。
     *
     * @param jobName        Job名称。
     * @param jobGroup       Job组名。
     */
    public void trigger(String jobName, String jobGroup) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 重新调度Job。
     *
     * @param jobName        Job名称。
     * @param jobGroup       Job组名。
     * @param cronExpression 新的Cron表达式。
     * @return
     */
    public String reschedule(String jobName, String jobGroup, String cronExpression, Date startTime, Date endTime) throws SchedulerException {
        Trigger.TriggerState triggerState =
                scheduler.getTriggerState(TriggerKey.triggerKey(jobName, jobGroup));
        CronTrigger cronTrigger =
                (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(jobName, jobGroup));
        if (cronTrigger == null) {
            // 该任务已经执行结束，需要重新添加进
            return "该任务已结束，无法修改，请重新调度运行！";
        }
        if (!cronTrigger.getCronExpression().equals(cronExpression) || startTime != null || endTime != null) {
            TriggerBuilder<Trigger> triggerTriggerBuilder = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, jobGroup);

            if (startTime != null) triggerTriggerBuilder.startAt(startTime);
            if (endTime != null) triggerTriggerBuilder.endAt(endTime);

            TriggerBuilder<CronTrigger> cronTriggerTriggerBuilder;
            if (cronExpression == null) {
                cronTriggerTriggerBuilder = triggerTriggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronTrigger.getCronExpression()));
            } else {
                cronTriggerTriggerBuilder = triggerTriggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
            }
            CronTrigger newCronTrigger = cronTriggerTriggerBuilder.build();
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobName, jobGroup), newCronTrigger);
            if ("PAUSED".equals(triggerState.name())) {
                this.pause(jobName, jobGroup);
            }
        }
        return "SUCCESS";
    }

    /**
     * 暂定Job。
     *
     * @param jobName        Job名称。
     * @param jobGroup       Job组名。
     */
    public void resume(String jobName, String jobGroup) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 恢复暂定的Job。
     *
     * @param jobName        Job名称。
     * @param jobGroup       Job组名。
     */
    public void pause(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
    }
}
