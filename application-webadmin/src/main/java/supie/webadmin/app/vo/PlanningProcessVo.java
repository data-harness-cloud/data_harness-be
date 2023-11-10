package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * PlanningProcessVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("PlanningProcessVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanningProcessVo extends BaseVo {

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
     * 关联分类id。
     */
    @ApiModelProperty(value = "关联分类id")
    private Long classificationId;

    /**
     * 关联主题域id。
     */
    @ApiModelProperty(value = "关联主题域id")
    private Long processThemeId;

    /**
     * 业务过程名称。
     */
    @ApiModelProperty(value = "业务过程名称")
    private String processName;

    /**
     * 业务过程代码。
     */
    @ApiModelProperty(value = "业务过程代码")
    private String processCode;

    /**
     * 业务过程状态。
     */
    @ApiModelProperty(value = "业务过程状态")
    private String processStatus;

    /**
     * 业务过程描述。
     */
    @ApiModelProperty(value = "业务过程描述")
    private String processDescription;

    /**
     * 父过程id。
     */
    @ApiModelProperty(value = "父过程id")
    private Long processParentId;
}
