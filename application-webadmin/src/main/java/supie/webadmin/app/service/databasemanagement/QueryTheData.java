package supie.webadmin.app.service.databasemanagement;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询数据库的表操作-----公共类
 * @author zhiwuzhu
 */
@Slf4j
public class QueryTheData {

    /**
     * 根据传入的数据源类型，创建Connection连接信息并查询对应的表
     *
     * @param jdbcDriver 数据库类型
     * @param jdbcUrl    数据库链接地址
     * @param userName   用户名
     * @param passWord   密码
     * @param targetDatabase  目标数据库
     * @param autoCommit  是否自动提交
     * @author litao
     */

    public static List<Map<String, Object>> queryDatabaseTable(String jdbcDriver, String jdbcUrl, String userName, String passWord,String targetDatabase, boolean autoCommit) throws Exception {
        Connection conn = null;
        try {
            Class.forName(jdbcDriver);
            // 连接数据库
            conn = DriverManager.getConnection(jdbcUrl, userName, passWord);

            // 查询表中所有表名和字段相关信息
            List<Map<String, Object>> queryTableNames = queryTables(conn,targetDatabase);
            conn.setAutoCommit(autoCommit);
            return queryTableNames;
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
     * 根据connection查询该数据库连接的所有数据库表
     *
     * @param connection 数据库类型
     * @author litao
     */
    public static List<Map<String, Object>>  queryTables(Connection connection,String targetDatabase ) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        // 获取数据库的表
        ResultSet resultSet = metaData.getTables(targetDatabase, null, null, new String[]{"TABLE"});

        ArrayList<Map<String, Object>> tableList = new ArrayList<>();
        // 循环TABLE，将结果记录在Map中
        while (resultSet.next()) {
            HashMap<String, Object> tableMap = new HashMap<>();
            // 表名
            String tableName = resultSet.getString("TABLE_NAME");
            // 注释
            String tableComment = resultSet.getString("REMARKS");
            tableMap.put("tableName",tableName);
            tableMap.put("remarks",tableComment);
            tableList.add(tableMap);
        }
        resultSet.close();
        return tableList;
    }

    /**
     *   根据数据表查询数据
     * @param connection  数据库类型
     * @param tableName   表名
     * @throws SQLException
     */
    private static void queryTable(Connection connection, String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();

        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        }

        resultSet.close();
        statement.close();
    }

}
