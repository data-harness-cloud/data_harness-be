package supie.common.dict.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 全局系统字典项目Vo。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("全局系统字典项目Vo")
@Data
public class GlobalDictItemVo {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 字典编码。
     */
    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    /**
     * 字典数据项Id。
     */
    @ApiModelProperty(value = "字典数据项Id")
    private String itemId;

    /**
     * 字典数据项名称。
     */
    @ApiModelProperty(value = "字典数据项名称")
    private String itemName;

    /**
     * 显示顺序(数值越小越靠前)。
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 字典状态。具体值引用DictItemStatus常量类。
     */
    @ApiModelProperty(value = "字典状态")
    private Integer status;

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
