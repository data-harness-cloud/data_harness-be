package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ProjectHostRelationVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("ProjectHostRelationVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectHostRelationVo extends BaseVo {

    /**
     * 主键。
     */
    @ApiModelProperty(value = "主键")
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
     * 项目id。
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 主机id。
     */
    @ApiModelProperty(value = "主机id")
    private Long hostId;
}
