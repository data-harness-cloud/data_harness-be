package supie.common.core.cache;

import cn.hutool.core.map.MapUtil;
import supie.common.core.exception.MapCacheAccessException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 字典数据内存缓存对象。
 *
 * @param <K> 字典表主键类型。
 * @param <V> 字典表对象类型。
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class MapDictionaryCache<K, V> implements DictionaryCache<K, V> {

    /**
     * 存储字典数据的Map。
     */
    protected final LinkedHashMap<K, V> dataMap = new LinkedHashMap<>();
    /**
     * 获取字典主键数据的函数对象。
     */
    protected final Function<V, K> idGetter;
    /**
     * 由于大部分场景是读取操作，所以使用读写锁提高并发的伸缩性。
     */
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * 超时时长。单位毫秒。
     */
    protected static final long TIMEOUT = 2000L;

    /**
     * 当前对象的构造器函数。
     *
     * @param idGetter 获取当前类主键字段值的函数对象。
     * @param <K>      字典主键类型。
     * @param <V>      字典对象类型
     * @return 实例化后的字典内存缓存对象。
     */
    public static <K, V> MapDictionaryCache<K, V> create(Function<V, K> idGetter) {
        if (idGetter == null) {
            throw new IllegalArgumentException("IdGetter can't be NULL.");
        }
        return new MapDictionaryCache<>(idGetter);
    }

    /**
     * 构造函数。
     *
     * @param idGetter 主键Id的获取函数对象。
     */
    public MapDictionaryCache(Function<V, K> idGetter) {
        this.idGetter = idGetter;
    }

    @Override
    public List<V> getAll() {
        return this.safeRead("getAll", () -> {
            List<V> resultList = new LinkedList<>();
            if (MapUtil.isNotEmpty(dataMap)) {
                resultList.addAll(dataMap.values());
            }
            return resultList;
        });
    }

    @Override
    public List<V> getInList(Set<K> keys) {
        return this.safeRead("getInList", () -> {
            List<V> resultList = new LinkedList<>();
            keys.forEach(key -> {
                V object = dataMap.get(key);
                if (object != null) {
                    resultList.add(object);
                }
            });
            return resultList;
        });
    }

    @Override
    public V get(K id) {
        if (id == null) {
            return null;
        }
        return this.safeRead("get", () -> dataMap.get(id));
    }

    @Override
    public void reload(List<V> dataList, boolean force) {
        if (!force && this.getCount() > 0) {
            return;
        }
        this.safeWrite("reload", () -> {
            dataMap.clear();
            dataList.forEach(dataObj -> {
                K id = idGetter.apply(dataObj);
                dataMap.put(id, dataObj);
            });
            return null;
        });
    }

    @Override
    public void put(K id, V object) {
        this.safeWrite("put", () -> dataMap.put(id, object));
    }

    @Override
    public int getCount() {
        return dataMap.size();
    }

    @Override
    public V invalidate(K id) {
        if (id == null) {
            return null;
        }
        return this.safeWrite("invalidate", () -> dataMap.remove(id));
    }

    @Override
    public void invalidateSet(Set<K> keys) {
        this.safeWrite("invalidateSet", () -> {
            keys.forEach(id -> {
                if (id != null) {
                    dataMap.remove(id);
                }
            });
            return null;
        });
    }

    @Override
    public void invalidateAll() {
        this.safeWrite("invalidateAll", () -> {
            dataMap.clear();
            return null;
        });
    }

    protected <T> T safeRead(String functionName, Supplier<T> supplier) {
        String exceptionMessage;
        try {
            if (lock.readLock().tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    return supplier.get();
                } finally {
                    lock.readLock().unlock();
                }
            } else {
                throw new TimeoutException();
            }
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            exceptionMessage = String.format(
                    "LOCK Operation of [MapDictionaryCache::%s] encountered EXCEPTION [%s] for DICT.",
                    functionName, e.getClass().getSimpleName());
            log.warn(exceptionMessage);
            throw new MapCacheAccessException(exceptionMessage, e);
        }
    }

    protected <T> T safeWrite(String functionName, Supplier<T> supplier) {
        String exceptionMessage;
        try {
            if (lock.writeLock().tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    return supplier.get();
                } finally {
                    lock.writeLock().unlock();
                }
            } else {
                throw new TimeoutException();
            }
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            exceptionMessage = String.format(
                    "LOCK Operation of [MapDictionaryCache::%s] encountered EXCEPTION [%s] for DICT.",
                    functionName, e.getClass().getSimpleName());
            log.warn(exceptionMessage);
            throw new MapCacheAccessException(exceptionMessage, e);
        }
    }
}
