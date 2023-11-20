package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ExternalAppVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ExternalAppVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExternalAppVo extends BaseVo {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 应用名称。
     */
    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     * App描述。
     */
    @ApiModelProperty(value = "App描述")
    private String appDescribe;

    /**
     * AppKey。
     */
    @ApiModelProperty(value = "AppKey")
    private String appKey;

    /**
     * 认证方式（1：key认证。2：无认证）。
     */
    @ApiModelProperty(value = "认证方式（1：key认证。2：无认证）")
    private Integer authenticationMethod;

    /**
     * 过程ID。
     */
    @ApiModelProperty(value = "过程ID")
    private Long processId;

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
}
