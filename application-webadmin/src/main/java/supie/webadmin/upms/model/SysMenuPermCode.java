package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 菜单与权限字关联实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_menu_perm_code")
public class SysMenuPermCode {

    /**
     * 关联菜单Id。
     */
    private Long menuId;

    /**
     * 关联权限字Id。
     */
    private Long permCodeId;
}
