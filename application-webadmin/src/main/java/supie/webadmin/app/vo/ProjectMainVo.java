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
 * ProjectMainVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectMainVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectMainVo extends BaseVo {

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
     * 项目名称。
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目描述。
     */
    @ApiModelProperty(value = "项目描述")
    private String projectDescription;

    /**
     * 项目存算引擎。
     */
    @ApiModelProperty(value = "项目存算引擎")
    private Long projectEngineId;

    /**
     * 项目状态。
     */
    @ApiModelProperty(value = "项目状态")
    private Integer projectCurrentsStatus;

    /**
     * 项目组名称。
     */
    @ApiModelProperty(value = "项目组名称")
    private String projectGroupName;

    /**
     * 项目负责人。
     */
    @ApiModelProperty(value = "项目负责人")
    private Long projectHeaderId;

    /**
     * 项目流程状态。
     */
    @ApiModelProperty(value = "项目流程状态")
    private Long projectFlowStatus;

    /**
     * 项目审批状态字段。
     */
    @ApiModelProperty(value = "项目审批状态字段")
    private Long projectFlowApproveStatus;

    /**
     * 项目英文名称。
     */
    @ApiModelProperty(value = "项目英文名称")
    private String projectCode;

    /**
     * RemoteHost 的一对多关联表数据对象。数据对应类型为RemoteHost。
     */
    @ApiModelProperty(value = "RemoteHost 的一对多关联表数据对象。数据对应类型为RemoteHost")
    private List<Map<String, Object>> remoteHostList;

    /**
     * SeatunnelConfig 的一对多关联表数据对象。数据对应类型为SeatunnelConfig。
     */
    @ApiModelProperty(value = "SeatunnelConfig 的一对多关联表数据对象。数据对应类型为SeatunnelConfig")
    private List<Map<String, Object>> seatunnelConfigList;

    /**
     * projectEngineId 的一对一关联数据对象，数据对应类型为ProjectEngineVo。
     */
    @ApiModelProperty(value = "projectEngineId 的一对一关联数据对象，数据对应类型为ProjectEngineVo")
    private Map<String, Object> projectEngine;

    /**
     * dataUserId 字典关联数据。
     */
    @ApiModelProperty(value = "dataUserId 字典关联数据")
    private Map<String, Object> dataUserIdDictMap;

    /**
     * dataDeptId 字典关联数据。
     */
    @ApiModelProperty(value = "dataDeptId 字典关联数据")
    private Map<String, Object> dataDeptIdDictMap;

    /**
     * projectHeaderId 字典关联数据。
     */
    @ApiModelProperty(value = "projectHeaderId 字典关联数据")
    private Map<String, Object> projectHeaderIdDictMap;

    /**
     * projectCurrentsStatus 全局字典关联数据。
     */
    @ApiModelProperty(value = "projectCurrentsStatus 全局字典关联数据")
    private Map<String, Object> projectCurrentsStatusDictMap;
}
