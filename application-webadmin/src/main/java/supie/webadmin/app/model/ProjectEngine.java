package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ProjectEngineVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * ProjectEngine实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_project_engine")
public class ProjectEngine extends BaseModel {

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
     * 引擎名称。
     */
    private String engineName;

    /**
     * 引擎类型。
     */
    private String engineType;

    /**
     * 引擎地址。
     */
    private String engineHost;

    /**
     * 引擎端口。
     */
    private String enginePort;

    /**
     * 引擎用户名。
     */
    private String engineUsername;

    /**
     * 引擎密码。
     */
    private String enginePassword;

    /**
     * 引擎位置。
     */
    private String enginePath;

    /**
     * 引擎配置i文件。
     */
    private String engineConfig;

    /**
     * 引擎状态。
     */
    private String engineStatus;

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
     * engine_name / engine_type / engine_username LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface ProjectEngineModelMapper extends BaseModelMapper<ProjectEngineVo, ProjectEngine> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param projectEngineVo 域对象。
         * @return 实体对象。
         */
        @Override
        ProjectEngine toModel(ProjectEngineVo projectEngineVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param projectEngine 实体对象。
         * @return 域对象。
         */
        @Override
        ProjectEngineVo fromModel(ProjectEngine projectEngine);
    }
    public static final ProjectEngineModelMapper INSTANCE = Mappers.getMapper(ProjectEngineModelMapper.class);
}
