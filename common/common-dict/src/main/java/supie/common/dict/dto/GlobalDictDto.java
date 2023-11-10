package supie.common.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import supie.common.core.validator.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 全局系统字典Dto。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("全局系统字典Dto")
@Data
public class GlobalDictDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long dictId;

    /**
     * 字典编码。
     */
    @ApiModelProperty(value = "字典编码")
    @NotBlank(message = "数据验证失败，字典编码不能为空！")
    private String dictCode;

    /**
     * 字典中文名称。
     */
    @ApiModelProperty(value = "字典中文名称")
    @NotBlank(message = "数据验证失败，字典中文名称不能为空！")
    private String dictName;
}
