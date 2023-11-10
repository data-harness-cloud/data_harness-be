package supie.common.ext.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * common-ext通用扩展模块的自动配置引导类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableConfigurationProperties({CommonExtProperties.class})
public class CommonExtAutoConfig {
}
