package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 数据权限与用户关联实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_data_perm_user")
public class SysDataPermUser {

    /**
     * 数据权限Id。
     */
    private Long dataPermId;

    /**
     * 用户Id。
     */
    private Long userId;
}
