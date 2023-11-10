package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * DefinitionDimensionPublishRecordVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DefinitionDimensionPublishRecordVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DefinitionDimensionPublishRecordVo extends BaseVo {

    /**
     * 编号。
     */
    @ApiModelProperty(value = "编号")
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
}
