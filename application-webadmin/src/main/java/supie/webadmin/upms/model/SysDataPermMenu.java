package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * 数据权限与菜单关联实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@ToString(of = {"menuId"})
@TableName(value = "sdt_sys_data_perm_menu")
public class SysDataPermMenu {

    /**
     * 数据权限Id。
     */
    private Long dataPermId;

    /**
     * 关联菜单Id。
     */
    private Long menuId;
}
