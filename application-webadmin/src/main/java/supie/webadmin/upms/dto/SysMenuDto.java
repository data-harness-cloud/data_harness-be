package supie.webadmin.upms.dto;

import supie.common.core.validator.ConstDictRef;
import supie.common.core.validator.UpdateGroup;
import supie.webadmin.upms.model.constant.SysMenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单Dto。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("菜单Dto")
@Data
public class SysMenuDto {

    /**
     * 菜单Id。
     */
    @ApiModelProperty(value = "菜单Id", required = true)
    @NotNull(message = "菜单Id不能为空！", groups = {UpdateGroup.class})
    private Long menuId;

    /**
     * 父菜单Id，目录菜单的父菜单为null
     */
    @ApiModelProperty(value = "父菜单Id")
    private Long parentId;

    /**
     * 菜单显示名称。
     */
    @ApiModelProperty(value = "菜单显示名称", required = true)
    @NotBlank(message = "菜单显示名称不能为空！")
    private String menuName;

    /**
     * 字符id。
     */
    @ApiModelProperty(value = "字符id")
    private String strId;

    /**
     * 菜单类型 (0: 目录 1: 菜单 2: 按钮 3: UI片段)。
     */
    @ApiModelProperty(value = "菜单类型", required = true)
    @NotNull(message = "菜单类型不能为空！")
    @ConstDictRef(constDictClass = SysMenuType.class, message = "数据验证失败，菜单类型为无效值！")
    private Integer menuType;

    /**
     * 前端表单路由名称，仅用于menu_type为1的菜单类型。
     */
    @ApiModelProperty(value = "前端表单路由名称")
    private String formRouterName;

    /**
     * 在线表单主键Id，仅用于在线表单绑定的菜单。
     */
    @ApiModelProperty(value = "在线表单主键Id")
    private Long onlineFormId;

    /**
     * 统计页面主键Id，仅用于统计页面绑定的菜单。
     */
    @ApiModelProperty(value = "统计页面主键Id")
    private Long reportPageId;

    /**
     * 仅用于在线表单的流程Id。
     */
    @ApiModelProperty(value = "仅用于在线表单的流程Id")
    private Long onlineFlowEntryId;

    /**
     * 菜单显示顺序 (值越小，排序越靠前)。
     */
    @ApiModelProperty(value = "菜单显示顺序", required = true)
    @NotNull(message = "菜单显示顺序不能为空！")
    private Integer showOrder;

    /**
     * 菜单图标。
     */
    @ApiModelProperty(value = "菜单显示顺序")
    private String icon;

    /**
     * 附加信息。
     */
    @ApiModelProperty(value = "附加信息")
    private String extraData;
}
