package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * DevLiteflowScriptVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("DevLiteflowScriptVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DevLiteflowScriptVo extends BaseVo {

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
     * 应用名称。
     */
    @ApiModelProperty(value = "应用名称")
    private String applicationName;

    /**
     * 脚本id。
     */
    @ApiModelProperty(value = "脚本id")
    private String scriptId;

    /**
     * 脚本名称。
     */
    @ApiModelProperty(value = "脚本名称")
    private String scriptName;

    /**
     * 脚本内容。
     */
    @ApiModelProperty(value = "脚本内容")
    private String scriptData;

    /**
     * 脚本种类。
     */
    @ApiModelProperty(value = "脚本种类")
    private String scriptType;

    /**
     * 脚本语言。
     */
    @ApiModelProperty(value = "脚本语言")
    private String scriptLanguage;

    /**
     * createUserId 字典关联数据。
     */
    @ApiModelProperty(value = "createUserId 字典关联数据")
    private Map<String, Object> createUserIdDictMap;

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
