package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * DefinitionDimensionVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionDimensionVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DefinitionDimensionVo extends BaseVo {

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
     * processId 的一对一关联数据对象，数据对应类型为PlanningProcessVo。
     */
    @ApiModelProperty(value = "processId 的一对一关联数据对象，数据对应类型为PlanningProcessVo")
    private Map<String, Object> planningProcess;
}
