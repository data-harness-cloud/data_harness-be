package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * StandardQualityDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("StandardQualityDto对象")
@Data
public class StandardQualityDto {

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
     * 质量标准名称。
     */
    @ApiModelProperty(value = "质量标准名称")
    private String standardQualityName;

    /**
     * 质量标准编码。
     */
    @ApiModelProperty(value = "质量标准编码")
    private String standardQualityCode;

    /**
     * 质量标准分类。
     */
    @ApiModelProperty(value = "质量标准分类")
    private String staidardQualityType;

    /**
     * 质量标准父id。
     */
    @ApiModelProperty(value = "质量标准父id")
    private Long standardQualityParentId;

    /**
     * 质量校验正则。
     */
    @ApiModelProperty(value = "质量校验正则")
    private String standardQaulityRe;

    /**
     * 质量校验长度限制（正数->大等于。负数->小等于）。
     */
    @ApiModelProperty(value = "质量校验长度限制（正数->大等于。负数->小等于）")
    private Integer standardQualityLang;

    /**
     * 质量校验不为空（1：不为空。0：可为空）。
     */
    @ApiModelProperty(value = "质量校验不为空（1：不为空。0：可为空）")
    private Integer standardQualityNotNull;

    /**
     * SQL条件。
     */
    @ApiModelProperty(value = "SQL条件")
    private String customJudgment;

    /**
     * 质量校验中心关联规则。
     */
    @ApiModelProperty(value = "质量校验中心关联规则")
    private Long standardQualityQualityCenterId;

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
     * standard_quality_name / standard_quality_code / staidard_quality_type / standard_qaulity_re / standard_quality_lang / standard_quality_not_null LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
