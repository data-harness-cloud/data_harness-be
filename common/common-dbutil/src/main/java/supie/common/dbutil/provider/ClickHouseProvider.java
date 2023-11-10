package supie.common.dbutil.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import supie.common.core.constant.ObjectFieldType;
import supie.common.dbutil.constant.DblinkType;

/**
 * ClickHouse数据源的提供者实现类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class ClickHouseProvider implements DataSourceProvider {

    @Override
    public int getDblinkType() {
        return DblinkType.CLICKHOUSE;
    }

    @Override
    public JdbcConfig getJdbcConfig(String configuration) {
        return JSON.parseObject(configuration, ClickHouseConfig.class);
    }

    @Override
    public String getTableMetaListSql(String searchString) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(this.getTableMetaListSql());
        if (StrUtil.isNotBlank(searchString)) {
            sb.append(" AND name LIKE ?");
        }
        return sb.append(" ORDER BY name").toString();
    }

    @Override
    public String getTableMetaSql() {
        return this.getTableMetaListSql().concat(" AND name = ?");
    }

    @Override
    public String getTableColumnMetaListSql() {
        return "SELECT "
                + "    name columnName, "
                + "    types columnType, "
                + "    comment columnComment, "
                + "    is_in_primary_key AS primaryKey, "
                + "    position columnShowOrder "
                + "FROM "
                + "    system.columns "
                + "WHERE "
                + "    table = ?"
                + "    AND database = (SELECT database()) "
                + "ORDER BY position";
    }

    @Override
    public String convertColumnTypeToJavaType(String columnType, Integer numericPrecision, Integer numericScale) {
        if ("String".equals(columnType)
                || "FixedString".equals(columnType)) {
            return ObjectFieldType.STRING;
        }
        if ("UInt8".equals(columnType)
                || "UInt16".equals(columnType)
                || "UInt32".equals(columnType)
                || "Int8".equals(columnType)
                || "Int16".equals(columnType)
                || "Int32".equals(columnType)) {
            return ObjectFieldType.INTEGER;
        }
        if ("UInt64".equals(columnType)
                || "Int64".equals(columnType)) {
            return ObjectFieldType.LONG;
        }
        if ("decimal".equals(columnType)) {
            return ObjectFieldType.BIG_DECIMAL;
        }
        if ("Bool".equals(columnType)) {
            return ObjectFieldType.BOOLEAN;
        }
        if ("Float32".equals(columnType)
                || "Float64".equals(columnType)) {
            return ObjectFieldType.DOUBLE;
        }
        if ("DateTime".equals(columnType)) {
            return ObjectFieldType.DATE;
        }
        return null;
    }

    @Override
    public String makePageSql(String sql, Integer pageNum, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        int offset = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        return sql + " LIMIT " + offset + "," + pageSize;
    }

    private String getTableMetaListSql() {
        return "SELECT "
                + "    name tableName, "
                + "    comment tableComment "
                + "FROM "
                + "    system.tables "
                + "WHERE "
                + "    database = (SELECT database()) ";
    }
}
