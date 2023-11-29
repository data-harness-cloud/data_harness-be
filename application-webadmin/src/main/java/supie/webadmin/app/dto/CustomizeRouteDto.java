package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * CustomizeRouteDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("CustomizeRouteDto对象")
@Data
public class CustomizeRouteDto {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "数据验证失败，主键ID不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 名称。
     */
    @ApiModelProperty(value = "名称")
    private String name;

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
     * 描述。
     */
    @ApiModelProperty(value = "描述")
    private String routeDescribe;

    /**
     * 地址（不可重复）。
     */
    @ApiModelProperty(value = "地址（不可重复）", required = true)
    @NotBlank(message = "数据验证失败，地址（不可重复）不能为空！")
    private String url;

    /**
     * 请求类型（1：GET。2：POST。默认为POST）。
     */
    @ApiModelProperty(value = "请求类型（1：GET。2：POST。默认为POST）")
    private Integer requestType;

    /**
     * 状态（1：上线。-1：下线）。
     */
    @ApiModelProperty(value = "状态（1：上线。-1：下线）")
    private Integer state;

    /**
     * 存算引擎项目ID。
     */
    @ApiModelProperty(value = "存算引擎项目ID")
    private Long projectId;

    /**
     * 目标数据库名称。
     */
    @ApiModelProperty(value = "目标数据库名称")
    private String databaseName;

    /**
     * SQL语句。
     */
    @ApiModelProperty(value = "SQL语句")
    private String sqlScript;

    /**
     * 参数集（JSON字符串形式存储）。
     */
    @ApiModelProperty(value = "参数集（JSON字符串形式存储）")
    private String parameter;

    /**
     * 业务规程ID。
     */
    @ApiModelProperty(value = "业务规程ID")
    private Long processId;

    /**
     * 指标ID。
     */
    @ApiModelProperty(value = "指标ID")
    private Long definitionIndexId;

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
     * name / routeDescribe / url / database_name / sql_script / parameter LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
