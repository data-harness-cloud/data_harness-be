package supie.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记Controller中的方法参数，参数解析器会根据该注解将请求中的JSON数据，映射到参数中的绑定字段。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRequestBody {

    /**
     * 是否必须出现的参数。
     */
    boolean required() default false;
    /**
     * 解析时用到的JSON的key。
     */
    String value() default "";
}