package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 部门岗位多对多关联实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_dept_post")
public class SysDeptPost {

    /**
     * 部门岗位Id。
     */
    @TableId(value = "dept_post_id")
    private Long deptPostId;

    /**
     * 部门Id。
     */
    private Long deptId;

    /**
     * 岗位Id。
     */
    private Long postId;

    /**
     * 部门岗位显示名称。
     */
    private String postShowName;
}
