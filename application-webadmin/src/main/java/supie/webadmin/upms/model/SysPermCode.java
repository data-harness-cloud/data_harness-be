package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.annotation.RelationManyToMany;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.upms.vo.SysPermCodeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * 权限字实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_sys_perm_code")
public class SysPermCode extends BaseModel {

    /**
     * 权限字Id。
     */
    @TableId(value = "perm_code_id")
    private Long permCodeId;

    /**
     * 上级权限字Id。
     */
    private Long parentId;

    /**
     * 权限字标识(一般为有含义的英文字符串)。
     */
    private String permCode;

    /**
     * 权限类型(0: 表单 1: UI片段 2: 操作)。
     */
    private Integer permCodeType;

    /**
     * 显示名称。
     */
    private String showName;

    /**
     * 显示顺序(数值越小，越靠前)。
     */
    private Integer showOrder;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @RelationManyToMany(
            relationMasterIdField = "permCodeId",
            relationModelClass = SysPermCodePerm.class)
    @TableField(exist = false)
    private List<SysPermCodePerm> sysPermCodePermList;

    @Mapper
    public interface SysPermCodeModelMapper extends BaseModelMapper<SysPermCodeVo, SysPermCode> {
        /**
         * 转换VO对象到实体对象。
         *
         * @param sysPermCodeVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "sysPermCodePermList", expression = "java(mapToBean(sysPermCodeVo.getSysPermCodePermList(), supie.webadmin.upms.model.SysPermCodePerm.class))")
        @Override
        SysPermCode toModel(SysPermCodeVo sysPermCodeVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysPermCode 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "sysPermCodePermList", expression = "java(beanToMap(sysPermCode.getSysPermCodePermList(), false))")
        @Override
        SysPermCodeVo fromModel(SysPermCode sysPermCode);
    }
    public static final SysPermCodeModelMapper INSTANCE = Mappers.getMapper(SysPermCode.SysPermCodeModelMapper.class);
}
