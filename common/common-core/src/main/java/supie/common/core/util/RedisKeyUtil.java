package supie.common.core.util;

import supie.common.core.object.TokenData;

/**
 * Redis 键生成工具类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class RedisKeyUtil {

    private static final String SESSIONID_PREFIX = "SESSIONID:";

    /**
     * 获取通用的session缓存的键前缀。
     *
     * @return session缓存的键前缀。
     */
    public static String getSessionIdPrefix() {
        TokenData tokenData = TokenData.takeFromRequest();
        if (tokenData.getTenantId() == null) {
            return SESSIONID_PREFIX;
        }
        return SESSIONID_PREFIX + tokenData.getTenantId() + "_";
    }

    /**
     * 获取指定用户Id的session缓存的键前缀。
     *
     * @param loginName 指定的用户登录名。
     * @return session缓存的键前缀。
     */
    public static String getSessionIdPrefix(String loginName) {
        TokenData tokenData = TokenData.takeFromRequest();
        if (tokenData.getTenantId() == null) {
            return SESSIONID_PREFIX + loginName + "_";
        }
        return SESSIONID_PREFIX + tokenData.getTenantId() + "_" + loginName + "_";
    }

    /**
     * 获取指定用户Id和登录设备类型的session缓存的键前缀。
     *
     * @param loginName  指定的用户登录名。
     * @param deviceType 设备类型。
     * @return session缓存的键前缀。
     */
    public static String getSessionIdPrefix(String loginName, int deviceType) {
        TokenData tokenData = TokenData.takeFromRequest();
        if (tokenData.getTenantId() == null) {
            return SESSIONID_PREFIX + loginName + "_" + deviceType + "_";
        }
        return SESSIONID_PREFIX + tokenData.getTenantId() + "_" + loginName + "_" + deviceType + "_";
    }

    /**
     * 计算SessionId返回存储于Redis中的键。
     *
     * @param sessionId 会话Id。
     * @return 会话存储于Redis中的键值。
     */
    public static String makeSessionIdKey(String sessionId) {
        return SESSIONID_PREFIX + sessionId;
    }

    /**
     * 计算SessionId关联的权限数据存储于Redis中的键。
     *
     * @param sessionId 会话Id。
     * @return 会话关联的权限数据存储于Redis中的键值。
     */
    public static String makeSessionPermIdKey(String sessionId) {
        return "PERM:" + sessionId;
    }

    /**
     * 计算SessionId关联的数据权限数据存储于Redis中的键。
     *
     * @param sessionId 会话Id。
     * @return 会话关联的数据权限数据存储于Redis中的键值。
     */
    public static String makeSessionDataPermIdKey(String sessionId) {
        return "DATA_PERM:" + sessionId;
    }

    /**
     * 计算包含全局字典及其数据项的缓存键。
     *
     * @param dictCode 全局字典编码。
     * @return 全局字典指定编码的缓存键。
     */
    public static String makeGlobalDictKey(String dictCode) {
        return "GLOBAL_DICT:" + dictCode;
    }

    /**
     * 计算仅仅包含全局字典对象数据的缓存键。
     *
     * @param dictCode 全局字典编码。
     * @return 全局字典指定编码的缓存键。
     */
    public static String makeGlobalDictOnlyKey(String dictCode) {
        return "GLOBAL_DICT_ONLY:" + dictCode;
    }

    /**
     * 计算会话的菜单Id关联权限资源URL的缓存键。
     *
     * @param sessionId 会话Id。
     * @param menuId    菜单Id。
     * @return 计算后的缓存键。
     */
    public static String makeSessionMenuPermKey(String sessionId, Object menuId) {
        return "SESSION_MENU_ID:" + sessionId + "-" + menuId.toString();
    }

    /**
     * 计算会话的菜单Id关联权限资源URL的缓存键的前缀。
     *
     * @param sessionId 会话Id。
     * @return 计算后的缓存键前缀。
     */
    public static String getSessionMenuPermPrefix(String sessionId) {
        return "SESSION_MENU_ID:" + sessionId + "-";
    }

    /**
     * 计算会话关联的白名单URL的缓存键。
     *
     * @param sessionId 会话Id。
     * @return 计算后的缓存键。
     */
    public static String makeSessionWhiteListPermKey(String sessionId) {
        return "SESSION_WHITE_LIST:" + sessionId;
    }

    /**
     * 计算会话关联指定部门Ids的子部门Ids的缓存键。
     *
     * @param sessionId 会话Id。
     * @param deptIds   部门Id，多个部门Id之间逗号分割。
     * @return 计算后的缓存键。
     */
    public static String makeSessionChildrenDeptIdKey(String sessionId, String deptIds) {
        return "SESSION_CHILDREN_DEPT_ID:" + sessionId + "-" + deptIds;
    }

    /**
     * 计算租户编码的缓存键。
     *
     * @param tenantCode 租户编码。
     */
    public static String makeTenantCodeKey(String tenantCode) {
        return "TENANT_CODE:" + tenantCode;
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private RedisKeyUtil() {
    }

    /**
     * 构建自定义路由权限Key
     *
     * @param customizeRouteId 自定义路由ID
     * @return 字符串
     * @author 王立宏
     * @date 2023/11/20 10:08
     */
    public static String makeCustomizeRouteRightKey(Long customizeRouteId) {
        return "CUSTOMIZE_ROUTE_RIGHT:" + customizeRouteId;
    }

    /**
     * 构建AI对话记录Key
     * @param dialogueStrId 对话记录ID
     * @return
     */
    public static String makeAiConversationKey(String dialogueStrId) {
        return "AI_CONVERSATION:" + dialogueStrId;
    }
}
