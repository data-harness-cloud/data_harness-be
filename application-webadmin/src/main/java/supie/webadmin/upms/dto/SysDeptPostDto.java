package supie.webadmin.upms.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 部门岗位Dto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("部门岗位Dto")
@Data
public class SysDeptPostDto {

    /**
     * 部门岗位Id。
     */
    @ApiModelProperty(value = "部门岗位Id", required = true)
    @NotNull(message = "数据验证失败，部门岗位Id不能为空！", groups = {UpdateGroup.class})
    private Long deptPostId;

    /**
     * 部门Id。
     */
    @ApiModelProperty(value = "部门Id", required = true)
    @NotNull(message = "数据验证失败，部门Id不能为空！", groups = {UpdateGroup.class})
    private Long deptId;

    /**
     * 岗位Id。
     */
    @ApiModelProperty(value = "岗位Id", required = true)
    @NotNull(message = "数据验证失败，岗位Id不能为空！", groups = {UpdateGroup.class})
    private Long postId;

    /**
     * 部门岗位显示名称。
     */
    @ApiModelProperty(value = "部门岗位显示名称", required = true)
    @NotBlank(message = "数据验证失败，部门岗位显示名称不能为空！")
    private String postShowName;
}
