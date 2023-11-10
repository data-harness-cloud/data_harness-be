package supie.webadmin.app.service.databasemanagement;


import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询数据库的字段的操作----公共类
 * @author zhiwuzhu
 */
@Slf4j
public class QueryTheFields {

    /**
     * 根据传入的数据源类型，创建Connection连接信息并测试链接是否正确
     *
     * @param jdbcDriver 数据库类型
     * @param jdbcUrl    数据库链接地址
     * @param userName   用户名
     * @param passWord   密码
     * @param autoCommit 是否自动提交
     * @param tableName  表名
     * @param databaseName  架构名
     * @author litao
     */
    public static List<Map<String, Object>> queryTableFields(String jdbcDriver, String jdbcUrl, String userName, String passWord, boolean autoCommit,String tableName,String databaseName) throws Exception {
        Connection conn = null;
        try {
            Class.forName(jdbcDriver);
            // 连接数据库
            conn = DriverManager.getConnection(jdbcUrl, userName, passWord);

            // 查询表中所有表名和字段相关信息
            List<Map<String, Object>> queryFieldsColumns = queryFieldsColumns(conn, tableName,databaseName);
            conn.setAutoCommit(autoCommit);
            return   queryFieldsColumns;
        } catch (ClassNotFoundException se) {
            // 连接失败
            log.error("找不到驱动程序类", se);
            throw new Exception("找不到驱动程序类");
        } catch (SQLException se) {
            // 连接失败
            log.error("连接失败", se);
            throw new Exception("连接失败");
        } finally {
            // 关闭数据库连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 根据connection和表名查询该数据库表的字段的基本信息（类型，大小等等）和主键，外键，索引
     *
     * @param connection 数据库类型
     * @param tableName 表名
     * @author litao
     */
    private static List<Map<String, Object>> queryFieldsColumns(Connection connection, String tableName,String databaseName) throws SQLException {
        String catalog = databaseName;  // 代表数据库的目录名。在某些数据库管理系统中，一个数据库可以包含多个目录，每个目录下又包含多个模式（或称为架构）。通过指定 catalog 参数，可以限定查询只在特定的目录下进行。
        String schema = null;  // 代表数据库中的模式（或称为架构）。一个模式是具有一组相关表的命名空间。通过指定 schema 参数，可以限定查询只在特定的模式下进行。
        String schemaPattern = null;  //是用于指定模式（或架构）名的模式匹配字符串。在调用 JDBC 方法时，可以将 schemaPattern 参数传入以获取满足特定模式匹配条件的模式。
        boolean unique = false; // 是否只返回唯一性索引
        boolean approximate = false; // 是否返回近似结果
        DatabaseMetaData metaData = connection.getMetaData();

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Object> columnNameMap = new HashMap<>();

        // 主键
        ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(catalog, schema, tableName);
        while (primaryKeyResultSet.next()) {
            String columnName = primaryKeyResultSet.getString("COLUMN_NAME");
            // 处理每个主键列的逻辑
        }
        primaryKeyResultSet.close();

//        ResultSetMetaData resultSetMetaData = primaryKeyResultSet.getMetaData();
//        int columnCount = resultSetMetaData.getColumnCount();
//
//        while (primaryKeyResultSet.next()) {
//            for (int i = 1; i <= columnCount; i++) {
//                String columnName = resultSetMetaData.getColumnName(i);
//                String columnValue = primaryKeyResultSet.getString(columnName);
//                // 处理每个主键列的逻辑，包括列名和对应的值
//            }
//        }

        primaryKeyResultSet.close();

        // 索引
        ResultSet indexResultSet = metaData.getIndexInfo(catalog, schemaPattern, tableName, unique, approximate);
        while (indexResultSet.next()) {
            String indexName = indexResultSet.getString("INDEX_NAME");
            boolean nonUnique = indexResultSet.getBoolean("NON_UNIQUE");
            // 处理每个索引的逻辑
        }
        indexResultSet.close();

        // 外键
        ResultSet foreignKeyResultSet = metaData.getImportedKeys(catalog, schema, tableName);
        while (foreignKeyResultSet.next()) {
            String fkTableName = foreignKeyResultSet.getString("FKTABLE_NAME");
            String fkColumnName = foreignKeyResultSet.getString("FKCOLUMN_NAME");
            // 处理每个外键的逻辑
        }
        foreignKeyResultSet.close();

        // 字段
        ResultSet resultSet = metaData.getColumns(catalog, schemaPattern, tableName, null);

        while (resultSet.next()) {

            HashMap<String, Object> dataTypeMap = new HashMap<>();
            // 字段名
            String columnName = resultSet.getString("COLUMN_NAME");
            // 字段类型
            String dataType = resultSet.getString("TYPE_NAME");
            // 字段大小
            int columnSize = resultSet.getInt("COLUMN_SIZE");
            // 字段注释
            String columnComment = resultSet.getString("REMARKS");
//            下面是获取关于列级别的信息
//            获取列的名称：resultSet.getString("COLUMN_NAME")
//            获取列的标签（别名）：resultSet.getString("LABEL")
//            获取列的显示大小：resultSet.getInt("COLUMN_DISPLAY_SIZE")
//            获取列的数据类型的编号：resultSet.getInt("DATA_TYPE")
//            获取列的数据类型的名称：resultSet.getString("TYPE_NAME")
//            获取列的精度：resultSet.getInt("PRECISION")
//            获取列的小数位数：resultSet.getInt("SCALE")
//            获取列是否为只读：resultSet.getBoolean("IS_READONLY")
//            获取列是否自动递增：resultSet.getBoolean("IS_AUTOINCREMENT")

            dataTypeMap.put("fieldName",columnName);
            dataTypeMap.put("typeName",dataType);
            dataTypeMap.put("columnSize",columnSize);
            dataTypeMap.put("remarks",columnComment);

            mapArrayList.add(dataTypeMap);
        }
        resultSet.close();
        return mapArrayList;
    }

}
