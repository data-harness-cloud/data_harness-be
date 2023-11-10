package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * DevConsoleVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevConsoleVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DevConsoleVo extends BaseVo {

    /**
     * 主表控制台id。
     */
    @ApiModelProperty(value = "主表控制台id")
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
}
