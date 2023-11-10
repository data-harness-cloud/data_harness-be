package supie.common.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 操作日志的配置类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@ConfigurationProperties(prefix = "common-log.operation-log")
public class OperationLogProperties {

    /**
     * 是否采集操作日志。
     */
    private boolean enabled = true;
    /**
     * 接口调用的毫秒数大于该值后，将输出慢日志警告。
     */
    private long slowLogMs = 50000;
}
