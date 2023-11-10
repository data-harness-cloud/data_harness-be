package supie.webadmin.upms.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 权限资源模块VO。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("权限资源模块VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPermModuleVo extends BaseVo {

    /**
     * 权限模块Id。
     */
    @ApiModelProperty(value = "权限模块Id")
    private Long moduleId;

    /**
     * 权限模块名称。
     */
    @ApiModelProperty(value = "权限模块名称")
    private String moduleName;

    /**
     * 上级权限模块Id。
     */
    @ApiModelProperty(value = "上级权限模块Id")
    private Long parentId;

    /**
     * 权限模块类型(0: 普通模块 1: Controller模块)。
     */
    @ApiModelProperty(value = "权限模块类型")
    private Integer moduleType;

    /**
     * 权限模块在当前层级下的顺序，由小到大。
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 权限资源对象列表。
     */
    @ApiModelProperty(value = "权限资源对象列表")
    private List<SysPermVo> sysPermList;
}