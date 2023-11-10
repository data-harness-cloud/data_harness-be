package supie.common.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段过滤类型常量字典对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class FieldFilterType {
    /**
     * 等于过滤。
     */
    public static final int EQUAL = 0;
    /**
     * 不等于过滤。
     */
    public static final int NOT_EQUAL = 1;
    /**
     * 大于等于。
     */
    public static final int GE = 2;
    /**
     * 大于。
     */
    public static final int GT = 3;
    /**
     * 小于等于。
     */
    public static final int LE = 4;
    /**
     * 小于。
     */
    public static final int LT = 5;
    /**
     * 模糊查询。
     */
    public static final int LIKE = 6;
    /**
     * IN列表过滤。
     */
    public static final int IN = 7;
    /**
     * NOT IN列表过滤。
     */
    public static final int NOT_IN = 8;
    /**
     * 范围过滤。
     */
    public static final int BETWEEN = 9;
    /**
     * 不为空。
     */
    public static final int IS_NOT_NULL = 100;
    /**
     * 为空。
     */
    public static final int IS_NULL = 101;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(9);
    static {
        DICT_MAP.put(EQUAL, " = ");
        DICT_MAP.put(NOT_EQUAL, " <> ");
        DICT_MAP.put(GE, " >= ");
        DICT_MAP.put(GT, " > ");
        DICT_MAP.put(LE, " <= ");
        DICT_MAP.put(LT, " < ");
        DICT_MAP.put(LIKE, " LIKE ");
        DICT_MAP.put(IN, " IN ");
        DICT_MAP.put(NOT_IN, " NOT IN ");
        DICT_MAP.put(BETWEEN, " BETWEEN ");
        DICT_MAP.put(IS_NOT_NULL, " IS NOT NULL ");
        DICT_MAP.put(IS_NULL, " IS NULL ");
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
     * 获取显示名。
     * @param value 常量值。
     * @return 常量值对应的显示名。
     */
    public static String getName(Integer value) {
        return DICT_MAP.get(value);
    }
    
    /**
     * 不支持日期型字段的过滤类型。
     * 
     * @param filterType 过滤类型。
     * @return 不支持返回true，否则false。
     */
    public static boolean unsupportDateFilterType(int filterType) {
        return filterType == FieldFilterType.IN
                || filterType == FieldFilterType.NOT_IN
                || filterType == FieldFilterType.NOT_EQUAL
                || filterType == FieldFilterType.LIKE;
    }

    /**
     * 支持多过滤值的过滤类型。
     *
     * @param filterType 过滤类型。
     * @return 支持返回true，否则false。
     */
    public static boolean supportMultiValueFilterType(int filterType) {
        return filterType == FieldFilterType.IN
                || filterType == FieldFilterType.NOT_IN
                || filterType == FieldFilterType.BETWEEN;
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private FieldFilterType() {
    }
}
