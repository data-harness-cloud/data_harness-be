package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ProjectDatasourceTemplateDictVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * ProjectDatasourceTemplateDict实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_project_datasource_template_dict")
public class ProjectDatasourceTemplateDict extends BaseModel {

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
     * 模板类型。
     */
    private String templateType;

    /**
     * 模板名称。
     */
    private String templateName;

    /**
     * source配置。
     */
    private String templateSource;

    /**
     * sink配置。
     */
    private String templateSink;

    /**
     * 转换配置。
     */
    private String templateTrans;

    /**
     * 图标。
     */
    private String templateIcon;

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
     * template_type / template_name LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

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
    public interface ProjectDatasourceTemplateDictModelMapper extends BaseModelMapper<ProjectDatasourceTemplateDictVo, ProjectDatasourceTemplateDict> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param projectDatasourceTemplateDictVo 域对象。
         * @return 实体对象。
         */
        @Override
        ProjectDatasourceTemplateDict toModel(ProjectDatasourceTemplateDictVo projectDatasourceTemplateDictVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param projectDatasourceTemplateDict 实体对象。
         * @return 域对象。
         */
        @Override
        ProjectDatasourceTemplateDictVo fromModel(ProjectDatasourceTemplateDict projectDatasourceTemplateDict);
    }
    public static final ProjectDatasourceTemplateDictModelMapper INSTANCE = Mappers.getMapper(ProjectDatasourceTemplateDictModelMapper.class);
}
