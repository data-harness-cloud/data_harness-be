package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DefinitionDimensionLevelDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionDimensionLevelDto对象")
@Data
public class DefinitionDimensionLevelDto {

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
     * levelNumber 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "levelNumber 范围过滤起始值(>=)")
    private Integer levelNumberStart;

    /**
     * levelNumber 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "levelNumber 范围过滤结束值(<=)")
    private Integer levelNumberEnd;

    /**
     * level_name / level_code / level_scale LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
