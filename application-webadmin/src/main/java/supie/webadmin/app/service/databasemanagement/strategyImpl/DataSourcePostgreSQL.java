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
public class DataSourcePostgreSQL extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_PG, this);
    }

    /**
     * 初始化连接信息
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password,int type) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_PG;
        this.jdbcDriver = "org.postgresql.Driver";
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:postgresql://" + hostIp + ":" + hostPort;
        } else {
            this.jdbcUrl = "jdbc:postgresql://" + hostIp + ":" + hostPort + "/" + databaseName;
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
        queryAllDatabaseNameSql = "SELECT datname FROM pg_database WHERE datistemplate = false;";
        return super.queryAllDatabaseName();
    }

}
