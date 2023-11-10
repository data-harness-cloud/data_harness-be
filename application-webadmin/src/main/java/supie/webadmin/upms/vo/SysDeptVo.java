package supie.webadmin.upms.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * SysDeptVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("SysDeptVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptVo extends BaseVo {

    /**
     * 测试字段。
     */
    @ApiModelProperty(value = "测试字段")
    private Long mark;

    /**
     * 部门Id。
     */
    @ApiModelProperty(value = "部门Id")
    private Long deptId;

    /**
     * 部门名称。
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 显示顺序。
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 父部门Id。
     */
    @ApiModelProperty(value = "父部门Id")
    private Long parentId;
}
