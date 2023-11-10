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
public class DataSourceSapHana extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_SAP_HANA, this);
    }

    /**
     * 初始化连接信息
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_SAP_HANA;
        this.jdbcDriver = "com.sap.db.jdbc.Driver";
        this.jdbcUrl = "jdbc:sap://" + hostIp + ":" + hostPort + ";DatabaseName=" + databaseName;
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

//    public static String initSapHana(String jdbcUrl, String userName, String passWord, boolean autoCommit) {
//        Connection conn = null;
//        try {
//            Class.forName("com.sap.db.jdbc.Driver");
//            //连接数据库
//            conn = DriverManager.getConnection(jdbcUrl, userName, passWord);
//            conn.setAutoCommit(autoCommit);
//            return "SAP HANA连接成功!";
//        } catch (ClassNotFoundException e) {
//            System.out.println("找不到驱动程序类");
//            return "找不到驱动程序类!";
//        } catch (SQLException se) {
//            //连接失败
//            logger.error("SAP HANA连接失败", se);
//            return "SAP HANA连接失败" + se;
//        }
//    }
}
