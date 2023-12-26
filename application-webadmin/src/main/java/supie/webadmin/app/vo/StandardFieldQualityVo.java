package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * StandardFieldQualityVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("StandardFieldQualityVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardFieldQualityVo extends BaseVo {

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
     * 标准主表id。
     */
    @ApiModelProperty(value = "标准主表id")
    private Long staidardId;

    /**
     * 标准字段id。
     */
    @ApiModelProperty(value = "标准字段id")
    private Long staidardFieldId;

    /**
     * 质量校验id。
     */
    @ApiModelProperty(value = "质量校验id")
    private Long staidardQualityId;
}
