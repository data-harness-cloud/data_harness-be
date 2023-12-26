package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.util.sqlUtil.ColumnField;
import supie.webadmin.app.util.sqlUtil.ColumnType;
import supie.webadmin.app.vo.ModelDesginFieldVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * ModelDesginField实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_model_desgin_field")
public class ModelDesginField extends BaseModel {

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
     * 字段名称。
     */
    private String modelFieldName;

    /**
     * 字段代码。
     */
    @ColumnField(ColumnType.COLUMN_CODE)
    private String modelFieldCode;

    /**
     * 字段类型。
     */
    @ColumnField(ColumnType.COLUMN_TYPE)
    private String modelFieldType;

    /**
     * 字段指标。
     */
    private String modelFieldIndex;

    /**
     * 关联元数据标准。
     */
    private String modelFieldMetaStandard;

    /**
     * 关联值域校验。
     */
    private String modelFieldValueStandard;

    /**
     * 字段描述。
     */
    @ColumnField(ColumnType.COLUMN_NAME)
    private String modelFieldDescription;

    /**
     * 主键字段。
     */
    @ColumnField(ColumnType.COLUMN_IS_KEY)
    private Integer modelFieldKey;

    /**
     * 长度。
     */
    @ColumnField(ColumnType.COLUMN_LENGTH)
    private Integer modelFieldLength;

    /**
     * 小数点（小数点<=长度）。
     */
    @ColumnField(ColumnType.COLUMN_DECIMAL)
    private Integer modelFieldDecimalPoint;

    /**
     * 字段分区。
     */
    private String modelFieldPpartition;

    /**
     * 是否为空（0：可为空。1：不为空）。
     */
    @ColumnField(ColumnType.COLUMN_IS_NULLABLE)
    private Integer modelFieldIsNull;

    /**
     * 默认值
     */
    @ColumnField(ColumnType.COLUMN_DEFAULT_VALUE)
    private String modelFieldDefaultValue;

    /**
     * 模型表来源字段名。
     */
    private String modelFieldSourceName;

    /**
     * 模型表来源字段类型。
     */
    private String modelFieldSourceType;

    /**
     * 字段来源表。
     */
    private String modelFieldSourceTable;

    /**
     * 业务映射字段Json数据。
     */
    private String modelFieldMapping;

    /**
     * 模型id。
     */
    private Long modelId;

    /**
     * 模型表来源字段ID。
     */
    private Long modelFieldSourceId;

    /**
     * 模型引用的标准。
     */
    private String modelQuoteStandard;

    /**
     * 字段计算脚本，仅展示关系。
     */
    private String modelFieldScript;

    /**
     * 显示顺序。
     */
    private Integer showOrder;

    /**
     * 业务过程id。
     */
    private Long processId;

    /**
     * 数据标准主表ID standard_main_id。
     */
    private Long standardMainId;

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
     * modelFieldLength 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Integer modelFieldLengthStart;

    /**
     * modelFieldLength 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Integer modelFieldLengthEnd;

    /**
     * model_field_name / model_field_code / model_field_type / model_field_index / model_field_meta_standard / model_field_value_standard / model_field_description / model_field_key / model_field_ppartition / model_field_source_name / model_field_source_type / model_field_source_table / model_field_mapping / model_quote_standard / model_field_script LIKE搜索字符串。
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
    private DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation;

    @Mapper
    public interface ModelDesginFieldModelMapper extends BaseModelMapper<ModelDesginFieldVo, ModelDesginField> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param modelDesginFieldVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "definitionIndexModelFieldRelation", expression = "java(mapToBean(modelDesginFieldVo.getDefinitionIndexModelFieldRelation(), supie.webadmin.app.model.DefinitionIndexModelFieldRelation.class))")
        @Override
        ModelDesginField toModel(ModelDesginFieldVo modelDesginFieldVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param modelDesginField 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "definitionIndexModelFieldRelation", expression = "java(beanToMap(modelDesginField.getDefinitionIndexModelFieldRelation(), false))")
        @Override
        ModelDesginFieldVo fromModel(ModelDesginField modelDesginField);
    }
    public static final ModelDesginFieldModelMapper INSTANCE = Mappers.getMapper(ModelDesginFieldModelMapper.class);
}
