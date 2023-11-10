package supie.webadmin.app.dto;

import supie.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * SeatunnelConfigDto对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("SeatunnelConfigDto对象")
@Data
public class SeatunnelConfigDto {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "数据验证失败，主键ID不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 显示名称。
     */
    @ApiModelProperty(value = "显示名称")
    private String showName;

    /**
     * Seatunnel 的调用方式（1：接口。2：ssh。默认为1）。
     */
    @ApiModelProperty(value = "Seatunnel 的调用方式（1：接口。2：ssh。默认为1）")
    private Integer callMode = 1;

    /**
     * 接口方式调用 Seatunnel 的访问根地址（包含端口）
     */
    @ApiModelProperty(value = "接口方式调用 Seatunnel 的访问根地址（包含端口）")
    private String localhostUri;

    /**
     * Seatunnel 的提交job的地址（默认为版本2.3.3的Submit Job地址：/hazelcast/rest/maps/submit-job）
     */
    @ApiModelProperty(value = "Seatunnel 的提交job的地址（默认为版本2.3.3的Submit Job地址：/hazelcast/rest/maps/submit-job）")
    private String submitJobUrl = "/hazelcast/rest/maps/submit-job";

    /**
     * 远程主机ID。
     */
    @ApiModelProperty(value = "远程主机ID")
    private Long remoteHostId;

    /**
     * seatunnel安装路径。
     */
    @ApiModelProperty(value = "seatunnel安装路径")
    private String seatunnelPath;

    /**
     * 数据所属人ID。
     */
    @ApiModelProperty(value = "数据所属人ID")
    private Long dataUserId;

    /**
     * 数据所属部门ID。
     */
    @ApiModelProperty(value = "数据所属部门ID")
    private Long dataDeptId;

    /**
     * 关联项目id。
     */
    @ApiModelProperty(value = "关联项目id")
    private Long projectId;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值(>=)")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值(<=)")
    private String createTimeEnd;

    /**
     * updateTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤起始值(>=)")
    private String updateTimeStart;

    /**
     * updateTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤结束值(<=)")
    private String updateTimeEnd;

    /**
     * show_name LIKE搜索字符串。
     */
    @ApiModelProperty(value = "LIKE模糊搜索字符串")
    private String searchString;
}
