package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * 权限模块实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_sys_perm_module")
public class SysPermModule extends BaseModel {

    /**
     * 权限模块Id。
     */
    @TableId(value = "module_id")
    private Long moduleId;

    /**
     * 上级权限模块Id。
     */
    private Long parentId;

    /**
     * 权限模块名称。
     */
    private String moduleName;

    /**
     * 权限模块类型(0: 普通模块 1: Controller模块)。
     */
    private Integer moduleType;

    /**
     * 权限模块在当前层级下的顺序，由小到大。
     */
    private Integer showOrder;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @TableField(exist = false)
    private List<SysPerm> sysPermList;
}
