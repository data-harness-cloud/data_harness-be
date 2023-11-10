package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 角色菜单实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_role_menu")
public class SysRoleMenu {

    /**
     * 角色Id。
     */
    private Long roleId;

    /**
     * 菜单Id。
     */
    private Long menuId;
}
