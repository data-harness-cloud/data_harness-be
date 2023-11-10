package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.util.sqlUtil.ColumnField;
import supie.webadmin.app.util.sqlUtil.ColumnType;
import supie.webadmin.app.vo.ModelLogicalMainVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * ModelLogicalMain实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_model_logical_main")
public class ModelLogicalMain extends BaseModel {

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
     * 关联业务过程id。
     */
    private Long processId;

    /**
     * 模型名称。
     */
    @ColumnField(ColumnType.TABLE_NAME)
    private String modelName;

    /**
     * 模型代码。
     */
    @ColumnField(ColumnType.TABLE_CODE)
    private String modelCode;

    /**
     * 模型状态。
     */
    private String modelStatus;

    /**
     * 模型描述。
     */
    private String modelDescription;

    /**
     * 模型数据源类型。
     */
    private String modelDatasourceType;

    /**
     * 模型业务分类。
     */
    private String modelClassType;

    /**
     * 模型主题域类型。
     */
    private String modelThemeType;

    /**
     * 模型分层类型。
     */
    private String modelLayerType;

    /**
     * 模型表类型，字典表。
     */
    private String modelTableType;

    /**
     * 模型更新周期，字典类型，对应命名。
     */
    private String modelUpdateCycle;

    /**
     * 模型数仓表类型。
     */
    private String modelWarehouseType;

    /**
     * 模型表权限类型，个人还是项目共享。
     */
    private String modelTablePermissions;

    /**
     * 模型表别名。
     */
    private String modelTableAlias;

    /**
     * 模型表等级，字典表。
     */
    private String modelTableLevel;

    /**
     * 模型表生命周期，字典。
     */
    private String modelLifeCycle;

    /**
     * 模型发布状态。
     */
    private String modelPublishStatus;

    /**
     * 模型SQL脚本。
     */
    private String modelSqlScript;

    /**
     * 模型版本。
     */
    private String modelVersion;

    /**
     * 模型发布物理库。
     */
    private String modelPhysicalDatabase;

    /**
     * 模型数据源。
     */
    private Long modelDatasourceId;

    /**
     * 模型发布类型，新建表、覆盖表。
     */
    private String modelPublicType;

    /**
     * 模型发布描述。
     */
    private String modelPublicDescription;

    /**
     * 数据分层id warehouse_layer_id
     */
    private Long warehouseLayerId;

    /**
     * 自定义表名 model_table_custom_name
     */
    private String modelTableCustomName;

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
     * model_name / model_code / model_status / model_description / model_datasource_type / model_class_type / model_theme_type / model_layer_type / model_table_type / model_update_cycle / model_warehouse_type / model_table_permissions / model_table_alias / model_table_level / model_life_cycle / model_publish_status / model_sql_script / model_version / model_physical_database / model_public_type / model_public_description LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @RelationOneToOne(
            masterIdField = "id",
            slaveModelClass = ModelPhysicsScript.class,
            slaveIdField = "modelId")
    @TableField(exist = false)
    private ModelPhysicsScript modelPhysicsScript;

    @Mapper
    public interface ModelLogicalMainModelMapper extends BaseModelMapper<ModelLogicalMainVo, ModelLogicalMain> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param modelLogicalMainVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "modelPhysicsScript", expression = "java(mapToBean(modelLogicalMainVo.getModelPhysicsScript(), supie.webadmin.app.model.ModelPhysicsScript.class))")
        @Override
        ModelLogicalMain toModel(ModelLogicalMainVo modelLogicalMainVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param modelLogicalMain 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "modelPhysicsScript", expression = "java(beanToMap(modelLogicalMain.getModelPhysicsScript(), false))")
        @Override
        ModelLogicalMainVo fromModel(ModelLogicalMain modelLogicalMain);
    }
    public static final ModelLogicalMainModelMapper INSTANCE = Mappers.getMapper(ModelLogicalMainModelMapper.class);
}
