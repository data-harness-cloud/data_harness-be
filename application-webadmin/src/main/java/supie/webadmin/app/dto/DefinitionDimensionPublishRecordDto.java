package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DefinitionDimensionPublishRecordDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionDimensionPublishRecordDto对象")
@Data
public class DefinitionDimensionPublishRecordDto {

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
     * 维度表id。
     */
    @ApiModelProperty(value = "维度表id")
    private Long dimensionId;

    /**
     * 数据库。
     */
    @ApiModelProperty(value = "数据库")
    private String publishDatabase;

    /**
     * 数据库类型。
     */
    @ApiModelProperty(value = "数据库类型")
    private String publishDatabaseType;

    /**
     * 发布类型。
     */
    @ApiModelProperty(value = "发布类型")
    private String publishType;

    /**
     * 物理表名。
     */
    @ApiModelProperty(value = "物理表名")
    private String physicsTableName;

    /**
     * 描述信息。
     */
    @ApiModelProperty(value = "描述信息")
    private String publishDescription;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤起始值(>=)")
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤结束值(<=)")
    private String updateTimeEnd;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值(>=)")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值(<=)")
    private String createTimeEnd;

    /**
     * publish_database / publish_database_type / publish_type / physics_table_name / publish_description LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
