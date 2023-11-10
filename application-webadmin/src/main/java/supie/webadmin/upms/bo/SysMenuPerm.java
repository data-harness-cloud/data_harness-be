package supie.webadmin.upms.bo;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 菜单相关的业务对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class SysMenuPerm {


    /**
     * 菜单Id。
     */
    private Long menuId;

    /**
     * 父菜单Id，目录菜单的父菜单为null
     */
    private Long parentId;

    /**
     * 菜单显示名称。
     */
    private String menuName;

    /**
     * 菜单类型 (0: 目录 1: 菜单 2: 按钮 3: UI片段)。
     */
    private Integer menuType;

    /**
     * 在线表单主键Id，仅用于在线表单绑定的菜单。
     */
    private Long onlineFormId;

    /**
     * 在线表单菜单的权限控制类型，具体值可参考SysOnlineMenuPermType常量对象。
     */
    private Integer onlineMenuPermType;

    /**
     * 统计页面主键Id，仅用于统计页面绑定的菜单。
     */
    private Long reportPageId;

    /**
     * 仅用于在线表单的流程Id。
     */
    private Long onlineFlowEntryId;

    /**
     * 关联权限URL集合。
     */
    Set<String> permUrlSet = new HashSet<>();

    /**
     * 关联的某一个url。
     */
    String url;
}
