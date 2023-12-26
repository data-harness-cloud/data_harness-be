package supie.webadmin.app.liteFlow.node;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.liteFlow.exception.MyLiteFlowException;
import supie.webadmin.app.liteFlow.model.DataTransferModel;
import supie.webadmin.app.liteFlow.model.ErrorMessageModel;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
import supie.webadmin.app.model.*;
import supie.webadmin.app.util.remoteshell.RemoteShell;
import supie.webadmin.app.util.remoteshell.impl.RemoteShellSshjImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 描述:
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

    String envJobModelValue = null;

    @Autowired
    private SeatunnelConfigMapper seatunnelConfigMapper;
    @Autowired
    private RemoteHostMapper remoteHostMapper;


    @Override
    public void beforeProcess() {
        dataTransferModel = JSONUtil.toBean(devLiteflowNode.getFieldJsonData(), DataTransferModel.class);
        if (dataTransferModel.getSeaTunnelId() == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未配置该节点需要的SeaTunnel!"));
        }
        seatunnelConfigModel = seatunnelConfigMapper.selectById(dataTransferModel.getSeaTunnelId());
        if (seatunnelConfigModel == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag,
                    "未查询到ID为[" + dataTransferModel.getSeaTunnelId() + "]的SeaTunnel配置数据!"));
        }
    }

    @Override
    public void process() throws Exception {
        try {
            JsonNode jsonNode = new JsonMapper().readTree(dataTransferModel.getSeaTunnelConfig());
            envJobModelValue = jsonNode.get("env").get("job.mode").asText();
        } catch (JsonProcessingException e) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, e.getMessage()));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), e.getMessage()));
        }
        //判断什么方式调用 Seatunnel
        if (this.seatunnelConfigModel.getCallMode() == 1) {
            // 通过接口的方式调用 Seatunnel
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "使用RestApi方式调用Seatunnel."));
            restApiSubmitJob();
        } else if (this.seatunnelConfigModel.getCallMode() == 2) {
            // 判断是否需要等待 Seatunnel 任务执行完毕
            // 通过 SSH 方式调用 Seatunnel
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "使用SSH方式执行Seatunnel任务."));
            if ("BATCH".equals(envJobModelValue)) {
                // BATCH：需要等待执行完再执行下一个节点
                sshSubmitJob();
            } else {
                String logFileName = "RunSeatunnelTask_" + DateUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss-SSS") + ".log";
                String message = "env节点下的job.mode节点值为[" + envJobModelValue +
                        "], 使用异步方式执行, Seatunnel任务执行日志[" + logFileName + "]将保存在Seatunnel安装位置下的 taskLog 目录下.";
                nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, message));
//                String a = "nohup ./bin/seatunnel.sh --config ./config/v2.batch.config.template -e local > ./taskLog/RunSeatunnelTask.log 2>&1 &";
//                String b = "./bin/seatunnel.sh --config ./config/v2.batch.config.template -e local | tee ./taskLog/seatunnel.log && sync";
                // 开启一个线程
                // 执行远程命令，并保存日志至
                new Thread(() -> {
                    sshSubmitJobAndUploadLogFil(logFileName);
                }).start();
            }
        }
    }

    /**
     * <p>seatunnel任务已经调用并开始执行.判断是否需要等待任务执行完成再执行下一个节点.</p>
     * 根据(seaTunnelConfig -> env-> job.mode)的值判断是否需要等待执行完再执行下一节点.
     * BATCH：需要等待执行完再执行下一个节点,其他值不用管,每次执行也都执行一次.
     *
     * @author 王立宏
     * @date 2023/11/27 11:17
     */
    @Override
    public void afterProcess() {
        if ("BATCH".equals(envJobModelValue)) {
            // 判断该任务是否执行完毕（Api调用判断、SSH执行判断）.
            if (this.seatunnelConfigModel.getCallMode() == 2) {
                // SSH方式执行Seatunnel任务, 当前只能够等待任务执行完成才可以执行下一组件
                // 非等待任务则开启一个线程来执行 SSH 命令，log日志则保存在 Seatunnel 路径的log文件夹中
                return;
            }
            // API调用判断
            long startTime = System.currentTimeMillis();
            int sleepTime = 1000; //休眠时间
            while (true) {
                StringBuilder url = new StringBuilder(seatunnelConfigModel.getLocalhostUri());
                url.append("/hazelcast/rest/maps/running-job/").append(dataTransferModel.getJobId());
                HttpResponse execute;
                try {
                    execute = HttpRequest.get(url.toString()).execute();
                } catch (Exception e) {
                    String errorMessage = "SeatunnelRestApi(" + url.toString() + ")调用报错: " + e.getMessage();
                    nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "执行失败: " + errorMessage));
                    throw new MyLiteFlowException(new ErrorMessageModel(getClass(), errorMessage));
                }
                if (!execute.isOk()) {
                    nodeLog.add(LiteFlowNodeLogModel.warn(nodeId, nodeTag,
                            "Seatunnel任务[jobId:" + dataTransferModel.getJobId() + "]执行信息获取失败,失败信息为:" + execute.body()));
                    if ((System.currentTimeMillis() - startTime) > 60 * 60 * 1000) {
                        nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "任务执行超时(1小时),请检查!"));
                        throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "任务执行超时(1小时),请检查!"));
                    }
                    continue;
                }
                String body = URLUtil.decode(execute.body());
                // bodyJsonNode => {} 或 {"jobId":"","jobName":"","jobStatus":"","envOptions":{},"createTime":"","jobDag":{"vertices":[],"edges":[]},"pluginJarsUrls":[],"isStartWithSavePoint":false,"metrics":{"sourceReceivedCount":"","sinkWriteCount":""}}
                JsonNode bodyJsonNode;
                try {
                    bodyJsonNode = new JsonMapper().readTree(body);
                } catch (JsonProcessingException e) {
                    throw new MyLiteFlowException(new ErrorMessageModel(getClass(), e.getMessage()));
                }
                if (bodyJsonNode.size() == 0) {
                    // 未查询到该jobId所对应的任务信息{},认为该任务已经执行完毕。
                    nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                            "Seatunnel任务[jobId:" + dataTransferModel.getJobId() + "]执行完毕!"));
                    return;
                }
                if ("RUNNING".equals(bodyJsonNode.get("jobStatus").asText())) {
                    // 任务运行中,休眠5秒后继续判断是否完成.
                    nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                            "Seatunnel任务[jobId:" + dataTransferModel.getJobId() + "]执行中..."));
                    if ((System.currentTimeMillis() - startTime) > 60 * 60 * 1000) {
                        nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "任务执行超时(1小时),请检查!"));
                        throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "任务执行超时(1小时),请检查!"));
                    }
                    ThreadUtil.sleep(sleepTime);
                    //每次休眠后都追加3秒的时间，直至休眠时间大于1分钟。
                    if (sleepTime < 60000) sleepTime = sleepTime + 3000;
                } else {
                    nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                            "Seatunnel任务[jobId:" + dataTransferModel.getJobId() + "]执行完毕!"));
                    return;
                }
            }
        } else if ("STREAMING".equals(envJobModelValue)) {
            // 无需等待Seatunnel任务执行完成,直接执行下一个节点.
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                    "env节点下的job.mode节点值为\"STREAMING\", 不关心执行结果, 直接执行下一组件节点."));
            return;
        }
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                "'env'节点下的'job.mode'节点值为'" + envJobModelValue + "', 不关心该数据传输组件中的 Seatunnel 执行结果, 直接执行下一组件节点."));
    }

    private void restApiSubmitJob() {
        if (seatunnelConfigModel.getSubmitJobUrl() == null) {
            seatunnelConfigModel.setSubmitJobUrl(new SeatunnelConfig().getSubmitJobUrl());
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                    "未配置Seatunnel提交Job的接口地址,使用默认地址:" + seatunnelConfigModel.getSubmitJobUrl()));
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
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "提交Job:" + url.toString()));
        HttpResponse execute;
        try {
            execute = HttpRequest.post(url.toString())
                    .body(dataTransferModel.getSeaTunnelConfig())
                    .execute();
        } catch (Exception e) {
            String errorMessage = "SeatunnelRestApi(" + url.toString() + ")调用报错: " + e.getMessage();
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "执行失败: " + errorMessage));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), errorMessage));
        }
        String body = URLUtil.decode(execute.body());
        // 存储执行信息
        if (body == null) body = "无回执结果信息!";
        devLiteflowNodeMapper.setExecutionMessage(this.rulerId, this.nodeId, this.nodeTag, body);
        if (!execute.isOk()) {
            // 失败
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "执行失败: " + body));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), body));
        } else {
            // body => {"jobId":733584788375666689,"jobName":"rest_api_test"}
            nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "任务调用成功,返回值为: " + body));
            try {
                // 成功,设置jobId,jobId将在该 DataTransferNode 组件的 afterProcess() 方法使用.
                JsonNode jsonNode = new JsonMapper().readTree(JSONUtil.toJsonStr(body));
                Long jobId = jsonNode.get("jobId").asLong();
                dataTransferModel.setJobId(jobId);
            } catch (Exception e) {
                String errorMessage = "设置jobId失败,失败学习为: " + e.getMessage();
                nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, errorMessage));
                throw new MyLiteFlowException(new ErrorMessageModel(getClass(), errorMessage));
            }
        }
    }

    private void sshSubmitJob() {
        RemoteHost remoteHost = remoteHostMapper.selectById(seatunnelConfigModel.getRemoteHostId());
        // 根据项目ID 获取到该项目的远程服务器的配置
        RemoteShell remoteShell = new RemoteShellSshjImpl(
                remoteHost.getHostIp(), remoteHost.getHostPort(),
                remoteHost.getLoginName(), remoteHost.getPassword(), null);
        /**
         * 临时文件路径
         */
        String tempFilePath = "./tempFolder/" + DateUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss-SSS-")
                + RandomUtil.randomString(5) + "-config.json";
        contentWriteToFile(tempFilePath, JSONUtil.toJsonStr(dataTransferModel.getSeaTunnelConfig()));
        // 上传配置文件（v2.batch.config.template）至 seatunnel 的 ./config/ 中
        String remoteConfigName = "v2-supie-config" + DateUtil.format(new Date(), "-yyyy-MM-dd-HH-mm-ss-SSS") + ".json";
        String remoteFilePath = seatunnelConfigModel.getSeatunnelPath() + "/config/" + remoteConfigName;
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "上传Seatunnel配置文件,remoteFilePath:" + remoteFilePath + "."));
        remoteShell.uploadFile(tempFilePath, remoteFilePath);
        // 执行命令
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag,
                "开始执行Seatunnel命令:[\"cd " + seatunnelConfigModel.getSeatunnelPath() +
                        "\", \"sh bin/seatunnel.sh --config config/" + remoteConfigName + " -e local\"]"));
        String resultMsg = remoteShell.execCommands(
                "cd " + seatunnelConfigModel.getSeatunnelPath(),
                "sh bin/seatunnel.sh --config config/" + remoteConfigName + " -e local");
        remoteShell.close();
        // 存储执行结果信息
        if (resultMsg == null) resultMsg = "无回执结果信息, 请检查!";
        nodeLog.add(LiteFlowNodeLogModel.warn(nodeId, nodeTag, resultMsg));
        devLiteflowNodeMapper.setExecutionMessage(this.rulerId, this.nodeId, this.nodeTag, resultMsg);
        // 删除创建的临时文件
        File file = new File(tempFilePath);
        file.delete();
    }

    private void sshSubmitJobAndUploadLogFil(String logFileName) {
        // 根据项目ID 获取到该项目的远程服务器的配置
        RemoteHost remoteHost = remoteHostMapper.selectById(seatunnelConfigModel.getRemoteHostId());
        RemoteShell remoteShell = new RemoteShellSshjImpl(
                remoteHost.getHostIp(), remoteHost.getHostPort(),
                remoteHost.getLoginName(), remoteHost.getPassword(), null);
        /**
         * 临时文件路径
         */
        String tempFilePathOfConfig = "./tempFolder/" + DateUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss-SSS-")
                + RandomUtil.randomString(5) + "-config.json";
        contentWriteToFile(tempFilePathOfConfig, JSONUtil.toJsonStr(dataTransferModel.getSeaTunnelConfig()));
        // 上传配置文件（v2.batch.config.template）至 seatunnel 的 ./config/ 中
        String remoteConfigName = "v2-supie-config" + DateUtil.format(new Date(), "-yyyy-MM-dd-HH-mm-ss-SSS") + ".json";
        String remoteFilePathOfConfig = seatunnelConfigModel.getSeatunnelPath() + "/config/" + remoteConfigName;
        remoteShell.uploadFile(tempFilePathOfConfig, remoteFilePathOfConfig);
        // 执行命令
        String resultMsg = remoteShell.execCommands(
                "cd " + seatunnelConfigModel.getSeatunnelPath(),
                "sh bin/seatunnel.sh --config config/" + remoteConfigName + " -e local");
        // 日志保存及上传
        String tempFilePathOfLog = "./tempFolder/" + logFileName;
        String remoteFilePathOfLog = seatunnelConfigModel.getSeatunnelPath() + "/taskLog/" + logFileName;
        contentWriteToFile(tempFilePathOfLog, resultMsg);
        remoteShell.uploadFile(tempFilePathOfLog, remoteFilePathOfLog);
        remoteShell.close();
        // 存储执行结果信息
        if (resultMsg == null) resultMsg = "无回执结果信息, 请检查!";
        devLiteflowNodeMapper.setExecutionMessage(this.rulerId, this.nodeId, this.nodeTag, resultMsg);
        // 删除创建的临时文件
        File configFile = new File(tempFilePathOfConfig);
        configFile.delete();
        File logFile = new File(tempFilePathOfLog);
        logFile.delete();
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
