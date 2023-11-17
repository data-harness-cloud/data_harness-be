package supie.webadmin.app.liteFlow.node;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.dao.DevLiteflowNodeMapper;
import supie.webadmin.app.dao.DevLiteflowRulerMapper;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
import supie.webadmin.app.model.DevLiteflowNode;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/3 10:49
 * @path SDT-supie.webadmin.app.liteFlow.node-BaseNode
 */
@Component
public abstract class BaseNode extends NodeComponent{

    /**
     * 规则链ID
     */
    protected String rulerId = null;
    /**
     * 组件ID
     */
    protected String nodeId = null;
    /**
     * 组件标签
     */
    protected String nodeTag = null;
    /**
     * 上下文日志对象
     */
    protected List<LiteFlowNodeLogModel> nodeLog;
    /**
     * 节点配置信息
     */
    protected DevLiteflowNode devLiteflowNode;

    @Autowired
    protected DevLiteflowNodeMapper devLiteflowNodeMapper;
    @Autowired
    protected DevLiteflowRulerMapper devLiteflowRulerMapper;

    @Override
    public boolean isAccess() {
        rulerId = this.getChainId(); // 规则链ID
        nodeId = this.getNodeId();   // 组件ID
        nodeTag = this.getTag();     // 组件标签
        nodeLog = this.getContextBean(LinkedList.class);
        devLiteflowNode = devLiteflowNodeMapper.queryNode(rulerId, nodeId, nodeTag);
        int status = devLiteflowNode.getStatus();
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "节点[" + nodeId + ".tag(" + nodeTag + ")]的启用状态:" + (status == 1)));
        return status == 1;
    }

}
