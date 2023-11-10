package supie.common.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典Dto。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("租户全局系统字典Dto")
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantGlobalDictDto extends GlobalDictDto {

    /**
     * 是否为所有租户的通用字典。
     */
    @ApiModelProperty(value = "是否为所有租户的通用字典")
    private Boolean tenantCommon;

    /**
     * 租户的非公用字典的初始化字典数据。
     */
    @ApiModelProperty(value = "租户的非公用字典的初始化字典数据")
    private String initialData;
}
