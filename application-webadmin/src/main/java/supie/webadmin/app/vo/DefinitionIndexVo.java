package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * DefinitionIndexVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionIndexVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DefinitionIndexVo extends BaseVo {

    /**
     * 编号。
     */
    @ApiModelProperty(value = "编号")
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
     * 指标编码。
     */
    @ApiModelProperty(value = "指标编码")
    private String indexCode;


    /**
     * 所属项目id。
     */
    @ApiModelProperty(value = "所属项目id")
    private String projectId;

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

}
