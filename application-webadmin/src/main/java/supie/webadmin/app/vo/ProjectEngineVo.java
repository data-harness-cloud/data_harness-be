package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ProjectEngineVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectEngineVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectEngineVo extends BaseVo {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号")
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
     * 引擎名称。
     */
    @ApiModelProperty(value = "引擎名称")
    private String engineName;

    /**
     * 引擎类型。
     */
    @ApiModelProperty(value = "引擎类型")
    private String engineType;

    /**
     * 引擎地址。
     */
    @ApiModelProperty(value = "引擎地址")
    private String engineHost;

    /**
     * 引擎端口。
     */
    @ApiModelProperty(value = "引擎端口")
    private String enginePort;

    /**
     * 引擎用户名。
     */
    @ApiModelProperty(value = "引擎用户名")
    private String engineUsername;

    /**
     * 引擎密码。
     */
    @ApiModelProperty(value = "引擎密码")
    private String enginePassword;

    /**
     * 引擎位置。
     */
    @ApiModelProperty(value = "引擎位置")
    private String enginePath;

    /**
     * 引擎配置i文件。
     */
    @ApiModelProperty(value = "引擎配置i文件")
    private String engineConfig;

    /**
     * 引擎状态。
     */
    @ApiModelProperty(value = "引擎状态")
    private String engineStatus;
}
