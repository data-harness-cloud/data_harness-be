package supie.webadmin.app.service.databasemanagement;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/6 11:42
 * @path SDT-supie.webadmin.app.service.databasemanagement-DataBaseTypeEnum
 */
public enum DataBaseTypeEnum {

    /**
     * MySQL。
     */
    DATASOURCE_MYSQL("MYSQL"),
    /**
     * PostgreSQL。
     */
    DATASOURCE_PG("POSTGRESQL"),
    /**
     * Oracle。
     */
    DATASOURCE_ORACLE("ORACLE"),
    /**
     * SQLServer。
     */
    DATASOURCE_SQLSERVER("SQLSERVER"),
    /**
     * Doris。
     */
    DATASOURCE_DORIS("DORIS"),
    /**
     * ClickHouse。
     */
    DATASOURCE_CLICK_HOUSE("CLICKHOUSE"),
    /**
     * Hive。
     */
    DATASOURCE_HIVE("HIVE"),
    /**
     * Ftp。
     */
    DATASOURCE_FTP("FTP"),
    /**
     * Api。
     */
    DATASOURCE_API("API"),
    /**
     * DB2。
     */
    DATASOURCE_DB2("DB2"),
    /**
     * TDSQL_C
     */
    DATASOURCE_TDSQL_C("TDSQL_C"),
    /**
     * 达梦DM
     */
    DATASOURCE_DM("DM"),
    /**
     * SapHana
     */
    DATASOURCE_SAP_HANA("SAPHANA"),
    /**
     * SyBase
     */
    DATASOURCE_SY_BASE("SYBASE"),
    /**
     * SyBase
     */
    DATASOURCE_TIDB("TIDB");

    private String name;

    DataBaseTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
