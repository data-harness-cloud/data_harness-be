package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.*;

import javax.annotation.PostConstruct;

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
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_SQLSERVER;
        this.jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:sqlserver://" + hostIp + ":" + hostPort;
        } else {
            this.jdbcUrl = "jdbc:sybase://" + hostIp + ":" + hostPort + ";databaseName=" + databaseName;
        }
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        // 获取数据库连接，使数据库连接在该对象存在前都保持住
        initConnection();
    }

}
