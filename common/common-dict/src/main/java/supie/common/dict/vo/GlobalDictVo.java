package supie.common.dict.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 全局系统字典Vo。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("全局系统字典Vo")
@Data
public class GlobalDictVo {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    private Long dictId;

    /**
     * 字典编码。
     */
    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    /**
     * 字典中文名称。
     */
    @ApiModelProperty(value = "字典中文名称")
    private String dictName;

    /**
     * 创建用户Id。
     */
    @ApiModelProperty(value = "创建用户Id")
    private Long createUserId;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建用户名。
     */
    @ApiModelProperty(value = "创建用户名")
    private Long updateUserId;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
