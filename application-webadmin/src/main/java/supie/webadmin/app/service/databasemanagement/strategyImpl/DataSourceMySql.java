package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.DataBaseTypeEnum;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

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
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:mysql://" + hostIp + ":" + hostPort + "/?useUnicode=true&characterEncoding=utf-8";
        } else {
            this.jdbcUrl = "jdbc:mysql://" + hostIp + ":" + hostPort + "/" + databaseName + "?useUnicode=true&characterEncoding=utf-8";
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


