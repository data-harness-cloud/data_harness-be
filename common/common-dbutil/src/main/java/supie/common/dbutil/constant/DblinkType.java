package supie.common.dbutil.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接类型常量对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class DblinkType {

    /**
     * MySQL。
     */
    public static final int MYSQL = 0;
    /**
     * PostgreSQL。
     */
    public static final int POSTGRESQL = 1;
    /**
     * Oracle。
     */
    public static final int ORACLE = 2;
    /**
     * Dameng。
     */
    public static final int DAMENG = 3;
    /**
     * 人大金仓。
     */
    public static final int KINGBASE = 4;
    /**
     * ClickHouse。
     */
    public static final int CLICKHOUSE = 10;
    /**
     * Doris。
     */
    public static final int DORIS = 11;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);
    static {
        DICT_MAP.put(MYSQL, "MySQL");
        DICT_MAP.put(POSTGRESQL, "PostgreSQL");
        DICT_MAP.put(ORACLE, "Oracle");
        DICT_MAP.put(DAMENG, "Dameng");
        DICT_MAP.put(KINGBASE, "人大金仓");
        DICT_MAP.put(CLICKHOUSE, "ClickHouse");
        DICT_MAP.put(DORIS, "Doris");
    }

    /**
     * 判断参数是否为当前常量字典的合法值。
     *
     * @param value 待验证的参数值。
     * @return 合法返回true，否则false。
     */
    public static boolean isValid(Integer value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private DblinkType() {
    }
}
