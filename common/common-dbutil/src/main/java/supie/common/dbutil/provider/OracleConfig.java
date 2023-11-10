package supie.common.dbutil.provider;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Oracle JDBC配置。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OracleConfig extends JdbcConfig {

    /**
     * oracle的sid。
     */
    private String serviceId;
    /**
     * JDBC 驱动名。
     */
    private String driver = "oracle.jdbc.OracleDriver";
    /**
     * 连接池验证查询的语句。
     */
    private String validationQuery = "SELECT 'x' FROM DUAL";

    /**
     * 获取拼好后的JDBC连接串。
     *
     * @return 拼好后的JDBC连接串。
     */
    @Override
    public String getJdbcConnectionString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("jdbc:oracle:thin:@")
                .append(getHost())
                .append(":")
                .append(getPort())
                .append(":")
                .append(serviceId);
        return sb.toString();
    }
}
