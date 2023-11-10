package supie.common.dbutil.provider;

import lombok.Data;

/**
 * JDBC配置。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class JdbcConfig {

    /**
     * 驱动名。由子类提供。
     */
    private String driver;
    /**
     * 连接池验证查询的语句。
     */
    private String validationQuery = "SELECT 'x'";
    /**
     * Jdbc连接串，需要子类提供实现。
     */
    private String jdbcConnectionString;
    /**
     * 主机名。
     */
    private String host;
    /**
     * 端口号。
     */
    private Integer port;
    /**
     * 用户名。
     */
    private String username;
    /**
     * 密码。
     */
    private String password;
    /**
     * 数据库名。
     */
    private String database;
    /**
     * 模式名。
     */
    private String schema;
    /**
     * 连接池初始大小。
     */
    private int initialPoolSize = 5;
    /**
     * 连接池最小连接数。
     */
    private int minPoolSize = 5;
    /**
     * 连接池最大连接数。
     */
    private int maxPoolSize = 50;
}
