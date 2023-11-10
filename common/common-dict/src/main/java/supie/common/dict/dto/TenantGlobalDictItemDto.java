package supie.common.dict.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典项目Dto。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("租户全局系统字典项目Dto")
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantGlobalDictItemDto extends GlobalDictItemDto {

}
