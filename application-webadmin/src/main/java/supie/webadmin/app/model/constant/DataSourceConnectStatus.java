package supie.webadmin.app.model.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源连通性常量字典常量字典对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class DataSourceConnectStatus {

    /**
     * 已连通。
     */
    public static final int CONNECTED = 1;
    /**
     * 未连通。
     */
    public static final int NOT_CONNECTED = -1;
    /**
     * 未测试。
     */
    public static final int NOT_TEST = 0;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);
    static {
        DICT_MAP.put(CONNECTED, "已连通");
        DICT_MAP.put(NOT_CONNECTED, "未连通");
        DICT_MAP.put(NOT_TEST, "未测试");
    }

    /**
     * 判断参数是否为当前常量字典的合法值。
     *
     * @param value 待验证的参数值。
     * @return 合法返回true，否则false。
     */
    public static boolean isValid(Integer value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private DataSourceConnectStatus() {
    }
}
