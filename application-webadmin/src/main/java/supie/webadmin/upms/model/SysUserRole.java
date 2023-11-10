package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户角色实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_user_role")
public class SysUserRole {

    /**
     * 用户Id。
     */
    private Long userId;

    /**
     * 角色Id。
     */
    private Long roleId;
}
