package supie.common.dbutil.provider;

/**
 * 数据源操作的提供者接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface DataSourceProvider {

    /**
     * 返回数据库链接类型，具体值可参考DblinkType常量类。
     * @return 返回数据库链接类型
     */
    int getDblinkType();

    /**
     * 返回Jdbc的配置对象。
     *
     * @param configuration Jdbc 的配置数据，JSON格式。
     * @return Jdbc的配置对象。
     */
    JdbcConfig getJdbcConfig(String configuration);

    /**
     * 获取当前数据库表meta列表数据的SQL语句。
     *
     * @param searchString 表名的模糊匹配字符串。如果为空，则没有前缀规律。
     * @return 查询数据库表meta列表数据的SQL语句。
     */
    String getTableMetaListSql(String searchString);

    /**
     * 获取当前数据库表meta数据的SQL语句。
     *
     * @return 查询数据库表meta数据的SQL语句。
     */
    String getTableMetaSql();

    /**
     * 获取当前数据库指定表字段meta列表数据的SQL语句。
     *
     * @return 查询指定表字段meta列表数据的SQL语句。
     */
    String getTableColumnMetaListSql();

    /**
     * 获取测试数据库连接的查询SQL。
     *
     * @return 测试数据库连接的查询SQL
     */
    default String getTestQuery() {
        return "SELECT 'x'";
    }

    /**
     * 为当前的SQL参数，加上分页部分。
     *
     * @param sql      SQL查询语句。
     * @param pageNum  页号，从1开始。
     * @param pageSize 每页数据量，如果为null，则取出后面所有数据。
     * @return 加上分页功能的SQL语句。
     */
    String makePageSql(String sql, Integer pageNum, Integer pageSize);

    /**
     * 将数据表字段类型转换为Java字段类型。
     *
     * @param columnType       数据表字段类型。
     * @param numericPrecision 数值精度。
     * @param numericScale     数值刻度。
     * @return 转换后的类型。
     */
    String convertColumnTypeToJavaType(String columnType, Integer numericPrecision, Integer numericScale);

    /**
     * Having从句中，统计字段参与过滤时，是否可以直接使用别名。
     *
     * @return 返回true，支持"HAVING sumOfColumn > 0"，返回false，则为"HAVING sum(count) > 0"。
     */
    default boolean havingClauseUsingAlias() {
        return true;
    }

    /**
     * SELECT的字段别名，是否需要加双引号，对于有些数据库，如果不加双引号，就会被数据库进行强制性的规则转义。
     *
     * @return 返回true，SELECT grade_id "gradeId"，否则 SELECT grade_id gradeId
     */
    default boolean aliasWithQuotes() {
        return false;
    }

    /**
     * 获取日期类型过滤条件语句。
     *
     * @param columnName 字段名。
     * @param operator   操作符。
     * @return 过滤从句。
     */
    default String makeDateTimeFilterSql(String columnName, String operator) {
        StringBuilder s = new StringBuilder(128);
        if (columnName == null) {
            columnName = "";
        }
        return s.append(columnName).append(" ").append(operator).append(" ?").toString();
    }
}
