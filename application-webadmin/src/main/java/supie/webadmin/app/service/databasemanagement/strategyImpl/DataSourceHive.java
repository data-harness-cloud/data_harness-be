package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Hive的数据操作类
 * @author zhiwuzhu
 */
@Slf4j
@Component
public class DataSourceHive extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_HIVE, this);
    }

    /**
     * 初始化连接信息
     */
    @Override
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password,int type) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_HIVE;
        this.jdbcDriver = "org.apache.hive.jdbc.HiveDriver";
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:hive2://" + hostIp + ":" + hostPort;
        } else {
            this.jdbcUrl = "jdbc:hive2://" + hostIp + ":" + hostPort + "/" + databaseName;
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
