package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * ProjectDatasourceTemplateDictVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectDatasourceTemplateDictVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectDatasourceTemplateDictVo extends BaseVo {

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
     * 模板类型。
     */
    @ApiModelProperty(value = "模板类型")
    private String templateType;

    /**
     * 模板名称。
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    /**
     * source配置。
     */
    @ApiModelProperty(value = "source配置")
    private String templateSource;

    /**
     * sink配置。
     */
    @ApiModelProperty(value = "sink配置")
    private String templateSink;

    /**
     * 转换配置。
     */
    @ApiModelProperty(value = "转换配置")
    private String templateTrans;

    /**
     * 图标。
     */
    @ApiModelProperty(value = "图标")
    private String templateIcon;

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
}
