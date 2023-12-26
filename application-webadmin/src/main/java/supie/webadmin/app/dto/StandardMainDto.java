package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * StandardMainDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("StandardMainDto对象")
@Data
public class StandardMainDto {

    /**
     * 主键。
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "数据验证失败，主键不能为空！", groups = {UpdateGroup.class})
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
     * 标准目录id。
     */
    @ApiModelProperty(value = "标准目录id")
    private Long standardDirectoryId;

    /**
     * 标准负责人id。
     */
    @ApiModelProperty(value = "标准负责人id")
    private Long standardHeaderId;

    /**
     * 标准名称。
     */
    @ApiModelProperty(value = "标准名称")
    private String standardName;

    /**
     * 标准编码。
     */
    @ApiModelProperty(value = "标准编码")
    private String standardCode;

    /**
     * 标准分类。
     */
    @ApiModelProperty(value = "标准分类")
    private String standardType;

    /**
     * 标准英语名称。
     */
    @ApiModelProperty(value = "标准英语名称")
    private String standardEnglish;

    /**
     * 标准描述。
     */
    @ApiModelProperty(value = "标准描述")
    private String standardDescription;

    /**
     * 标准录入方式。
     */
    @ApiModelProperty(value = "标准录入方式")
    private String standardInputMode;

    /**
     * 标准状态。
     */
    @ApiModelProperty(value = "标准状态")
    private String standardStatus;

    /**
     * 正则表达式。
     */
    @ApiModelProperty(value = "正则表达式")
    private String standardRegular;

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
     * standard_name / standard_code / standard_type / standard_english / standard_description / standard_input_mode / standard_status LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
