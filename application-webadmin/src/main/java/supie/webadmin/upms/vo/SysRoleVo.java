package supie.webadmin.upms.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 角色VO。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("角色VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleVo extends BaseVo {

    /**
     * 角色Id。
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    /**
     * 角色名称。
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 角色与菜单关联对象列表。
     */
    @ApiModelProperty(value = "角色与菜单关联对象列表")
    private List<Map<String, Object>> sysRoleMenuList;
}
