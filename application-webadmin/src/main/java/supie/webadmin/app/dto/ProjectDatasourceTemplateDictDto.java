package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * ProjectDatasourceTemplateDictDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectDatasourceTemplateDictDto对象")
@Data
public class ProjectDatasourceTemplateDictDto {

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
     * 模板类型。
     */
    @ApiModelProperty(value = "模板类型")
    private String templateType;

    /**
     * 模板名称。
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    /**
     * source配置。
     */
    @ApiModelProperty(value = "source配置")
    private String templateSource;

    /**
     * sink配置。
     */
    @ApiModelProperty(value = "sink配置")
    private String templateSink;

    /**
     * 转换配置。
     */
    @ApiModelProperty(value = "转换配置")
    private String templateTrans;

    /**
     * 图标。
     */
    @ApiModelProperty(value = "图标")
    private String templateIcon;

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
     * template_type / template_name LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
