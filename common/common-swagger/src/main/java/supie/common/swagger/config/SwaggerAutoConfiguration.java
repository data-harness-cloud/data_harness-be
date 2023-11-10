package supie.common.swagger.config;

import supie.common.core.annotation.MyRequestBody;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 自动加载bean的配置对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableSwagger2WebMvc
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "swagger", name = "enabled")
public class SwaggerAutoConfiguration {

    @Bean
    public Docket upmsDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1.用户权限分组接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getServiceBasePackage() + ".upms.controller"))
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public Docket bizDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("2.业务应用分组接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getServiceBasePackage() + ".app.controller"))
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public Docket workflowDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3.工作流通用操作接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage() + ".common.flow.controller"))
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public Docket onlineDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("4.在线表单操作接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage() + ".common.online.controller"))
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public Docket reportDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("5.报表打印操作接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage() + ".common.report.controller"))
                .paths(PathSelectors.any()).build();
    }

//    @Bean
//    public Docket quartzDocket(SwaggerProperties properties) {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("6.Quartz任务调度接口")
//                .ignoredParameterTypes(MyRequestBody.class)
//                .apiInfo(apiInfo(properties))
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage() + ".common.quartz.controller"))
//                .paths(PathSelectors.any()).build();
//    }

    private ApiInfo apiInfo(SwaggerProperties properties) {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion()).build();
    }
}
