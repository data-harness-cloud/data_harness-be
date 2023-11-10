package supie.common.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段过滤参数类型常量字典对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class FilterParamType {

    /**
     * 整数数值型。
     */
    public static final int LONG = 0;
    /**
     * 浮点型。
     */
    public static final int FLOAT = 1;
    /**
     * 字符型。
     */
    public static final int STRING = 2;
    /**
     * 日期型。
     */
    public static final int DATE = 3;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(9);
    static {
        DICT_MAP.put(LONG, "整数数值型");
        DICT_MAP.put(FLOAT, "浮点型");
        DICT_MAP.put(STRING, "字符型");
        DICT_MAP.put(DATE, "日期型");
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
    private FilterParamType() {
    }
}
