package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * SeatunnelConfigVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("SeatunnelConfigVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SeatunnelConfigVo extends BaseVo {

    /**
     * 主键ID。
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 显示名称。
     */
    @ApiModelProperty(value = "显示名称")
    private String showName;

    /**
     * Seatunnel 的调用方式（1：接口。2：ssh。默认为1）
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
     * remoteHostId 的一对一关联数据对象，数据对应类型为RemoteHostVo。
     */
    @ApiModelProperty(value = "remoteHostId 的一对一关联数据对象，数据对应类型为RemoteHostVo")
    private Map<String, Object> remoteHost;

    /**
     * createUserId 字典关联数据。
     */
    @ApiModelProperty(value = "createUserId 字典关联数据")
    private Map<String, Object> createUserIdDictMap;

    /**
     * dataUserId 字典关联数据。
     */
    @ApiModelProperty(value = "dataUserId 字典关联数据")
    private Map<String, Object> dataUserIdDictMap;

    /**
     * dataDeptId 字典关联数据。
     */
    @ApiModelProperty(value = "dataDeptId 字典关联数据")
    private Map<String, Object> dataDeptIdDictMap;
}
