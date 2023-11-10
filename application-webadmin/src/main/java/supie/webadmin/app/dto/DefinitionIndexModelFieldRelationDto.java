package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DefinitionIndexModelFieldRelationDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionIndexModelFieldRelationDto对象")
@Data
public class DefinitionIndexModelFieldRelationDto {

    /**
     * 编号。
     */
    @ApiModelProperty(value = "编号", required = true)
    @NotNull(message = "数据验证失败，编号不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 字符编号。
     */
    @ApiModelProperty(value = "字符编号")
    private String strId;

    /**
     * 数据所属人。
     */
    @ApiModelProperty(value = "数据所属人")
    private Long dataUserId;

    /**
     * 数据所属部门。
     */
    @ApiModelProperty(value = "数据所属部门")
    private Long dataDeptId;

    /**
     * 指标id。
     */
    @ApiModelProperty(value = "指标id")
    private Long indexId;

    /**
     * 指标业务过程id。
     */
    @ApiModelProperty(value = "指标业务过程id")
    private Long indexProcessId;

    /**
     * 模型id。
     */
    @ApiModelProperty(value = "模型id")
    private Long modelId;

    /**
     * 模型字段id。
     */
    @ApiModelProperty(value = "模型字段id")
    private Long modelFieldId;

    /**
     * 模型业务过程id。
     */
    @ApiModelProperty(value = "模型业务过程id")
    private Long modelProcessId;
}
