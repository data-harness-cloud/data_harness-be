package supie.common.core.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记无需Token验证的接口
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuthInterface {
}
