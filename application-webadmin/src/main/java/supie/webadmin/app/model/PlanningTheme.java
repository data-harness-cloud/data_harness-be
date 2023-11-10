package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.PlanningThemeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * PlanningTheme实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_planning_theme")
public class PlanningTheme extends BaseModel {

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
     * 分层关联项目id。
     */
    private Long projectId;

    /**
     * 关联分类id。
     */
    private Long classificationId;

    /**
     * 主题名称。
     */
    private String themeName;

    /**
     * 主题代码。
     */
    private String themeCode;

    /**
     * 主题状态。
     */
    private String themeStatus;

    /**
     * 主题描述。
     */
    private String themeDescription;

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
     * theme_name / theme_code / theme_status / theme_description LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @RelationOneToOne(
            masterIdField = "id",
            slaveModelClass = PlanningProcess.class,
            slaveIdField = "processThemeId")
    @TableField(exist = false)
    private PlanningProcess planningProcess;

    @Mapper
    public interface PlanningThemeModelMapper extends BaseModelMapper<PlanningThemeVo, PlanningTheme> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param planningThemeVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "planningProcess", expression = "java(mapToBean(planningThemeVo.getPlanningProcess(), supie.webadmin.app.model.PlanningProcess.class))")
        @Override
        PlanningTheme toModel(PlanningThemeVo planningThemeVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param planningTheme 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "planningProcess", expression = "java(beanToMap(planningTheme.getPlanningProcess(), false))")
        @Override
        PlanningThemeVo fromModel(PlanningTheme planningTheme);
    }
    public static final PlanningThemeModelMapper INSTANCE = Mappers.getMapper(PlanningThemeModelMapper.class);
}
