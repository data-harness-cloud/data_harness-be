package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门关联实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sdt_sys_dept_relation")
public class SysDeptRelation {

    /**
     * 上级部门Id。
     */
    private Long parentDeptId;

    /**
     * 部门Id。
     */
    private Long deptId;
}
