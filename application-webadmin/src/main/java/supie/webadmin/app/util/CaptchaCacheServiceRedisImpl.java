package supie.webadmin.app.util;

import com.anji.captcha.service.CaptchaCacheService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Aj验证码 redis实现
 */
public class CaptchaCacheServiceRedisImpl implements CaptchaCacheService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public String type() {
        return "redis";
    }

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        redissonClient.getBucket(key).set(value, expiresInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    @Override
    public void delete(String key) {
        redissonClient.getBucket(key).delete();
    }

    @Override
    public String get(String key) {
        return (String) redissonClient.getBucket(key).get();
    }

    @Override
    public Long increment(String key, long val) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.addAndGet(val);
    }
}
