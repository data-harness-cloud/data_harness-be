package supie.webadmin.app.service.databasemanagement.strategyImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.service.databasemanagement.DataBaseTypeEnum;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;

import javax.annotation.PostConstruct;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/30 14:57
 * @path SDT-supie.webadmin.app.service.databasemanagement.strategyImpl-DataSourceClickHouse
 */
@Slf4j
@Component
public class DataSourceClickHouse extends BaseDataSource implements Strategy {

    @Autowired
    private StrategyFactory strategyFactory;

    @PostConstruct
    public void doRegister() {
        strategyFactory.registerStrategy(DataBaseTypeEnum.DATASOURCE_CLICK_HOUSE, this);
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
    public void initStrategy(String hostIp, String hostPort, String databaseName, String userName, String password,int type) {
        this.databaseType = DataBaseTypeEnum.DATASOURCE_HIVE;
        this.jdbcDriver = "com.clickhouse.jdbc.ClickHouseDriver";
        if (StrUtil.isBlank(databaseName)) {
            this.jdbcUrl = "jdbc:clickhouse://" + hostIp + ":" + hostPort;
        } else {
            this.jdbcUrl = "jdbc:clickhouse://" + hostIp + ":" + hostPort + "/" + databaseName;
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
