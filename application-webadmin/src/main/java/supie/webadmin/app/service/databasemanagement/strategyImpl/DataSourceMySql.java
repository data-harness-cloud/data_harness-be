package supie.webadmin.app.service.databasemanagement.strategyImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.*;
import supie.webadmin.app.service.databasemanagement.model.DatabaseManagement;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * MySql
 * @author zhiwuzhu
 */
@Slf4j
@Component
public class DataSourceMySql extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_MYSQL, this);
    }

    /**
     * 初始化连接信息
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_MYSQL;
        this.jdbcDriver = "com.mysql.cj.jdbc.Driver";
        this.jdbcUrl = "jdbc:mysql://" + hostIp + ":" + hostPort + "/" + databaseName + "?useUnicode=true&characterEncoding=utf-8";
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        // 获取数据库连接，使数据库连接在该对象存在前都保持住
        initConnection();
    }

    @Override
    public List<Map<String, Object>> queryDatabaseTable(DatabaseManagement databaseManagement) throws Exception {
        String  jdbcUrl = "jdbc:mysql://" + databaseManagement.getIp() + ":" + databaseManagement.getPort() + "/" + databaseManagement.getDatabaseName() + "?useUnicode=true&characterEncoding=utf-8";
        List<Map<String, Object>> map;
        map = QueryTheData.queryDatabaseTable("com.mysql.cj.jdbc.Driver",jdbcUrl, databaseManagement.getUsername(), databaseManagement.getPassword(),databaseManagement.getDatabaseName(),false);
        return map;
    }

    @Override
    public List<Map<String, Object>> queryTableFields(DatabaseManagement databaseManagement) throws Exception {
        String  jdbcUrl = "jdbc:mysql://" + databaseManagement.getIp() + ":" + databaseManagement.getPort() + "/" + databaseManagement.getDatabaseName() + "?useUnicode=true&characterEncoding=utf-8";
        List<Map<String, Object>> map;
        map = QueryTheFields.queryTableFields("com.mysql.cj.jdbc.Driver",jdbcUrl, databaseManagement.getUsername(), databaseManagement.getPassword(),false,databaseManagement.getTableName(),databaseManagement.getDatabaseName());
        return map;
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
        String createDatabaseSql = "CREATE DATABASE " + databaseName + " DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;";
        List<Map<String, Object>> resultDataList = executeSqlList(createDatabaseSql);
        Map<String, Object> resultMap = resultDataList.get(0);
        if (Boolean.TRUE.equals(resultMap.get("isSuccess")) && ((int) resultMap.get("updateResultData") == 1)) return;
        throw new RuntimeException("数据库创建[" + resultMap.get("sql") + "]失败！" + resultMap.get("message").toString());
    }

    /**
     * TODO 获取表结构
     *
     * @param tableName 表名
     */
    @Override
    public List<Map<String, Object>> queryTableStructure(String tableName) {
        return null;
    }
}


