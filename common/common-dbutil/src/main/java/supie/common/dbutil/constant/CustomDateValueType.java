package supie.common.dbutil.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义日期过滤值类型。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class CustomDateValueType {
    /**
     * 本日。
     */
    public static final String CURRENT_DAY = "1";
    /**
     * 本周。
     */
    public static final String CURRENT_WEEK = "2";
    /**
     * 本月。
     */
    public static final String CURRENT_MONTH = "3";
    /**
     * 本季度。
     */
    public static final String CURRENT_QUARTER = "4";
    /**
     * 今年。
     */
    public static final String CURRENT_YEAR = "5";
    /**
     * 昨天。
     */
    public static final String LAST_DAY = "11";
    /**
     * 上周。
     */
    public static final String LAST_WEEK = "12";
    /**
     * 上月。
     */
    public static final String LAST_MONTH = "13";
    /**
     * 上季度。
     */
    public static final String LAST_QUARTER = "14";
    /**
     * 去年。
     */
    public static final String LAST_YEAR = "15";

    private static final Map<Object, String> DICT_MAP = new HashMap<>(2);
    static {
        DICT_MAP.put(CURRENT_DAY, "本日");
        DICT_MAP.put(CURRENT_WEEK, "本周");
        DICT_MAP.put(CURRENT_MONTH, "本月");
        DICT_MAP.put(CURRENT_QUARTER, "本季度");
        DICT_MAP.put(CURRENT_YEAR, "今年");
        DICT_MAP.put(LAST_DAY, "昨日");
        DICT_MAP.put(LAST_WEEK, "上周");
        DICT_MAP.put(LAST_MONTH, "上月");
        DICT_MAP.put(LAST_QUARTER, "上季度");
        DICT_MAP.put(LAST_YEAR, "去年");
    }

    /**
     * 判断参数是否为当前常量字典的合法值。
     *
     * @param value 待验证的参数值。
     * @return 合法返回true，否则false。
     */
    public static boolean isValid(String value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private CustomDateValueType() {
    }
}
