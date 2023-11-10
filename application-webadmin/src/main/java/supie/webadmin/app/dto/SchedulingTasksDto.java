package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import supie.common.quartz.object.JobField;
import supie.common.quartz.object.JobFieldType;

import javax.validation.constraints.*;

import java.util.Date;

/**
 * SchedulingTasksDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("SchedulingTasksDto对象")
@Data
public class SchedulingTasksDto {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "数据验证失败，主键ID不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建者ID。
     */
    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    /**
     * 数据所属部门ID。
     */
    @ApiModelProperty(value = "数据所属部门ID")
    private Long dataDeptId;

    /**
     * 数据所属人ID。
     */
    @ApiModelProperty(value = "数据所属人ID")
    private Long dataUserId;

    /**
     * 任务名称。
     */
    @ApiModelProperty(value = "任务名称")
    @JobField(JobFieldType.JOB_NAME)
    private String taskName;

    /**
     * 任务所属分组。
     */
    @ApiModelProperty(value = "任务所属分组")
    @JobField(JobFieldType.JOB_GROUP)
    private String taskGroup;

    /**
     * 任务状态（未执行、运行中、暂停中）。
     */
    @ApiModelProperty(value = "任务状态（未执行、运行中、暂停中）", required = true)
    @NotBlank(message = "数据验证失败，任务状态（未上线、运行中、暂停中）不能为空！")
    @JobField(JobFieldType.STATE)
    private String taskState;

    /**
     * 任务规则链ID。
     */
    @ApiModelProperty(value = "任务规则链ID")
    private Long rulerId;

    /**
     * 任务描述。
     */
    @ApiModelProperty(value = "任务描述")
    @JobField(JobFieldType.DESCRIPTION)
    private String taskDescription;

    /**
     * 任务执行类型（单次执行、周期执行）。
     */
    @ApiModelProperty(value = "任务执行类型（单次执行、周期执行）", required = true)
    @NotNull(message = "数据验证失败，任务执行类型（单次执行、周期执行）不能为空！")
    private Integer runType;

    /**
     * 任务运行次数。
     */
    @ApiModelProperty(value = "任务运行次数")
    private Integer runNumber;

    /**
     * 任务执行类。
     */
    @ApiModelProperty(value = "任务执行类")
    @JobField(JobFieldType.JOB_CLASS_NAME)
    private String taskClassName;

    /**
     * 配置类型（1：常规，2：crontab）。
     */
    @ApiModelProperty(value = "配置类型（1：常规，2：crontab）")
    private Integer configurationType;

    /**
     * 运行失败次数。
     */
    @ApiModelProperty(value = "运行失败次数")
    private Integer errorNumber;

    /**
     * cron表达式。
     */
    @ApiModelProperty(value = "cron表达式")
    @JobField(JobFieldType.CRON_EXPRESSION)
    private String cronExpression;

    /**
     * cron解析信息
     */
    @ApiModelProperty(value = "cron解析信息")
    private String cronAnalyticInformation;

    /**
     * 运行成功次数。
     */
    @ApiModelProperty(value = "运行成功次数")
    private Integer successNumber;

    /**
     * 过程ID。
     */
    @ApiModelProperty(value = "过程ID")
    private Long processId;

    /**
     * 起始时间。
     */
    @ApiModelProperty(value = "起始时间")
    @JobField(JobFieldType.START_TIME)
    private Date startTime;

    /**
     * 任务参数Map类型数据。
     */
    @ApiModelProperty(value = "任务参数Map类型数据")
    @JobField(JobFieldType.JOB_DATA_MAP)
    private String taskDataMap;

    /**
     * 截止时间。
     */
    @ApiModelProperty(value = "截止时间")
    @JobField(JobFieldType.END_TIME)
    private Date endTime;

    /**
     * 所属项目ID。
     */
    @ApiModelProperty(value = "所属项目ID")
    private Long projectId;

    /**
     * 错误信息。
     */
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    /**
     * 业务分类ID。
     */
    @ApiModelProperty(value = "业务分类ID")
    private Long classificationId;

    /**
     * 主题域ID
     */
    @ApiModelProperty(value = "主题域ID")
    private Long processThemeId;

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
     * runNumber 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "runNumber 范围过滤起始值(>=)")
    private Integer runNumberStart;

    /**
     * runNumber 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "runNumber 范围过滤结束值(<=)")
    private Integer runNumberEnd;

    /**
     * errorNumber 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "errorNumber 范围过滤起始值(>=)")
    private Integer errorNumberStart;

    /**
     * errorNumber 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "errorNumber 范围过滤结束值(<=)")
    private Integer errorNumberEnd;

    /**
     * successNumber 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "successNumber 范围过滤起始值(>=)")
    private Integer successNumberStart;

    /**
     * successNumber 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "successNumber 范围过滤结束值(<=)")
    private Integer successNumberEnd;

    /**
     * startTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "startTime 范围过滤起始值(>=)")
    private String startTimeStart;

    /**
     * startTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "startTime 范围过滤结束值(<=)")
    private String startTimeEnd;

    /**
     * endTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "endTime 范围过滤起始值(>=)")
    private String endTimeStart;

    /**
     * endTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "endTime 范围过滤结束值(<=)")
    private String endTimeEnd;

    /**
     * task_name LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
