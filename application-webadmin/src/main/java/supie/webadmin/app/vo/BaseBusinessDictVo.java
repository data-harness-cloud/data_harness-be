package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * BaseBusinessDictVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("BaseBusinessDictVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseBusinessDictVo extends BaseVo {

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
     * 父级id。
     */
    @ApiModelProperty(value = "父级id")
    private Long parentId;

    /**
     * 绑定类型。
     */
    @ApiModelProperty(value = "绑定类型")
    private String bindType;

    /**
     * 绑定类型列表。
     */
    @ApiModelProperty(value = "绑定类型列表")
    private List<String> bindTypeList;

    /**
     * 字典名称。
     */
    @ApiModelProperty(value = "字典名称")
    private String dictName;

    /**
     * 字典描述。
     */
    @ApiModelProperty(value = "字典描述")
    private String dictDesc;

    /**
     *　显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 创建者ID
     */
    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    /**
     * 修改者ID
     */
    @ApiModelProperty(value = "修改者ID")
    private Long updateUserId;

}
