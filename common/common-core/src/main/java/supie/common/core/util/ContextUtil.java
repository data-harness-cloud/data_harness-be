package supie.common.core.util;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取Servlet HttpRequest和HttpResponse的工具类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class ContextUtil {

    /**
     * 判断当前是否处于HttpServletRequest上下文环境。
     *
     * @return 是返回true，否则false。
     */
    public static boolean hasRequestContext() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    /**
     * 获取Servlet请求上下文的HttpRequest对象。
     *
     * @return 请求上下文中的HttpRequest对象。
     */
    public static HttpServletRequest getHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(attributes, "RequestContextHolder.getRequestAttributes can't be NULL");
        return attributes.getRequest();
    }

    /**
     * 获取Servlet请求上下文的HttpResponse对象。
     *
     * @return 请求上下文中的HttpResponse对象。
     */
    public static HttpServletResponse getHttpResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(attributes, "RequestContextHolder.getRequestAttributes can't be NULL");
        return attributes.getResponse();
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private ContextUtil() {
    }
}
