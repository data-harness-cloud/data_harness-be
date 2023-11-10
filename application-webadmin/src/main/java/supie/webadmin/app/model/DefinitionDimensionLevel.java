package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DefinitionDimensionLevelVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DefinitionDimensionLevel实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_definition_dimension_level")
public class DefinitionDimensionLevel extends BaseModel {

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
     * 维度表id。
     */
    private Long dimensionId;

    /**
     * 层级数字。
     */
    private Integer levelNumber;

    /**
     * 层级名称。
     */
    private String levelName;

    /**
     * 层级编码。
     */
    private String levelCode;

    /**
     * 预计规模。
     */
    private String levelScale;

    /**
     * 是否启用。
     */
    private String levelEnable;

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
     * levelNumber 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private Integer levelNumberStart;

    /**
     * levelNumber 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private Integer levelNumberEnd;

    /**
     * level_name / level_code / level_scale LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface DefinitionDimensionLevelModelMapper extends BaseModelMapper<DefinitionDimensionLevelVo, DefinitionDimensionLevel> {
    }
    public static final DefinitionDimensionLevelModelMapper INSTANCE = Mappers.getMapper(DefinitionDimensionLevelModelMapper.class);
}
