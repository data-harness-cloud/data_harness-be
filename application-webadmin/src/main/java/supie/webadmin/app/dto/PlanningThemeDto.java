package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * PlanningThemeDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("PlanningThemeDto对象")
@Data
public class PlanningThemeDto {

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
     * theme_name / theme_code / theme_status / theme_description LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
