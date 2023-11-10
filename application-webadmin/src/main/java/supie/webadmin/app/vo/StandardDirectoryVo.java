package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * StandardDirectoryVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("StandardDirectoryVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardDirectoryVo extends BaseVo {

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
     * 分层关联项目id。
     */
    @ApiModelProperty(value = "分层关联项目id")
    private Long projectId;

    /**
     * 标准目录名称。
     */
    @ApiModelProperty(value = "标准目录名称")
    private String directoryName;

    /**
     * 标准目录编码。
     */
    @ApiModelProperty(value = "标准目录编码")
    private String directoryCode;

    /**
     * 父目录id。
     */
    @ApiModelProperty(value = "父目录id")
    private Long directoryParentId;

    /**
     * 目录分类。
     */
    @ApiModelProperty(value = "目录分类")
    private String directoryType;
}
