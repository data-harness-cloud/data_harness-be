package supie.common.redis.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrFormatter;
import com.alibaba.fastjson.JSON;
import supie.common.core.object.MyPrintInfo;
import supie.common.core.object.TokenData;
import supie.common.core.exception.MyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Session数据缓存辅助类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@SuppressWarnings("unchecked")
@Component
public class SessionCacheHelper {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 缓存当前session内，上传过的文件名。
     *
     * @param filename 通常是本地存储的文件名，而不是上传时的原始文件名。
     */
    public void putSessionUploadFile(String filename) {
        if (filename != null) {
            Set<String> sessionUploadFileSet = null;
            Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
            if (cache == null) {
                String msg = StrFormatter.format("No redisson cache [{}].",
                        RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
                throw new MyRuntimeException(msg);
            }
            Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
            if (valueWrapper != null) {
                sessionUploadFileSet = (Set<String>) valueWrapper.get();
            }
            if (sessionUploadFileSet == null) {
                sessionUploadFileSet = new HashSet<>();
            }
            sessionUploadFileSet.add(filename);
            cache.put(TokenData.takeFromRequest().getSessionId(), sessionUploadFileSet);
        }
    }

    /**
     * 缓存当前Session可以下载的文件集合。
     *
     * @param filenameSet 后台服务本地存储的文件名，而不是上传时的原始文件名。
     */
    public void putSessionDownloadableFileNameSet(Set<String> filenameSet) {
        if (CollUtil.isEmpty(filenameSet)) {
            return;
        }
        Set<String> sessionUploadFileSet = null;
        Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
        if (cache == null) {
            throw new MyRuntimeException(StrFormatter.format("No redisson cache [{}]!",
                    RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name()));
        }
        Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
        if (valueWrapper != null) {
            sessionUploadFileSet = (Set<String>) valueWrapper.get();
        }
        if (sessionUploadFileSet == null) {
            sessionUploadFileSet = new HashSet<>();
        }
        sessionUploadFileSet.addAll(filenameSet);
        cache.put(TokenData.takeFromRequest().getSessionId(), sessionUploadFileSet);
    }

    /**
     * 判断参数中的文件名，是否有当前session上传。
     *
     * @param filename 通常是本地存储的文件名，而不是上传时的原始文件名。
     * @return true表示该文件是由当前session上传并存储在本地的，否则false。
     */
    public boolean existSessionUploadFile(String filename) {
        if (filename == null) {
            return false;
        }
        Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
        if (cache == null) {
            String msg = StrFormatter.format("No redisson cache [{}]!",
                    RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
            throw new MyRuntimeException(msg);
        }
        Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
        if (valueWrapper == null) {
            return false;
        }
        Object cachedData = valueWrapper.get();
        if (cachedData == null) {
            return false;
        }
        return ((Set<String>) cachedData).contains(filename);
    }

    /**
     * 缓存当前session内，可打印的安全令牌。
     *
     * @param token     打印安全令牌。
     * @param printInfo 打印参数信息。
     */
    public void putSessionPrintTokenAndInfo(String token, MyPrintInfo printInfo) {
        Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.PRINT_ACCESS_TOKEN_CACHE.name());
        if (cache == null) {
            String msg = StrFormatter.format("No redisson cache [{}].",
                    RedissonCacheConfig.CacheEnum.PRINT_ACCESS_TOKEN_CACHE.name());
            throw new MyRuntimeException(msg);
        }
        Map<String, String> sessionPrintTokenMap = null;
        Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
        if (valueWrapper != null) {
            sessionPrintTokenMap = (Map<String, String>) valueWrapper.get();
        }
        if (sessionPrintTokenMap == null) {
            sessionPrintTokenMap = new HashMap<>(4);
        }
        sessionPrintTokenMap.put(token, JSON.toJSONString(printInfo));
        cache.put(TokenData.takeFromRequest().getSessionId(), sessionPrintTokenMap);
    }

    /**
     * 获取当前session中，指定打印令牌所关联的打印信息。
     *
     * @param token 打印安全令牌。
     * @return 当前session中，指定打印令牌所关联的打印信息。不存在返回null。
     */
    public MyPrintInfo getSessionPrintInfoByToken(String token) {
        Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.PRINT_ACCESS_TOKEN_CACHE.name());
        if (cache == null) {
            String msg = StrFormatter.format("No redisson cache [{}]!",
                    RedissonCacheConfig.CacheEnum.PRINT_ACCESS_TOKEN_CACHE.name());
            throw new MyRuntimeException(msg);
        }
        Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
        if (valueWrapper == null) {
            return null;
        }
        Object cachedData = valueWrapper.get();
        if (cachedData == null) {
            return null;
        }
        String data = ((Map<String, String>) cachedData).get(token);
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data, MyPrintInfo.class);
    }

    /**
     * 清除当前session的所有缓存数据。
     *
     * @param sessionId 当前会话的SessionId。
     */
    public void removeAllSessionCache(String sessionId) {
        for (RedissonCacheConfig.CacheEnum c : RedissonCacheConfig.CacheEnum.values()) {
            Cache cache = cacheManager.getCache(c.name());
            if (cache != null) {
                cache.evict(sessionId);
            }
        }
    }
}
