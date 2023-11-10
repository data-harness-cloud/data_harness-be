package supie.webadmin.upms.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 岗位VO对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("岗位VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPostVo extends BaseVo {

    /**
     * 岗位Id。
     */
    @ApiModelProperty(value = "岗位Id")
    private Long postId;

    /**
     * 岗位名称。
     */
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 岗位层级，数值越小级别越高。
     */
    @ApiModelProperty(value = "岗位层级，数值越小级别越高")
    private Integer postLevel;

    /**
     * 是否领导岗位。
     */
    @ApiModelProperty(value = "是否领导岗位")
    private Boolean leaderPost;

    /**
     * postId 的多对多关联表数据对象，数据对应类型为SysDeptPostVo。
     */
    @ApiModelProperty(value = "postId 的多对多关联表数据对象")
    private Map<String, Object> sysDeptPost;
}
