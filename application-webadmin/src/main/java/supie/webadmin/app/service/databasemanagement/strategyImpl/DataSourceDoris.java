package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.DataBaseTypeEnum;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/6 16:46
 * @path SDT-supie.webadmin.app.service.databasemanagement.strategyImpl-DataSourceDoris
 */
@Slf4j
@Component
public class DataSourceDoris extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_DORIS, this);
    }

    /**
     * 初始化连接信息，并连接数据库
     *
     * @param hostIp       主机 IP
     * @param hostPort     主机端口
     * @param databaseName 数据库名称
     * @param userName     用户名
     * @param password     密码
     * @author 王立宏
     * @date 2023/11/06 04:51
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password,int type) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_DORIS;
        this.jdbcDriver = "com.mysql.cj.jdbc.Driver";
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:mysql://" + hostIp + ":" + hostPort;
        } else {
            this.jdbcUrl = "jdbc:mysql://" + hostIp + ":" + hostPort + "/" + databaseName;
        }
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        // 获取数据库连接，使数据库连接在该对象存在前都保持住
        initConnection();
    }

    /**
     * 查询数据库数据的字段名及类型
     *
     * @param databaseName
     * @param tableName
     * @return
     */
    @Override
    public List<Map<String, Object>> queryTableFields(String databaseName, String tableName) {
        List<Map<String, Object>> resultData;
        try {
            // 查询表结构
            Statement statement = connection.createStatement();
            String query = "SHOW COLUMNS FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            resultData = new ArrayList<>();
            while (resultSet.next()) {
                // 获取到字段名称 (Field、Type、Null、Key、Default)
                HashMap<String, Object> dataTypeMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String tableFieldName = metaData.getColumnLabel(i);
                    dataTypeMap.put(tableFieldName, resultSet.getObject(tableFieldName));
                }
//                // 字段名
//                String columnName = resultSet.getString("Field");
//                // 字段类型
//                String dataType = resultSet.getString("Type");
//                // 字段大小
//                int columnSize = resultSet.getInt("COLUMN_SIZE");
//                // 字段注释
//                String columnComment = resultSet.getString("Default");
//                String nullable = resultSet.getString("Null");
//                dataTypeMap.put("fieldName", columnName);
//                dataTypeMap.put("typeName", dataType);
//                dataTypeMap.put("columnSize", columnSize);
//                dataTypeMap.put("remarks", columnComment);
//                dataTypeMap.put("nullable", nullable);
                resultData.add(dataTypeMap);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultData;
    }
}
