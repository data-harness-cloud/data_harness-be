package supie.webadmin.app.service.databasemanagement.strategyImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.*;
import supie.webadmin.app.service.databasemanagement.model.DatabaseManagement;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DataSourceSyBase extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_SY_BASE, this);
    }

    /**
     * 初始化连接信息
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_SY_BASE;
        this.jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.jdbcUrl = "jdbc:sybase://" + hostIp + ":" + hostPort + "/" + databaseName + "?useUnicode=true&characterEncoding=utf-8";
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        // 获取数据库连接，使数据库连接在该对象存在前都保持住
        initConnection();
    }

    @Override
    public List<Map<String,Object>> queryDatabaseTable(DatabaseManagement databaseManagement) throws Exception {
        return null;
    }

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
