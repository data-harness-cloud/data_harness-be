package supie.webadmin.config;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 第三方应用鉴权配置。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "third-party")
public class ThirdPartyAuthConfig implements InitializingBean {

    private List<AuthProperties> auth;

    private Map<String, AuthProperties> applicationMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollUtil.isEmpty(auth)) {
            applicationMap = new HashMap<>(1);
        } else {
            applicationMap = auth.stream().collect(Collectors.toMap(AuthProperties::getAppCode, c -> c));
        }
    }

    @Data
    public static class AuthProperties {
        /**
         * 应用Id。
         */
        private String appCode;
        /**
         * 身份验证相关url的base地址。
         */
        private String baseUrl;
        /**
         * 是否为橙单框架。
         */
        private Boolean orangeFramework = true;
        /**
         * 数据权限和用户操作权限缓存过期时间，单位秒。
         */
        private Integer permExpiredSeconds = 86400;
        /**
         * 用户Token缓存过期时间，单位秒。
         * 如果为0，则每次都要去第三方服务进行验证。
         */
        private Integer tokenExpiredSeconds = 0;
    }
}
