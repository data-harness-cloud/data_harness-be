package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DevLiteflowRulerVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * DevLiteflowRuler实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_dev_liteflow_ruler")
public class DevLiteflowRuler extends BaseModel {

    /**
     * 编号。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 字符编号。
     */
    private String strId;

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
     * 应用名称。
     */
    private String applicationName;

    /**
     * 流程chain名称。
     */
    private String chainName;

    /**
     * 流程chain描述。
     */
    private String chainDesc;

    /**
     * 规则表达式。
     */
    private String elData;

    /**
     * 流程结构JSON数据。
     */
    private String chainStructureJson;

    /**
     * 上线类型（0：开发中。1：发布上线）。
     */
    private Integer onlineType;

    /**
     * 上一个版本的ID。
     */
    private Long previousVersionId;

    /**
     * 过程ID。
     */
    private Long processId;

    /**
     * 所属项目ID。
     */
    private Long projectId;

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
     * application_name / chain_name LIKE搜索字符串。
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
            masterIdField = "dataUserId",
            slaveModelClass = SysUser.class,
            slaveIdField = "userId",
            slaveNameField = "loginName")
    @TableField(exist = false)
    private Map<String, Object> dataUserIdDictMap;

    @RelationDict(
            masterIdField = "dataDeptId",
            slaveModelClass = SysDept.class,
            slaveIdField = "deptId",
            slaveNameField = "deptName")
    @TableField(exist = false)
    private Map<String, Object> dataDeptIdDictMap;

    @Mapper
    public interface DevLiteflowRulerModelMapper extends BaseModelMapper<DevLiteflowRulerVo, DevLiteflowRuler> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param devLiteflowRulerVo 域对象。
         * @return 实体对象。
         */
        @Override
        DevLiteflowRuler toModel(DevLiteflowRulerVo devLiteflowRulerVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param devLiteflowRuler 实体对象。
         * @return 域对象。
         */
        @Override
        DevLiteflowRulerVo fromModel(DevLiteflowRuler devLiteflowRuler);
    }
    public static final DevLiteflowRulerModelMapper INSTANCE = Mappers.getMapper(DevLiteflowRulerModelMapper.class);
}
