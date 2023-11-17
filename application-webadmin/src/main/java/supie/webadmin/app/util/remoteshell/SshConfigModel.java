package supie.webadmin.app.util.remoteshell;

import lombok.Data;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/17 15:42
 * @path SDT-supie.webadmin.app.util.remoteshell-SSHJConfig
 */
@Data
public class SshConfigModel {

    protected String ip;
    protected String port = "22";
    protected String userName = "root";
    protected String password;
    protected String hostKey = "localhost ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPmhSBtMctNa4hsZt8QGlsYSE5/gMkjeand69Vj4ir13";
//    protected String keyPath = separator + "Users" + separator + sysUserName + separator + ".ssh" + separator + "a.pem";

    protected int timeout = 3000;

}
