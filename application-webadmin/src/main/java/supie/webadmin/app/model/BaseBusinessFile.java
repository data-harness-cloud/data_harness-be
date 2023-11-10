package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.upload.UploadStoreTypeEnum;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.BaseBusinessFileVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * BaseBusinessFile实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_base_business_file")
public class BaseBusinessFile extends BaseModel {

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
     * 绑定id。
     */
    private Long bindId;

    /**
     * 绑定字符id。
     */
    private String bindStrId;

    /**
     * 绑定类型。
     */
    private String bindType;

    /**
     * 文件名称。
     */
    private String fileName;

    /**
     * json字段。
     */
    @UploadFlagColumn(storeType = UploadStoreTypeEnum.LOCAL_SYSTEM)
    private String fileJson;

    /**
     * 文件大小。
     */
    private Long fileSize;

    /**
     * 文件类型。
     */
    private String fileType;

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
     * fileSize 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Long fileSizeStart;

    /**
     * fileSize 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Long fileSizeEnd;

    @Mapper
    public interface BaseBusinessFileModelMapper extends BaseModelMapper<BaseBusinessFileVo, BaseBusinessFile> {
    }
    public static final BaseBusinessFileModelMapper INSTANCE = Mappers.getMapper(BaseBusinessFileModelMapper.class);
}
