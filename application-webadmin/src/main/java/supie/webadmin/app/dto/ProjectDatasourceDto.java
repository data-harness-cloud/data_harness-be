package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;
import supie.common.core.validator.ConstDictRef;
import supie.webadmin.app.model.constant.DataSourceConnectStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * ProjectDatasourceDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectDatasourceDto对象")
@Data
public class ProjectDatasourceDto {

    /**
     * 租户号。
     */
    @ApiModelProperty(value = "租户号", required = true)
    @NotNull(message = "数据验证失败，租户号不能为空！", groups = {UpdateGroup.class})
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
    @ConstDictRef(constDictClass = DataSourceConnectStatus.class, message = "数据验证失败，数据源连通性为无效值！")
    private Integer datasourceConnect;

    /**
     * 是否采集元数据。
     */
    @ApiModelProperty(value = "是否采集元数据")
    private Integer isMetaCollect;

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
     * datasource_name / datasource_show_name / datasource_description / datasource_content / datasource_group LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;

    /**
     * 所属项目ID
     */
    @ApiModelProperty(value = "所属项目ID")
    private Long projectId;

}
