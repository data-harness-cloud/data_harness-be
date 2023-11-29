package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * CustomizeRouteVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("CustomizeRouteVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomizeRouteVo extends BaseVo {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID")
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
    @ApiModelProperty(value = "地址（不可重复）")
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
     * 指标ID。
     */
    @ApiModelProperty(value = "指标ID")
    private Long definitionIndexId;

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
}
