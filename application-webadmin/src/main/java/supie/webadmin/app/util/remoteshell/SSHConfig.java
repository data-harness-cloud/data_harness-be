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
public class SSHConfig {

    private String ip = "120.0.0.1";
    private String userName = "root";
    private String password;
    private String key = "/Users/Data/.ssh/a.pem";
    private int port = 22;

    private int timeout = 3000;

}
