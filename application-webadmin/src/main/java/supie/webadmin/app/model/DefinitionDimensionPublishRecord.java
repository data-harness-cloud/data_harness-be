package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DefinitionDimensionPublishRecordVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DefinitionDimensionPublishRecord实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_definition_dimension_publish_record")
public class DefinitionDimensionPublishRecord extends BaseModel {

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
     * 维度表id。
     */
    private Long dimensionId;

    /**
     * 数据库。
     */
    private String publishDatabase;

    /**
     * 数据库类型。
     */
    private String publishDatabaseType;

    /**
     * 发布类型。
     */
    private String publishType;

    /**
     * 物理表名。
     */
    private String physicsTableName;

    /**
     * 描述信息。
     */
    private String publishDescription;

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
     * publish_database / publish_database_type / publish_type / physics_table_name / publish_description LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface DefinitionDimensionPublishRecordModelMapper extends BaseModelMapper<DefinitionDimensionPublishRecordVo, DefinitionDimensionPublishRecord> {
    }
    public static final DefinitionDimensionPublishRecordModelMapper INSTANCE = Mappers.getMapper(DefinitionDimensionPublishRecordModelMapper.class);
}
