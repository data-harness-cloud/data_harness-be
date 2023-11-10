package supie.webadmin.app.util.remoteshell;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.DirectConnection;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import java.io.IOException;

/**
 * 远程连接sshj工具库
 */
public class SSHJUtil {

    /*通过密钥字符串连接主机*/
    public void connHost() throws IOException {
        String privateKey = SSHUtilParse.key;

        SSHClient sshClient = new SSHClient();
        try {
            KeyProvider keyProvider = sshClient.loadKeys(privateKey, null, null);
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.connect(SSHUtilParse.ip, SSHUtilParse.port);
            sshClient.authPublickey(SSHUtilParse.username, keyProvider);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sshClient.disconnect();
        }
    }

    /*通过跳板机连接主机*/
    public void connHostByJumpServer() throws IOException {
        String jumpPrivateKey = SSHUtilParse.jump_key;
        String hostPrivateKey = SSHUtilParse.host_key;

        SSHClient sshClient = new SSHClient();
        SSHClient jumpClient = null;
        try {
            jumpClient = new SSHClient();
            jumpClient.addHostKeyVerifier(new PromiscuousVerifier());
            jumpClient.setTimeout(SSHUtilParse.timeout);
            jumpClient.connect(SSHUtilParse.jump_ip, SSHUtilParse.jump_port);
            KeyProvider jumpKeyProvider = jumpClient.loadKeys(jumpPrivateKey, null, null);
            jumpClient.authPublickey(SSHUtilParse.jump_username, jumpKeyProvider);
            DirectConnection tunnel = jumpClient.newDirectConnection(SSHUtilParse.host_ip, SSHUtilParse.host_port);

            KeyProvider hostKeyProvider = sshClient.loadKeys(hostPrivateKey, null, null);
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.setTimeout(SSHUtilParse.timeout);
            sshClient.connectVia(tunnel);
            sshClient.authPublickey(SSHUtilParse.host_username, hostKeyProvider);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sshClient.disconnect();
            if (jumpClient != null) jumpClient.disconnect();
        }
    }

    /*执行远程命令*/
    public void execCommand() throws IOException {
        String privateKey = SSHUtilParse.key;

        final SSHClient sshClient = new SSHClient();
        try {
            KeyProvider keyProvider = sshClient.loadKeys(privateKey, null, null);
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.connect( SSHUtilParse.ip,  SSHUtilParse.port);
            sshClient.authPublickey( SSHUtilParse.username, keyProvider);

            try (Session session = sshClient.startSession()) {
                String tmpDir = "/tmp/123";
                String command = "sudo mkdir -p " + tmpDir
                        + " && sudo chmod -R 755 " + tmpDir;
                session.exec(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sshClient.disconnect();
        }
    }

    /*scp 下载文件*/
    public void scpDownloadFields() throws IOException {
        String privateKey = SSHUtilParse.key;
        final SSHClient sshClient = new SSHClient();
        try {
            KeyProvider keyProvider = sshClient.loadKeys(privateKey, null, null);
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.connect(SSHUtilParse.ip, SSHUtilParse.port);
            sshClient.authPublickey(SSHUtilParse.username, keyProvider);

            sshClient.newSCPFileTransfer().download("/home/ubuntu/data", "/Users/Data/download");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sshClient.disconnect();
        }
    }

    /*sftp 下载文件*/
    public void sftpDownloadFields() throws IOException {
        String privateKey = SSHUtilParse.key;

        final SSHClient sshClient = new SSHClient();
        try {
            KeyProvider keyProvider = sshClient.loadKeys(privateKey, null, null);
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.connect(SSHUtilParse.ip, SSHUtilParse.port);
            sshClient.authPublickey(SSHUtilParse.username, keyProvider);

            try (SFTPClient sftp = sshClient.newSFTPClient()) {
                sftp.get("/home/ubuntu/data", "/Users/Data/download");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sshClient.disconnect();
        }
    }

}
