package supie.common.dict.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典实体类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "zz_tenant_global_dict")
public class TenantGlobalDict extends GlobalDict {

    /**
     * 是否为所有租户的通用字典。
     */
    @TableField(value = "tenant_common")
    private Boolean tenantCommon;

    /**
     * 租户的非公用字典的初始化字典数据。
     */
    @TableField(value = "initial_data")
    private String initialData;
}
