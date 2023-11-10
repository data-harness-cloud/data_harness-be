package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * ProjectDatasourceVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectDatasourceVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectDatasourceVo extends BaseVo {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号")
    private Long id;

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
     * 数据源名称。
     */
    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    /**
     * 数据源类型。
     */
    @ApiModelProperty(value = "数据源类型")
    private String datasourceType;

    /**
     * 数据源显示名称。
     */
    @ApiModelProperty(value = "数据源显示名称")
    private String datasourceShowName;

    /**
     * 数据源描述。
     */
    @ApiModelProperty(value = "数据源描述")
    private String datasourceDescription;

    /**
     * 数据源连接信息存储为json字段。
     */
    @ApiModelProperty(value = "数据源连接信息存储为json字段")
    private String datasourceContent;

    /**
     * 数据源图标。
     */
    @ApiModelProperty(value = "数据源图标")
    private String datasourceIcon;

    /**
     * 数据源分组。
     */
    @ApiModelProperty(value = "数据源分组")
    private String datasourceGroup;

    /**
     * 数据源连通性。
     */
    @ApiModelProperty(value = "数据源连通性")
    private Integer datasourceConnect;

    /**
     * 是否采集元数据。
     */
    @ApiModelProperty(value = "是否采集元数据")
    private Integer isMetaCollect;

    /**
     * id 的多对多关联表数据对象，数据对应类型为ProjectDatasourceRelationVo。
     */
    @ApiModelProperty(value = "id 的多对多关联表数据对象，数据对应类型为ProjectDatasourceRelationVo")
    private Map<String, Object> projectDatasourceRelation;

    /**
     * dataUserId 字典关联数据。
     */
    @ApiModelProperty(value = "dataUserId 字典关联数据")
    private Map<String, Object> dataUserIdDictMap;

    /**
     * dataDeptId 字典关联数据。
     */
    @ApiModelProperty(value = "dataDeptId 字典关联数据")
    private Map<String, Object> dataDeptIdDictMap;

    /**
     * datasourceConnect 常量字典关联数据。
     */
    @ApiModelProperty(value = "datasourceConnect 常量字典关联数据")
    private Map<String, Object> datasourceConnectDictMap;

    /**
     * 所属项目ID
     */
    @ApiModelProperty(value = "所属项目ID")
    private Long projectId;

}
