package supie.webadmin.app.liteFlow.node;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.liteFlow.exception.MyLiteFlowException;
import supie.webadmin.app.liteFlow.model.DataTransferModel;
import supie.webadmin.app.liteFlow.model.ErrorMessageModel;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
import supie.webadmin.app.model.*;
import supie.webadmin.app.util.remoteshell.JschUtil;
import supie.webadmin.app.util.remoteshell.SSHConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/22 9:09
 * @path SDT-supie.webadmin.app.node-DataTransferNode
 */
@Slf4j
@Component
@LiteflowComponent(id = "dataTransfer", name = "数据传输组件")
public class DataTransferNode extends BaseNode {

    /**
     * DataTransferNode 的自身属性
     */
    private DataTransferModel dataTransferModel;
    /**
     * Seatunnel 配置信息
     */
    private SeatunnelConfig seatunnelConfigModel;
    /**
     * 临时文件路径
     */
    private String tempFilePath;

    @Autowired
    private SeatunnelConfigMapper seatunnelConfigMapper;
    @Autowired
    private RemoteHostMapper remoteHostMapper;

    @Override
    public void beforeProcess() {
        dataTransferModel = JSONUtil.toBean(devLiteflowNode.getFieldJsonData(), DataTransferModel.class);
        if (dataTransferModel.getSeaTunnelId() == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未配置该节点需要的SeaTunnel！"));
        }
        seatunnelConfigModel = seatunnelConfigMapper.selectById(dataTransferModel.getSeaTunnelId());
        if (seatunnelConfigModel == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag,
                    "未查询到ID为[" + dataTransferModel.getSeaTunnelId() + "]的SeaTunnel配置数据！"));
        }
    }

    @Override
    public void process() throws Exception {
        //判断什么方式调用 Seatunnel
        if (this.seatunnelConfigModel.getCallMode() == 1) {
            // 通过接口的方式调用 Seatunnel
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "使用RestApi方式调用Seatunnel。"));
            restApiSubmitJob();
        } else if (this.seatunnelConfigModel.getCallMode() == 2) {
            // 通过 SSH 方式调用 Seatunnel
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "使用SSH方式调用Seatunnel。"));
            sshSubmitJob();
        }
    }

    private void restApiSubmitJob() {
        if (seatunnelConfigModel.getSubmitJobUrl() == null) {
            seatunnelConfigModel.setSubmitJobUrl(new SeatunnelConfig().getSubmitJobUrl());
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                    "未配置Seatunnel提交Job的接口地址，使用默认地址：" + seatunnelConfigModel.getSubmitJobUrl()));
        }
        StringBuilder url = new StringBuilder(seatunnelConfigModel.getLocalhostUri());
        // 判断字符串第一个字符是否为"/"
        if (StrUtil.startWith(seatunnelConfigModel.getLocalhostUri(), "/")) {
            url.append("/");
        }
        url.append(seatunnelConfigModel.getSubmitJobUrl()).append("?");
        // jobId=1001&jobName=restApiTest1001&isStartWithSavePoint=
        if (dataTransferModel.getJobId() != null) {
            url.append("jobId=").append(dataTransferModel.getJobId()).append("&");
        } else {
            url.append("jobId=").append(devLiteflowNode.getId()).append("&");
        }
        if (dataTransferModel.getJobName() != null) {
            url.append("jobName=").append(dataTransferModel.getJobName()).append("&");
        }
        if (dataTransferModel.getIsStartWithSavePoint() != null) {
            url.append("isStartWithSavePoint=").append(dataTransferModel.getIsStartWithSavePoint());
        }
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "提交Job：" + url.toString()));
        HttpResponse execute = null;
        try {
            execute = HttpRequest.post(url.toString())
                    .body(dataTransferModel.getSeaTunnelConfig())
                    .execute();
        } catch (Exception e) {
            String errorMessage = "RestApi(" + url.toString() + ")调用报错：" + e.getMessage();
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "执行失败：" + errorMessage));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), errorMessage));
        }
        String body = URLUtil.decode(execute.body());
        // 存储执行信息
        if (body == null) body = "无回执结果信息！";
        devLiteflowNodeMapper.setExecutionMessage(this.rulerId, this.nodeId, this.nodeTag, body);
        if (!execute.isOk()) {
            // 失败
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "执行失败：" + body));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), body));
        } else {
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "执行成功：" + body));
        }
    }

    private void sshSubmitJob() {
        RemoteHost remoteHost = remoteHostMapper.selectById(seatunnelConfigModel.getRemoteHostId());
        // 根据项目ID 获取到该项目的远程服务器的配置
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setIp(remoteHost.getHostIp());
        sshConfig.setPort(Integer.parseInt(remoteHost.getHostPort()));
        sshConfig.setPassword(remoteHost.getPassword());
        JschUtil jschUtil = new JschUtil(sshConfig);
        tempFilePath = "./tempFolder/" + DateUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss-SSS-")
                + RandomUtil.randomString(5) + "-config.json";
        contentWriteToFile(tempFilePath, JSONUtil.toJsonStr(dataTransferModel.getSeaTunnelConfig()));
        // 上传配置文件（v2.batch.config.template）至 seatunnel 的 ./config/ 中
        String remoteConfigName = "v2.supie.config.json";
        String remoteFilePath = seatunnelConfigModel.getSeatunnelPath() + "/config/" + remoteConfigName;
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "上传Seatunnel配置文件，remoteFilePath：" + remoteFilePath + "。"));
        jschUtil.uploadFile(tempFilePath, remoteFilePath);
        // 执行命令
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                "开始执行Seatunnel命令：[\"cd " + seatunnelConfigModel.getSeatunnelPath() +
                        "\", \"sh bin/seatunnel.sh --config config/" + remoteConfigName + " -e local\"]"));
        String resultMsg = jschUtil.executeRemoteCommand(
                null,
                "cd " + seatunnelConfigModel.getSeatunnelPath(),
                "sh bin/seatunnel.sh --config config/" + remoteConfigName + " -e local");
        // 存储执行结果信息
        if (resultMsg == null) resultMsg = "无回执结果信息！";
        nodeLog.add(LiteFlowNodeLogModel.warn(nodeId, nodeTag, resultMsg));
        devLiteflowNodeMapper.setExecutionMessage(this.rulerId, this.nodeId, this.nodeTag, resultMsg);
        // 删除创建的临时文件
        File file = new File(tempFilePath);
        file.delete();
    }

    /**
     * 写入文件输入流
     *
     * @param fileName 文件名
     * @param content  内容
     * @return 输入流
     * @author 王立宏
     * @date 2023/10/22 04:25
     */
    private static void contentWriteToFile(String fileName, String content) {
        Path path = Paths.get(fileName);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
