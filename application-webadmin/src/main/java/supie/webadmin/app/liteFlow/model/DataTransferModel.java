package supie.webadmin.app.liteFlow.model;

import lombok.Data;

import java.util.Map;

/**
 * 描述：DataTransfer组件的相关信息
 *
 * @author 王立宏
 * @date 2023/10/27 11:58
 * @path SDT-supie.webadmin.app.liteFlow.model-DataTransferModel
 */
@Data
public class DataTransferModel {

    /**
     * jobId
     */
    private Long jobId;
    /**
     * jobName
     */
    private String jobName;
    /**
     * isStartWithSavePoint
     */
    private String isStartWithSavePoint;

    /**
     * Seatunnel ID
     */
    private Long seaTunnelId;
    /**
     * SeatunnelJson 配置信息
     */
    private String seaTunnelConfig;

}
