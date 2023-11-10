package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 权限字与权限资源关联实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_perm_code_perm")
public class SysPermCodePerm {

    /**
     * 权限字Id。
     */
    private Long permCodeId;

    /**
     * 权限Id。
     */
    private Long permId;
}
