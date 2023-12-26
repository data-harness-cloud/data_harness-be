package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * StandardQualityVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("StandardQualityVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardQualityVo extends BaseVo {

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
     * id 的多对多关联表数据对象，数据对应类型为StandardFieldQualityVo。
     */
    @ApiModelProperty(value = "id 的多对多关联表数据对象，数据对应类型为StandardFieldQualityVo")
    private Map<String, Object> standardFieldQuality;
}
