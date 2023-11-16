package supie.webadmin.app.liteFlow;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.upload.BaseUpDownloader;
import supie.common.core.upload.UpDownloaderFactory;
import supie.common.core.upload.UploadResponseInfo;
import supie.common.core.upload.UploadStoreInfo;
import supie.common.core.util.MyModelUtil;
import supie.common.redis.cache.SessionCacheHelper;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import supie.webadmin.app.dao.DevLiteflowLogMapper;
import supie.webadmin.app.dao.SchedulingTasksMapper;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
import supie.webadmin.app.model.BaseBusinessFile;
import supie.webadmin.app.model.DevLiteflowLog;
import supie.webadmin.app.model.SchedulingTasks;
import supie.webadmin.config.ApplicationConfig;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/22 10:41
 * @path SDT-supie.webadmin.app.cmp-QuartzJobCheckCycle
 */
@Slf4j
@Component
public class QuartzJobCheckCycle implements Job {

    @Resource
    private FlowExecutor flowExecutor;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private SchedulingTasksMapper schedulingTasksMapper;
    @Autowired
    private DevLiteflowLogMapper devLiteflowLogMapper;
    @Autowired
    private ApplicationConfig appConfig;
    @Autowired
    private UpDownloaderFactory upDownloaderFactory;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        // 任务调度的ID
        Long schedulingTasksId = dataMap.getLong("schedulingTasksId");
        log.info("------------------------------> 开始执行任务[" + schedulingTasksId + "] <------------------------------");
        SchedulingTasks schedulingTasks = schedulingTasksMapper.selectById(schedulingTasksId);
        if (schedulingTasks.getRunNumber() == null) {
            schedulingTasks.setRunNumber(1);
        } else {
            schedulingTasks.setRunNumber(schedulingTasks.getRunNumber() + 1);
        }
        Long rulerId = schedulingTasks.getRulerId();

        // 创建日志信息表
        DevLiteflowLog liteflowLog = buildLiteflowLog(schedulingTasks);

        //执行规则
        LiteflowResponse response = flowExecutor.execute2Resp(String.valueOf(rulerId), null, new LinkedList<LiteFlowNodeLogModel>());

        // 获取节点执行日志信息
        LinkedList<LiteFlowNodeLogModel> logModelList = response.getContextBean(LinkedList.class);

        // 设置任务显示状态为 成功 或 失败
        if (response.isSuccess()) {
            Integer successNumber = schedulingTasks.getSuccessNumber();
            if (null == successNumber) successNumber = 0;
            schedulingTasks.setTaskState("正常");
            schedulingTasks.setSuccessNumber(successNumber + 1);
            liteflowLog.setRunResult("成功");
            String successMessage = "任务[" + schedulingTasksId + "]的规则链[" + rulerId + "]执行成功!";
            log.info(successMessage);
            liteflowLog.setRunResultMsg(successMessage);
            logModelList.add(LiteFlowNodeLogModel.info("RESULT", "RESULT", successMessage));
        } else {
            Integer errorNumber = schedulingTasks.getErrorNumber();
            if (null == errorNumber) errorNumber = 0;
            schedulingTasks.setErrorNumber(errorNumber + 1);
            liteflowLog.setRunResult("失败");
            String errorMessage = "错误代码为:" + response.getCode() + "\n" + response.getMessage();
            liteflowLog.setRunResultMsg(errorMessage);
            schedulingTasks.setErrorMsg(errorMessage);
            schedulingTasks.setTaskState("出错");
            String result = "任务[" + schedulingTasksId + "]的规则链[" + rulerId + "]执行失败!";
            log.error(result);
            log.error("失败信息如下:" + errorMessage);
            logModelList.add(LiteFlowNodeLogModel.error("RESULT", "RESULT", result));
            logModelList.add(LiteFlowNodeLogModel.error("RESULT", "RESULT", errorMessage));
        }
        // 创建一个日志文件，用于存储 LiteFlow 节点执行日志信息
        String localFilePath = "./zzlogs/liteFlow/" + liteflowLog.getLogFileName();
        try {
            writeLogToFile(logModelList, localFilePath);
            String fieldName = "logFileJson";
            Boolean asImage = false;
            // 日志文件存入 minio
            UploadResponseInfo uploadResponseInfo = uploadFile(liteflowLog.getLogFileName(), new FileInputStream(localFilePath), fieldName, asImage);
            Map<String, Object> fileJson = new HashMap<>();
            fileJson.put("fieldName", fieldName);
            fileJson.put("asImage", asImage);
            fileJson.put("filename", uploadResponseInfo.getFilename());
            // minio 存储信息存入 liteflowLog
            liteflowLog.setLogFileJson(JSONUtil.toJsonStr(fileJson));
            liteflowLog.setLogFileSize(Files.size(Paths.get(localFilePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 删除本地零时文件
            if (localFilePath != null) {
                try {
                    Files.delete(Paths.get(localFilePath));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        liteflowLog.setSchedulingTasksId(schedulingTasks.getId());
        // liteflowLog存入数据库
        devLiteflowLogMapper.insert(liteflowLog);
        schedulingTasksMapper.updateById(schedulingTasks);
    }

    public UploadResponseInfo uploadFile(String originalFilename, FileInputStream uploadFileInputStream, String fieldName, Boolean asImage) throws IOException {
        UploadStoreInfo storeInfo = MyModelUtil.getUploadStoreInfo(DevLiteflowLog.class, fieldName);
        // 这里就会判断参数中指定的字段，是否支持上传操作。
        if (!storeInfo.isSupportUpload()) {
            return null;
        }
        // 根据字段注解中的存储类型，通过工厂方法获取匹配的上传下载实现类，从而解耦。
        BaseUpDownloader upDownloader = upDownloaderFactory.get(storeInfo.getStoreType());
        return upDownloader.doUpload(null,
                appConfig.getUploadFileBaseDir(), DevLiteflowLog.class.getSimpleName(), fieldName, asImage, uploadFileInputStream, originalFilename);
    }

    private static void writeLogToFile(List<LiteFlowNodeLogModel> logModelList, String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 将节点日志信息存储入日志文件
            for (LiteFlowNodeLogModel log : logModelList) {
                // 存储日志信息，一行行写入日志文件中
                StringBuilder logInfo = new StringBuilder();
                String logLevel = StrUtil.fillAfter(log.getLevel(), ' ', 5);
                logInfo.append("[").append(logLevel).append("] ");
                logInfo.append("[").append(log.getNodeId()).append("] ");
                logInfo.append("[").append(log.getNodeTag()).append("] ");
                logInfo.append("[").append(DateUtil.formatDateTime(log.getLogTime())).append("] ");
                logInfo.append("==> ").append(log.getLogMessage()).append(" ");
                writer.write(logInfo.toString());
                writer.newLine();
            }
            writer.close();
        }
    }

    /**
     * 构建 LiteFlow 日志
     *
     * @param schedulingTasks 任务对象
     * @return dev liteflow 日志
     * @author 王立宏
     * @date 2023/10/31 11:25
     */
    private DevLiteflowLog buildLiteflowLog(SchedulingTasks schedulingTasks) {
        DevLiteflowLog liteflowLog = new DevLiteflowLog();
        Date nowDate = new Date();
        liteflowLog.setId(idGenerator.nextLongId());
        liteflowLog.setRulerId(schedulingTasks.getRulerId());
        liteflowLog.setRunVersion(schedulingTasks.getRunNumber());
        liteflowLog.setRunTime(nowDate);
        liteflowLog.setLogFileName(
                "liteFlowLog_" + schedulingTasks.getRulerId() + "_" + DateUtil.format(nowDate, "yyyy-MM-dd-HH-mm-ss") + ".log");
        liteflowLog.setIsDelete(GlobalDeletedFlag.NORMAL);
        liteflowLog.setCreateTime(nowDate);
        liteflowLog.setUpdateTime(nowDate);
        if (schedulingTasks.getCreateUserId() != null) {
            liteflowLog.setCreateUserId(schedulingTasks.getCreateUserId());
            liteflowLog.setUpdateUserId(schedulingTasks.getCreateUserId());
        }
        if (schedulingTasks.getDataUserId() != null) {
            liteflowLog.setDataUserId(schedulingTasks.getDataUserId());
        }
        if (schedulingTasks.getDataDeptId() != null) {
            liteflowLog.setDataDeptId(schedulingTasks.getDataDeptId());
        }
        return liteflowLog;
    }
}
