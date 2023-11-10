package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * DefinitionDimensionLevelVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionDimensionLevelVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DefinitionDimensionLevelVo extends BaseVo {

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
     * 层级数字。
     */
    @ApiModelProperty(value = "层级数字")
    private Integer levelNumber;

    /**
     * 层级名称。
     */
    @ApiModelProperty(value = "层级名称")
    private String levelName;

    /**
     * 层级编码。
     */
    @ApiModelProperty(value = "层级编码")
    private String levelCode;

    /**
     * 预计规模。
     */
    @ApiModelProperty(value = "预计规模")
    private String levelScale;

    /**
     * 是否启用。
     */
    @ApiModelProperty(value = "是否启用")
    private String levelEnable;
}
