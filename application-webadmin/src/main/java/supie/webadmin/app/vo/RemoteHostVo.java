package supie.webadmin.app.vo;

import supie.common.core.base.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * RemoteHostVO视图对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@ApiModel("RemoteHostVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class RemoteHostVo extends BaseVo {

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
     * SSH-IP地址。
     */
    @ApiModelProperty(value = "SSH-IP地址")
    private String hostIp;

    /**
     * 端口。
     */
    @ApiModelProperty(value = "端口")
    private String hostPort;

    /**
     * ssh-key文件存储路径。
     */
    @ApiModelProperty(value = "ssh-key文件存储路径")
    private String hostKeyFilePath;

    /**
     * 登录名。
     */
    @ApiModelProperty(value = "登录名")
    private String loginName;

    /**
     * 密码。
     */
    @ApiModelProperty(value = "密码")
    private String password;

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
     * 项目关联id。
     */
    @ApiModelProperty(value = "项目关联id")
    private Long projectId;

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
