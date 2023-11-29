package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * ModelDesginFieldVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ModelDesginFieldVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelDesginFieldVo extends BaseVo {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号")
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
     * id 的多对多关联表数据对象，数据对应类型为DefinitionIndexModelFieldRelationVo。
     */
    @ApiModelProperty(value = "id 的多对多关联表数据对象，数据对应类型为DefinitionIndexModelFieldRelationVo")
    private Map<String, Object> definitionIndexModelFieldRelation;
}
