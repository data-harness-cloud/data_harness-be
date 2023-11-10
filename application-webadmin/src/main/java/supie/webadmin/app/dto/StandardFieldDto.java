package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * StandardFieldDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("StandardFieldDto对象")
@Data
public class StandardFieldDto {

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
     * standard_field_name / standard_field_code / standard_field_type LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
