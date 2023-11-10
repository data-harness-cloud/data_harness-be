package supie.common.dbutil.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import supie.common.core.constant.ObjectFieldType;
import supie.common.dbutil.constant.DblinkType;

/**
 * MySQL数据源的提供者实现类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class OracleProvider implements DataSourceProvider {

    @Override
    public int getDblinkType() {
        return DblinkType.ORACLE;
    }

    @Override
    public JdbcConfig getJdbcConfig(String configuration) {
        return JSON.parseObject(configuration, OracleConfig.class);
    }

    @Override
    public String getTableMetaSql() {
        return this.getTableMetaListSql() + " AND a.object_name = ?";
    }

    @Override
    public String getTableMetaListSql(String searchString) {
        String querySql = this.getTableMetaListSql();
        if (StrUtil.isNotBlank(searchString)) {
            querySql = querySql + " AND a.object_name LIKE ?";
        }
        return querySql + " ORDER BY a.object_name";
    }

    @Override
    public String getTableColumnMetaListSql() {
        return "SELECT "
                + "    a.column_name \"columnName\", "
                + "    a.data_type \"columnType\", "
                + "    b.comments \"columnComment\", "
                + "    a.nullable \"nullable\", "
                + "    a.column_id \"columnShowOrder\", "
                + "    0 \"autoIncrement\", "
                + "    (CASE WHEN (SELECT COUNT(*) FROM user_cons_columns cu, user_constraints au WHERE a.table_name = cu.table_name AND a.column_name = cu.column_name AND UPPER(cu.constraint_name) = UPPER(au.constraint_name) AND au.constraint_type = 'P') > 0 THEN 1 ELSE 0 END) \"primaryKey\", "
                + "    a.data_length \"stringPrecision\", "
                + "    (CASE WHEN a.data_type = 'NUMBER' and a.data_precision IS NULL THEN 20 ELSE a.data_precision END) \"numericPrecision\", "
                + "    (CASE WHEN a.data_type = 'NUMBER' and a.data_scale IS NULL THEN 0 ELSE a.data_scale END) \"numericScale\", "
                + "    a.data_default \"columnDefault\" "
                + "FROM "
                + "    user_tab_columns a, "
                + "    user_col_comments b "
                + "WHERE "
                + "    a.table_name = ?"
                + "    AND a.table_name = b.table_name "
                + "    AND a.column_name = b.column_name "
                + "ORDER BY a.column_id";
    }

    @Override
    public String getTestQuery() {
        return "SELECT 'x' FROM DUAL";
    }

    @Override
    public String makePageSql(String sql, Integer pageNum, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        int offset = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        int end = offset + pageSize;
        return "SELECT rownum, s.* FROM (SELECT rownum r, s.* FROM ("
                + sql + ") s WHERE rownum <= " + end + ") s WHERE r > " + offset;
    }

    @Override
    public String convertColumnTypeToJavaType(String columnType, Integer numericPrecision, Integer numericScale) {
        if ("VARCHAR2".equalsIgnoreCase(columnType)
                || "NVARCHAR2".equals(columnType)
                || "CHAR".equals(columnType)
                || "NCHAR".equals(columnType)
                || "CLOB".equals(columnType)
                || "NCLOB".equals(columnType)) {
            return ObjectFieldType.STRING;
        }
        if ("DATE".equalsIgnoreCase(columnType)
                || StrUtil.startWithIgnoreCase(columnType, "TIMESTAMP")) {
            return ObjectFieldType.DATE;
        }
        if ("NUMBER".equalsIgnoreCase(columnType)) {
            if (numericScale != null && numericScale > 0) {
                return ObjectFieldType.DOUBLE;
            }
            if (numericPrecision == null) {
                return ObjectFieldType.LONG;
            }
            return numericPrecision > 10 ? ObjectFieldType.LONG : ObjectFieldType.INTEGER;
        }
        return null;
    }

    @Override
    public boolean aliasWithQuotes() {
        return true;
    }

    @Override
    public boolean havingClauseUsingAlias() {
        return false;
    }

    @Override
    public String makeDateTimeFilterSql(String columnName, String operator) {
        StringBuilder s = new StringBuilder(128);
        if (columnName == null) {
            columnName = "";
        }
        return s.append(columnName).append(" ").append(operator).append(" TO_DATE(?, 'yyyy-mm-dd hh24:mi:ss')").toString();
    }

    private String getTableMetaListSql() {
        return "SELECT "
                + "    a.object_name \"tableName\", "
                + "    b.comments \"tableComment\", "
                + "    a.created \"createTime\" "
                + "FROM "
                + "    user_objects a, "
                + "    user_tab_comments b "
                + "WHERE "
                + "    a.object_type = 'TABLE' "
                + "    AND a.object_name = b.table_name ";
    }
}
