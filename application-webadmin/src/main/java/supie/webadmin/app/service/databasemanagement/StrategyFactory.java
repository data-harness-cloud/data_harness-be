package supie.webadmin.app.service.databasemanagement;

import jodd.util.StringUtil;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/6 9:35
 * @path SDT-supie.webadmin.app.service.databasemanagement-StrategyFactory
 */
@Component
public class StrategyFactory {

    private final Map<DataBaseTypeEnum, Strategy> strategyMap = new EnumMap<>(DataBaseTypeEnum.class);

    /**
     * 根据存储类型获取上传下载对象。
     * @param strategyType 存储类型。
     * @return 匹配的上传下载对象。
     */
    public Strategy getStrategy(String strategyType, String hostIp, String hostPort, String databaseName, String userName, String password,int type) {
        if (StringUtil.isBlank(strategyType)) throw new NullPointerException("数据库类型不能为空！");
        if (StringUtil.isBlank(hostIp)) throw new NullPointerException("主机地址不能为空！");
        if (StringUtil.isBlank(hostPort)) throw new NullPointerException("连接端口不能为空！");
        DataBaseTypeEnum dataBaseTypeEnum = null;
        for (DataBaseTypeEnum dataBaseTypeEnum1 : DataBaseTypeEnum.values()) {
            if (dataBaseTypeEnum1.getName().equals(strategyType.toUpperCase())) {
                dataBaseTypeEnum = dataBaseTypeEnum1;
                break;
            }
        }
        if (dataBaseTypeEnum == null) {
            throw new UnsupportedOperationException(
                    "The dataBaseType [" + strategyType + "] isn't supported.");
        }
        Strategy strategy = strategyMap.get(dataBaseTypeEnum);
        if (strategy == null) {
            throw new UnsupportedOperationException(
                    "The storeType [" + strategyType + "] isn't supported, please add dependency jar first.");
        }
        strategy.initStrategy(hostIp, hostPort, databaseName, userName, password,type);
        return strategy;
    }

    /**
     * 注册上传下载对象到工厂。
     *
     * @param dataBaseTypeEnum  存储类型。
     * @param strategy      上传下载对象。
     */
    public void registerStrategy(DataBaseTypeEnum dataBaseTypeEnum, Strategy strategy) {
        if (dataBaseTypeEnum == null || strategy == null) {
            throw new IllegalArgumentException("The Argument can't be NULL.");
        }
        if (strategyMap.containsKey(dataBaseTypeEnum)) {
            throw new UnsupportedOperationException(
                    "The storeType [" + dataBaseTypeEnum.name() + "] has been registered already.");
        }
        strategyMap.put(dataBaseTypeEnum, strategy);
    }

}
