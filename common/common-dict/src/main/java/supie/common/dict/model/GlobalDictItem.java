package supie.common.dict.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 全局系统字典项目实体类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "zz_global_dict_item")
public class GlobalDictItem {

    /**
     * 主键Id。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 字典编码。
     */
    @TableField(value = "dict_code")
    private String dictCode;

    /**
     * 字典数据项Id。
     */
    @TableField(value = "item_id")
    private String itemId;

    /**
     * 字典数据项名称。
     */
    @TableField(value = "item_name")
    private String itemName;

    /**
     * 显示顺序(数值越小越靠前)。
     */
    @TableField(value = "show_order")
    private Integer showOrder;

    /**
     * 字典状态。具体值引用DictItemStatus常量类。
     */
    private Integer status;

    /**
     * 创建用户Id。
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间。
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新用户名。
     */
    @TableField(value = "update_user_id")
    private Long updateUserId;

    /**
     * 更新时间。
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除字段。
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;
}
