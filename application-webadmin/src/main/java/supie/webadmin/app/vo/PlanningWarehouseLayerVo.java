package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * PlanningWarehouseLayerVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("PlanningWarehouseLayerVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanningWarehouseLayerVo extends BaseVo {

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
     * ModelPhysicsScript 的一对多关联表数据对象。数据对应类型为ModelPhysicsScript。
     */
    @ApiModelProperty(value = "ModelPhysicsScript 的一对多关联表数据对象。数据对应类型为ModelPhysicsScript")
    private List<Map<String, Object>> modelPhysicsScriptList;
}
