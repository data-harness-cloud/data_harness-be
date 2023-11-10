package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 白名单实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_perm_whitelist")
public class SysPermWhitelist {

    /**
     * 权限资源的URL。
     */
    @TableId(value = "perm_url")
    private String permUrl;

    /**
     * 权限资源所属模块名字(通常是Controller的名字)。
     */
    private String moduleName;

    /**
     * 权限的名称。
     */
    private String permName;
}
