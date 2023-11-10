package supie.common.core.util;

import org.springframework.stereotype.Component;

/**
 * 常量值指向的数据源。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Component
public class DefaultDataSourceResolver implements DataSourceResolver {

    private static final ThreadLocal<Integer> DEFAULT_CONTEXT_HOLDER = new ThreadLocal<>();

    @Override
    public Integer resolve(String arg, Integer intArg, Object[] methodArgs) {
        Integer datasourceType = DEFAULT_CONTEXT_HOLDER.get();
        return datasourceType != null ? datasourceType : intArg;
    }

    /**
     * 设置报表数据源类型值。
     *
     * @param type 数据源类型
     * @return 原有数据源类型，如果第一次设置则返回null。
     */
    public static Integer setDataSourceType(Integer type) {
        Integer datasourceType = DEFAULT_CONTEXT_HOLDER.get();
        DEFAULT_CONTEXT_HOLDER.set(type);
        return datasourceType;
    }

    /**
     * 获取当前报表数据库操作执行线程的数据源类型，同时由动态数据源的路由函数调用。
     *
     * @return 数据源类型。
     */
    public static Integer getDataSourceType() {
        return DEFAULT_CONTEXT_HOLDER.get();
    }

    /**
     * 清除线程本地变量，以免内存泄漏。

     * @param originalType 原有的数据源类型，如果该值为null，则情况本地化变量。
     */
    public static void unset(Integer originalType) {
        if (originalType == null) {
            DEFAULT_CONTEXT_HOLDER.remove();
        } else {
            DEFAULT_CONTEXT_HOLDER.set(originalType);
        }
    }
}
