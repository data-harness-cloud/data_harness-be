package supie.common.quartz.object;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 添加Quartz Job时的参数对象。
 *
 * @author zw
 * @date 2021-06-19
 */
@Data
public class QuartzJobParam {

    /**
     * job 名称。
     */
    private String jobName;
    /**
     * 任务分组。
     */
    private String jobGroup;
    /**
     * 任务描述。
     */
    private String description;
    /**
     * 作业执行类。
     */
    private String jobClassName;
    /**
     * cron表达式。
     */
    private String cronExpression;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * job的参数数据。
     */
    private Map<String, Object> jobDataMap;
}
