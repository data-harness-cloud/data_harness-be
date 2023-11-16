package supie.webadmin.app.util.remoteshell;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/10 14:20
 * @path SDT-supie.webadmin.app.util.remoteshell-RemoteShell
 */
public interface RemoteShell {

    /**
     * 关闭SSH连接
     */
    void close();

    /**
     * 执行命令
     *
     * @param commands 命令
     * @return 执行命令的结果日志
     * @author 王立宏
     * @date 2023/11/10 02:23
     */
    String execCommands(String... commands);

    /**
     * 上传文件
     *
     * @param localFilePath  本地文件路径
     * @param remoteFilePath 远程文件路径
     * @author 王立宏
     * @date 2023/11/10 02:29
     */
    void uploadFile(String localFilePath, String remoteFilePath);

    /**
     * 下载文件
     *
     * @param localFilePath  本地文件路径
     * @param remoteFilePath 远程文件路径
     * @author 王立宏
     * @date 2023/11/10 02:59
     */
    void downloadFile(String localFilePath, String remoteFilePath);

}
