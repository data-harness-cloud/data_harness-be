package supie.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门岗位VO对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("部门岗位VO")
@Data
public class SysDeptPostVo {

    /**
     * 部门岗位Id。
     */
    @ApiModelProperty(value = "部门岗位Id")
    private Long deptPostId;

    /**
     * 部门Id。
     */
    @ApiModelProperty(value = "部门Id")
    private Long deptId;

    /**
     * 岗位Id。
     */
    @ApiModelProperty(value = "岗位Id")
    private Long postId;

    /**
     * 部门岗位显示名称。
     */
    @ApiModelProperty(value = "部门岗位显示名称")
    private String postShowName;
}
