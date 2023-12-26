package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DefinitionIndexVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DefinitionIndex实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_definition_index")
public class DefinitionIndex extends BaseModel {

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
     * 指标类型。
     */
    private String indexType;

    /**
     * 指标名称。
     */
    private String indexName;

    /**
     * 所属项目id。
     */
    private String projectId;

    /**
     * 指标英文名称。
     */
    private String indexEnName;

    /**
     * 指标编码。
     */
    private String indexCode;

    /**
     * 指标等级;核心、重要、一般、临时。
     */
    private String indexLevel;

    /**
     * 业务过程id。
     */
    private Long processId;

    /**
     * 业务描述。
     */
    private String indexDescription;

    /**
     * 动态路由id。
     */
    private Long customizeRouteId;

    /**
     * 关联字段。
     */
    private Long modelDesginFieldId;

    /**
     * 数据类型。
     */
    private String dataType;

    /**
     * 生产周期。
     */
    private String productPeriod;

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
     * index_type / index_name / index_en_name / index_code / index_level / index_description / data_type / product_period / LIKE搜索字符串。
     */
    @TableField(exist = false)
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = MyCommonUtil.replaceSqlWildcard(searchString);
    }

    @Mapper
    public interface DefinitionIndexModelMapper extends BaseModelMapper<DefinitionIndexVo, DefinitionIndex> {
    }
    public static final DefinitionIndexModelMapper INSTANCE = Mappers.getMapper(DefinitionIndexModelMapper.class);
}
