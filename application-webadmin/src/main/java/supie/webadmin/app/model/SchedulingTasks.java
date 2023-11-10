package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.quartz.object.JobField;
import supie.common.quartz.object.JobFieldType;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.SchedulingTasksVo;
import lombok.Data;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.Map;

/**
 * SchedulingTasks实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_scheduling_tasks")
public class SchedulingTasks {

    /**
     * 主键ID。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 创建时间。
     */
    private Date createTime;

    /**
     * 创建者ID。
     */
    private Long createUserId;

    /**
     * 修改时间。
     */
    private Date updateTime;

    /**
     * 修改者ID。
     */
    private Long updateUserId;

    /**
     * 数据所属部门ID。
     */
    @DeptFilterColumn
    private Long dataDeptId;

    /**
     * 数据所属人ID。
     */
    @UserFilterColumn
    private Long dataUserId;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 任务名称。
     */
    @JobField(JobFieldType.JOB_NAME)
    private String taskName;

    /**
     * 任务所属分组。
     */
    @JobField(JobFieldType.JOB_GROUP)
    private String taskGroup;

    /**
     * 任务状态（未执行、运行中、暂停中）。
     */
    @JobField(JobFieldType.STATE)
    private String taskState;

    /**
     * 任务规则链ID。
     */
    private Long rulerId;

    /**
     * 任务描述。
     */
    @JobField(JobFieldType.DESCRIPTION)
    private String taskDescription;

    /**
     * 任务执行类型（单次执行、周期执行）。
     */
    private Integer runType;

    /**
     * 任务运行次数。
     */
    private Integer runNumber;

    /**
     * 任务执行类。
     */
    @JobField(JobFieldType.JOB_CLASS_NAME)
    private String taskClassName;

    /**
     * 配置类型（1：常规，2：crontab）。
     */
    private Integer configurationType;

    /**
     * 运行失败次数。
     */
    private Integer errorNumber;

    /**
     * cron表达式。
     */
    @JobField(JobFieldType.CRON_EXPRESSION)
    private String cronExpression;

    /**
     * cron解析信息
     */
    private String cronAnalyticInformation;

    /**
     * 运行成功次数。
     */
    private Integer successNumber;

    /**
     * 过程ID。
     */
    private Long processId;

    /**
     * 起始时间。
     */
    @JobField(JobFieldType.START_TIME)
    private Date startTime;

    /**
     * 任务参数Map类型数据。
     */
    @JobField(JobFieldType.JOB_DATA_MAP)
    private String taskDataMap;

    /**
     * 截止时间。
     */
    @JobField(JobFieldType.END_TIME)
    private Date endTime;

    /**
     * 所属项目ID。
     */
    private Long projectId;

    /**
     * 错误信息。
     */
    private String errorMsg;

    /**
     * 业务分类ID。
     */
    private Long classificationId;

    /**
     * 主题域ID
     */
    private Long processThemeId;

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
     * runNumber 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Integer runNumberStart;

    /**
     * runNumber 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Integer runNumberEnd;

    /**
     * errorNumber 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Integer errorNumberStart;

    /**
     * errorNumber 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Integer errorNumberEnd;

    /**
     * successNumber 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Integer successNumberStart;

    /**
     * successNumber 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Integer successNumberEnd;

    /**
     * startTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String startTimeStart;

    /**
     * startTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String startTimeEnd;

    /**
     * endTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String endTimeStart;

    /**
     * endTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String endTimeEnd;

    /**
     * task_name LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @RelationOneToOne(
            masterIdField = "projectId",
            slaveModelClass = ProjectMain.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private ProjectMain projectMain;

    @RelationOneToOne(
            masterIdField = "processId",
            slaveModelClass = PlanningProcess.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private PlanningProcess planningProcess;

    @RelationOneToOne(
            masterIdField = "classificationId",
            slaveModelClass = PlanningClassification.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private PlanningClassification planningClassification;

    @RelationOneToOne(
            masterIdField = "processThemeId",
            slaveModelClass = PlanningTheme.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private PlanningTheme planningTheme;

    @RelationDict(
            masterIdField = "createUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> createUserIdDictMap;

    @RelationDict(
            masterIdField = "dataDeptId",
            slaveModelClass = SysDept.class,
            slaveIdField = "deptId",
            slaveNameField = "deptName")
    @TableField(exist = false)
    private Map<String, Object> dataDeptIdDictMap;

    @RelationDict(
            masterIdField = "dataUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> dataUserIdDictMap;

    @Mapper
    public interface SchedulingTasksModelMapper extends BaseModelMapper<SchedulingTasksVo, SchedulingTasks> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param schedulingTasksVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "projectMain", expression = "java(mapToBean(schedulingTasksVo.getProjectMain(), supie.webadmin.app.model.ProjectMain.class))")
        @Mapping(target = "planningProcess", expression = "java(mapToBean(schedulingTasksVo.getPlanningProcess(), supie.webadmin.app.model.PlanningProcess.class))")
        @Override
        SchedulingTasks toModel(SchedulingTasksVo schedulingTasksVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param schedulingTasks 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "projectMain", expression = "java(beanToMap(schedulingTasks.getProjectMain(), false))")
        @Mapping(target = "planningProcess", expression = "java(beanToMap(schedulingTasks.getPlanningProcess(), false))")
        @Override
        SchedulingTasksVo fromModel(SchedulingTasks schedulingTasks);
    }
    public static final SchedulingTasksModelMapper INSTANCE = Mappers.getMapper(SchedulingTasksModelMapper.class);
}
