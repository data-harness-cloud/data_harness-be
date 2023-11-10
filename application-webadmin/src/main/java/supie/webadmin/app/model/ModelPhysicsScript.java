package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.ModelPhysicsScriptVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * ModelPhysicsScript实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_model_physics_script")
public class ModelPhysicsScript extends BaseModel {

    /**
     * 租户号。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 数据所属人。
     */
    @UserFilterColumn
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @DeptFilterColumn
    private Long dataDeptId;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 分层关联项目id。
     */
    private Long projectId;

    /**
     * 模型id。
     */
    private Long modelId;

    /**
     * 物理模型建库语句。
     */
    private String modelPhysicsDatabase;

    /**
     * 物理模型建表语句。
     */
    private String modelPhysicsTable;

    /**
     * 物理模型表名称。
     */
    private String modelPhysicsTableName;

    /**
     * 物理模型数据源。
     */
    private String modelPhysicsDatasourceId;

    /**
     * 物理模型描述。
     */
    private String modelPhysicsDescription;

    /**
     * 物理模型状态（1、未建表。2、已建表）。
     */
    private Integer modelPhysicsState;

    /**
     * 模型权限：是否项目共享。
     */
    private String modelPhysicsPremise;

    /**
     * 负责人id。
     */
    private Long modelPhysicsHeaderId;

    /**
     * 显示顺序。
     */
    private Integer showOrder;

    /**
     * 分层id。
     */
    private Long planningWarehouseLayerId;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String updateTimeEnd;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String createTimeEnd;

    /**
     * model_physics_database / model_physics_table / model_physics_description / model_physics_premise LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface ModelPhysicsScriptModelMapper extends BaseModelMapper<ModelPhysicsScriptVo, ModelPhysicsScript> {
    }
    public static final ModelPhysicsScriptModelMapper INSTANCE = Mappers.getMapper(ModelPhysicsScriptModelMapper.class);
}
