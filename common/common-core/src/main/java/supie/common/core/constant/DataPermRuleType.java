package supie.common.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据权限规则类型常量类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class DataPermRuleType {

    /**
     * 查看全部。
     */
    public static final int TYPE_ALL = 0;

    /**
     * 仅查看当前用户。
     */
    public static final int TYPE_USER_ONLY = 1;

    /**
     * 仅查看当前部门。
     */
    public static final int TYPE_DEPT_ONLY = 2;

    /**
     * 所在部门及子部门。
     */
    public static final int TYPE_DEPT_AND_CHILD_DEPT = 3;

    /**
     * 多部门及子部门。
     */
    public static final int TYPE_MULTI_DEPT_AND_CHILD_DEPT = 4;

    /**
     * 自定义部门列表。
     */
    public static final int TYPE_CUSTOM_DEPT_LIST = 5;

    /**
     * 本部门所有用户。
     */
    public static final int TYPE_DEPT_USERS = 6;

    /**
     * 本部门及子部门所有用户。
     */
    public static final int TYPE_DEPT_AND_CHILD_DEPT_USERS = 7;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(6);
    static {
        DICT_MAP.put(TYPE_ALL, "查看全部");
        DICT_MAP.put(TYPE_USER_ONLY, "仅查看当前用户");
        DICT_MAP.put(TYPE_DEPT_ONLY, "仅查看所在部门");
        DICT_MAP.put(TYPE_DEPT_AND_CHILD_DEPT, "所在部门及子部门");
        DICT_MAP.put(TYPE_MULTI_DEPT_AND_CHILD_DEPT, "多部门及子部门");
        DICT_MAP.put(TYPE_CUSTOM_DEPT_LIST, "自定义部门列表");
        DICT_MAP.put(TYPE_DEPT_USERS, "本部门所有用户");
        DICT_MAP.put(TYPE_DEPT_AND_CHILD_DEPT_USERS, "本部门及子部门所有用户");
    }

    /**
     * 判断参数是否为当前常量字典的合法取值范围。
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
    private DataPermRuleType() {
    }
}
