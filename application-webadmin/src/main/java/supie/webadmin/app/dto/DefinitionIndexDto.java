package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DefinitionIndexDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionIndexDto对象")
@Data
public class DefinitionIndexDto {

    /**
     * 编号。
     */
    @ApiModelProperty(value = "编号", required = true)
    @NotNull(message = "数据验证失败，编号不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 字符编号。
     */
    @ApiModelProperty(value = "字符编号")
    private String strId;

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
     * 指标类型。
     */
    @ApiModelProperty(value = "指标类型")
    private String indexType;

    /**
     * 指标名称。
     */
    @ApiModelProperty(value = "指标名称")
    private String indexName;

    /**
     * 指标英文名称。
     */
    @ApiModelProperty(value = "指标英文名称")
    private String indexEnName;

    /**
     * 所属项目id。
     */
    @ApiModelProperty(value = "所属项目id")
    private String projectId;

    /**
     * 指标编码。
     */
    @ApiModelProperty(value = "指标编码")
    private String indexCode;

    /**
     * 指标等级;核心、重要、一般、临时。
     */
    @ApiModelProperty(value = "指标等级;核心、重要、一般、临时")
    private String indexLevel;

    /**
     * 业务过程id。
     */
    @ApiModelProperty(value = "业务过程id")
    private Long processId;

    /**
     * 业务描述。
     */
    @ApiModelProperty(value = "业务描述")
    private String indexDescription;

    /**
     * 动态路由id。
     */
    @ApiModelProperty(value = "动态路由id")
    private Long customizeRouteId;

    /**
     * 关联字段。
     */
    @ApiModelProperty(value = "关联字段")
    private Long modelDesginFieldId;

    /**
     * 数据类型。
     */
    @ApiModelProperty(value = "数据类型")
    private String dataType;

    /**
     * 生产周期。
     */
    @ApiModelProperty(value = "生产周期")
    private String productPeriod;

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
     * index_type / index_name / index_en_name / index_code / index_level / index_description / data_type / product_period / LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
