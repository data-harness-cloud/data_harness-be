package supie.webadmin.upms.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据权限与菜单关联Dto。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("数据权限与菜单关联Dto")
@Data
public class SysDataPermMenuDto {

    /**
     * 数据权限Id。
     */
    @ApiModelProperty(value = "数据权限Id", required = true)
    private Long dataPermId;

    /**
     * 关联菜单Id。
     */
    @ApiModelProperty(value = "关联菜单Id", required = true)
    private Long menuId;
}