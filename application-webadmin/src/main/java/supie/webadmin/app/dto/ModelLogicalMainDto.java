package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import supie.webadmin.app.util.sqlUtil.ColumnField;
import supie.webadmin.app.util.sqlUtil.ColumnType;

import javax.validation.constraints.*;

/**
 * ModelLogicalMainDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ModelLogicalMainDto对象")
@Data
public class ModelLogicalMainDto {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号", required = true)
    @NotNull(message = "数据验证失败，租户号不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 数据所属人。
     */
    @ApiModelProperty(value = "数据所属人")
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @ApiModelProperty(value = "数据所属部门")
    private Long dataDeptId;

    /**
     * 分层关联项目id。
     */
    @ApiModelProperty(value = "分层关联项目id")
    private Long projectId;

    /**
     * 关联业务过程id。
     */
    @ApiModelProperty(value = "关联业务过程id")
    private Long processId;

    /**
     * 模型名称。
     */
    @ApiModelProperty(value = "模型名称")
    @ColumnField(ColumnType.TABLE_NAME)
    private String modelName;

    /**
     * 模型代码。
     */
    @ApiModelProperty(value = "模型代码")
    @ColumnField(ColumnType.TABLE_CODE)
    private String modelCode;

    /**
     * 模型状态。
     */
    @ApiModelProperty(value = "模型状态")
    private String modelStatus;

    /**
     * 模型描述。
     */
    @ApiModelProperty(value = "模型描述")
    private String modelDescription;

    /**
     * 模型数据源类型。
     */
    @ApiModelProperty(value = "模型数据源类型")
    private String modelDatasourceType;

    /**
     * 模型业务分类。
     */
    @ApiModelProperty(value = "模型业务分类")
    private String modelClassType;

    /**
     * 模型主题域类型。
     */
    @ApiModelProperty(value = "模型主题域类型")
    private String modelThemeType;

    /**
     * 模型分层类型。
     */
    @ApiModelProperty(value = "模型分层类型")
    private String modelLayerType;

    /**
     * 模型表类型，字典表。
     */
    @ApiModelProperty(value = "模型表类型，字典表")
    private String modelTableType;

    /**
     * 模型更新周期，字典类型，对应命名。
     */
    @ApiModelProperty(value = "模型更新周期，字典类型，对应命名")
    private String modelUpdateCycle;

    /**
     * 模型数仓表类型。
     */
    @ApiModelProperty(value = "模型数仓表类型")
    private String modelWarehouseType;

    /**
     * 模型表权限类型，个人还是项目共享。
     */
    @ApiModelProperty(value = "模型表权限类型，个人还是项目共享")
    private String modelTablePermissions;

    /**
     * 模型表别名。
     */
    @ApiModelProperty(value = "模型表别名")
    private String modelTableAlias;

    /**
     * 模型表等级，字典表。
     */
    @ApiModelProperty(value = "模型表等级，字典表")
    private String modelTableLevel;

    /**
     * 模型表生命周期，字典。
     */
    @ApiModelProperty(value = "模型表生命周期，字典")
    private String modelLifeCycle;

    /**
     * 模型发布状态。
     */
    @ApiModelProperty(value = "模型发布状态")
    private String modelPublishStatus;

    /**
     * 模型SQL脚本。
     */
    @ApiModelProperty(value = "模型SQL脚本")
    private String modelSqlScript;

    /**
     * 模型版本。
     */
    @ApiModelProperty(value = "模型版本")
    private String modelVersion;

    /**
     * 模型发布物理库。
     */
    @ApiModelProperty(value = "模型发布物理库")
    private String modelPhysicalDatabase;

    /**
     * 模型数据源。
     */
    @ApiModelProperty(value = "模型数据源ID")
    private Long modelDatasourceId;

    /**
     * 模型发布类型，新建表、覆盖表。
     */
    @ApiModelProperty(value = "模型发布类型，新建表、覆盖表")
    private String modelPublicType;

    /**
     * 模型发布描述。
     */
    @ApiModelProperty(value = "模型发布描述")
    private String modelPublicDescription;

    /**
     * 数据分层id warehouse_layer_id
     */
    @ApiModelProperty(value = "数据分层ID")
    private Long warehouseLayerId;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤起始值(>=)")
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤结束值(<=)")
    private String updateTimeEnd;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值(>=)")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值(<=)")
    private String createTimeEnd;

    /**
     * model_name / model_code / model_status / model_description / model_datasource_type / model_class_type / model_theme_type / model_layer_type / model_table_type / model_update_cycle / model_warehouse_type / model_table_permissions / model_table_alias / model_table_level / model_life_cycle / model_publish_status / model_sql_script / model_version / model_physical_database / model_public_type / model_public_description LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;

    /**
     * 自定义表名 model_table_custom_name
     */
    @ApiModelProperty(value = "自定义表名")
    private String modelTableCustomName;

}
