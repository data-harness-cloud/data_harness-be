package supie.common.core.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记Service所依赖的数据源类型。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyDataSource {

    /**
     * 标注的数据源类型
     * @return 当前标注的数据源类型。
     */
    int value();
}
