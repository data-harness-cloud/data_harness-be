package supie.common.dbutil.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import supie.common.core.constant.ObjectFieldType;
import supie.common.dbutil.constant.DblinkType;

/**
 * PostgreSQL数据源的提供者实现类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class PostgreSqlProvider implements DataSourceProvider {

    @Override
    public int getDblinkType() {
        return DblinkType.POSTGRESQL;
    }

    @Override
    public JdbcConfig getJdbcConfig(String configuration) {
        return JSON.parseObject(configuration, PostgreSqlConfig.class);
    }

    @Override
    public String getTableMetaListSql(String searchString) {
        String querySql = this.getTableMetaListSql();
        if (StrUtil.isNotBlank(searchString)) {
            querySql = querySql + " AND a.tablename LIKE ?";
        }
        return querySql + " ORDER BY a.tablename";
    }

    @Override
    public String getTableMetaSql() {
        return this.getTableMetaListSql() + " AND a.tablename = ?";
    }

    @Override
    public String getTableColumnMetaListSql() {
        return "SELECT "
                + "    pa.attname column_name, "
                + "    (CASE WHEN pt.typname = 'bpchar' THEN 'char' ELSE pt.typname END) column_type, "
                + "    pd.description AS column_comment, "
                + "    (CASE WHEN (SELECT COUNT(*) FROM pg_constraint WHERE conrelid = pa.attrelid AND conkey[1] = attnum AND contype = 'p') > 0 THEN 1 ELSE 0 END) primary_key, "
                + "    (CASE pa.attnotnull WHEN 't' THEN 0 ELSE 1 END) nullable, "
                + "    pa.attnum column_show_order, "
                + "    cc.is_identity auto_increment, "
                + "    (CASE WHEN pa.attlen > 0 THEN pa.attlen ELSE pa.atttypmod - 4 END) column_length, "
                + "    cc.character_maximum_length string_precision, "
                + "    cc.numeric_precision numeric_precision, "
                + "    pg_get_expr(padef.adbin, padef.adrelid) column_default "
                + "FROM "
                + "    information_schema.columns cc, "
                + "    pg_class pc, "
                + "    pg_type pt, "
                + "    pg_attribute pa "
                + "LEFT JOIN "
                + "    pg_attrdef padef ON (pa.attrelid, pa.attnum) = (padef.adrelid, padef.adnum) "
                + "LEFT JOIN "
                + "    pg_description pd ON (pd.objoid = pa.attrelid AND pd.objsubid = pa.attnum) "
                + "WHERE "
                + "    pc.oid = pa.attrelid "
                + "    AND pt.oid = pa.atttypid "
                + "    AND pc.relname = cc.table_name "
                + "    AND pa.attname = cc.column_name "
                + "    AND pc.relname = ? "
                + "ORDER BY pa.attnum";
    }

    @Override
    public String makePageSql(String sql, Integer pageNum, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        int offset = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        return sql + " LIMIT " + pageSize + " OFFSET " + offset;
    }

    @Override
    public String convertColumnTypeToJavaType(String columnType, Integer numericPrecision, Integer numericScale) {
        if ("varchar".equals(columnType)
                || "char".equals(columnType)
                || "bpchar".equals(columnType)
                || "text".equals(columnType)) {
            return ObjectFieldType.STRING;
        }
        if ("int4".equals(columnType)
                || "int2".equals(columnType)
                || "bit".equals(columnType)) {
            return ObjectFieldType.INTEGER;
        }
        if ("bool".equals(columnType)) {
            return ObjectFieldType.BOOLEAN;
        }
        if ("int8".equals(columnType)) {
            return ObjectFieldType.LONG;
        }
        if ("numeric".equals(columnType)) {
            return ObjectFieldType.BIG_DECIMAL;
        }
        if ("float4".equals(columnType)
                || "float8".equals(columnType)) {
            return ObjectFieldType.DOUBLE;
        }
        if ("date".equals(columnType)
                || "timestamp".equals(columnType)
                || "time".equals(columnType)) {
            return ObjectFieldType.DATE;
        }
        if ("bytea".equals(columnType)) {
            return ObjectFieldType.BYTE_ARRAY;
        }
        return null;
    }

    @Override
    public boolean havingClauseUsingAlias() {
        return false;
    }

    @Override
    public boolean aliasWithQuotes() {
        return true;
    }

    @Override
    public String makeDateTimeFilterSql(String columnName, String operator) {
        StringBuilder s = new StringBuilder(128);
        if (columnName == null) {
            columnName = "";
        }
        return s.append(columnName).append(" ").append(operator).append(" TO_TIMESTAMP(?, 'yyyy-mm-dd hh24:mi:ss')").toString();
    }

    private String getTableMetaListSql() {
        return "SELECT "
                + "    tablename \"tableName\", "
                + "    obj_description(relfilenode,'pg_class') \"tableComment\""
                + "FROM "
                + "    pg_tables a, pg_class b "
                + "WHERE "
                + "    a.tablename = b.relname "
                + "    AND a.tablename NOT LIKE 'pg%'"
                + "    AND a.tablename NOT LIKE 'sql_%'";
    }
}
