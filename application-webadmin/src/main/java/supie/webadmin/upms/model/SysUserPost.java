package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户岗位多对多关系实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "sdt_sys_user_post")
public class SysUserPost {

    /**
     * 用户Id。
     */
    private Long userId;

    /**
     * 部门岗位Id。
     */
    private Long deptPostId;

    /**
     * 岗位Id。
     */
    private Long postId;
}
