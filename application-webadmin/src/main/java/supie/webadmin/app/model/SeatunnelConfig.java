package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.SeatunnelConfigVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;

/**
 * SeatunnelConfig实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_seatunnel_config")
public class SeatunnelConfig extends BaseModel {

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
     * Seatunnel 的调用方式（1：接口。2：ssh。默认为1）。
     */
    private Integer callMode = 1;

    /**
     * 接口方式调用 Seatunnel 的访问根地址（包含端口）
     */
    private String localhostUri;

    /**
     * Seatunnel 的提交job的地址（默认为版本2.3.3的Submit Job地址：/hazelcast/rest/maps/submit-job）
     */
    private String submitJobUrl = "/hazelcast/rest/maps/submit-job";

    /**
     * 远程主机ID。
     */
    private Long remoteHostId;

    /**
     * seatunnel安装路径。
     */
    private String seatunnelPath;

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
     * 关联项目id。
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
     * show_name LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @RelationOneToOne(
            masterIdField = "remoteHostId",
            slaveModelClass = RemoteHost.class,
            slaveIdField = "id")
    @TableField(exist = false)
    private RemoteHost remoteHost;

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
    public interface SeatunnelConfigModelMapper extends BaseModelMapper<SeatunnelConfigVo, SeatunnelConfig> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param seatunnelConfigVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "remoteHost", expression = "java(mapToBean(seatunnelConfigVo.getRemoteHost(), supie.webadmin.app.model.RemoteHost.class))")
        @Override
        SeatunnelConfig toModel(SeatunnelConfigVo seatunnelConfigVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param seatunnelConfig 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "remoteHost", expression = "java(beanToMap(seatunnelConfig.getRemoteHost(), false))")
        @Override
        SeatunnelConfigVo fromModel(SeatunnelConfig seatunnelConfig);
    }
    public static final SeatunnelConfigModelMapper INSTANCE = Mappers.getMapper(SeatunnelConfigModelMapper.class);
}
