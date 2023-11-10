package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * StandardFieldVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("StandardFieldVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardFieldVo extends BaseVo {

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
     * 字段标准名称。
     */
    @ApiModelProperty(value = "字段标准名称")
    private String standardFieldName;

    /**
     * 字段标准编码。
     */
    @ApiModelProperty(value = "字段标准编码")
    private String standardFieldCode;

    /**
     * 数据类型。
     */
    @ApiModelProperty(value = "数据类型")
    private String standardFieldType;
}
