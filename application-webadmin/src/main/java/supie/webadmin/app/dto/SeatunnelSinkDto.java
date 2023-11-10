package supie.webadmin.app.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhiwuzhu
 */
@Data
@ApiModel("SeatunnelSinkDto对象")
public class SeatunnelSinkDto {




    /**
     * # 表名
     */
    @ApiModelProperty(value = "source中的表名", required = true)
    String sourceTableName;


    /**
     * # 表名
     */
    @ApiModelProperty(value = "表名", required = true)
    String seatunnelSinkTable;


    /**
     * # 数据库
     */
    @ApiModelProperty(value = "数据库", required = true)
    String seatunnelSinkDatabase;




    /**
     * # 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    String seatunnelSinkUser;

    /**
     * # 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    String seatunnelSinkPassword;


    /**
     * #数据类型
     */
    @ApiModelProperty(value = "数据类型", required = true)
    String seatunnelSinkDriver;


    /**
     * #url
     */
    @ApiModelProperty(value = "url", required = true)
    String seatunnelSinkUrl;
}
