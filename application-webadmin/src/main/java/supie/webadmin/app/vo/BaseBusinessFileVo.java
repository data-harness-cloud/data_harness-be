package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * BaseBusinessFileVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("BaseBusinessFileVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseBusinessFileVo extends BaseVo {

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
     * 绑定id。
     */
    @ApiModelProperty(value = "绑定id")
    private Long bindId;

    /**
     * 绑定字符id。
     */
    @ApiModelProperty(value = "绑定字符id")
    private String bindStrId;

    /**
     * 绑定类型。
     */
    @ApiModelProperty(value = "绑定类型")
    private String bindType;

    /**
     * 文件名称。
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * json字段。
     */
    @ApiModelProperty(value = "json字段")
    private String fileJson;

    /**
     * 文件大小。
     */
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    /**
     * 文件类型。
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;
}
