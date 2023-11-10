package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DevLiteflowNodeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * DevLiteflowNode实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_dev_liteflow_node")
public class DevLiteflowNode extends BaseModel {

    /**
     * 主键id。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 规则链ID（EL-ID）。
     */
    private Long rulerId;

    /**
     * 组件ID。
     */
    private String nodeId;

    /**
     * 组件标签。
     */
    private String nodeTag;

    /**
     * 字段值数据（JSON数据）。
     */
    private String fieldJsonData;

    /**
     * 上一次执行结果信息
     */
    private String executionMessage;

    /**
     * 节点名称。
     */
    private String name;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 节点状态（1：启用。0：停用）。
     */
    private Integer status;

    /**
     * 数据所属人ID。
     */
    @UserFilterColumn
    private Long dataUserId;

    /**
     * 数据所属部门ID。
     */
    @DeptFilterColumn
    private Long dataDeptId;

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
     * node_tag LIKE搜索字符串。
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
    public interface DevLiteflowNodeModelMapper extends BaseModelMapper<DevLiteflowNodeVo, DevLiteflowNode> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param devLiteflowNodeVo 域对象。
         * @return 实体对象。
         */
        @Override
        DevLiteflowNode toModel(DevLiteflowNodeVo devLiteflowNodeVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param devLiteflowNode 实体对象。
         * @return 域对象。
         */
        @Override
        DevLiteflowNodeVo fromModel(DevLiteflowNode devLiteflowNode);
    }
    public static final DevLiteflowNodeModelMapper INSTANCE = Mappers.getMapper(DevLiteflowNodeModelMapper.class);
}
