package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * PlanningWarehouseLayerDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("PlanningWarehouseLayerDto对象")
@Data
public class PlanningWarehouseLayerDto {

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
     * 分层名称。
     */
    @ApiModelProperty(value = "分层名称")
    private String houseLayerName;

    /**
     * 分层编码。
     */
    @ApiModelProperty(value = "分层编码")
    private String houseLayerCode;

    /**
     * 分层状态。
     */
    @ApiModelProperty(value = "分层状态")
    private Integer houseLayerStatus;

    /**
     * 分层关联库。
     */
    @ApiModelProperty(value = "分层关联库")
    private String houseLayerDatabase;

    /**
     * 分层描述。
     */
    @ApiModelProperty(value = "分层描述")
    private String houseLayerDescription;

    /**
     * 分层负责人id。
     */
    @ApiModelProperty(value = "分层负责人id")
    private Long houseLayerHeaderUserId;

    /**
     * 分层类型：是否为维度层。
     */
    @ApiModelProperty(value = "分层类型：是否为维度层")
    private String houseLayerType;

    /**
     * 分层数据源类型。
     */
    @ApiModelProperty(value = "分层数据源类型")
    private String houseLayerDatasourceType;

    /**
     * 数据源id。
     */
    @ApiModelProperty(value = "数据源id")
    private Long datasourceId;

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
     * house_layer_name / house_layer_code / house_layer_database / house_layer_description / house_layer_type / house_layer_datasource_type LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
