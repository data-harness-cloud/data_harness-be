package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.PlanningClassificationVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * PlanningClassification实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_planning_classification")
public class PlanningClassification extends BaseModel {

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
     * 关联项目id。
     */
    private Long projectId;

    /**
     * 分类名称。
     */
    private String classificationName;

    /**
     * 分类代码。
     */
    private String classificationCode;

    /**
     * 分类状态。
     */
    private String classificationStatus;

    /**
     * 分类描述。
     */
    private String classificationDescription;

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
     * classification_name / classification_code / classification_status / classification_description LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @RelationDict(
            masterIdField = "createUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> createUserIdDictMap;

    @RelationDict(
            masterIdField = "updateUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> updateUserIdDictMap;

    @RelationDict(
            masterIdField = "dataUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> dataUserIdDictMap;

    @Mapper
    public interface PlanningClassificationModelMapper extends BaseModelMapper<PlanningClassificationVo, PlanningClassification> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param planningClassificationVo 域对象。
         * @return 实体对象。
         */
        @Override
        PlanningClassification toModel(PlanningClassificationVo planningClassificationVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param planningClassification 实体对象。
         * @return 域对象。
         */
        @Override
        PlanningClassificationVo fromModel(PlanningClassification planningClassification);
    }
    public static final PlanningClassificationModelMapper INSTANCE = Mappers.getMapper(PlanningClassificationModelMapper.class);
}
