package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.StandardDirectoryVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * StandardDirectory实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_standard_directory")
public class StandardDirectory extends BaseModel {

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
     * 标准目录名称。
     */
    private String directoryName;

    /**
     * 标准目录编码。
     */
    private String directoryCode;

    /**
     * 父目录id。
     */
    private Long directoryParentId;

    /**
     * 目录分类。
     */
    private String directoryType;

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
     * directory_name / directory_code / directory_type LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface StandardDirectoryModelMapper extends BaseModelMapper<StandardDirectoryVo, StandardDirectory> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param standardDirectoryVo 域对象。
         * @return 实体对象。
         */
        @Override
        StandardDirectory toModel(StandardDirectoryVo standardDirectoryVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param standardDirectory 实体对象。
         * @return 域对象。
         */
        @Override
        StandardDirectoryVo fromModel(StandardDirectory standardDirectory);
    }
    public static final StandardDirectoryModelMapper INSTANCE = Mappers.getMapper(StandardDirectoryModelMapper.class);
}
