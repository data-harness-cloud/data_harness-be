package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.annotation.RelationManyToMany;
import supie.common.core.annotation.RelationOneToMany;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.model.BaseBusinessFile;
import supie.webadmin.upms.vo.SysMenuVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * 菜单实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_sys_menu")
public class SysMenu extends BaseModel {

    /**
     * 菜单Id。
     */
    @TableId(value = "menu_id")
    private Long menuId;

    /**
     * 父菜单Id，目录菜单的父菜单为null。
     */
    private Long parentId;

    /**
     * 菜单显示名称。
     */
    private String menuName;

    /**
     * 字符id。
     */
    @TableField(value = "str_id")
    private String strId;

    /**
     * 附件列表。
     */
    @RelationOneToMany(
            masterIdField = "strId",
            slaveModelClass = BaseBusinessFile.class,
            slaveIdField = "bindStrId")
    @TableField(exist = false)
    private List<BaseBusinessFile> fileDataList;

    /**
     * 菜单类型(0: 目录 1: 菜单 2: 按钮 3: UI片段)。
     */
    private Integer menuType;

    /**
     * 前端表单路由名称，仅用于menu_type为1的菜单类型。
     */
    private String formRouterName;

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
     * 菜单显示顺序 (值越小，排序越靠前)。
     */
    private Integer showOrder;

    /**
     * 菜单图标。
     */
    private String icon;

    /**
     * 附加信息。
     */
    @TableField(value = "extra_data")
    private String extraData;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @RelationManyToMany(
            relationMasterIdField = "menuId",
            relationModelClass = SysMenuPermCode.class)
    @TableField(exist = false)
    private List<SysMenuPermCode> sysMenuPermCodeList;

    @Mapper
    public interface SysMenuModelMapper extends BaseModelMapper<SysMenuVo, SysMenu> {
        /**
         * 转换VO对象到实体对象。
         *
         * @param sysMenuVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "sysMenuPermCodeList", expression = "java(mapToBean(sysMenuVo.getSysMenuPermCodeList(), supie.webadmin.upms.model.SysMenuPermCode.class))")
        @Override
        SysMenu toModel(SysMenuVo sysMenuVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysMenu 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "sysMenuPermCodeList", expression = "java(beanToMap(sysMenu.getSysMenuPermCodeList(), false))")
        @Override
        SysMenuVo fromModel(SysMenu sysMenu);
    }
    public static final SysMenuModelMapper INSTANCE = Mappers.getMapper(SysMenu.SysMenuModelMapper.class);
}
