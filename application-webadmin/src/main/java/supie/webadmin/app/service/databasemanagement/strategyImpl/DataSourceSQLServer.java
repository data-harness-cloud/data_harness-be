package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class DataSourceSQLServer extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_SQLSERVER, this);
    }

    /**
     * 初始化连接信息
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password,int type) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_SQLSERVER;
        this.jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:sqlserver://" + hostIp + ":" + hostPort;
        } else {
            this.jdbcUrl = "jdbc:sqlserver://" + hostIp + ":" + hostPort + ";databaseName=" + databaseName;
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
     * 查询可操作的所有数据库名称
     *
     * @return 该账户可操作的所有数据库集
     * @author 王立宏
     * @date 2023/11/23 03:29
     */
    @Override
    public List<String> queryAllDatabaseName() {
        this.queryAllDatabaseNameSql = "SELECT name FROM sys.databases;";
        return super.queryAllDatabaseName();
//        List<String> databaseNameList = new ArrayList<>();
//        try {
//            DatabaseMetaData metaData = connection.getMetaData();
//            ResultSet resultSet = metaData.getCatalogs();
//            while (resultSet.next()) {
//                databaseNameList.add(resultSet.getString("TABLE_CAT"));
//            }
//            resultSet.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return databaseNameList;
    }
}
