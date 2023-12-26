package supie.webadmin.app.liteFlow.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/31 9:06
 * @path SDT-supie.webadmin.app.liteFlow.model-LiteFlowNodeLogModel
 */
@Data
public class LiteFlowNodeLogModel {

    /**
     * 日志级别
     */
    private String level;
    /**
     * 组件ID
     */
    private String nodeId;
    /**
     * 组件标签
     */
    private String nodeTag;
    /**
     * 日志时间
     */
    private Date logTime;
    /**
     * 日志信息
     */
    private String logMessage;

    public static LiteFlowNodeLogModel info(String nodeId, String nodeTag, String logMessage) {
        return new LiteFlowNodeLogModel("INFO", nodeId, nodeTag, new Date(), logMessage);
    }

    public static LiteFlowNodeLogModel warn(String nodeId, String nodeTag, String logMessage) {
        return new LiteFlowNodeLogModel("WARN", nodeId, nodeTag, new Date(), logMessage);
    }

    public static LiteFlowNodeLogModel error(String nodeId, String nodeTag, String logMessage) {
        return new LiteFlowNodeLogModel("ERROR", nodeId, nodeTag, new Date(), logMessage);
    }

    public LiteFlowNodeLogModel(String nodeId, String nodeTag, String logMessage) {
        this.level = "INFO";
        this.nodeId = nodeId;
        this.nodeTag = nodeTag;
        this.logTime = new Date();
        this.logMessage = logMessage;
    }

    public LiteFlowNodeLogModel(String level, String nodeId, String nodeTag, Date logTime, String logMessage) {
        this.level = level;
        this.nodeId = nodeId;
        this.nodeTag = nodeTag;
        this.logTime = logTime;
        this.logMessage = logMessage;
    }

}
