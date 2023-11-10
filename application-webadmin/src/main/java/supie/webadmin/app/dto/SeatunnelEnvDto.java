package supie.webadmin.app.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Seatunnel配置文件实体类
 * @author   litao
 */
@Data
@ApiModel("SeatunnelEnvDto对象-Seatunnel配置文件基本设置")
public class SeatunnelEnvDto {


    // 指定任务是批模式还是流模式，job.mode = "BATCH"为批模式，job.mode = "STREAMING"为流模式
    @ApiModelProperty(value = "指定任务是批模式还是流模式，job.mode = \"BATCH\"为批模式，job.mode = \"STREAMING\"为流模式", required = true)
    String jobMode;


    /**
     * 并行条数
     */
    @ApiModelProperty(value = "并行条数", required = true)
    String executionParallelism;
}
