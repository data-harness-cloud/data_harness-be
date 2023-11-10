package supie.common.redis.cache;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import supie.common.core.cache.DictionaryCache;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.exception.RedisCacheAccessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典数据Redis缓存对象。
 *
 * @param <K> 字典表主键类型。
 * @param <V> 字典表对象类型。
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class RedisDictionaryCache<K, V> implements DictionaryCache<K, V> {

    /**
     * 字典数据前缀，便于Redis工具分组显示。
     */
    protected static final String DICT_PREFIX = "DICT-TABLE:";
    /**
     * redisson客户端。
     */
    protected final RedissonClient redissonClient;
    /**
     * 数据存储对象。
     */
    protected final RMap<K, String> dataMap;
    /**
     * 字典值对象类型。
     */
    protected final Class<V> valueClazz;
    /**
     * 获取字典主键数据的函数对象。
     */
    protected final Function<V, K> idGetter;

    /**
     * 当前对象的构造器函数。
     *
     * @param redissonClient Redisson的客户端对象。
     * @param dictionaryName 字典表的名称。等同于redis hash对象的key。
     * @param valueClazz     值对象的Class对象。
     * @param idGetter       获取当前类主键字段值的函数对象。
     * @param <K>            字典主键类型。
     * @param <V>            字典对象类型
     * @return 实例化后的字典内存缓存对象。
     */
    public static <K, V> RedisDictionaryCache<K, V> create(
            RedissonClient redissonClient,
            String dictionaryName,
            Class<V> valueClazz,
            Function<V, K> idGetter) {
        if (idGetter == null) {
            throw new IllegalArgumentException("IdGetter can't be NULL.");
        }
        return new RedisDictionaryCache<>(redissonClient, dictionaryName, valueClazz, idGetter);
    }

    /**
     * 构造函数。
     *
     * @param redissonClient Redisson的客户端对象。
     * @param dictionaryName 字典表的名称。等同于redis hash对象的key。确保全局唯一。
     * @param valueClazz     值对象的Class对象。
     * @param idGetter       获取当前类主键字段值的函数对象。
     */
    public RedisDictionaryCache(
            RedissonClient redissonClient,
            String dictionaryName,
            Class<V> valueClazz,
            Function<V, K> idGetter) {
        this.redissonClient = redissonClient;
        this.dataMap = redissonClient.getMap(
                DICT_PREFIX + dictionaryName + ApplicationConstant.DICT_CACHE_NAME_SUFFIX);
        this.valueClazz = valueClazz;
        this.idGetter = idGetter;
    }

    protected RMap<K, String> getDataMap() {
        return dataMap;
    }

    @Override
    public List<V> getAll() {
        Collection<String> dataList;
        String exceptionMessage;
        try {
            dataList = getDataMap().readAllValues();
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::getAll] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
        if (dataList == null) {
            return new LinkedList<>();
        }
        return dataList.stream()
                .map(data -> JSON.parseObject(data, valueClazz))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<V> getInList(Set<K> keys) {
        if (CollUtil.isEmpty(keys)) {
            return new LinkedList<>();
        }
        Collection<String> dataList;
        String exceptionMessage;
        try {
            dataList = getDataMap().getAll(keys).values();
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::getInList] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
        if (dataList == null) {
            return new LinkedList<>();
        }
        return dataList.stream()
                .map(data -> JSON.parseObject(data, valueClazz))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public V get(K id) {
        if (id == null) {
            return null;
        }
        String data;
        String exceptionMessage;
        try {
            data = getDataMap().get(id);
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::get] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data, valueClazz);
    }

    @Override
    public int getCount() {
        return getDataMap().size();
    }

    @Override
    public void put(K id, V data) {
        if (id == null || data == null) {
            return;
        }
        String exceptionMessage;
        try {
            getDataMap().fastPut(id, JSON.toJSONString(data));
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::put] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }

    @Override
    public void reload(List<V> dataList, boolean force) {
        String exceptionMessage;
        try {
            // 如果不强制刷新，需要先判断缓存中是否存在数据。
            if (!force && this.getCount() > 0) {
                return;
            }
            Map<K, String> map = null;
            if (CollUtil.isNotEmpty(dataList)) {
                map = dataList.stream().collect(Collectors.toMap(idGetter, JSON::toJSONString));
            }
            RMap<K, String> localDataMap = getDataMap();
            localDataMap.clear();
            if (map != null) {
                localDataMap.putAll(map);
            }
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::reload] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }

    @Override
    public V invalidate(K id) {
        if (id == null) {
            return null;
        }
        String data;
        String exceptionMessage;
        try {
            data = getDataMap().remove(id);
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::invalidate] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data, valueClazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void invalidateSet(Set<K> keys) {
        if (CollUtil.isEmpty(keys)) {
            return;
        }
        Object[] keyArray = keys.toArray(new Object[]{});
        String exceptionMessage;
        try {
            getDataMap().fastRemove((K[]) keyArray);
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::invalidateSet] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }

    @Override
    public void invalidateAll() {
        String exceptionMessage;
        try {
            getDataMap().clear();
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "[%s::invalidateAll] encountered EXCEPTION [%s] for DICT [%s].",
                    this.getClass().getSimpleName(), e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }
}
