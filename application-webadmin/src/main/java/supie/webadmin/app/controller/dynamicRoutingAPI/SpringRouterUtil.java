//package supie.webadmin.app.controller.dynamicRoutingAPI;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//import java.lang.reflect.Method;
//
///**
// * 描述：
// *
// * @author 王立宏
// * @date 2023/11/15 15:14
// * @path SDT-supie.webadmin.app.controller.dynamicRoutingAPI-SpringRouterUtil
// */
//@Component
//public class SpringRouterUtil {
//
//    // 这个静态变量用于存储RequestMappingHandlerMapping实例，它是Spring MVC用于管理所有路由（或称为请求映射）的组件
//    @Autowired
//    private RequestMappingHandlerMapping requestMappingHandlerMapping;
//
//    public void register(RequestMappingInfo requestMappingInfo, Object handler, Method method){
////        requestMappingHandlerMapping.registerMapping(requestMappingInfo, handler, method);
//    }
//
//    public void remove(RequestMappingInfo requestMappingInfo){
////        requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
//    }
//
//}
