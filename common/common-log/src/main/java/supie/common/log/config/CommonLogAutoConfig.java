package supie.common.log.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * common-log模块的自动配置引导类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableConfigurationProperties({OperationLogProperties.class})
public class CommonLogAutoConfig {
}
