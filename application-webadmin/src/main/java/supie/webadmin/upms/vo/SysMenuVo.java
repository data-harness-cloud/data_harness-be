package supie.webadmin.upms.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import supie.webadmin.app.model.BaseBusinessFile;

import java.util.List;
import java.util.Map;

/**
 * 菜单VO。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("菜单VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuVo extends BaseVo {

    /**
     * 菜单Id。
     */
    @ApiModelProperty(value = "菜单Id")
    private Long menuId;

    /**
     * 父菜单Id，目录菜单的父菜单为null
     */
    @ApiModelProperty(value = "父菜单Id")
    private Long parentId;

    /**
     * 菜单显示名称。
     */
    @ApiModelProperty(value = "菜单显示名称")
    private String menuName;

    /**
     * 字符id。
     */
    @ApiModelProperty(value = "字符id")
    private String strId;
    /**
     * 附件列表。
     */
    @ApiModelProperty(value = "附件列表")
    private List<BaseBusinessFile> fileDataList;

    /**
     * 菜单类型 (0: 目录 1: 菜单 2: 按钮 3: UI片段)。
     */
    @ApiModelProperty(value = "菜单类型")
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
     * 在线表单菜单的权限控制类型，具体值可参考SysOnlineMenuPermType常量对象。
     */
    @ApiModelProperty(value = "在线表单菜单的权限控制类型")
    private Integer onlineMenuPermType;

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
    @ApiModelProperty(value = "菜单显示顺序")
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

    /**
     * 菜单与权限字关联对象列表。
     */
    @ApiModelProperty(value = "菜单与权限字关联对象列表")
    private List<Map<String, Object>> sysMenuPermCodeList;
}
