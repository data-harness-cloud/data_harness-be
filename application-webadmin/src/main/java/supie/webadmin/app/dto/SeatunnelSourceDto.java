package supie.webadmin.app.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhiwuzhu
 */
@Data
@ApiModel("SeatunnelSourceDto对象-Seatunnel配置文件配置数据源")
public class SeatunnelSourceDto {





    /**
     * 并行度
     */
    @ApiModelProperty(value = "并行度", required = true)
    Integer seatunnelSourceParallelism;


    /**
     *  # 使用id字段来进行split的拆分，目前只支持数字类型的主键列，而且该列的值最好是离线的，自增id最佳
     */
    @ApiModelProperty(value = "使用字段来进行split的拆分，目前只支持数字类型的主键列，而且该列的值最好是离线的，自增id最佳", required = true)
    String partitionColumn;

    /**
     * # 拆分成20个split，这20个split会被分配给5个Source Task来处理
     */
    @ApiModelProperty(value = "使用字段来进行split的拆分，目前只支持数字类型的主键列，而且该列的值最好是离线的，自增id最佳", required = true)
    String partitionNum;

    /**
     * # 表名
     */
    @ApiModelProperty(value = "表名", required = true)
    String resultTableName;


    /**
     * # 查询语句
     */
    @ApiModelProperty(value = "查询语句", required = true)
    String seatunnelSourceQuery;


    /**
     * # 数据库
     */
    @ApiModelProperty(value = "数据库", required = true)
    String seatunnelSourceDatabase;

    /**
     * # 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    String seatunnelSourceUser;

    /**
     * # 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    String seatunnelSourcePassword;


    /**
     * #数据类型
     */
    @ApiModelProperty(value = "数据类型", required = true)
    String seatunnelSourceDriver;



    /**
     * #url
     */
    @ApiModelProperty(value = "url", required = true)
    String seatunnelSourceUrl;

}
