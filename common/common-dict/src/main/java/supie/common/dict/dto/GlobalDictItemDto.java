package supie.common.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import supie.common.core.validator.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 全局系统字典项目Dto。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("全局系统字典项目Dto")
@Data
public class GlobalDictItemDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 字典编码。
     */
    @ApiModelProperty(value = "字典编码")
    @NotBlank(message = "数据验证失败，字典编码不能为空！")
    private String dictCode;

    /**
     * 字典数据项Id。
     */
    @ApiModelProperty(value = "字典数据项Id")
    @NotNull(message = "数据验证失败，字典数据项Id不能为空！")
    private String itemId;

    /**
     * 字典数据项名称。
     */
    @ApiModelProperty(value = "字典数据项名称")
    @NotBlank(message = "数据验证失败，字典数据项名称不能为空！")
    private String itemName;

    /**
     * 显示顺序(数值越小越靠前)。
     */
    @ApiModelProperty(value = "显示顺序")
    @NotNull(message = "数据验证失败，显示顺序不能为空！")
    private Integer showOrder;
}
