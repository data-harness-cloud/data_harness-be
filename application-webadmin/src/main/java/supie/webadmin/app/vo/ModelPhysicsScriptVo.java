package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ModelPhysicsScriptVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ModelPhysicsScriptVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelPhysicsScriptVo extends BaseVo {

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
     * 模型id。
     */
    @ApiModelProperty(value = "模型id")
    private Long modelId;

    /**
     * 物理模型建库语句。
     */
    @ApiModelProperty(value = "物理模型建库语句")
    private String modelPhysicsDatabase;

    /**
     * 物理模型建表语句。
     */
    @ApiModelProperty(value = "物理模型建表语句")
    private String modelPhysicsTable;

    /**
     * 物理模型表名称。
     */
    @ApiModelProperty(value = "物理模型表名称")
    private String modelPhysicsTableName;

    /**
     * 物理模型数据源。
     */
    @ApiModelProperty(value = "物理模型数据源")
    private String modelPhysicsDatasourceId;

    /**
     * 物理模型描述。
     */
    @ApiModelProperty(value = "物理模型描述")
    private String modelPhysicsDescription;

    /**
     * 物理模型状态（1、未建表。2、已建表）。
     */
    @ApiModelProperty(value = "物理模型状态（1、未建表。2、已建表）")
    private Integer modelPhysicsState;

    /**
     * 模型权限：是否项目共享。
     */
    @ApiModelProperty(value = "模型权限：是否项目共享")
    private String modelPhysicsPremise;

    /**
     * 负责人id。
     */
    @ApiModelProperty(value = "负责人id")
    private Long modelPhysicsHeaderId;

    /**
     * 显示顺序。
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 分层id。
     */
    @ApiModelProperty(value = "分层id")
    private Long planningWarehouseLayerId;
}
