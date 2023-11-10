package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * PlanningThemeVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("PlanningThemeVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanningThemeVo extends BaseVo {

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
     * 主题名称。
     */
    @ApiModelProperty(value = "主题名称")
    private String themeName;

    /**
     * 主题代码。
     */
    @ApiModelProperty(value = "主题代码")
    private String themeCode;

    /**
     * 主题状态。
     */
    @ApiModelProperty(value = "主题状态")
    private String themeStatus;

    /**
     * 主题描述。
     */
    @ApiModelProperty(value = "主题描述")
    private String themeDescription;

    /**
     * id 的一对一关联数据对象，数据对应类型为PlanningProcessVo。
     */
    @ApiModelProperty(value = "id 的一对一关联数据对象，数据对应类型为PlanningProcessVo")
    private Map<String, Object> planningProcess;
}
