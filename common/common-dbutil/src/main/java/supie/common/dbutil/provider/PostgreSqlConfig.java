package supie.common.dbutil.provider;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PostgreSQL JDBC配置。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostgreSqlConfig extends JdbcConfig {

    /**
     * JDBC 驱动名。
     */
    private String driver = "org.postgresql.Driver";

    /**
     * 获取拼好后的JDBC连接串。
     *
     * @return 拼好后的JDBC连接串。
     */
    @Override
    public String getJdbcConnectionString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("jdbc:postgresql://")
                .append(getHost())
                .append(":")
                .append(getPort())
                .append("/")
                .append(getDatabase());
        if (StrUtil.isBlank(getSchema())) {
            sb.append("?currentSchema=public");
        } else {
            sb.append("?currentSchema=").append(getSchema());
        }
        sb.append("&TimeZone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8");
        return sb.toString();
    }
}
