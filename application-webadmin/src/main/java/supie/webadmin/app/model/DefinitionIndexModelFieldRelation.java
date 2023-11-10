package supie.webadmin.app.model;

import com.baomidou.mybatisplus.annotation.*;
import supie.common.core.annotation.*;
import supie.common.core.base.model.BaseModel;
import supie.common.core.base.mapper.BaseModelMapper;
import supie.webadmin.app.vo.DefinitionIndexModelFieldRelationVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DefinitionIndexModelFieldRelation实体对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sdt_definition_index_model_field_relation")
public class DefinitionIndexModelFieldRelation extends BaseModel {

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
     * 指标id。
     */
    private Long indexId;

    /**
     * 指标业务过程id。
     */
    private Long indexProcessId;

    /**
     * 模型id。
     */
    private Long modelId;

    /**
     * 模型字段id。
     */
    private Long modelFieldId;

    /**
     * 模型业务过程id。
     */
    private Long modelProcessId;

    @Mapper
    public interface DefinitionIndexModelFieldRelationModelMapper extends BaseModelMapper<DefinitionIndexModelFieldRelationVo, DefinitionIndexModelFieldRelation> {
    }
    public static final DefinitionIndexModelFieldRelationModelMapper INSTANCE = Mappers.getMapper(DefinitionIndexModelFieldRelationModelMapper.class);
}
