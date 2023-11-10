package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ProjectMainVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.List;

/**
 * ProjectMain实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_project_main")
public class ProjectMain extends BaseModel {

    /**
     * 租户号。
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
     * 项目名称。
     */
    private String projectName;

    /**
     * 项目描述。
     */
    private String projectDescription;

    /**
     * 项目存算引擎。
     */
    private Long projectEngineId;

    /**
     * 项目状态。
     */
    private Integer projectCurrentsStatus;

    /**
     * 项目组名称。
     */
    private String projectGroupName;

    /**
     * 项目负责人。
     */
    private Long projectHeaderId;

    /**
     * 项目流程状态。
     */
    @FlowStatusColumn
    private Long projectFlowStatus;

    /**
     * 项目审批状态字段。
     */
    @FlowLatestApprovalStatusColumn
    private Long projectFlowApproveStatus;

    /**
     * 项目英文名称。
     */
    private String projectCode;

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
     * project_name / project_description / project_group_name LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    /**
     * RemoteHost 的一对多关联表数据对象。
     * 通常在一对多的关联中，我们基于从表数据过滤主表数据，此时需要先对从表数据进行嵌套子查询过滤，并将从表过滤数据列表集成到该字段。
     */
    @RelationOneToMany(
            masterIdField = "id",
            slaveModelClass = RemoteHost.class,
            slaveIdField = "projectId")
    @TableField(exist = false)
    private List<RemoteHost> remoteHostList;

    /**
     * SeatunnelConfig 的一对多关联表数据对象。
     * 通常在一对多的关联中，我们基于从表数据过滤主表数据，此时需要先对从表数据进行嵌套子查询过滤，并将从表过滤数据列表集成到该字段。
     */
    @RelationOneToMany(
            masterIdField = "id",
            slaveModelClass = SeatunnelConfig.class,
            slaveIdField = "projectId")
    @TableField(exist = false)
    private List<SeatunnelConfig> seatunnelConfigList;

    @RelationOneToOne(
            masterIdField = "projectEngineId",
            slaveModelClass = ProjectEngine.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private ProjectEngine projectEngine;

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

    @RelationDict(
            masterIdField = "projectHeaderId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> projectHeaderIdDictMap;

    @RelationGlobalDict(
            masterIdField = "projectCurrentsStatus",
            dictCode = "ProjectStatus")
    @TableField(exist = false)
    private Map<String, Object> projectCurrentsStatusDictMap;

    @Mapper
    public interface ProjectMainModelMapper extends BaseModelMapper<ProjectMainVo, ProjectMain> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param projectMainVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "projectEngine", expression = "java(mapToBean(projectMainVo.getProjectEngine(), supie.webadmin.app.model.ProjectEngine.class))")
        @Mapping(target = "remoteHostList", expression = "java(mapToBean(projectMainVo.getRemoteHostList(), supie.webadmin.app.model.RemoteHost.class))")
        @Mapping(target = "seatunnelConfigList", expression = "java(mapToBean(projectMainVo.getSeatunnelConfigList(), supie.webadmin.app.model.SeatunnelConfig.class))")
        @Override
        ProjectMain toModel(ProjectMainVo projectMainVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param projectMain 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "projectEngine", expression = "java(beanToMap(projectMain.getProjectEngine(), false))")
        @Mapping(target = "remoteHostList", expression = "java(beanToMap(projectMain.getRemoteHostList(), false))")
        @Mapping(target = "seatunnelConfigList", expression = "java(beanToMap(projectMain.getSeatunnelConfigList(), false))")
        @Override
        ProjectMainVo fromModel(ProjectMain projectMain);
    }
    public static final ProjectMainModelMapper INSTANCE = Mappers.getMapper(ProjectMainModelMapper.class);
}
