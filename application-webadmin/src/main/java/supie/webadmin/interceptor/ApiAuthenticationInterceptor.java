package supie.webadmin.interceptor;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.constant.ErrorCodeEnum;
import supie.common.core.object.ResponseResult;
import supie.common.core.util.ApplicationContextHolder;
import supie.common.core.util.MyCommonUtil;
import supie.webadmin.app.dao.CustomizeRouteMapper;
import supie.webadmin.app.dao.ExternalAppMapper;
import supie.webadmin.app.model.CustomizeRoute;
import supie.webadmin.app.model.ExternalApp;
import supie.webadmin.config.ApplicationConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/16 16:50
 * @path SDT-supie.webadmin.interceptor-ApiAuthenticationInterceptor
 */
@Slf4j
public class ApiAuthenticationInterceptor implements HandlerInterceptor {

    private final ApplicationConfig appConfig =
            ApplicationContextHolder.getBean("applicationConfig");

    private final RedissonClient redissonClient = ApplicationContextHolder.getBean(RedissonClient.class);
    
    private final CustomizeRouteMapper customizeRouteMapper =
            ApplicationContextHolder.getBean("customizeRouteMapper");

    private final ExternalAppMapper externalAppMapper =
            ApplicationContextHolder.getBean("externalAppMapper");

    private CustomizeRoute customizeRoute = null;
    private List<VerificationInfo> verificationInfoList = null;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            String url = request.getRequestURI();
            // 获取当前的请求所携带的AppKey信息
            String appKey = this.getTokenFromRequest(request);
            List<VerificationInfo> verificationInfoList = getVerificationInfoList(url);
            if (StrUtil.isBlank(appKey)) {
                // AppKey为空，判断该地址是否为无需验证的地址
                if (verificationInfoList == null) throw new RuntimeException("当前应用没有操作权限，请核对！");
                for (VerificationInfo verificationInfo : verificationInfoList) {
                    if (verificationInfo.getAuthenticationMethod() != 2) continue;
                    // 无认证类型
                    return setCustomizeRouteDataToRedis(url);
                }
                throw new RuntimeException("当前应用没有操作权限，请核对！");
            }
            // AppKey不为空,进行相关的合法性验证.
            // 判断AppKey是否为本系统所发放
            if (!ExternalApp.verifyAppKey(appKey)) {
                String warnMessage = StrFormatter.format(
                        "IP[{}]通过[{}]端请求[{}]动态路由接口携带了非本系统发放的AppKey({})信息!",
                        MyCommonUtil.getClientIpAddress(), MyCommonUtil.getDeviceTypeByUserAgent(), url, appKey);
                log.warn(warnMessage);
                throw new RuntimeException("所携带的验证信息不合法!");
            }
            // AppKey合法，对比是否为该url所拥有的AppKey。
            for (VerificationInfo verificationInfo : verificationInfoList) {
                if (verificationInfo.getAuthenticationMethod() == 1 && appKey.equals(verificationInfo.getAppKey())) {
                    return setCustomizeRouteDataToRedis(url);
                } else if (verificationInfo.getAuthenticationMethod() == 2) {
                    // 无认证类型
                    return setCustomizeRouteDataToRedis(url);
                }
            }
            throw new RuntimeException("AppKey认证失败,当前AppKey无权访问该路由地址!");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            this.outputResponseMessage(response, ResponseResult.error(ErrorCodeEnum.NO_OPERATION_PERMISSION, e.getMessage()));
            return false;
        }
    }

    /**
     * 获取验证信息.
     * 先从redis获取,若redis中不存在则从数据库中查找并存入redis.
     * 相应的权限有变动修改则删除redis中缓存的权限信息。
     *
     * @param url 路由地址
     * @return 验证信息集
     * @author 王立宏
     * @date 2023/11/22 11:41
     */
    @Nullable
    private List<VerificationInfo> getVerificationInfoList(String url) {
        if (verificationInfoList != null) return verificationInfoList;
        // 获取redis中存储的信息，若redis中不存在则从数据库获取并存储至redis。
        RBucket<String> verificationInfoListRBucket = redissonClient.getBucket("CustomizeRouteVerificationInfo:" + url);
        if (!verificationInfoListRBucket.isExists()) {
            // 不存在于redis中,从数据库查询相关信息并存储至redis。
            List<ExternalApp> externalAppList = externalAppMapper.queryExternalAppByUrl(url);
            if (externalAppList.size() == 0) throw new RuntimeException("该路由未关联任何外部App!");
            verificationInfoList = externalAppListToVerificationInfoList(externalAppList);
            verificationInfoListRBucket.set(JSONUtil.toJsonStr(verificationInfoList));
            verificationInfoListRBucket.expire(appConfig.getSessionExpiredSeconds(), TimeUnit.SECONDS);
        } else {
            verificationInfoList = JSONUtil.toList(verificationInfoListRBucket.get(), VerificationInfo.class);
        }
        return verificationInfoList;
    }

    /**
     * 外部应用集合 转为 验证信息集合
     *
     * @param externalAppList 外部应用集
     * @return 列表<验证信息>
     * @author 王立宏
     * @date 2023/11/22 11:30
     */
    private List<VerificationInfo> externalAppListToVerificationInfoList(List<ExternalApp> externalAppList) {
        List<VerificationInfo> verificationInfoList = new ArrayList<>();
        for (ExternalApp externalApp : externalAppList) {
            verificationInfoList.add(
                    new VerificationInfo(externalApp.getId(), externalApp.getAppKey(), externalApp.getAuthenticationMethod())
            );
        }
        // 清理
        return verificationInfoList;
    }

    /**
     * 权限判断通过，自定义路由信息存储至redis，以供业务代码获取
     *
     * @param url 路由地址
     * @author 王立宏
     * @date 2023/11/22 10:44
     */
    private Boolean setCustomizeRouteDataToRedis(String url) {
        CustomizeRoute customizeRoute = getCustomizeRoute(url);
        if (customizeRoute == null) throw new RuntimeException("当前的路由相关信息获取失败，请联系管理员！");
        this.customizeRoute = null;
        this.verificationInfoList = null;
        return true;
    }

    /**
     * 查询当前请求的路由的相关信息,并存储至redis中
     *
     * @param url 网址
     * @author 王立宏
     * @date 2023/11/22 10:58
     */
    private CustomizeRoute getCustomizeRoute(String url) {
        if (this.customizeRoute != null) return this.customizeRoute;
        // 先查询redis中是否存在。不存在则从数据库中查询，并将其存储至redis中。
        // 若 CustomizeRoute 有修改、删除则同步删除数据库中存储的数据 及其 注册的路由信息。
        RBucket<String> customizeRouteRBucket = redissonClient.getBucket("CustomizeRoute:" + url);
        if (customizeRouteRBucket.isExists()) {
            // 存在于redis中，判断是否已经在当前内存中存在，不存在则从redis中取出来
            this.customizeRoute = JSONUtil.toBean(customizeRouteRBucket.get(), CustomizeRoute.class);
        } else {
            // 不存在于redis中，从数据库中查找到相关数据，并存储至redia中
            QueryWrapper<CustomizeRoute> customizeRouteQueryWrapper = new QueryWrapper<>();
            customizeRouteQueryWrapper.eq("url", url);
            this.customizeRoute = customizeRouteMapper.selectOne(customizeRouteQueryWrapper);
            if (this.customizeRoute == null) throw new RuntimeException("未查找到该路由下相关信息！请联系管理员");
            customizeRouteRBucket.set(JSONUtil.toJsonStr(this.customizeRoute));
            customizeRouteRBucket.expire(appConfig.getSessionExpiredSeconds(), TimeUnit.SECONDS);
        }
        return this.customizeRoute;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(appConfig.getTokenHeaderKey());
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(appConfig.getTokenHeaderKey());
        }
        if (StrUtil.isBlank(token)) {
            token = request.getHeader(ApplicationConstant.HTTP_HEADER_INTERNAL_TOKEN);
        }
        return token;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 这里需要空注解，否则sonar会不happy。
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 这里需要空注解，否则sonar会不happy。
    }

    private void outputResponseMessage(HttpServletResponse response, ResponseResult<Object> respObj) {
        PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error("Failed to call OutputResponseMessage.", e);
            return;
        }
        response.setContentType("application/json; charset=utf-8");
        out.print(JSON.toJSONString(respObj));
        out.flush();
        out.close();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class VerificationInfo {
        /**
         * ExternalAppId
         */
        private Long externalAppId;
        /**
         * AppKey
         */
        private String appKey;
        /**
         * 认证方式（1：key认证。2：无认证）。
         */
        private Integer authenticationMethod;
    }

}
