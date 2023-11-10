package supie.common.log.annotation;

import java.lang.annotation.*;

/**
 * 忽略接口应答数据记录日志的注解。该注解会被OperationLogAspect处理。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreResponseLog {

}
