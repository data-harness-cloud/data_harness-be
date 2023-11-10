package supie.common.redis.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Redis的常用工具方法。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Component
public class CommonRedisUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 计算流水号前缀部分。
     *
     * @param prefix      前缀字符串。
     * @param precisionTo 精确到的时间单元，目前仅仅支持 YEAR/MONTH/DAYS/HOURS/MINUTES/SECONDS。
     * @param middle      日期和流水号之间的字符串。
     * @return 返回计算后的前缀部分。
     */
    public String calculateTransIdPrefix(String prefix, String precisionTo, String middle) {
        String key = prefix;
        if (key == null) {
            key = "";
        }
        DateTime dateTime = new DateTime();
        switch (precisionTo) {
            case "YEAR":
                key = key + dateTime.toString("yyyy");
                break;
            case "MONTH":
                key = key + dateTime.toString("yyyyMM");
                break;
            case "DAYS":
                key = key + dateTime.toString("yyyyMMdd");
                break;
            case "HOURS":
                key = key + dateTime.toString("yyyyMMddHH");
                break;
            case "MINUTES":
                key = key + dateTime.toString("yyyyMMddHHmm");
                break;
            case "SECONDS":
                key = key + dateTime.toString("yyyyMMddHHmmss");
                break;
            default:
                throw new UnsupportedOperationException("Only Support YEAR/MONTH/DAYS/HOURS/MINUTES/SECONDS");
        }
        return middle != null ? key + middle : key;
    }

    /**
     * 生成基于时间的流水号方法。
     *
     * @param prefix      前缀字符串。
     * @param precisionTo 精确到的时间单元，目前仅仅支持 YEAR/MONTH/DAYS/HOURS/MINUTES/SECONDS。
     * @param middle      日期和流水号之间的字符串。
     * @param idWidth     计算出的流水号宽度，前面补充0。比如idWidth = 3, 输出值为 005/012/123。
     *                    需要注意的是，流水号值超出idWidth指定宽度，低位会被截取。
     * @return 基于时间的流水号方法。
     */
    public String generateTransId(String prefix, String precisionTo, String middle, int idWidth) {
        TimeUnit unit = EnumUtil.fromString(TimeUnit.class, precisionTo, null);
        int unitCount = 1;
        if (unit == null) {
            unit = TimeUnit.DAYS;
            DateTime now = DateTime.now();
            if (StrUtil.equals(precisionTo, "MONTH")) {
                DateTime endOfMonthDay = DateUtil.endOfMonth(now);
                unitCount = endOfMonthDay.getField(DateField.DAY_OF_MONTH) - now.getField(DateField.DAY_OF_MONTH) + 1;
            } else if (StrUtil.equals(precisionTo, "YEAR")) {
                DateTime endOfYearDay = DateUtil.endOfYear(now);
                unitCount = endOfYearDay.getField(DateField.DAY_OF_YEAR) - now.getField(DateField.DAY_OF_YEAR) + 1;
            }
        }
        String key = this.calculateTransIdPrefix(prefix, precisionTo, middle);
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        long value = atomicLong.incrementAndGet();
        if (value == 1L) {
            atomicLong.expire(unitCount, unit);
        }
        return key + StrUtil.padPre(String.valueOf(value), idWidth, "0");
    }

    /**
     * 为指定的键设置流水号的初始值。
     *
     * @param key         指定的键。
     * @param initalValue 初始值。
     */
    public void initTransId(String key, Long initalValue) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        atomicLong.set(initalValue);
    }

    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key   缓存的键。
     * @param id    数据Id。
     * @param f     获取数据的方法。
     * @param clazz 数据对象类型。
     * @return 数据对象。
     */
    public <M> M getFromCache(String key, Serializable id, Function<Serializable, M> f, Class<M> clazz) {
        return this.getFromCache(key, id, f, clazz, null);
    }

    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key    缓存的键。
     * @param filter mybatis plus的过滤对象。
     * @param f      获取数据的方法。
     * @param clazz  数据对象类型。
     * @return 数据对象。
     */
    public <N> N getFromCache(
            String key, LambdaQueryWrapper<N> filter, Function<LambdaQueryWrapper<N>, N> f, Class<N> clazz) {
        N m;
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            m = f.apply(filter);
            if (m != null) {
                bucket.set(JSON.toJSONString(m));
            }
        } else {
            m = JSON.parseObject(bucket.get(), clazz);
        }
        return m;
    }

    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key    缓存的键。
     * @param filter 过滤对象。
     * @param f      获取数据的方法。
     * @param clazz  数据对象类型。
     * @return 数据对象。
     */
    public <M, N> N getFromCache(String key, M filter, Function<M, N> f, Class<N> clazz) {
        N m;
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            m = f.apply(filter);
            if (m != null) {
                bucket.set(JSON.toJSONString(m));
            }
        } else {
            m = JSON.parseObject(bucket.get(), clazz);
        }
        return m;
    }

    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key     缓存的键。
     * @param id      数据Id。
     * @param f       获取数据的方法。
     * @param clazz   数据对象类型。
     * @param seconds 过期秒数。
     * @return 数据对象。
     */
    public <M> M getFromCache(
            String key, Serializable id, Function<Serializable, M> f, Class<M> clazz, Integer seconds) {
        M m;
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            m = f.apply(id);
            if (m != null) {
                bucket.set(JSON.toJSONString(m));
                if (seconds != null) {
                    bucket.expire(seconds, TimeUnit.SECONDS);
                }
            }
        } else {
            m = JSON.parseObject(bucket.get(), clazz);
        }
        return m;
    }

    /**
     * 移除指定Key。
     *
     * @param key 键名。
     */
    public void evictFormCache(String key) {
        redissonClient.getBucket(key).delete();
    }
}
