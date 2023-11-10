package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

import java.util.Date;

/**
 * DefinitionDimensionDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionDimensionDto对象")
@Data
public class DefinitionDimensionDto {

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
     * 分层关联项目id。
     */
    @ApiModelProperty(value = "分层关联项目id")
    private Long projectId;

    /**
     * 关联业务过程id。
     */
    @ApiModelProperty(value = "关联业务过程id")
    private Long processId;

    /**
     * 维度类型。
     */
    @ApiModelProperty(value = "维度类型")
    private String dimensionType;

    /**
     * 维度名称。
     */
    @ApiModelProperty(value = "维度名称")
    private String dimensionName;

    /**
     * 维度英文名称。
     */
    @ApiModelProperty(value = "维度英文名称")
    private String dimensionEnName;

    /**
     * 维度编码。
     */
    @ApiModelProperty(value = "维度编码")
    private String dimensionCode;

    /**
     * 维度描述。
     */
    @ApiModelProperty(value = "维度描述")
    private String dimensionDescribe;

    /**
     * 维度目录id。
     */
    @ApiModelProperty(value = "维度目录id")
    private Long dimensionDirectoryId;

    /**
     * 是否自动建表。
     */
    @ApiModelProperty(value = "是否自动建表")
    private String isAutoCreateTable;

    /**
     * 周期类型。
     */
    @ApiModelProperty(value = "周期类型")
    private String dimensionPeriodType;

    /**
     * 周期开始日期。
     */
    @ApiModelProperty(value = "周期开始日期")
    private Date dimensionPeriodStartDate;

    /**
     * 周期结束日期。
     */
    @ApiModelProperty(value = "周期结束日期")
    private Date dimensionPeriodEndDate;

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
     * dimensionPeriodStartDate 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "dimensionPeriodStartDate 范围过滤起始值(>=)")
    private String dimensionPeriodStartDateStart;

    /**
     * dimensionPeriodStartDate 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "dimensionPeriodStartDate 范围过滤结束值(<=)")
    private String dimensionPeriodStartDateEnd;

    /**
     * dimensionPeriodEndDate 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "dimensionPeriodEndDate 范围过滤起始值(>=)")
    private String dimensionPeriodEndDateStart;

    /**
     * dimensionPeriodEndDate 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "dimensionPeriodEndDate 范围过滤结束值(<=)")
    private String dimensionPeriodEndDateEnd;

    /**
     * dimension_type / dimension_name / dimension_en_name / dimension_code / dimension_describe / dimension_period_type LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
