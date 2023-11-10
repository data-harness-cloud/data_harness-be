package supie.webadmin.app.liteFlow.node;

import cn.hutool.json.JSONUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.dao.DevLiteflowNodeMapper;
import supie.webadmin.app.dao.DevLiteflowRulerMapper;
import supie.webadmin.app.dao.RemoteHostMapper;
import supie.webadmin.app.liteFlow.exception.MyLiteFlowException;
import supie.webadmin.app.liteFlow.model.ErrorMessageModel;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
import supie.webadmin.app.liteFlow.model.NodeMessage;
import supie.webadmin.app.liteFlow.model.SqlAndShellModel;
import supie.webadmin.app.model.DevLiteflowNode;
import supie.webadmin.app.model.RemoteHost;
import supie.webadmin.app.util.remoteshell.JschUtil;
import supie.webadmin.app.util.remoteshell.SSHConfig;

import java.util.LinkedList;
import java.util.List;

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
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未关联远程主机！"));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "未关联远程主机！"));
        }
        remoteHost = remoteHostMapper.selectById(sqlAndShellModel.getSourceId());
        if (remoteHost == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未找到关联的远程主机！"));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "未找到关联的远程主机！"));
        }
    }

    @Override
    public void process() {
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "开始执行SSH脚本"));
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setIp(remoteHost.getHostIp());
        sshConfig.setPort(Integer.parseInt(remoteHost.getHostPort()));
        sshConfig.setUserName(remoteHost.getLoginName());
        sshConfig.setPassword(remoteHost.getPassword());
        sshConfig.setKey(remoteHost.getHostKeyFilePath());
        JschUtil jschUtil = new JschUtil(sshConfig);
        try {
            String resultData = jschUtil.executeRemoteCommand(sqlAndShellModel.getScript());
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, resultData));
        } catch (Exception e) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, e.getMessage()));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), e.getMessage()));
        }
    }

}
