package supie.common.quartz.object;

import java.lang.annotation.*;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/24 10:54
 * @path SDT-supie.common.quartz.utils-JobField
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface JobField {
    JobFieldType value();
}
