package supie.common.core.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记Job实体对象的更新时间字段。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobUpdateTimeColumn {

}
