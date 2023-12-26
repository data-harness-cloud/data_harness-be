package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import supie.common.core.object.MyPageParam;
import supie.webadmin.app.service.databasemanagement.DataBaseTypeEnum;
import supie.webadmin.app.util.JsqlparserUtil;

import java.sql.*;
import java.util.*;

import static cn.hutool.db.DbUtil.close;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/2 16:16
 * @path SDT-supie.webadmin.app.service.databasemanagement-BaseDataSource
 */
@Slf4j
public class BaseDataSource {

    /**
     * 数据库类型
     */
    protected DataBaseTypeEnum databaseType;
    /**
     * 数据库驱动
     */
    protected String jdbcDriver;
    /**
     * 连接地址
     */
    protected String jdbcUrl;
    /**
     * 主机地址
     */
    protected String hostIp;
    /**
     * 主机端口
     */
    protected String hostPort;
    /**
     * 数据库名称
     */
    protected String databaseName;
    /**
     * 用户名
     */
    protected String userName;
    /**
     * 密码
     */
    protected String password;

    /**
     * 数据库连接对象
     */
    protected Connection connection = null;

    /**
     * 查询所有数据库的名称
     * queryAllDatabaseName()
     */
    protected String queryAllDatabaseNameSql = "SHOW DATABASES";

    /**
     * 连接数据库
     */
    protected Boolean initConnection() {
        try {
            Class.forName(this.jdbcDriver);
            // 连接数据库
            connection = DriverManager.getConnection(this.jdbcUrl, this.userName, this.password);
            return !connection.isClosed();
        } catch (ClassNotFoundException | SQLException e) {
            connection = null;
            throw new RuntimeException(e);
        }
    }

    protected Boolean initConnectionAndCreatelibrarypermissions() {
        try {
            Class.forName(this.jdbcDriver);
            // 连接数据库
            connection = DriverManager.getConnection(this.jdbcUrl, this.userName, this.password);
            // 检查用户是否有建库的权限
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW GRANTS FOR '" + userName + "'@'%'");
            boolean hasCreateDatabasePrivilege = false;
            while (resultSet.next()) {
                String grant = resultSet.getString(1);
                // 如果权限中含有create，则说明具有创建数据库的权限
                if (grant.toLowerCase().contains("create")) {
                    hasCreateDatabasePrivilege = true;
                    break;
                }
            }
            if (hasCreateDatabasePrivilege == false){
                resultSet.close();
                statement.close();
                throw  new RuntimeException("该用户没有创建数据库的权限！！！！！");
            }
            resultSet.close();
            statement.close();
            return !connection.isClosed() && hasCreateDatabasePrivilege;
        } catch (ClassNotFoundException e) {
            connection = null;
            throw new RuntimeException("无法加载JDBC驱动", e);
        } catch (SQLException e) {
            connection = null;
            if (e.getMessage().contains("no such grant defined")) {
                throw new RuntimeException("用户在指定主机上没有相应的授权", e);
            } else {
                throw new RuntimeException("无法连接到数据库", e);
            }
        }
    }




    /**
     * 关闭所有连接
     * @author 王立宏
     * @date 2023/10/30 10:58
     */
    public void closeAll() {
        close(connection);
    }

    /**
     * 执行单条SQL语句。
     *
     * @param sqlScript SQL语句。
     * @param pageParam 分页对象。如果为 null 则不进行分页处理。
     * @return
     */
    public Map<String, Object> executeSql(String sqlScript, MyPageParam pageParam) {
        Map<String, Object> resultMapData = new HashMap<>();
        try {
            Statement statement = connection.createStatement();
            resultMapData.put("sql", sqlScript);
            try {
                // 判断是否需要进行分页处理
                if (pageParam != null) {
                    long totalCount = 0L;
                    // 判断是否属于查询语句。若为查询语句，则进行分页处理
                    if (JsqlparserUtil.isQuerySql(sqlScript)) {
                        String countSqlScript = JsqlparserUtil.buildCountSqlScript(sqlScript);
                        sqlScript = JsqlparserUtil.buildLimitSqlScript(sqlScript, pageParam);
                        ResultSet resultSet = statement.executeQuery(countSqlScript);
                        while (resultSet.next()) {
                            totalCount = resultSet.getLong(1);
                        }
                        resultSet.close();
                    }
                    resultMapData.put("totalCount", totalCount);
                }
                boolean result = statement.execute(sqlScript);
                if (result) {
                    ResultSet resultSet = statement.getResultSet(); // 查询结果
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    Map<String, LinkedList> queryResultData = new LinkedHashMap<>();
                    LinkedList<String> fieldList = null;
                    LinkedList<Map<String, Object>> queryDataList = new LinkedList<>();
                    while (resultSet.next()) {
                        // 获取字段数量
                        int columnCount = metaData.getColumnCount();
                        // 遍历每个字段
                        Map<String, Object> queryDataMap = new LinkedHashMap<>();
                        boolean setFieldList = false;
                        if (fieldList == null) {
                            fieldList = new LinkedList<>();
                            setFieldList = true;
                        }
                        for (int i = 1; i <= columnCount; i++) {
                            // 获取字段名
                            String columnName = metaData.getColumnName(i);
                            if (StrUtil.isBlankIfStr(columnName)) continue;
                            if (StrUtil.isBlankIfStr(resultSet)) {
                                throw new RuntimeException("resultSet为空");
                            }
                            // 获取字段值
                            Object columnValue = resultSet.getObject(columnName);
                            // 存入字段名和字段值
                            if (setFieldList) fieldList.add(columnName);
                            queryDataMap.put(columnName, columnValue);
                        }
                        queryDataList.add(queryDataMap);
                    }
                    resultSet.close();
                    queryResultData.put("fieldList", fieldList);
                    queryResultData.put("queryDataList", queryDataList);
                    resultMapData.put("queryResultData", queryResultData);
                } else {
                    int affectedDataNumber = statement.getUpdateCount(); // 影响的行数
                    resultMapData.put("updateResultData", affectedDataNumber);
                }
                resultMapData.put("success", true);
                resultMapData.put("message", "SUCCESS");
            } catch (SQLException e) {
                resultMapData.put("success", false);
                resultMapData.put("message", e.getMessage());
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultMapData;
    }

    /**
     * 执行位置数量的SQL
     * @param sql 字符（会将SQl以“;”切开成List来执行，结果也会按照语句的顺序来显示）
     * @return [
     *   {
     *     "success": true,
     *     "sql": "该信息所属的SQL语句。",
     *     "message": "执行信息，主要为错误时的错误消息。",
     *     "queryResultData": "查询语句查询到的数据集",
     *     "updateResultData": "修改语句影响的数据的条数"
     *   }
     * ]
     */
    public List<Map<String, Object>> executeSqlList(String sql) {
        List<String> sqlList = StrSplitter.split(sql, ";", 0, true, true);
        List<Map<String, Object>> resultDataList = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            for (String sqlOne : sqlList) {
                Map<String, Object> resultMapData = new HashMap<>();
                resultMapData.put("sql", sqlOne);
                try {
                    boolean result = statement.execute(sqlOne);
                    if (result) {
                        ResultSet resultSet = statement.getResultSet(); // 查询结果
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        Map<String, LinkedList> queryResultData = new LinkedHashMap<>();

                        LinkedList<String> fieldList = null;
                        LinkedList<Map<String, Object>> queryDataList = new LinkedList<>();
                        while (resultSet.next()) {
                            // 获取字段数量
                            int columnCount = metaData.getColumnCount();
                            // 遍历每个字段
                            Map<String, Object> queryDataMap = new LinkedHashMap<>();
                            Boolean setFieldList = false;
                            if (fieldList == null) {
                                fieldList = new LinkedList<>();
                                setFieldList = true;
                            }
                            for (int i = 1; i <= columnCount; i++) {
                                // 获取字段名
                                String columnName = metaData.getColumnName(i);
                                if (StrUtil.isBlankIfStr(columnName)) continue;
                                if (StrUtil.isBlankIfStr(resultSet)) {
                                    throw new RuntimeException("resultSet为空");
                                }
                                // 获取字段值
                                Object columnValue = resultSet.getObject(columnName);
                                // 存入字段名和字段值
                                if (setFieldList) fieldList.add(columnName);
                                queryDataMap.put(columnName, columnValue);
                            }
                            queryDataList.add(queryDataMap);
                        }
                        resultSet.close();
                        queryResultData.put("fieldList", fieldList);
                        queryResultData.put("queryDataList", queryDataList);
                        resultMapData.put("queryResultData", queryResultData);
                    } else {
                        int affectedDataNumber = statement.getUpdateCount(); // 影响的行数
                        resultMapData.put("updateResultData", affectedDataNumber);
                    }
                    resultMapData.put("success", true);
                    resultMapData.put("message", "SUCCESS");
                } catch (SQLException e) {
                    resultMapData.put("success", false);
                    resultMapData.put("message", e.getMessage());
                    log.error(e.toString());
                }
                resultDataList.add(resultMapData);
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultDataList;
    }

    /**
     * 创建数据库
     *
     * @param databaseName 创建的数据库的名称
     * @author 王立宏
     * @date 2023/11/02 04:30
     */
    public void createDatabase(String databaseName) {
        String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        List<Map<String, Object>> resultDataList = executeSqlList(createDatabaseSql);
        Map<String, Object> resultMap = resultDataList.get(0);
        if (Boolean.TRUE.equals(resultMap.get("success")) && ((int) resultMap.get("updateResultData") == 1)) return;
        throw new RuntimeException("数据库创建[" + resultMap.get("sql") + "]失败！" + resultMap.get("message").toString());
    }

    /**
     * 查询可操作的所有数据库名称
     *
     * @return 该账户可操作的所有数据库集
     * @author 王立宏
     * @date 2023/11/23 03:29
     */
    public List<String> queryAllDatabaseName() {
        List<String> databaseNameList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryAllDatabaseNameSql);
            while (resultSet.next()) {
                databaseNameList.add(resultSet.getString(1));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return databaseNameList;
    }

    /**
     * 查询数据库数据表名及类型
     * @param databaseName
     * @return
     */
    public List<Map<String, Object>> queryDatabaseTable(String databaseName) {
        ArrayList<Map<String, Object>> tableList = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(databaseName, null, null, new String[]{"TABLE"});
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            // 循环TABLE，将结果记录在Map中
            while (resultSet.next()) {
                HashMap<String, Object> tableMap = new HashMap<>();
//                for (int i = 1; i <= columnCount; i++) {
//                    String tableFieldName = resultSetMetaData.getColumnLabel(i);
//                    tableMap.put(tableFieldName, resultSet.getObject(tableFieldName));
//                }
                // 获取表所属的schema
                String tableSchema = resultSet.getString("TABLE_SCHEM");
                // 表名
                String tableName = resultSet.getString("TABLE_NAME");
                // 注释
                String tableComment = resultSet.getString("REMARKS");
                tableMap.put("tableSchema",tableSchema);
                tableMap.put("table",tableName);
                tableMap.put("remarks",tableComment);
                tableList.add(tableMap);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableList;
    }

    /**
     * 查询数据库数据的字段名及类型
     * @param databaseName
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> queryTableFields(String databaseName, String tableName) {
        List<Map<String, Object>> resultData;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            List<String> sqlList = StrSplitter.split(tableName, ".", 2, true, true);
            String schemaPattern = null;
            if (sqlList.size() == 2) {
                schemaPattern = sqlList.get(0);
                tableName = sqlList.get(1);
            }
            ResultSet resultSet = metaData.getColumns(databaseName, schemaPattern, tableName, null);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            resultData = new ArrayList<>();
            while (resultSet.next()) {
                HashMap<String, Object> dataTypeMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String tableFieldName = resultSetMetaData.getColumnLabel(i);
                    dataTypeMap.put(tableFieldName, resultSet.getObject(tableFieldName));
                }
//                // 字段名
//                String columnName = resultSet.getString("COLUMN_NAME");
//                // 字段类型
//                String dataType = resultSet.getString("TYPE_NAME");
//                // 字段大小
//                int columnSize = resultSet.getInt("COLUMN_SIZE");
//                // 字段注释
//                String columnComment = resultSet.getString("REMARKS");
//                String nullable = resultSet.getString("NULLABLE");
//                dataTypeMap.put("fieldName", columnName);
//                dataTypeMap.put("typeName", dataType);
//                dataTypeMap.put("columnSize", columnSize);
//                dataTypeMap.put("remarks", columnComment);
//                dataTypeMap.put("nullable", nullable);
                resultData.add(dataTypeMap);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultData;
    }

    /**
     * 获取表结构
     *
     * @param tableName 表名
     */
    public List<Map<String, Object>> queryTableStructure(String tableName) {
        List<Map<String, Object>> resultData = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
            // 5. 处理结果集，提取表结构信息
            while (resultSet.next()) {
                Map<String, Object> fieldMap = new HashMap<>();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                // 获取字段数量
                int columnCount = resultSetMetaData.getColumnCount();
                // 遍历每个字段
                for (int i = 1; i <= columnCount; i++) {
                    // 获取字段名
                    String columnName = resultSetMetaData.getColumnName(i);
                    // 获取字段值
                    Object columnValue = resultSet.getObject(i);
                    fieldMap.put(columnName, columnValue);
                }
                resultData.add(fieldMap);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultData;
    }

}
