package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ExternalAppCustomizeRouteVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ExternalAppCustomizeRouteVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExternalAppCustomizeRouteVo extends BaseVo {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID")
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
     * 动态路由ID。
     */
    @ApiModelProperty(value = "动态路由ID")
    private Long customizeRouteId;

    /**
     * 外部APP ID。
     */
    @ApiModelProperty(value = "外部APP ID")
    private Long externalAppId;
}
