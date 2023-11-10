package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DefinitionDimensionVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;

/**
 * DefinitionDimension实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_definition_dimension")
public class DefinitionDimension extends BaseModel {

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
     * 关联业务过程id。
     */
    private Long processId;

    /**
     * 维度类型。
     */
    private String dimensionType;

    /**
     * 维度名称。
     */
    private String dimensionName;

    /**
     * 维度英文名称。
     */
    private String dimensionEnName;

    /**
     * 维度编码。
     */
    private String dimensionCode;

    /**
     * 维度描述。
     */
    private String dimensionDescribe;

    /**
     * 维度目录id。
     */
    private Long dimensionDirectoryId;

    /**
     * 是否自动建表。
     */
    private String isAutoCreateTable;

    /**
     * 周期类型。
     */
    private String dimensionPeriodType;

    /**
     * 周期开始日期。
     */
    private Date dimensionPeriodStartDate;

    /**
     * 周期结束日期。
     */
    private Date dimensionPeriodEndDate;

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
     * dimensionPeriodStartDate 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String dimensionPeriodStartDateStart;

    /**
     * dimensionPeriodStartDate 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String dimensionPeriodStartDateEnd;

    /**
     * dimensionPeriodEndDate 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String dimensionPeriodEndDateStart;

    /**
     * dimensionPeriodEndDate 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String dimensionPeriodEndDateEnd;

    /**
     * dimension_type / dimension_name / dimension_en_name / dimension_code / dimension_describe / dimension_period_type LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @RelationOneToOne(
            masterIdField = "processId",
            slaveModelClass = PlanningProcess.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private PlanningProcess planningProcess;

    @Mapper
    public interface DefinitionDimensionModelMapper extends BaseModelMapper<DefinitionDimensionVo, DefinitionDimension> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param definitionDimensionVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "planningProcess", expression = "java(mapToBean(definitionDimensionVo.getPlanningProcess(), supie.webadmin.app.model.PlanningProcess.class))")
        @Override
        DefinitionDimension toModel(DefinitionDimensionVo definitionDimensionVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param definitionDimension 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "planningProcess", expression = "java(beanToMap(definitionDimension.getPlanningProcess(), false))")
        @Override
        DefinitionDimensionVo fromModel(DefinitionDimension definitionDimension);
    }
    public static final DefinitionDimensionModelMapper INSTANCE = Mappers.getMapper(DefinitionDimensionModelMapper.class);
}
