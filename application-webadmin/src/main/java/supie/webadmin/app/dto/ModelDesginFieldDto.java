package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * ModelDesginFieldDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ModelDesginFieldDto对象")
@Data
public class ModelDesginFieldDto {
    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号", required = true)
    @NotNull(message = "数据验证失败，租户号不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 数据所属人。
     */
    @ApiModelProperty(value = "数据所属人")
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @ApiModelProperty(value = "数据所属部门")
    private Long dataDeptId;

    /**
     * 字段名称。
     */
    @ApiModelProperty(value = "字段名称")
    private String modelFieldName;

    /**
     * 字段代码。
     */
    @ApiModelProperty(value = "字段代码")
    private String modelFieldCode;

    /**
     * 字段类型。
     */
    @ApiModelProperty(value = "字段类型")
    private String modelFieldType;

    /**
     * 字段指标。
     */
    @ApiModelProperty(value = "字段指标")
    private String modelFieldIndex;

    /**
     * 关联元数据标准。
     */
    @ApiModelProperty(value = "关联元数据标准")
    private String modelFieldMetaStandard;

    /**
     * 关联值域校验。
     */
    @ApiModelProperty(value = "关联值域校验")
    private String modelFieldValueStandard;

    /**
     * 字段描述。
     */
    @ApiModelProperty(value = "字段描述")
    private String modelFieldDescription;

    /**
     * 主键字段。
     */
    @ApiModelProperty(value = "主键字段")
    private String modelFieldKey;

    /**
     * 长度。
     */
    @ApiModelProperty(value = "长度")
    private Integer modelFieldLength;

    /**
     * 小数点（小数点<=长度）。
     */
    @ApiModelProperty(value = "小数点（小数点<=长度）")
    private Integer modelFieldDecimalPoint;

    /**
     * 字段分区。
     */
    @ApiModelProperty(value = "字段分区")
    private String modelFieldPpartition;

    /**
     * 是否为空（0：可为空。1：不为空）。
     */
    @ApiModelProperty(value = "是否为空（0：可为空。1：不为空）")
    private Integer modelFieldIsNull;

    /**
     * 模型表来源字段名。
     */
    @ApiModelProperty(value = "模型表来源字段名")
    private String modelFieldSourceName;

    /**
     * 默认值。
     */
    @ApiModelProperty(value = "默认值")
    private String modelFieldDefaultValue;

    /**
     * 模型表来源字段类型。
     */
    @ApiModelProperty(value = "模型表来源字段类型")
    private String modelFieldSourceType;

    /**
     * 字段来源表。
     */
    @ApiModelProperty(value = "字段来源表")
    private String modelFieldSourceTable;

    /**
     * 业务映射字段Json数据。
     */
    @ApiModelProperty(value = "业务映射字段Json数据")
    private String modelFieldMapping;

    /**
     * 模型id。
     */
    @ApiModelProperty(value = "模型id")
    private Long modelId;

    /**
     * 模型表来源字段ID。
     */
    @ApiModelProperty(value = "模型表来源字段ID")
    private Long modelFieldSourceId;

    /**
     * 模型引用的标准。
     */
    @ApiModelProperty(value = "模型引用的标准")
    private String modelQuoteStandard;

    /**
     * 字段计算脚本，仅展示关系。
     */
    @ApiModelProperty(value = "字段计算脚本，仅展示关系")
    private String modelFieldScript;

    /**
     * 显示顺序。
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 业务过程id。
     */
    @ApiModelProperty(value = "业务过程id")
    private Long processId;

    /**
     * 数据标准主表ID standard_main_id。
     */
    @ApiModelProperty(value = "数据标准主表ID")
    private Long standardMainId;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤起始值(>=)")
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤结束值(<=)")
    private String updateTimeEnd;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值(>=)")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值(<=)")
    private String createTimeEnd;

    /**
     * modelFieldLength 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "modelFieldLength 范围过滤起始值(>=)")
    private Integer modelFieldLengthStart;

    /**
     * modelFieldLength 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "modelFieldLength 范围过滤结束值(<=)")
    private Integer modelFieldLengthEnd;

    /**
     * model_field_name / model_field_code / model_field_type / model_field_index / model_field_meta_standard / model_field_value_standard / model_field_description / model_field_key / model_field_ppartition / model_field_source_name / model_field_source_type / model_field_source_table / model_field_mapping / model_quote_standard / model_field_script LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
