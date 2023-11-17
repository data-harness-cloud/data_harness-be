package supie.webadmin.app.controller.dynamicRoutingAPI;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;
import supie.common.core.annotation.MyRequestBody;
import supie.common.core.object.ResponseResult;
import supie.common.core.util.ApplicationContextHolder;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/15 15:08
 * @path SDT-supie.webadmin.app.controller.dynamicRoutingAPI-DynamicRoutingController
 */
@Slf4j
@RestController
@ApiOperation("动态路由测试")
public class DynamicRoutingController {

    private static RequestMappingHandlerMapping requestMappingHandlerMapping = null;

    @PostMapping("/registerApi")
    public String registerApi(
            @MyRequestBody String path, @MyRequestBody Map<String, String> params) throws NoSuchMethodException {
//        RequestMappingInfo.BuilderConfiguration builderConfiguration = new RequestMappingInfo.BuilderConfiguration();
//        builderConfiguration.setPatternParser(new PathPatternParser());
//
//        RequestMappingInfo mappingInfo = RequestMappingInfo
//                .paths(path)
//                .methods(RequestMethod.GET)
//                .options(builderConfiguration)
//                .build();
        log.warn("添加路由[" + path + "]");
        RequestMappingInfo mappingInfo = RequestMappingInfo
                .paths(path)
                .methods(RequestMethod.POST)
                .build();

        // 反射获取ExampleController中的hello方法，用于执行实际逻辑
        Method method = MyDynamicController.class.getDeclaredMethod("test", Map.class);

        // 使用SpringRouterUtils注册这个新的路由
//        ApplicationContextHolder.register(mappingInfo, new MyDynamicController(), method);
        if (requestMappingHandlerMapping == null) {
//            requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
//            requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            requestMappingHandlerMapping = ApplicationContextHolder.getBean("requestMappingHandlerMapping");
//            requestMappingHandlerMapping = ApplicationContextHolder.getBean(requestMappingHandlerMapping.class);
        }
        MyDynamicController myDynamicController = ApplicationContextHolder.getBean(MyDynamicController.class);
        requestMappingHandlerMapping.registerMapping(mappingInfo, myDynamicController, method);

        return "SUCCESS";
    }

    @PostMapping("/removeApi")
    public String removeApi(@MyRequestBody String path) throws NoSuchMethodException {
//        RequestMappingInfo.BuilderConfiguration builderConfiguration = new RequestMappingInfo.BuilderConfiguration();
//        builderConfiguration.setPatternParser(new PathPatternParser());
//
//        RequestMappingInfo mappingInfo = RequestMappingInfo
//                .paths(path)
//                .methods(RequestMethod.GET)
//                .options(builderConfiguration)
//                .build();

        // 使用SpringRouterUtils注册这个新的路由
//        ApplicationContextHolder.remove(mappingInfo);
        if (requestMappingHandlerMapping == null) {
//            requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
//            requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            requestMappingHandlerMapping = ApplicationContextHolder.getBean("requestMappingHandlerMapping");
//            requestMappingHandlerMapping = ApplicationContextHolder.getBean(RequestMappingHandlerMapping.class);
        }
//        requestMappingHandlerMapping.unregisterMapping(mappingInfo);

        RequestMappingInfo requestMappingInfo = requestMappingHandlerMapping.getHandlerMethods().keySet().stream()
                .filter(mapping -> mapping.getPatternsCondition().getPatterns().contains(path))
                .findFirst()
                .orElse(null);
        log.warn("删除路由[" + path + "]");
        // 删除指定路径的映射
        requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);

        return "SUCCESS";
    }

    @GetMapping("/getAllMappings")
    public ResponseResult<String> getAllMappings() {
        if (requestMappingHandlerMapping == null) {
            requestMappingHandlerMapping = ApplicationContextHolder.getBean("requestMappingHandlerMapping");
        }
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        return ResponseResult.success(JSONUtil.toJsonStr(handlerMethods));
    }

}
