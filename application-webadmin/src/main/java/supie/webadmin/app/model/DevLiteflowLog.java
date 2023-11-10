package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.webadmin.upms.model.SysDept;
import supie.webadmin.upms.model.SysUser;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.upload.UploadStoreTypeEnum;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DevLiteflowLogVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.Map;

/**
 * DevLiteflowLog实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_dev_liteflow_log")
public class DevLiteflowLog extends BaseModel {

    /**
     * 主键ID。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 规则链ID。
     */
    private Long rulerId;
    /**
     * 所属任务ID
     */
    private Long schedulingTasksId;

    /**
     * 运行版本。
     */
    private Integer runVersion;

    /**
     * 运行时间。
     */
    private Date runTime;

    /**
     * 运行结果。
     */
    private String runResult;

    /**
     * 运行结果信息
     */
    private String runResultMsg;

    /**
     * 日志文件名称（rulerId_version_nowDate.log）。
     */
    private String logFileName;

    /**
     * 日志文件Json字段。
     */
    @UploadFlagColumn(storeType = UploadStoreTypeEnum.MINIO_SYSTEM)
    private String logFileJson;

    /**
     * 日志文件大小。
     */
    private Long logFileSize;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer isDelete;

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
     * runTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String runTimeStart;

    /**
     * runTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String runTimeEnd;

    /**
     * logFileSize 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Long logFileSizeStart;

    /**
     * logFileSize 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Long logFileSizeEnd;

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
     * log_file_name LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

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
    public interface DevLiteflowLogModelMapper extends BaseModelMapper<DevLiteflowLogVo, DevLiteflowLog> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param devLiteflowLogVo 域对象。
         * @return 实体对象。
         */
        @Override
        DevLiteflowLog toModel(DevLiteflowLogVo devLiteflowLogVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param devLiteflowLog 实体对象。
         * @return 域对象。
         */
        @Override
        DevLiteflowLogVo fromModel(DevLiteflowLog devLiteflowLog);
    }
    public static final DevLiteflowLogModelMapper INSTANCE = Mappers.getMapper(DevLiteflowLogModelMapper.class);
}
