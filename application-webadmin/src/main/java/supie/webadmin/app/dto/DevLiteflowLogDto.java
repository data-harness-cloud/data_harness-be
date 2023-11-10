package supie.webadmin.app.dto;

import supie.common.core.validator.AddGroup;
import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

import java.util.Date;

/**
 * DevLiteflowLogDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowLogDto对象")
@Data
public class DevLiteflowLogDto {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "数据验证失败，主键ID不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 规则链ID。
     */
    @ApiModelProperty(value = "规则链ID", required = true)
    @NotNull(message = "数据验证失败，规则链ID不能为空！", groups = {AddGroup.class})
    private Long rulerId;

    /**
     * 所属任务ID
     */
    @ApiModelProperty(value = "所属任务ID", required = true)
    @NotNull(message = "数据验证失败，所属任务ID不能为空！", groups = {AddGroup.class})
    private Long schedulingTasksId;

    /**
     * 运行版本。
     */
    @ApiModelProperty(value = "运行版本")
    private Integer runVersion;

    /**
     * 运行时间。
     */
    @ApiModelProperty(value = "运行时间")
    private Date runTime;

    /**
     * 运行结果。
     */
    @ApiModelProperty(value = "运行结果")
    private String runResult;

    /**
     * 运行结果信息
     */
    @ApiModelProperty(value = "运行结果信息")
    private String runResultMsg;

    /**
     * 日志文件名称（rulerId_version_nowDate.log）。
     */
    @ApiModelProperty(value = "日志文件名称（rulerId_version_nowDate.log）")
    private String logFileName;

    /**
     * 日志文件Json字段。
     */
    @ApiModelProperty(value = "日志文件Json字段")
    private String logFileJson;

    /**
     * 日志文件大小。
     */
    @ApiModelProperty(value = "日志文件大小")
    private Long logFileSize;

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
     * runTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "runTime 范围过滤起始值(>=)")
    private String runTimeStart;

    /**
     * runTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "runTime 范围过滤结束值(<=)")
    private String runTimeEnd;

    /**
     * logFileSize 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "logFileSize 范围过滤起始值(>=)")
    private Long logFileSizeStart;

    /**
     * logFileSize 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "logFileSize 范围过滤结束值(<=)")
    private Long logFileSizeEnd;

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
     * log_file_name LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
