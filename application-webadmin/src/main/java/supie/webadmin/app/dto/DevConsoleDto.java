package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DevConsoleDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevConsoleDto对象")
@Data
public class DevConsoleDto {

    /**
     * 主表控制台id。
     */
    @ApiModelProperty(value = "主表控制台id", required = true)
    @NotNull(message = "数据验证失败，主表控制台id不能为空！", groups = {UpdateGroup.class})
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
     * 查询控制台名称。
     */
    @ApiModelProperty(value = "查询控制台名称")
    private String queryConsoleName;

    /**
     * 查询语句。
     */
    @ApiModelProperty(value = "查询语句")
    private String queryStatements;

    /**
     * 响应结果。
     */
    @ApiModelProperty(value = "响应结果")
    private String responseResults;

    /**
     * 所选数据库。
     */
    @ApiModelProperty(value = "所选数据库")
    private String queryDatabase;

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
     * query_console_name / query_database LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
