package supie.common.redis.cache;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.exception.RedisCacheAccessException;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.redisson.api.RListMultimap;
import org.redisson.api.RedissonClient;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树形字典数据Redis缓存对象。
 *
 * @param <K> 字典表主键类型。
 * @param <V> 字典表对象类型。
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class RedisTreeDictionaryCache<K, V> extends RedisDictionaryCache<K, V> {

    /**
     * 树形数据存储对象。
     */
    private final RListMultimap<K, String> allTreeMap;
    /**
     * 获取字典父主键数据的函数对象。
     */
    protected final Function<V, K> parentIdGetter;

    /**
     * 当前对象的构造器函数。
     *
     * @param redissonClient Redisson的客户端对象。
     * @param dictionaryName 字典表的名称。等同于redis hash对象的key。
     * @param valueClazz     值对象的Class对象。
     * @param idGetter       获取当前类主键字段值的函数对象。
     * @param parentIdGetter 获取当前类父主键字段值的函数对象。
     * @param <K>            字典主键类型。
     * @param <V>            字典对象类型
     * @return 实例化后的树形字典内存缓存对象。
     */
    public static <K, V> RedisTreeDictionaryCache<K, V> create(
            RedissonClient redissonClient,
            String dictionaryName,
            Class<V> valueClazz,
            Function<V, K> idGetter,
            Function<V, K> parentIdGetter) {
        if (idGetter == null) {
            throw new IllegalArgumentException("IdGetter can't be NULL.");
        }
        if (parentIdGetter == null) {
            throw new IllegalArgumentException("ParentIdGetter can't be NULL.");
        }
        return new RedisTreeDictionaryCache<>(
                redissonClient, dictionaryName, valueClazz, idGetter, parentIdGetter);
    }

    /**
     * 构造函数。
     *
     * @param redissonClient Redisson的客户端对象。
     * @param dictionaryName 字典表的名称。等同于redis hash对象的key。
     * @param valueClazz     值对象的Class对象。
     * @param idGetter       获取当前类主键字段值的函数对象。
     * @param parentIdGetter 获取当前类父主键字段值的函数对象。
     */
    public RedisTreeDictionaryCache(
            RedissonClient redissonClient,
            String dictionaryName,
            Class<V> valueClazz,
            Function<V, K> idGetter,
            Function<V, K> parentIdGetter) {
        super(redissonClient, dictionaryName, valueClazz, idGetter);
        this.allTreeMap = redissonClient.getListMultimap(
                DICT_PREFIX + dictionaryName + ApplicationConstant.TREE_DICT_CACHE_NAME_SUFFIX);
        this.parentIdGetter = parentIdGetter;
    }

    @Override
    public List<V> getListByParentId(K parentId) {
        List<String> dataList;
        String exceptionMessage;
        try {
            dataList = allTreeMap.get(parentId);
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "Operation of [RedisTreeDictionaryCache::getListByParentId] encountered EXCEPTION [%s] for DICT [%s].",
                    e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
        if (CollUtil.isEmpty(dataList)) {
            return new LinkedList<>();
        }
        return dataList.stream().map(data -> JSON.parseObject(data, valueClazz)).collect(Collectors.toList());
    }
    
    @Override
    public void reload(List<V> dataList, boolean force) {
        String exceptionMessage;
        try {
            // 如果不强制刷新，需要先判断缓存中是否存在数据。
            if (!force && this.getCount() > 0) {
                return;
            }
            dataMap.clear();
            allTreeMap.clear();
            if (CollUtil.isEmpty(dataList)) {
                return;
            }
            Map<K, String> map = dataList.stream().collect(Collectors.toMap(idGetter, JSON::toJSONString));
            // 这里现在本地内存构建树形数据关系，然后再批量存入到Redis缓存。
            // 以便减少与Redis的交互，同时提升运行时效率。
            Multimap<K, String> treeMap = LinkedListMultimap.create();
            for (V data : dataList) {
                treeMap.put(parentIdGetter.apply(data), JSON.toJSONString(data));
            }
            dataMap.putAll(map, 3000);
            for (Map.Entry<K, Collection<String>> entry : treeMap.asMap().entrySet()) {
                allTreeMap.putAll(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "Operation of [RedisDictionaryCache::reload] encountered EXCEPTION [%s] for DICT [%s].",
                    e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }

    @Override
    public void put(K id, V data) {
        if (id == null || data == null) {
            return;
        }
        String stringData = JSON.toJSONString(data);
        K parentId = parentIdGetter.apply(data);
        String exceptionMessage;
        try {
            String oldData = dataMap.put(id, stringData);
            if (oldData != null) {
                allTreeMap.remove(parentId, oldData);
            }
            allTreeMap.put(parentId, stringData);
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "Operation of [RedisTreeDictionaryCache::put] encountered EXCEPTION [%s] for DICT [%s].",
                    e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }

    @Override
    public V invalidate(K id) {
        if (id == null) {
            return null;
        }
        V data = null;
        String exceptionMessage;
        try {
            String stringData = dataMap.remove(id);
            if (stringData != null) {
                data = JSON.parseObject(stringData, valueClazz);
                K parentId = parentIdGetter.apply(data);
                allTreeMap.remove(parentId, stringData);
            }
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "Operation of [RedisTreeDictionaryCache::invalidate] encountered EXCEPTION [%s] for DICT [%s].",
                    e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
        return data;
    }

    @Override
    public void invalidateSet(Set<K> keys) {
        if (CollUtil.isEmpty(keys)) {
            return;
        }
        String exceptionMessage;
        try {
            keys.forEach(id -> {
                if (id != null) {
                    String stringData = dataMap.remove(id);
                    if (stringData != null) {
                        K parentId = parentIdGetter.apply(JSON.parseObject(stringData, valueClazz));
                        allTreeMap.remove(parentId, stringData);
                    }
                }
            });
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "Operation of [RedisTreeDictionaryCache::invalidateSet] encountered EXCEPTION [%s] for DICT [%s].",
                    e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }

    @Override
    public void invalidateAll() {
        String exceptionMessage;
        try {
            dataMap.clear();
            allTreeMap.clear();
        } catch (Exception e) {
            exceptionMessage = String.format(
                    "Operation of [RedisTreeDictionaryCache::invalidateAll] encountered EXCEPTION [%s] for DICT [%s].",
                    e.getClass().getSimpleName(), valueClazz.getSimpleName());
            log.warn(exceptionMessage);
            throw new RedisCacheAccessException(exceptionMessage, e);
        }
    }
}
