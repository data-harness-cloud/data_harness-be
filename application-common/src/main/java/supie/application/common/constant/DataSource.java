package supie.application.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源常量字典对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class DataSource {

    /**
     * MySQL。
     */
    public static final String DATASOURCE_MYSQL = "MYSQL";
    /**
     * PostgreSQL。
     */
    public static final String DATASOURCE_PG = "POSTGRESQL";
    /**
     * Oracle。
     */
    public static final String DATASOURCE_ORACLE = "ORACLE";
    /**
     * SQLServer。
     */
    public static final String DATASOURCE_SQLSERVER = "SQLSERVER";
    /**
     * Doris。
     */
    public static final String DATASOURCE_DORIS = "DORIS";
    /**
     * ClickHouse。
     */
    public static final String DATASOURCE_CK = "CLICKHOUSE";
    /**
     * Hive。
     */
    public static final String DATASOURCE_HIVE = "HIVE";
    /**
     * Ftp。
     */
    public static final String DATASOURCE_FTP = "FTP";
    /**
     * Api。
     */
    public static final String DATASOURCE_API = "API";
    /**
     * DB2。
     */
    public static final String DATASOURCE_DB2 = "DB2";
    /**
     * TDSQL_C
     */
    public static final String DATASOURCE_TDSQL_C = "TDSQL_C";
    /**
     * 达梦DM
     */
    public static final String DATASOURCE_DM = "DM";
    /**
     * SapHana
     */
    public static final String DATASOURCE_SAP_HANA = "SAPHANA";
    /**
     * SyBase
     */
    public static final String DATASOURCE_SY_BASE = "SYBASE";
    /**
     * SyBase
     */
    public static final String DATASOURCE_TIDB = "TIDB";

    private static final Map<Object, String> DICT_MAP = new HashMap<>(9);
    static {
        DICT_MAP.put(DATASOURCE_MYSQL, "MySQL");
        DICT_MAP.put(DATASOURCE_PG, "PostgreSQL");
        DICT_MAP.put(DATASOURCE_ORACLE, "Oracle");
        DICT_MAP.put(DATASOURCE_SQLSERVER, "SQLServer");
        DICT_MAP.put(DATASOURCE_DORIS, "Doris");
        DICT_MAP.put(DATASOURCE_CK, "ClickHouse");
        DICT_MAP.put(DATASOURCE_HIVE, "Hive");
        DICT_MAP.put(DATASOURCE_FTP, "Ftp");
        DICT_MAP.put(DATASOURCE_API, "Api");
        DICT_MAP.put(DATASOURCE_DB2, "DB2");
        DICT_MAP.put(DATASOURCE_TDSQL_C, "TdSql_C");
        DICT_MAP.put(DATASOURCE_DM, "DM");
        DICT_MAP.put(DATASOURCE_SAP_HANA, "SapHana");
        DICT_MAP.put(DATASOURCE_SY_BASE, "SyBase");
    }

    /**
     * 判断参数是否为当前常量字典的合法值。
     *
     * @param value 待验证的参数值。
     * @return 合法返回true，否则false。
     */
    public static boolean isValid(String value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private DataSource() {
    }
}
