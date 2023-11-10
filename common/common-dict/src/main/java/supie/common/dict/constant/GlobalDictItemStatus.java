package supie.common.dict.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局字典项目数据状态。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class GlobalDictItemStatus {

    /**
     * 正常。
     */
    public static final int NORMAL = 0;
    /**
     * 禁用。
     */
    public static final int DISABLED = 1;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(4);
    static {
        DICT_MAP.put(NORMAL, "正常");
        DICT_MAP.put(DISABLED, "禁用");
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
    private GlobalDictItemStatus() {
    }
}
