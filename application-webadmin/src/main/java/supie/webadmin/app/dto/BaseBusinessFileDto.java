package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * BaseBusinessFileDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("BaseBusinessFileDto对象")
@Data
public class BaseBusinessFileDto {

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
     * 绑定id。
     */
    @ApiModelProperty(value = "绑定id")
    private Long bindId;

    /**
     * 绑定字符id。
     */
    @ApiModelProperty(value = "绑定字符id")
    private String bindStrId;

    /**
     * 绑定类型。
     */
    @ApiModelProperty(value = "绑定类型")
    private String bindType;

    /**
     * 文件名称。
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * json字段。
     */
    @ApiModelProperty(value = "json字段")
    private String fileJson;

    /**
     * 文件大小。
     */
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    /**
     * 文件类型。
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

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
     * fileSize 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "fileSize 范围过滤起始值(>=)")
    private Long fileSizeStart;

    /**
     * fileSize 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "fileSize 范围过滤结束值(<=)")
    private Long fileSizeEnd;
}
