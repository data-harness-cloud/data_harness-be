package supie.common.core.config;

import lombok.Data;

/**
 * 主要用户动态多数据源使用的配置数据。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class DataSourceInfo {
    /**
     * 用于多数据源切换的数据源类型。
     */
    private Integer datasourceType;
    /**
     * 用户名。
     */
    private String username;
    /**
     * 密码。
     */
    private String password;
    /**
     * 数据库主机。
     */
    private String databaseHost;
    /**
     * 端口号。
     */
    private Integer port;
    /**
     * 模式名。
     */
    private String schemaName;
    /**
     * 数据库名称。
     */
    private String databaseName;
}
