package supie.common.core.annotation;

import java.lang.annotation.*;

/**
 * 该注解通常标记于Service中的事务方法，并且会和@Transactional注解同时存在。
 * 被注解标注的方法内代码，通常通过mybatis，并在同一个事务内访问数据库。与此同时还会存在基于
 * JDBC的跨库操作。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiDatabaseWriteMethod {

}
