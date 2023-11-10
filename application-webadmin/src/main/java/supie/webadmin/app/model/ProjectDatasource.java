package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.webadmin.app.model.constant.DataSourceConnectStatus;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ProjectDatasourceVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * ProjectDatasource实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_project_datasource")
public class ProjectDatasource extends BaseModel {

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
     * 数据源名称。
     */
    private String datasourceName;

    /**
     * 数据源类型。
     */
    private String datasourceType;

    /**
     * 数据源显示名称。
     */
    private String datasourceShowName;

    /**
     * 数据源描述。
     */
    private String datasourceDescription;

    /**
     * 数据源连接信息存储为json字段。
     */
    private String datasourceContent;

    /**
     * 数据源图标。
     */
    private String datasourceIcon;

    /**
     * 数据源分组。
     */
    private String datasourceGroup;

    /**
     * 数据源连通性。
     */
    private Integer datasourceConnect;

    /**
     * 是否采集元数据。
     */
    private Integer isMetaCollect;

    /**
     * 所属项目ID
     */
    private Long projectId;

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
     * datasource_name / datasource_show_name / datasource_description / datasource_content / datasource_group LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    /**
     * id 的多对多关联表数据对象。
     */
    @TableField(exist = false)
    private ProjectDatasourceRelation projectDatasourceRelation;

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

    @RelationConstDict(
            masterIdField = "datasourceConnect",
            constantDictClass = DataSourceConnectStatus.class)
    @TableField(exist = false)
    private Map<String, Object> datasourceConnectDictMap;

    @Mapper
    public interface ProjectDatasourceModelMapper extends BaseModelMapper<ProjectDatasourceVo, ProjectDatasource> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param projectDatasourceVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "projectDatasourceRelation", expression = "java(mapToBean(projectDatasourceVo.getProjectDatasourceRelation(), supie.webadmin.app.model.ProjectDatasourceRelation.class))")
        @Override
        ProjectDatasource toModel(ProjectDatasourceVo projectDatasourceVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param projectDatasource 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "projectDatasourceRelation", expression = "java(beanToMap(projectDatasource.getProjectDatasourceRelation(), false))")
        @Override
        ProjectDatasourceVo fromModel(ProjectDatasource projectDatasource);
    }
    public static final ProjectDatasourceModelMapper INSTANCE = Mappers.getMapper(ProjectDatasourceModelMapper.class);
}
