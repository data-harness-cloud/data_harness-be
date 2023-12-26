package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.*;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.*;

/**
 * Oracle
 * @author zhiwuzhu
 */
@Slf4j
@Component
public class DataSourceOracle extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_ORACLE, this);
    }

    /**
     * 初始化连接信息
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password,int type) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_ORACLE;
        this.jdbcDriver = "oracle.jdbc.driver.OracleDriver";
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:oracle:thin:@" + hostIp + ":" + hostPort;
        } else {
            this.jdbcUrl = "jdbc:oracle:thin:@" + hostIp + ":" + hostPort + ":" + databaseName;
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
     * 创建数据库
     *
     * @param databaseName 创建的数据库的名称
     * @author 王立宏
     * @date 2023/11/02 04:30
     */
    @Override
    public void createDatabase(String databaseName) {
//        super.createDatabase(databaseName);
        /*
        BEGIN
          DBMS_SERVICE.create_service(
            service_name => 'my_new_service',
            network_name => 'my_new_service'
          );
        END;
         */
        throw new RuntimeException("无法在Oracle数据库中创建[" + databaseName + "]数据库，请手动Oracle中添加[" + databaseName + "]数据库");
    }

    /**
     * 查询可操作的所有数据库名称
     *
     * @return 该账户可操作的所有数据库集
     * @author 王立宏
     * @date 2023/11/23 03:29
     */
    @Override
    public List<String> queryAllDatabaseName() {
        this.queryAllDatabaseNameSql = "SELECT NAME FROM V$DATABASE";
//        this.queryAllDatabaseNameSql = "SELECT USERNAME FROM ALL_USERS";
        return super.queryAllDatabaseName();
    }

    /**
     * 查询数据库数据表名及类型
     *
     * @param databaseName
     * @return
     */
    @Override
    public List<Map<String, Object>> queryDatabaseTable(String databaseName) {
        ArrayList<Map<String, Object>> tableList = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取所有表的元数据信息
            ResultSet resultSet = metaData.getTables(null, this.userName.toUpperCase(), "%", new String[]{"TABLE"});
            // 循环TABLE，将结果记录在Map中
            while (resultSet.next()) {
                HashMap<String, Object> tableMap = new HashMap<>();
                // 获取表所属的schema
                String tableSchema = resultSet.getString("TABLE_SCHEM");
                // 表名
                String tableName = resultSet.getString("TABLE_NAME");
                // 表类型
                String tableType = resultSet.getString("TABLE_TYPE");
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

}
