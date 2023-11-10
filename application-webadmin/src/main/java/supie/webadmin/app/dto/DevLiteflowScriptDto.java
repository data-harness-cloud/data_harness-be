package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DevLiteflowScriptDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowScriptDto对象")
@Data
public class DevLiteflowScriptDto {

    /**
     * 编号。
     */
    @ApiModelProperty(value = "编号", required = true)
    @NotNull(message = "数据验证失败，编号不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 字符编号。
     */
    @ApiModelProperty(value = "字符编号")
    private String strId;

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
     * 应用名称。
     */
    @ApiModelProperty(value = "应用名称")
    private String applicationName;

    /**
     * 脚本id。
     */
    @ApiModelProperty(value = "脚本id")
    private String scriptId;

    /**
     * 脚本名称。
     */
    @ApiModelProperty(value = "脚本名称")
    private String scriptName;

    /**
     * 脚本内容。
     */
    @ApiModelProperty(value = "脚本内容")
    private String scriptData;

    /**
     * 脚本种类。
     */
    @ApiModelProperty(value = "脚本种类")
    private String scriptType;

    /**
     * 脚本语言。
     */
    @ApiModelProperty(value = "脚本语言")
    private String scriptLanguage;

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
     * application_name / script_name / script_language LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
