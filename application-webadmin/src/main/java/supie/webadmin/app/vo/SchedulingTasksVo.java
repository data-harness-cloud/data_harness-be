package supie.webadmin.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import supie.common.quartz.object.JobField;
import supie.common.quartz.object.JobFieldType;

import java.util.Date;
import java.util.Map;

/**
 * SchedulingTasksVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("SchedulingTasksVO视图对象")
@Data
public class SchedulingTasksVo {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID")
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
     * 修改时间。
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 修改者ID。
     */
    @ApiModelProperty(value = "修改者ID")
    private Long updateUserId;

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
    @ApiModelProperty(value = "任务状态（未执行、运行中、暂停中）")
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
    @ApiModelProperty(value = "任务执行类型（单次执行、周期执行）")
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
     * projectId 的一对一关联数据对象，数据对应类型为ProjectMainVo。
     */
    @ApiModelProperty(value = "projectId 的一对一关联数据对象，数据对应类型为ProjectMainVo")
    private Map<String, Object> projectMain;

    /**
     * projectId 的一对一关联数据对象，数据对应类型为PlanningProcessVo。
     */
    @ApiModelProperty(value = "projectId 的一对一关联数据对象，数据对应类型为PlanningProcessVo")
    private Map<String, Object> planningProcess;

    /**
     * createUserId 字典关联数据。
     */
    @ApiModelProperty(value = "createUserId 字典关联数据")
    private Map<String, Object> createUserIdDictMap;

    /**
     * dataDeptId 字典关联数据。
     */
    @ApiModelProperty(value = "dataDeptId 字典关联数据")
    private Map<String, Object> dataDeptIdDictMap;

    /**
     * dataUserId 字典关联数据。
     */
    @ApiModelProperty(value = "dataUserId 字典关联数据")
    private Map<String, Object> dataUserIdDictMap;
}
