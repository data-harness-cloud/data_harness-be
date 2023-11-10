package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * ModelPhysicsScriptDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ModelPhysicsScriptDto对象")
@Data
public class ModelPhysicsScriptDto {

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
    @ApiModelProperty(value = "分层id", required = true)
    @NotNull(message = "数据验证失败，分层id不能为空！")
    private Long planningWarehouseLayerId;

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
     * model_physics_database / model_physics_table / model_physics_description / model_physics_premise LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
