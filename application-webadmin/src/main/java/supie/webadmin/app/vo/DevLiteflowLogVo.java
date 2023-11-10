package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * DevLiteflowLogVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowLogVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DevLiteflowLogVo extends BaseVo {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 规则链ID。
     */
    @ApiModelProperty(value = "规则链ID")
    private Long rulerId;
    /**
     * 所属任务ID
     */
    @ApiModelProperty(value = "所属任务ID")
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
     * dataUserId 字典关联数据。
     */
    @ApiModelProperty(value = "dataUserId 字典关联数据")
    private Map<String, Object> dataUserIdDictMap;

    /**
     * dataDeptId 字典关联数据。
     */
    @ApiModelProperty(value = "dataDeptId 字典关联数据")
    private Map<String, Object> dataDeptIdDictMap;
}
