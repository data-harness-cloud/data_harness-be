package supie.webadmin.app.util.remoteshell.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.ConsoleKnownHostsVerifier;
import net.schmizz.sshj.transport.verification.OpenSSHKnownHosts;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import supie.webadmin.app.util.remoteshell.RemoteShell;
import supie.webadmin.app.util.remoteshell.SshConfigModel;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：SSHJ 的 RemoteShell 实现
 *
 * @author 王立宏
 * @date 2023/11/10 15:01
 * @path SDT-supie.webadmin.app.util.remoteshell-RemoteShellSshjImpl
 */
@Slf4j
public class RemoteShellSshjImpl extends SshConfigModel implements RemoteShell {

    private SSHClient sshClient = null;

    public RemoteShellSshjImpl(String ip, String port, String userName, String password, String hostKey) {
        if (StrUtil.isNotBlank(ip)) {
            this.ip = ip;
        } else {
            throw new RuntimeException("目标主机IP不能为空！");
        }
        if (StrUtil.isNotBlank(port)) {
            this.port = port;
        }
        if (StrUtil.isNotBlank(userName)) {
            this.userName = userName;
        } else {
            throw new RuntimeException("登录用户名不能为空！");
        }
        if (StrUtil.isBlank(password) && StrUtil.isBlank(hostKey)) {
            throw new RuntimeException("密码和ssh-key不能同时为空！");
        } else {
            this.password = password;
            this.hostKey = hostKey;
        }
        getSshClient();
    }

    /**
     * 获取 SSH 连接
     *
     * @author 王立宏
     * @date 2023/11/10 03:37
     */
    private void getSshClient() {
        sshClient = new SSHClient();
        try {
            if (password != null) {
                sshClient.addHostKeyVerifier(new PromiscuousVerifier());
                final File khFile = new File(OpenSSHKnownHosts.detectSSHDir(), "known_hosts");
                sshClient.addHostKeyVerifier(new ConsoleKnownHostsVerifier(khFile, System.console()));
                sshClient.connect(this.ip, Integer.parseInt(this.port));
                sshClient.authPassword(this.userName, this.password);
            } else {
                InputStream entry = new ByteArrayInputStream(this.hostKey.getBytes(Charset.defaultCharset()));
                sshClient.addHostKeyVerifier(new OpenSSHKnownHosts(new InputStreamReader(entry, Charset.defaultCharset())));
                sshClient.authPublickey(this.userName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭SSH连接
     */
    @Override
    public void close() {
        try {
            sshClient.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行命令
     *
     * @param commands 命令
     * @return 执行命令的结果日志
     * @author 王立宏
     * @date 2023/11/10 02:23
     */
    @Override
    public String execCommands(String... commands) {
        StringBuilder resultMessage = new StringBuilder();
        Session session = null;
        try {
            session = sshClient.startSession();
            session.allocateDefaultPTY();
            Session.Shell shell = session.startShell();

            // 重定向 OutputStream，并且写入命令
            OutputStream outputStream = shell.getOutputStream();
            for (String command : commands) {
                outputStream.write((command + "\r").getBytes());
                outputStream.flush();
                TimeUnit.SECONDS.sleep(2);
            }
            outputStream.write(("exit\r").getBytes());
            outputStream.flush();

            // 重定向 InputStream，并且将其添加至回显日志
            InputStream inputStream = shell.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // 用正则表达式过滤掉表示颜色的字符
                String reg = "\\x1B\\[[;\\d]*m";
                Pattern pattern = Pattern.compile(reg);
                Matcher matcher = pattern.matcher(line);
                line = matcher.replaceAll("");

                resultMessage.append(line).append("\n");
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (TransportException | ConnectionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return resultMessage.toString();
    }

    /**
     * 上传文件
     *
     * @param localFilePath  本地文件路径
     * @param remoteFilePath 远程文件路径
     * @author 王立宏
     * @date 2023/11/10 02:29
     */
    @Override
    public void uploadFile(String localFilePath, String remoteFilePath) {
        SFTPClient sftpClient = null;
        try {
            sftpClient = sshClient.newSFTPClient();
            sftpClient.put(localFilePath, remoteFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (sftpClient != null) {
                try {
                    sftpClient.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
     * @date 2023/11/10 02:59
     */
    @Override
    public void downloadFile(String localFilePath, String remoteFilePath) {
        SFTPClient sftpClient = null;
        try {
            sftpClient = sshClient.newSFTPClient();
            sftpClient.get(remoteFilePath, localFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (sftpClient != null) {
                try {
                    sftpClient.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
