package supie.webadmin.app.util.remoteshell;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import supie.webadmin.app.model.RemoteHost;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/17 15:37
 * @path SDT-supie.webadmin.app.util.remoteshell-JschUtil
 */
@Slf4j
public class JschUtil {

    /**
     * SSH配置
     * SSH-Key与密码，优先使用密码，密码为空才使用SSH-Key。
     */
    private SSHConfig sshConfig;

    /**
     * 默认日志文件路径
     */
    public static final String DEFAULT_LOG_FILE_PATH = "./zzlogs/ssh/sshLog.log";

    public JschUtil() {
        this.sshConfig = new SSHConfig();
    }

    public JschUtil(SSHConfig sshConfig) {
        this.sshConfig = sshConfig;
    }

    /**
     * 执行远程命令
     *
     * @param logFilePath 远程命令日志文件（包含扩展名，为空则使用默认配置。）
     * @param commands    命令
     * @author 王立宏
     * @date 2023/10/17 10:31
     */
    public String executeRemoteCommand(String logFilePath, String... commands) {
        if (StrUtil.isBlankIfStr(logFilePath)) {
            logFilePath = DEFAULT_LOG_FILE_PATH;
        }
        StringBuilder resultMsg = new StringBuilder();
        resultMsg.append("消息日期：").append(DateUtil.now()).append("\n");
        Session session = null;
        ChannelShell channelShell = null;
        BufferedWriter bufferedWriter = null;
        try {
            JSch jsch = new JSch();
            if (null == sshConfig.getPassword()) {
                jsch.addIdentity(sshConfig.getKey());
            }
            session = jsch.getSession(sshConfig.getUserName(), sshConfig.getIp(), sshConfig.getPort());
            if (null != sshConfig.getPassword()) {
                session.setPassword(sshConfig.getPassword());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelShell = (ChannelShell) session.openChannel("shell");
            channelShell.setPtyType("vt102");
            channelShell.connect();

            OutputStream outputStream = channelShell.getOutputStream();//写入该流的数据  都将发送到远程端
            //使用PrintWriter 就是为了使用println 这个方法
            //好处就是不需要每次手动给字符加\n
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (String command : commands) {
                printWriter.println(command);
            }
            printWriter.println("exit"); //为了结束本次交互
            printWriter.flush(); //把缓冲区的数据强行输出

            InputStream inputStream = channelShell.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // 若文件及其文件夹不存在则创建
            Path path = Paths.get(logFilePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            FileWriter fileWriter = new FileWriter(logFilePath, true);

            bufferedWriter = new BufferedWriter(fileWriter);
            String line, title = "====================> SSH-ExecuteRemoteCommandLog(" + DateUtil.now() + ") <====================";
            resultMsg.append(title).append("\n");
            log.info(title);
            bufferedWriter.write(title);
            bufferedWriter.newLine();
            while ((line = reader.readLine()) != null) {
                log.info(line);
                resultMsg.append(line).append("\n");
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (channelShell != null) {
                channelShell.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return resultMsg.toString();
    }

    /**
     * 上传文件，若文件已存在则覆盖
     * @param localFilePath  本地文件路径
     * @param remoteFilePath 远程文件路径
     * @author 王立宏
     * @date 2023/10/17 11:27
     */
    public void uploadFile(String localFilePath, String remoteFilePath) {
        log.info("==========> SSH上传文件 <==========");
        log.info("本地文件路径：" + localFilePath);
        log.info("远程文件路径：" + remoteFilePath);
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            JSch jsch = new JSch();
            if (null == sshConfig.getPassword()) {
                jsch.addIdentity(sshConfig.getKey());
            }
            session = jsch.getSession(sshConfig.getUserName(), sshConfig.getIp(), sshConfig.getPort());
            if (null != sshConfig.getPassword()) {
                session.setPassword(sshConfig.getPassword());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            File localFile = new File(localFilePath);
            channelSftp.put(localFile.getAbsolutePath(), remoteFilePath, ChannelSftp.OVERWRITE);
        } catch (JSchException | SftpException e) {
            throw new RuntimeException(e);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    /**
     * @param fileInputStream 文件输入流（结束会关闭流）
     * @param remoteFilePath  远程文件路径
     * @author 王立宏
     * @date 2023/10/17 11:46
     */
    public void uploadFile(InputStream fileInputStream, String remoteFilePath) {
        log.info("==========> SSH上传文件 <==========");
        log.info("本地文件路径：以 InputStream 流形式上传");
        log.info("远程文件路径：" + remoteFilePath);
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            JSch jsch = new JSch();
            if (null == sshConfig.getPassword()) {
                jsch.addIdentity(sshConfig.getKey());
            }
            session = jsch.getSession(sshConfig.getUserName(), sshConfig.getIp(), sshConfig.getPort());
            if (null != sshConfig.getPassword()) {
                session.setPassword(sshConfig.getPassword());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            channelSftp.put(fileInputStream, remoteFilePath, ChannelSftp.OVERWRITE);
        } catch (JSchException | SftpException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (channelSftp != null) {
                    channelSftp.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param localFilePath  本地文件路径
     * @param remoteFilePath 远程文件路径
     * @author 王立宏
     * @date 2023/10/17 11:55
     */
    public void downloadFile(String localFilePath, String remoteFilePath) {
        log.info("==========> SSH下载文件 <==========");
        log.info("本地文件路径：" + localFilePath);
        log.info("远程文件路径：" + remoteFilePath);
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            JSch jsch = new JSch();
            if (null == sshConfig.getPassword()) {
                jsch.addIdentity(sshConfig.getKey());
            }
            session = jsch.getSession(sshConfig.getUserName(), sshConfig.getIp(), sshConfig.getPort());
            if (null != sshConfig.getPassword()) {
                session.setPassword(sshConfig.getPassword());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            channelSftp.get(remoteFilePath, localFilePath);
        } catch (JSchException | SftpException e) {
            throw new RuntimeException(e);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    /**
     * 下载文件
     *
     * @param remoteFilePath 本地文件路径
     * @author 王立宏
     * @date 2023/10/17 11:56
     */
    public InputStream downloadFile(String remoteFilePath) {
        log.info("==========> SSH下载文件 <==========");
        log.info("源文件地址：以 InputStream 形式输出");
        log.info("目标文件地址：" + remoteFilePath);
        Session session = null;
        ChannelSftp channelSftp = null;
        InputStream inputStream = null;
        try {
            JSch jsch = new JSch();
            if (null == sshConfig.getPassword()) {
                jsch.addIdentity(sshConfig.getKey());
            }
            session = jsch.getSession(sshConfig.getUserName(), sshConfig.getIp(), sshConfig.getPort());
            if (null != sshConfig.getPassword()) {
                session.setPassword(sshConfig.getPassword());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            inputStream = channelSftp.get(remoteFilePath);
        } catch (JSchException | SftpException e) {
            throw new RuntimeException(e);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return inputStream;
    }



    /**
     * 执行远程命令
     * @param commands    命令
     * @author 王立宏
     * @date 2023/10/17 10:31
     */
    public  String testConnection(String logFilePath,RemoteHost remoteHost, String... commands) {
        if (StrUtil.isBlankIfStr(logFilePath)) {
            logFilePath = DEFAULT_LOG_FILE_PATH;
        }
        StringBuilder resultMsg = new StringBuilder();
        resultMsg.append("消息日期：").append(DateUtil.now()).append("\n");
        Session session = null;
        ChannelShell channelShell = null;
        BufferedWriter bufferedWriter = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(remoteHost.getLoginName(), remoteHost.getHostIp(), Integer.parseInt(remoteHost.getHostPort()));
            if (null != remoteHost.getPassword()) {
                session.setPassword(remoteHost.getPassword());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelShell = (ChannelShell) session.openChannel("shell");
            channelShell.setPtyType("vt102");
            channelShell.connect();

            OutputStream outputStream = channelShell.getOutputStream();//写入该流的数据  都将发送到远程端
            //使用PrintWriter 就是为了使用println 这个方法
            //好处就是不需要每次手动给字符加\n
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (String command : commands) {
                printWriter.println(command);
            }
            printWriter.println("exit"); //为了结束本次交互
            printWriter.flush(); //把缓冲区的数据强行输出

            InputStream inputStream = channelShell.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // 若文件及其文件夹不存在则创建
            Path path = Paths.get(logFilePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            FileWriter fileWriter = new FileWriter(logFilePath, true);

            bufferedWriter = new BufferedWriter(fileWriter);
            String line, title = "====================> SSH-ExecuteRemoteCommandLog(" + DateUtil.now() + ") <====================";
            resultMsg.append(title).append("\n");
            log.info(title);
            bufferedWriter.write(title);
            bufferedWriter.newLine();
            while ((line = reader.readLine()) != null) {
                log.info(line);
                resultMsg.append(line).append("\n");
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (channelShell != null) {
                channelShell.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return resultMsg.toString();
    }

}
