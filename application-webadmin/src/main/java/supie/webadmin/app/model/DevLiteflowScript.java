package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DevLiteflowScriptVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * DevLiteflowScript实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_dev_liteflow_script")
public class DevLiteflowScript extends BaseModel {

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
     * 脚本id。
     */
    private String scriptId;

    /**
     * 脚本名称。
     */
    private String scriptName;

    /**
     * 脚本内容。
     */
    private String scriptData;

    /**
     * 脚本种类。
     */
    private String scriptType;

    /**
     * 脚本语言。
     */
    private String scriptLanguage;

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
     * application_name / script_name / script_language LIKE搜索字符串。
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
    public interface DevLiteflowScriptModelMapper extends BaseModelMapper<DevLiteflowScriptVo, DevLiteflowScript> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param devLiteflowScriptVo 域对象。
         * @return 实体对象。
         */
        @Override
        DevLiteflowScript toModel(DevLiteflowScriptVo devLiteflowScriptVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param devLiteflowScript 实体对象。
         * @return 域对象。
         */
        @Override
        DevLiteflowScriptVo fromModel(DevLiteflowScript devLiteflowScript);
    }
    public static final DevLiteflowScriptModelMapper INSTANCE = Mappers.getMapper(DevLiteflowScriptModelMapper.class);
}
