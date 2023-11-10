package supie.common.core.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.*;

/**
 * 动态数据源对象。当存在多个数据连接时使用。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Autowired
    private BaseMultiDataSourceConfig baseMultiDataSourceConfig;
    @Autowired
    private CoreProperties properties;

    private Set<Integer> dynamicDatasourceTypeSet = new HashSet<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }

    /**
     * 重新加载动态添加的数据源。既清空之前动态添加的数据源，同时添加参数中的新数据源列表。
     *
     * @param dataSourceInfoList 新动态数据源列表。
     */
    public synchronized void reloadAll(List<DataSourceInfo> dataSourceInfoList) {
        Map<Object, Object> dataSourceMap = new HashMap<>(this.getResolvedDataSources());
        dynamicDatasourceTypeSet.forEach(dataSourceMap::remove);
        dynamicDatasourceTypeSet.clear();
        for (DataSourceInfo dataSourceInfo : dataSourceInfoList) {
            dynamicDatasourceTypeSet.add(dataSourceInfo.getDatasourceType());
            DruidDataSource dataSource = this.doConvert(dataSourceInfo);
            baseMultiDataSourceConfig.applyCommonProps(dataSource);
            dataSourceMap.put(dataSourceInfo.getDatasourceType(), dataSource);
        }
        Object defaultTargetDatasource = this.getResolvedDefaultDataSource();
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(defaultTargetDatasource);
        super.afterPropertiesSet();
    }

    /**
     * 添加动态添加数据源。
     *
     * 动态添加数据源。
     */
    public synchronized void addDataSource(DataSourceInfo dataSourceInfo) {
        if (dynamicDatasourceTypeSet.contains(dataSourceInfo.getDatasourceType())) {
            return;
        }
        dynamicDatasourceTypeSet.add(dataSourceInfo.getDatasourceType());
        Map<Object, Object> dataSourceMap = new HashMap<>(this.getResolvedDataSources());
        DruidDataSource dataSource = this.doConvert(dataSourceInfo);
        baseMultiDataSourceConfig.applyCommonProps(dataSource);
        dataSourceMap.put(dataSourceInfo.getDatasourceType(), dataSource);
        Object defaultTargetDatasource = this.getResolvedDefaultDataSource();
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(defaultTargetDatasource);
        super.afterPropertiesSet();
    }

    /**
     * 添加动态添加数据源列表。
     *
     * @param dataSourceInfoList 数据源信息列表。
     */
    public synchronized void addDataSources(List<DataSourceInfo> dataSourceInfoList) {
        Map<Object, Object> dataSourceMap = new HashMap<>(this.getResolvedDataSources());
        for (DataSourceInfo dataSourceInfo : dataSourceInfoList) {
            if (!dynamicDatasourceTypeSet.contains(dataSourceInfo.getDatasourceType())) {
                dynamicDatasourceTypeSet.add(dataSourceInfo.getDatasourceType());
                DruidDataSource dataSource = this.doConvert(dataSourceInfo);
                baseMultiDataSourceConfig.applyCommonProps(dataSource);
                dataSourceMap.put(dataSourceInfo.getDatasourceType(), dataSource);
            }
        }
        Object defaultTargetDatasource = this.getResolvedDefaultDataSource();
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(defaultTargetDatasource);
        super.afterPropertiesSet();
    }

    /**
     * 动态移除数据源。
     *
     * @param datasourceType 数据源类型。
     */
    public synchronized void removeDataSource(int datasourceType) {
        if (!dynamicDatasourceTypeSet.remove(datasourceType)) {
            return;
        }
        Map<Object, Object> dataSourceMap = new HashMap<>(this.getResolvedDataSources());
        dataSourceMap.remove(datasourceType);
        Object defaultTargetDatasource = this.getResolvedDefaultDataSource();
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(defaultTargetDatasource);
        super.afterPropertiesSet();
    }

    private DruidDataSource doConvert(DataSourceInfo dataSourceInfo) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setUsername(dataSourceInfo.getUsername());
        dataSource.setPassword(dataSourceInfo.getPassword());
        StringBuilder urlBuilder = new StringBuilder(256);
        String hostAndPort = dataSourceInfo.getDatabaseHost() + ":" + dataSourceInfo.getPort();
        if (properties.isMySql()) {
            urlBuilder.append("jdbc:mysql://")
                    .append(hostAndPort)
                    .append("/")
                    .append(dataSourceInfo.getDatabaseName())
                    .append("?characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai");
        } else if (properties.isOracle()) {
            urlBuilder.append("jdbc:oracle:thin:@")
                    .append(hostAndPort)
                    .append(":")
                    .append(dataSourceInfo.getDatabaseName());
        } else if (properties.isPostgresql()) {
            urlBuilder.append("jdbc:postgresql://")
                    .append(hostAndPort)
                    .append("/")
                    .append(dataSourceInfo.getDatabaseName());
            if (StrUtil.isBlank(dataSourceInfo.getSchemaName())) {
                urlBuilder.append("?currentSchema=public");
            } else {
                urlBuilder.append("?currentSchema=").append(dataSourceInfo.getSchemaName());
            }
            urlBuilder.append("&TimeZone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8");
        } else if (properties.isDm()) {
            urlBuilder.append("jdbc:dm://")
                    .append(hostAndPort)
                    .append("?schema=")
                    .append(dataSourceInfo.getDatabaseName())
                    .append("&useJDBCCompliantTimezoneShift=true&serverTimezone=Asia/Shanghai&useSSL=true&characterEncoding=UTF-8");
        } else if (properties.isKingbase()) {
            urlBuilder.append("jdbc:kingbase8://")
                    .append(hostAndPort)
                    .append("/")
                    .append(dataSourceInfo.getDatabaseName())
                    .append("?useJDBCCompliantTimezoneShift=true&serverTimezone=Asia/Shanghai&useSSL=true&characterEncoding=UTF-8");
        }
        dataSource.setUrl(urlBuilder.toString());
        return dataSource;
    }
}
