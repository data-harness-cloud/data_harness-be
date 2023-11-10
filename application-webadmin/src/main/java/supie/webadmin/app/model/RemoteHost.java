package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.RemoteHostVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * RemoteHost实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_remote_host")
public class RemoteHost extends BaseModel {

    /**
     * 主键ID。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 显示名称。
     */
    private String showName;

    /**
     * SSH-IP地址。
     */
    private String hostIp;

    /**
     * 端口。
     */
    private String hostPort;

    /**
     * ssh-key文件存储路径。
     */
    private String hostKeyFilePath;

    /**
     * 登录名。
     */
    private String loginName;

    /**
     * 密码。
     */
    private String password;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer isDelete;

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
     * 项目关联id。
     */
    private Long projectId;

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
     * show_name / login_name LIKE搜索字符串。
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
    public interface RemoteHostModelMapper extends BaseModelMapper<RemoteHostVo, RemoteHost> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param remoteHostVo 域对象。
         * @return 实体对象。
         */
        @Override
        RemoteHost toModel(RemoteHostVo remoteHostVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param remoteHost 实体对象。
         * @return 域对象。
         */
        @Override
        RemoteHostVo fromModel(RemoteHost remoteHost);
    }
    public static final RemoteHostModelMapper INSTANCE = Mappers.getMapper(RemoteHostModelMapper.class);
}
