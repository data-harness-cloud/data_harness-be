package supie.common.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 基于Druid的数据源配置的基类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class BaseMultiDataSourceConfig {

    private String driverClassName;
    private String name;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Integer maxWait;
    private Integer timeBetweenEvictionRunsMillis;
    private Integer minEvictableIdleTimeMillis;
    private Boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private Integer maxOpenPreparedStatements;
    private String validationQuery;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;

    /**
     * 将连接池的通用配置应用到数据源对象上。
     *
     * @param druidDataSource Druid的数据源。
     * @return 应用后的Druid数据源。
     */
    protected DruidDataSource applyCommonProps(DruidDataSource druidDataSource) {
        druidDataSource.setConnectionErrorRetryAttempts(5);
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setName(name);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        druidDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        return druidDataSource;
    }
}
