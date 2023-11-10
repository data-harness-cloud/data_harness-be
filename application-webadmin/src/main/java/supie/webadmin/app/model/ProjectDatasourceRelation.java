package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ProjectDatasourceRelationVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * ProjectDatasourceRelation实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_project_datasource_relation")
public class ProjectDatasourceRelation extends BaseModel {

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
     * 项目id。
     */
    private Long projectId;

    /**
     * 数据源id。
     */
    private Long datasourceId;

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

    @Mapper
    public interface ProjectDatasourceRelationModelMapper extends BaseModelMapper<ProjectDatasourceRelationVo, ProjectDatasourceRelation> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param projectDatasourceRelationVo 域对象。
         * @return 实体对象。
         */
        @Override
        ProjectDatasourceRelation toModel(ProjectDatasourceRelationVo projectDatasourceRelationVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param projectDatasourceRelation 实体对象。
         * @return 域对象。
         */
        @Override
        ProjectDatasourceRelationVo fromModel(ProjectDatasourceRelation projectDatasourceRelation);
    }
    public static final ProjectDatasourceRelationModelMapper INSTANCE = Mappers.getMapper(ProjectDatasourceRelationModelMapper.class);
}
