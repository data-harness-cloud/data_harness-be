package supie.common.core.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记通过租户Id进行过滤的字段。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantFilterColumn {

}
