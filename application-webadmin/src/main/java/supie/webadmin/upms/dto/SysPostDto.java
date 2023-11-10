package supie.webadmin.upms.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 岗位Dto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("岗位Dto")
@Data
public class SysPostDto {

    /**
     * 岗位Id。
     */
    @ApiModelProperty(value = "岗位Id", required = true)
    @NotNull(message = "数据验证失败，岗位Id不能为空！", groups = {UpdateGroup.class})
    private Long postId;

    /**
     * 岗位名称。
     */
    @ApiModelProperty(value = "岗位名称", required = true)
    @NotBlank(message = "数据验证失败，岗位名称不能为空！")
    private String postName;

    /**
     * 岗位层级，数值越小级别越高。
     */
    @ApiModelProperty(value = "岗位层级", required = true)
    @NotNull(message = "数据验证失败，岗位层级不能为空！")
    private Integer postLevel;

    /**
     * 是否领导岗位。
     */
    @ApiModelProperty(value = "是否领导岗位", required = true)
    @NotNull(message = "数据验证失败，领导岗位不能为空！", groups = {UpdateGroup.class})
    private Boolean leaderPost;
}
