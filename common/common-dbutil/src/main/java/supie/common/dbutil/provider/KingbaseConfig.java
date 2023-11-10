package supie.common.dbutil.provider;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人大金仓JDBC配置。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KingbaseConfig extends JdbcConfig {

    /**
     * JDBC 驱动名。
     */
    private String driver = "com.kingbase8.Driver";
    /**
     * 数据库JDBC连接串的扩展部分。
     */
    private String extraParams = "?useJDBCCompliantTimezoneShift=true&serverTimezone=Asia/Shanghai&useSSL=true&characterEncoding=UTF-8";

    /**
     * 获取拼好后的JDBC连接串。
     *
     * @return 拼好后的JDBC连接串。
     */
    @Override
    public String getJdbcConnectionString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("jdbc:kingbase8://")
                .append(getHost())
                .append(":")
                .append(getPort())
                .append("/")
                .append(getDatabase())
                .append(extraParams);
        return sb.toString();
    }
}
