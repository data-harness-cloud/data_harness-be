package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * DefinitionDimensionPropertyColumnVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionDimensionPropertyColumnVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DefinitionDimensionPropertyColumnVo extends BaseVo {

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
     * 维度表id。
     */
    @ApiModelProperty(value = "维度表id")
    private Long dimensionId;

    /**
     * 属性名称。
     */
    @ApiModelProperty(value = "属性名称")
    private String propertyName;

    /**
     * 属性英文名称。
     */
    @ApiModelProperty(value = "属性英文名称")
    private String propertyEnName;

    /**
     * 数据类型。
     */
    @ApiModelProperty(value = "数据类型")
    private String propertyDataType;

    /**
     * 业务描述。
     */
    @ApiModelProperty(value = "业务描述")
    private String propertyDescription;

    /**
     * 小数点。
     */
    @ApiModelProperty(value = "小数点")
    private Integer propertyDecimalLength;

    /**
     * 是否主键。
     */
    @ApiModelProperty(value = "是否主键")
    private String isPrimary;
}
