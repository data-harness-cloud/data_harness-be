package supie.webadmin.app.service.databasemanagement.strategyImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.DataBaseTypeEnum;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;
import supie.webadmin.app.service.databasemanagement.model.DatabaseManagement;

import javax.annotation.PostConstruct;
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
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_DORIS;
        this.jdbcDriver = "com.mysql.cj.jdbc.Driver";
        this.jdbcUrl = "jdbc:mysql://" + hostIp + ":" + hostPort + "/" + databaseName;
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        // 获取数据库连接，使数据库连接在该对象存在前都保持住
        initConnection();
    }

    /**
     * 查询数据库数据表名及类型
     *
     * @param databaseManagement
     */
    @Override
    public List<Map<String, Object>> queryDatabaseTable(DatabaseManagement databaseManagement) throws Exception {
        return null;
    }

    /**
     * 查询数据库数据的字段名及类型
     *
     * @param databaseManagement
     */
    @Override
    public List<Map<String, Object>> queryTableFields(DatabaseManagement databaseManagement) throws Exception {
        return null;
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
        String createDatabaseSql = "CREATE DATABASE " + databaseName + ";";
        List<Map<String, Object>> resultDataList = executeSqlList(createDatabaseSql);
        Map<String, Object> resultMap = resultDataList.get(0);
        if (Boolean.TRUE.equals(resultMap.get("isSuccess")) && ((int) resultMap.get("updateResultData") == 1)) return;
        throw new RuntimeException("数据库创建[" + resultMap.get("sql") + "]失败！" + resultMap.get("message").toString());
    }

    /**
     * 获取表结构
     *
     * @param tableName 表名
     */
    @Override
    public List<Map<String, Object>> queryTableStructure(String tableName) {
        return null;
    }
}
