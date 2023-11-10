package supie.common.core.cache;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;

/**
 * 树形字典数据内存缓存对象。
 *
 * @param <K> 字典表主键类型。
 * @param <V> 字典表对象类型。
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class MapTreeDictionaryCache<K, V> extends MapDictionaryCache<K, V> {

    /**
     * 树形数据存储对象。
     */
    private final Multimap<K, V> allTreeMap = LinkedHashMultimap.create();
    /**
     * 获取字典父主键数据的函数对象。
     */
    protected final Function<V, K> parentIdGetter;

    /**
     * 当前对象的构造器函数。
     *
     * @param idGetter       获取当前类主键字段值的函数对象。
     * @param parentIdGetter 获取当前类父主键字段值的函数对象。
     * @param <K>            字典主键类型。
     * @param <V>            字典对象类型
     * @return 实例化后的树形字典内存缓存对象。
     */
    public static <K, V> MapTreeDictionaryCache<K, V> create(Function<V, K> idGetter, Function<V, K> parentIdGetter) {
        if (idGetter == null) {
            throw new IllegalArgumentException("IdGetter can't be NULL.");
        }
        if (parentIdGetter == null) {
            throw new IllegalArgumentException("ParentIdGetter can't be NULL.");
        }
        return new MapTreeDictionaryCache<>(idGetter, parentIdGetter);
    }

    /**
     * 构造函数。
     *
     * @param idGetter       获取当前类主键字段值的函数对象。
     * @param parentIdGetter 获取当前类父主键字段值的函数对象。
     */
    public MapTreeDictionaryCache(Function<V, K> idGetter, Function<V, K> parentIdGetter) {
        super(idGetter);
        this.parentIdGetter = parentIdGetter;
    }

    @Override
    public void reload(List<V> dataList, boolean force) {
        if (!force && this.getCount() > 0) {
            return;
        }
        this.safeWrite("reload", () -> {
            dataMap.clear();
            allTreeMap.clear();
            dataList.forEach(data -> {
                K id = idGetter.apply(data);
                dataMap.put(id, data);
                K parentId = parentIdGetter.apply(data);
                allTreeMap.put(parentId, data);
            });
            return null;
        });
    }

    @Override
    public List<V> getListByParentId(K parentId) {
        return this.safeRead("getListByParentId", () -> {
            List<V> resultList = new LinkedList<>();
            Collection<V> children = allTreeMap.get(parentId);
            if (CollUtil.isNotEmpty(children)) {
                resultList.addAll(children);
            }
            return resultList;
        });
    }

    @Override
    public void put(K id, V data) {
        this.safeWrite("put", () -> {
            dataMap.put(id, data);
            K parentId = parentIdGetter.apply(data);
            allTreeMap.remove(parentId, data);
            allTreeMap.put(parentId, data);
            return null;
        });
    }

    @Override
    public V invalidate(K id) {
        return this.safeWrite("invalidate", () -> {
            V v = dataMap.remove(id);
            if (v != null) {
                K parentId = parentIdGetter.apply(v);
                allTreeMap.remove(parentId, v);
            }
            return v;
        });
    }

    @Override
    public void invalidateSet(Set<K> keys) {
        this.safeWrite("invalidateSet", () -> {
            keys.forEach(id -> {
                if (id != null) {
                    V data = dataMap.remove(id);
                    if (data != null) {
                        K parentId = parentIdGetter.apply(data);
                        allTreeMap.remove(parentId, data);
                    }
                }
            });
            return null;
        });
    }

    @Override
    public void invalidateAll() {
        this.safeWrite("invalidateAll", () -> {
            dataMap.clear();
            allTreeMap.clear();
            return null;
        });
    }
}
