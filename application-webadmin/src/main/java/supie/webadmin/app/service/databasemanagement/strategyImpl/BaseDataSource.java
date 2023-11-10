package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import supie.webadmin.app.service.databasemanagement.DataBaseTypeEnum;

import java.sql.*;
import java.util.*;

import static cn.hutool.db.DbUtil.close;
import static supie.application.common.constant.DataSource.*;

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
    protected Statement statement = null;
    protected PreparedStatement preparedStatement = null;
    protected ResultSet resultSet = null;

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

    /**
     * 关闭所有连接
     * @author 王立宏
     * @date 2023/10/30 10:58
     */
    public void closeAll() {
        close(resultSet, preparedStatement, statement, connection);
    }

    /**
     * 执行位置数量的SQL
     * @param sql 字符（会将SQl以“;”切开成List来执行，结果也会按照语句的顺序来显示）
     * @return [
     *   {
     *     "isSuccess": true,
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
            statement = connection.createStatement();
            for (String sqlOne : sqlList) {
                Map<String, Object> resultMapData = new HashMap<>();
                resultMapData.put("sql", sqlOne);
                try {
                    boolean result = statement.execute(sqlOne);
                    if (result) {
                        resultSet = statement.getResultSet(); // 查询结果
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
                                // 获取字段值
                                Object columnValue = resultSet.getObject(i);
                                // 存入字段名和字段值
                                if (StrUtil.isBlankIfStr(columnName)) continue;
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
                    resultMapData.put("isSuccess", true);
                    resultMapData.put("message", "SUCCESS");
                } catch (SQLException e) {
                    resultMapData.put("isSuccess", false);
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

}
