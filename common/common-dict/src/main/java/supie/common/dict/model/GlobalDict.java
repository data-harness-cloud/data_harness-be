package supie.common.dict.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 全局系统字典实体类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@TableName(value = "zz_global_dict")
public class GlobalDict {

    /**
     * 主键Id。
     */
    @TableId(value = "dict_id")
    private Long dictId;

    /**
     * 字典编码。
     */
    @TableField(value = "dict_code")
    private String dictCode;

    /**
     * 字典中文名称。
     */
    @TableField(value = "dict_name")
    private String dictName;

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
     * 逻辑删除字段。
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;
}
