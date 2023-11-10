package supie.webadmin.app.util.sqlUtil;

import java.lang.annotation.*;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/26 9:07
 * @path SDT-supie.webadmin.app.util.sqlUtil-ColumnField
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ColumnField {
    ColumnType value();
}
