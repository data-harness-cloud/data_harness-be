package supie.webadmin.app.liteFlow.node;

import cn.hutool.json.JSONUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.dao.RemoteHostMapper;
import supie.webadmin.app.liteFlow.exception.MyLiteFlowException;
import supie.webadmin.app.liteFlow.model.ErrorMessageModel;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
import supie.webadmin.app.liteFlow.model.SqlAndShellModel;
import supie.webadmin.app.model.RemoteHost;
import supie.webadmin.app.util.remoteshell.RemoteShell;
import supie.webadmin.app.util.remoteshell.impl.RemoteShellSshjImpl;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/22 14:43
 * @path SDT-supie.webadmin.app.cmp-ShellNode
 */
@Slf4j
@Component
@LiteflowComponent(id = "shellNode", name = "shellNode")
public class ShellNode extends BaseNode {

    private SqlAndShellModel sqlAndShellModel;
    private RemoteHost remoteHost = null;
    @Autowired
    private RemoteHostMapper remoteHostMapper;

    @Override
    public void beforeProcess() {
        sqlAndShellModel = JSONUtil.toBean(devLiteflowNode.getFieldJsonData(), SqlAndShellModel.class);
        if (sqlAndShellModel.getSourceId() == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未关联远程主机!"));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "未关联远程主机!"));
        }
        remoteHost = remoteHostMapper.selectById(sqlAndShellModel.getSourceId());
        if (remoteHost == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未找到关联的远程主机!"));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "未找到关联的远程主机!"));
        }
    }

    @Override
    public void process() {
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "开始执行SSH脚本"));
        try {
            RemoteShell remoteShell = new RemoteShellSshjImpl(
                    remoteHost.getHostIp(), remoteHost.getHostPort(),
                    remoteHost.getLoginName(), remoteHost.getPassword(), null);
            String resultData = remoteShell.execCommands(sqlAndShellModel.getScript());
            remoteShell.close();
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, resultData));
        } catch (Exception e) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, e.getMessage()));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), e.getMessage()));
        }
    }

}
