package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.PlanningWarehouseLayerVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * PlanningWarehouseLayer实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_planning_warehouse_layer")
public class PlanningWarehouseLayer extends BaseModel {

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
     * 分层名称。
     */
    private String houseLayerName;

    /**
     * 分层编码。
     */
    private String houseLayerCode;

    /**
     * 分层状态。
     */
    private Integer houseLayerStatus;

    /**
     * 分层关联库。
     */
    private String houseLayerDatabase;

    /**
     * 分层描述。
     */
    private String houseLayerDescription;

    /**
     * 分层负责人id。
     */
    private Long houseLayerHeaderUserId;

    /**
     * 分层类型：是否为维度层。
     */
    private String houseLayerType;

    /**
     * 分层数据源类型。
     */
    private String houseLayerDatasourceType;

    /**
     * 数据源id。
     */
    private Long datasourceId;

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
     * house_layer_name / house_layer_code / house_layer_database / house_layer_description / house_layer_type / house_layer_datasource_type LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    /**
     * ModelPhysicsScript 的一对多关联表数据对象。
     * 通常在一对多的关联中，我们基于从表数据过滤主表数据，此时需要先对从表数据进行嵌套子查询过滤，并将从表过滤数据列表集成到该字段。
     */
    @RelationOneToMany(
            masterIdField = "id",
            slaveModelClass = ModelPhysicsScript.class,
            slaveIdField = "planningWarehouseLayerId")
    @TableField(exist = false)
    private List<ModelPhysicsScript> modelPhysicsScriptList;

    public PlanningWarehouseLayer(Long projectId, String houseLayerCode, String houseLayerName, Integer houseLayerStatus) {
        this.projectId = projectId;
        this.houseLayerCode = houseLayerCode;
        this.houseLayerName = houseLayerName;
        this.houseLayerStatus = houseLayerStatus;
    }

    @Mapper
    public interface PlanningWarehouseLayerModelMapper extends BaseModelMapper<PlanningWarehouseLayerVo, PlanningWarehouseLayer> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param planningWarehouseLayerVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "modelPhysicsScriptList", expression = "java(mapToBean(planningWarehouseLayerVo.getModelPhysicsScriptList(), supie.webadmin.app.model.ModelPhysicsScript.class))")
        @Override
        PlanningWarehouseLayer toModel(PlanningWarehouseLayerVo planningWarehouseLayerVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param planningWarehouseLayer 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "modelPhysicsScriptList", expression = "java(beanToMap(planningWarehouseLayer.getModelPhysicsScriptList(), false))")
        @Override
        PlanningWarehouseLayerVo fromModel(PlanningWarehouseLayer planningWarehouseLayer);
    }
    public static final PlanningWarehouseLayerModelMapper INSTANCE = Mappers.getMapper(PlanningWarehouseLayerModelMapper.class);
}
