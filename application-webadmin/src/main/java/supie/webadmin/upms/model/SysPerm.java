package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.annotation.RelationDict;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * 权限资源实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_sys_perm")
public class SysPerm extends BaseModel {

    /**
     * 权限资源Id。
     */
    @TableId(value = "perm_id")
    private Long permId;

    /**
     * 权限所在的权限模块Id。
     */
    private Long moduleId;

    /**
     * 权限名称。
     */
    private String permName;

    /**
     * 关联的URL。
     */
    private String url;

    /**
     * 权限在当前模块下的顺序，由小到大。
     */
    private Integer showOrder;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @RelationDict(
            masterIdField = "moduleId",
            slaveModelClass = SysPermModule.class,
            slaveIdField = "moduleId",
            slaveNameField = "moduleName")
    @TableField(exist = false)
    private Map<String, Object> moduleIdDictMap;
}
