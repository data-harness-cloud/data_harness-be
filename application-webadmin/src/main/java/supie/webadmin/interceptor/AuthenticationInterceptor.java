package supie.webadmin.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import supie.common.core.annotation.NoAuthInterface;
import supie.common.core.cache.CacheConfig;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.constant.DataPermRuleType;
import supie.common.core.constant.ErrorCodeEnum;
import supie.common.core.exception.MyRuntimeException;
import supie.common.core.object.ResponseResult;
import supie.common.core.object.TokenData;
import supie.common.core.util.ApplicationContextHolder;
import supie.common.core.util.JwtUtil;
import supie.common.core.util.RedisKeyUtil;
import supie.webadmin.config.ApplicationConfig;
import supie.webadmin.config.ThirdPartyAuthConfig;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录用户Token验证、生成和权限验证的拦截器。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final ApplicationConfig appConfig =
            ApplicationContextHolder.getBean("applicationConfig");

    private final ThirdPartyAuthConfig thirdPartyAuthConfig =
            ApplicationContextHolder.getBean("thirdPartyAuthConfig");

    private final RedissonClient redissonClient = ApplicationContextHolder.getBean(RedissonClient.class);

    private final CacheManager cacheManager = ApplicationContextHolder.getBean("caffeineCacheManager");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String url = request.getRequestURI();
        String token = this.getTokenFromRequest(request);
        boolean noLoginUrl = this.isNoAuthInterface(handler);
        // 如果接口方法标记NoAuthInterface注解，可以直接跳过Token鉴权验证，这里主要为了测试接口方便
        if (noLoginUrl && StrUtil.isBlank(token)) {
            return true;
        }
        String appCode = this.getAppCodeFromRequest(request);
        if (StrUtil.isNotBlank(appCode)) {
            return this.handleThirdPartyRequest(appCode, token, url, response);
        }
        Claims c = JwtUtil.parseToken(token, appConfig.getTokenSigningKey());
        if (JwtUtil.isNullOrExpired(c)) {
            // 如果免登陆接口携带的是过期的Token，这个时候直接返回给Controller即可。
            // 这样可以规避不必要的重新登录，而对于Controller，可以将本次请求视为未登录用户的请求。
            if (noLoginUrl) {
                return true;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            this.outputResponseMessage(response,
                    ResponseResult.error(ErrorCodeEnum.UNAUTHORIZED_LOGIN, "用户会话已过期或尚未登录，请重新登录！"));
            return false;
        }
        String sessionId = (String) c.get("sessionId");
        String sessionIdKey = RedisKeyUtil.makeSessionIdKey(sessionId);
        RBucket<String> sessionData = redissonClient.getBucket(sessionIdKey);
        TokenData tokenData = null;
        if (sessionData.isExists()) {
            tokenData = JSON.parseObject(sessionData.get(), TokenData.class);
        }
        if (tokenData == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            this.outputResponseMessage(response,
                    ResponseResult.error(ErrorCodeEnum.UNAUTHORIZED_LOGIN, "用户会话已失效，请重新登录！"));
            return false;
        }
        tokenData.setToken(token);
        TokenData.addToRequest(tokenData);
        // 如果url是免登陆、白名单中，则不需要进行鉴权操作
        if (!noLoginUrl && Boolean.FALSE.equals(tokenData.getIsAdmin()) && !this.hasPermission(sessionId, url)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            this.outputResponseMessage(response, ResponseResult.error(ErrorCodeEnum.NO_OPERATION_PERMISSION));
            return false;
        }
        if (JwtUtil.needToRefresh(c)) {
            String refreshedToken = JwtUtil.generateToken(c, appConfig.getExpiration(), appConfig.getTokenSigningKey());
            response.addHeader(appConfig.getRefreshedTokenHeaderKey(), refreshedToken);
        }
        return true;
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

    private String getAppCodeFromRequest(HttpServletRequest request) {
        String token = request.getHeader("AppCode");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("AppCode");
        }
        return token;
    }

    @SuppressWarnings("unchecked")
    private boolean hasPermission(String sessionId, String url) {
        // 为了提升效率，先检索Caffeine的一级缓存，如果不存在，再检索Redis的二级缓存，并将结果存入一级缓存。
        Set<String> localPermSet;
        String permKey = RedisKeyUtil.makeSessionPermIdKey(sessionId);
        Cache cache = cacheManager.getCache(CacheConfig.CacheEnum.USER_PERMISSION_CACHE.name());
        Assert.notNull(cache, "Cache USER_PERMISSION_CACHE can't be NULL.");
        Cache.ValueWrapper wrapper = cache.get(permKey);
        if (wrapper == null) {
            RSet<String> permSet = redissonClient.getSet(permKey);
            localPermSet = permSet.readAll();
            cache.put(permKey, localPermSet);
        } else {
            localPermSet = (Set<String>) wrapper.get();
        }
        return CollUtil.contains(localPermSet, url);
    }

    private boolean isNoAuthInterface(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            return hm.getBeanType().getAnnotation(NoAuthInterface.class) != null
                    || hm.getMethodAnnotation(NoAuthInterface.class) != null;
        }
        return false;
    }

    private boolean handleThirdPartyRequest(String appCode, String token, String url, HttpServletResponse response) {
        ThirdPartyAuthConfig.AuthProperties authProps = thirdPartyAuthConfig.getApplicationMap().get(appCode);
        if (authProps == null) {
            String msg = StrFormatter.format("请求的 appCode[{}] 信息，在当前服务中尚未配置！", appCode);
            this.outputResponseMessage(response, ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, msg));
            return false;
        }
        ResponseResult<TokenData> responseResult = this.getAndCacheThirdPartyTokenData(authProps, token);
        if (!responseResult.isSuccess()) {
            this.outputResponseMessage(response,
                    ResponseResult.error(ErrorCodeEnum.UNAUTHORIZED_LOGIN, responseResult.getErrorMessage()));
            return false;
        }
        TokenData tokenData = responseResult.getData();
        tokenData.setAppCode(appCode);
        tokenData.setSessionId(this.prependAppCode(authProps.getAppCode(), tokenData.getSessionId()));
        TokenData.addToRequest(tokenData);
        if (Boolean.FALSE.equals(tokenData.getIsAdmin())
                && !this.hasThirdPartyPermission(authProps, tokenData, url)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            this.outputResponseMessage(response, ResponseResult.error(ErrorCodeEnum.NO_OPERATION_PERMISSION));
            return false;
        }
        return true;
    }

    private ResponseResult<TokenData> getAndCacheThirdPartyTokenData(
            ThirdPartyAuthConfig.AuthProperties authProps, String token) {
        if (authProps.getTokenExpiredSeconds() == 0) {
            return this.getThirdPartyTokenData(authProps, token);
        }
        String tokeKey = this.prependAppCode(authProps.getAppCode(), RedisKeyUtil.makeSessionIdKey(token));
        RBucket<String> sessionData = redissonClient.getBucket(tokeKey);
        if (sessionData.isExists()) {
            return ResponseResult.success(JSON.parseObject(sessionData.get(), TokenData.class));
        }
        ResponseResult<TokenData> responseResult = this.getThirdPartyTokenData(authProps, token);
        if (responseResult.isSuccess()) {
            sessionData.set(JSON.toJSONString(responseResult.getData()), authProps.getTokenExpiredSeconds(), TimeUnit.SECONDS);
        }
        return responseResult;
    }

    private String prependAppCode(String appCode, String key) {
        return appCode.toUpperCase() + ":" + key;
    }

    private ResponseResult<TokenData> getThirdPartyTokenData(
            ThirdPartyAuthConfig.AuthProperties authProps, String token) {
        try {
            String resultData = this.invokeThirdPartyUrl(authProps.getBaseUrl() + "/getTokenData", token);
            return JSON.parseObject(resultData, new TypeReference<ResponseResult<TokenData>>() {});
        } catch (MyRuntimeException ex) {
            return ResponseResult.error(ErrorCodeEnum.FAILED_TO_INVOKE_THIRDPARTY_URL, ex.getMessage());
        }
    }

    private ResponseResult<ThirdPartyAppPermData> getThirdPartyPermData(
            ThirdPartyAuthConfig.AuthProperties authProps, String token) {
        try {
            String resultData = this.invokeThirdPartyUrl(authProps.getBaseUrl() + "/getPermData", token);
            return JSON.parseObject(resultData, new TypeReference<ResponseResult<ThirdPartyAppPermData>>() {});
        } catch (MyRuntimeException ex) {
            return ResponseResult.error(ErrorCodeEnum.FAILED_TO_INVOKE_THIRDPARTY_URL, ex.getMessage());
        }
    }

    private String invokeThirdPartyUrl(String url, String token) {
        Map<String, String> headerMap = new HashMap<>(1);
        headerMap.put("Authorization", token);
        StringBuilder fullUrl = new StringBuilder(128);
        fullUrl.append(url).append("?token=").append(token);
        HttpResponse httpResponse = HttpUtil.createGet(fullUrl.toString()).addHeaders(headerMap).execute();
        if (!httpResponse.isOk()) {
            String msg = StrFormatter.format(
                    "Failed to call [{}] with ERROR HTTP Status [{}] and [{}].",
                    url, httpResponse.getStatus(), httpResponse.body());
            log.error(msg);
            throw new MyRuntimeException(msg);
        }
        return httpResponse.body();
    }

    @SuppressWarnings("unchecked")
    private boolean hasThirdPartyPermission(
            ThirdPartyAuthConfig.AuthProperties authProps, TokenData tokenData, String url) {
        // 为了提升效率，先检索Caffeine的一级缓存，如果不存在，再检索Redis的二级缓存，并将结果存入一级缓存。
        String permKey = RedisKeyUtil.makeSessionPermIdKey(tokenData.getSessionId());
        Cache cache = cacheManager.getCache(CacheConfig.CacheEnum.USER_PERMISSION_CACHE.name());
        Assert.notNull(cache, "Cache USER_PERMISSION_CACHE can't be NULL");
        Cache.ValueWrapper wrapper = cache.get(permKey);
        if (wrapper != null) {
            Object cachedData = wrapper.get();
            if (cachedData != null) {
                return ((Set<String>) cachedData).contains(url);
            }
        }
        Set<String> localPermSet;
        RSet<String> permSet = redissonClient.getSet(permKey);
        if (permSet.isExists()) {
            localPermSet = permSet.readAll();
            cache.put(permKey, localPermSet);
            return localPermSet.contains(url);
        }
        ResponseResult<ThirdPartyAppPermData> responseResult = this.getThirdPartyPermData(authProps, tokenData.getToken());
        this.cacheThirdPartyDataPermData(authProps, tokenData, responseResult.getData().getDataPerms());
        if (CollUtil.isEmpty(responseResult.getData().urlPerms)) {
            return false;
        }
        permSet.addAll(responseResult.getData().urlPerms);
        permSet.expire(authProps.getPermExpiredSeconds(), TimeUnit.SECONDS);
        localPermSet = new HashSet<>(responseResult.getData().urlPerms);
        cache.put(permKey, localPermSet);
        return localPermSet.contains(url);
    }

    private void cacheThirdPartyDataPermData(
            ThirdPartyAuthConfig.AuthProperties authProps, TokenData tokenData, List<ThirdPartyAppDataPermData> dataPerms) {
        if (CollUtil.isEmpty(dataPerms)) {
            return;
        }
        Map<Integer, List<ThirdPartyAppDataPermData>> dataPermMap =
                dataPerms.stream().collect(Collectors.groupingBy(ThirdPartyAppDataPermData::getRuleType));
        Map<Integer, List<ThirdPartyAppDataPermData>> normalizedDataPermMap = new HashMap<>(dataPermMap.size());
        for (Map.Entry<Integer, List<ThirdPartyAppDataPermData>> entry : dataPermMap.entrySet()) {
            List<ThirdPartyAppDataPermData> ruleTypeDataPermDataList;
            if (entry.getKey().equals(DataPermRuleType.TYPE_DEPT_AND_CHILD_DEPT)) {
                ruleTypeDataPermDataList =
                        normalizedDataPermMap.computeIfAbsent(DataPermRuleType.TYPE_CUSTOM_DEPT_LIST, k -> new LinkedList<>());
            } else {
                ruleTypeDataPermDataList =
                        normalizedDataPermMap.computeIfAbsent(entry.getKey(), k -> new LinkedList<>());
            }
            ruleTypeDataPermDataList.addAll(entry.getValue());
        }
        Map<Integer, String> resultDataPermMap = new HashMap<>(normalizedDataPermMap.size());
        for (Map.Entry<Integer, List<ThirdPartyAppDataPermData>> entry : normalizedDataPermMap.entrySet()) {
            if (entry.getKey().equals(DataPermRuleType.TYPE_CUSTOM_DEPT_LIST)) {
                String deptIds = entry.getValue().stream()
                        .map(ThirdPartyAppDataPermData::getDeptIds).collect(Collectors.joining(","));
                resultDataPermMap.put(entry.getKey(), deptIds);
            } else {
                resultDataPermMap.put(entry.getKey(), "null");
            }
        }
        Map<String, Map<Integer, String>> menuDataPermMap = new HashMap<>(1);
        menuDataPermMap.put(ApplicationConstant.DATA_PERM_ALL_MENU_ID, resultDataPermMap);
        String dataPermSessionKey = RedisKeyUtil.makeSessionDataPermIdKey(tokenData.getSessionId());
        RBucket<String> bucket = redissonClient.getBucket(dataPermSessionKey);
        bucket.set(JSON.toJSONString(menuDataPermMap), authProps.getPermExpiredSeconds(), TimeUnit.SECONDS);
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
    public static class ThirdPartyAppPermData {
        /**
         * 当前用户会话可访问的url接口地址列表。
         */
        private List<String> urlPerms;
        /**
         * 当前用户会话的数据权限列表。
         */
        private List<ThirdPartyAppDataPermData> dataPerms;
    }

    @Data
    public static class ThirdPartyAppDataPermData {
        /**
         * 数据权限的规则类型。需要按照橙单的约定返回。具体值可参考DataPermRuleType常量类。
         */
        private Integer ruleType;
        /**
         * 部门Id集合，多个部门Id之间逗号分隔。
         * 注意：仅当ruleType为3、4、5时需要包含该字段值。
         */
        private String deptIds;
    }
}
