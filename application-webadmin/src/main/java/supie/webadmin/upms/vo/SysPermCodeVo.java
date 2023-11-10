package supie.webadmin.upms.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 权限字VO。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("权限字VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPermCodeVo extends BaseVo {

    /**
     * 权限字Id。
     */
    @ApiModelProperty(value = "权限字Id")
    private Long permCodeId;

    /**
     * 权限字标识(一般为有含义的英文字符串)。
     */
    @ApiModelProperty(value = "权限字标识")
    private String permCode;

    /**
     * 上级权限字Id。
     */
    @ApiModelProperty(value = "上级权限字Id")
    private Long parentId;

    /**
     * 权限字类型(0: 表单 1: UI片段 2: 操作)。
     */
    @ApiModelProperty(value = "权限字类型")
    private Integer permCodeType;

    /**
     * 显示名称。
     */
    @ApiModelProperty(value = "显示名称")
    private String showName;

    /**
     * 显示顺序(数值越小，越靠前)。
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer showOrder;

    /**
     * 权限字与权限资源关联对象列表。
     */
    @ApiModelProperty(value = "权限字与权限资源关联对象列表")
    private List<Map<String, Object>> sysPermCodePermList;
}