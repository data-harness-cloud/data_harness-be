package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DefinitionDimensionPropertyColumnVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DefinitionDimensionPropertyColumn实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_definition_dimension_property_column")
public class DefinitionDimensionPropertyColumn extends BaseModel {

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
     * 维度表id。
     */
    private Long dimensionId;

    /**
     * 属性名称。
     */
    private String propertyName;

    /**
     * 属性英文名称。
     */
    private String propertyEnName;

    /**
     * 数据类型。
     */
    private String propertyDataType;

    /**
     * 业务描述。
     */
    private String propertyDescription;

    /**
     * 小数点。
     */
    private Integer propertyDecimalLength;

    /**
     * 是否主键。
     */
    private String isPrimary;

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
     * propertyDecimalLength 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Integer propertyDecimalLengthStart;

    /**
     * propertyDecimalLength 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Integer propertyDecimalLengthEnd;

    /**
     * property_name / property_en_name / property_data_type / property_description LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface DefinitionDimensionPropertyColumnModelMapper extends BaseModelMapper<DefinitionDimensionPropertyColumnVo, DefinitionDimensionPropertyColumn> {
    }
    public static final DefinitionDimensionPropertyColumnModelMapper INSTANCE = Mappers.getMapper(DefinitionDimensionPropertyColumnModelMapper.class);
}
