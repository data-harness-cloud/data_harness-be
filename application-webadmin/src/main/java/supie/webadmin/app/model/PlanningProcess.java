package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.PlanningProcessVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * PlanningProcess实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_planning_process")
public class PlanningProcess extends BaseModel {

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
     * 关联分类id。
     */
    private Long classificationId;

    /**
     * 关联主题域id。
     */
    private Long processThemeId;

    /**
     * 业务过程名称。
     */
    private String processName;

    /**
     * 业务过程代码。
     */
    private String processCode;

    /**
     * 业务过程状态。
     */
    private String processStatus;

    /**
     * 业务过程描述。
     */
    private String processDescription;

    /**
     * 父过程id。
     */
    private Long processParentId;

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
     * process_name / process_code / process_status / process_description LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface PlanningProcessModelMapper extends BaseModelMapper<PlanningProcessVo, PlanningProcess> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param planningProcessVo 域对象。
         * @return 实体对象。
         */
        @Override
        PlanningProcess toModel(PlanningProcessVo planningProcessVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param planningProcess 实体对象。
         * @return 域对象。
         */
        @Override
        PlanningProcessVo fromModel(PlanningProcess planningProcess);
    }
    public static final PlanningProcessModelMapper INSTANCE = Mappers.getMapper(PlanningProcessModelMapper.class);
}
