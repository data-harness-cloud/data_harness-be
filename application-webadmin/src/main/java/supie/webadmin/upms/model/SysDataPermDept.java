package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

/**
 * 数据权限与部门关联实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@ToString(of = {"deptId"})
@TableName(value = "sdt_sys_data_perm_dept")
public class SysDataPermDept {

    /**
     * 数据权限Id。
     */
    private Long dataPermId;

    /**
     * 关联部门Id。
     */
    private Long deptId;
}
