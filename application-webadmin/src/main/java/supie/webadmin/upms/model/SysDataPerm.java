package supie.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.RelationManyToMany;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.upms.vo.SysDataPermVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * 数据权限实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_sys_data_perm")
public class SysDataPerm extends BaseModel {

    /**
     * 主键Id。
     */
    @TableId(value = "data_perm_id")
    private Long dataPermId;

    /**
     * 显示名称。
     */
    private String dataPermName;

    /**
     * 数据权限规则类型(0: 全部可见 1: 只看自己 2: 只看本部门 3: 本部门及子部门 4: 多部门及子部门 5: 自定义部门列表)。
     */
    private Integer ruleType;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @TableField(exist = false)
    private String deptIdListString;

    @RelationManyToMany(
            relationMasterIdField = "dataPermId",
            relationModelClass = SysDataPermDept.class)
    @TableField(exist = false)
    private List<SysDataPermDept> dataPermDeptList;

    @RelationManyToMany(
            relationMasterIdField = "dataPermId",
            relationModelClass = SysDataPermMenu.class)
    @TableField(exist = false)
    private List<SysDataPermMenu> dataPermMenuList;

    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface SysDataPermModelMapper extends BaseModelMapper<SysDataPermVo, SysDataPerm> {
        /**
         * 转换VO对象到实体对象。
         *
         * @param sysDataPermVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "dataPermDeptList", expression = "java(mapToBean(sysDataPermVo.getDataPermDeptList(), supie.webadmin.upms.model.SysDataPermDept.class))")
        @Mapping(target = "dataPermMenuList", expression = "java(mapToBean(sysDataPermVo.getDataPermMenuList(), supie.webadmin.upms.model.SysDataPermMenu.class))")
        @Override
        SysDataPerm toModel(SysDataPermVo sysDataPermVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysDataPerm 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "dataPermDeptList", expression = "java(beanToMap(sysDataPerm.getDataPermDeptList(), false))")
        @Mapping(target = "dataPermMenuList", expression = "java(beanToMap(sysDataPerm.getDataPermMenuList(), false))")
        @Override
        SysDataPermVo fromModel(SysDataPerm sysDataPerm);
    }
    public static final SysDataPermModelMapper INSTANCE = Mappers.getMapper(SysDataPerm.SysDataPermModelMapper.class);
}
