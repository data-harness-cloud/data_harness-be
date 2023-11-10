package supie.common.dict.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import supie.common.core.annotation.MyDataSource;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.base.service.BaseService;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.TokenData;
import supie.common.core.util.RedisKeyUtil;
import supie.common.dict.constant.GlobalDictItemStatus;
import supie.common.dict.dao.TenantGlobalDictMapper;
import supie.common.dict.model.TenantGlobalDict;
import supie.common.dict.model.TenantGlobalDictItem;
import supie.common.dict.service.TenantGlobalDictItemService;
import supie.common.dict.service.TenantGlobalDictService;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 租户全局字典数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@MyDataSource(ApplicationConstant.TENANT_COMMON_DATASOURCE_TYPE)
@Slf4j
@Service("tenantGlobalDictService")
public class TenantGlobalDictServiceImpl
        extends BaseService<TenantGlobalDict, Long> implements TenantGlobalDictService {

    @Autowired
    private TenantGlobalDictMapper tenantGlobalDictMapper;
    @Autowired
    private TenantGlobalDictItemService tenantGlobalDictItemService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<TenantGlobalDict> mapper() {
        return tenantGlobalDictMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TenantGlobalDict saveNew(TenantGlobalDict dict, Set<Long> tenantIdSet) {
        String initialData = dict.getInitialData();
        dict.setDictId(idGenerator.nextLongId());
        dict.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        dict.setCreateUserId(TokenData.takeFromRequest().getUserId());
        dict.setUpdateUserId(dict.getCreateUserId());
        dict.setCreateTime(new Date());
        dict.setUpdateTime(dict.getCreateTime());
        if (BooleanUtil.isTrue(dict.getTenantCommon())) {
            dict.setInitialData(null);
        }
        tenantGlobalDictMapper.insert(dict);
        List<TenantGlobalDictItem> dictItemList = null;
        if (StrUtil.isNotBlank(initialData)) {
            dictItemList = JSONArray.parseArray(initialData, TenantGlobalDictItem.class);
            dictItemList.forEach(dictItem -> {
                dictItem.setDictCode(dict.getDictCode());
                dictItem.setCreateUserId(dict.getCreateUserId());
            });
        }
        if (BooleanUtil.isTrue(dict.getTenantCommon())) {
            tenantGlobalDictItemService.saveNewBatch(dictItemList);
        } else {
            if (CollUtil.isEmpty(tenantIdSet) || dictItemList == null) {
                return dict;
            }
            for (Long tenantId : tenantIdSet) {
                dictItemList.forEach(dictItem -> {
                    dictItem.setId(idGenerator.nextLongId());
                    dictItem.setTenantId(tenantId);
                });
                tenantGlobalDictItemService.saveNewBatch(dictItemList);
            }
        }
        return dict;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(TenantGlobalDict dict, TenantGlobalDict originalDict) {
        this.removeGlobalDictAllCache(originalDict);
        dict.setCreateUserId(originalDict.getCreateUserId());
        dict.setCreateTime(originalDict.getCreateTime());
        dict.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        dict.setUpdateTime(new Date());
        if (tenantGlobalDictMapper.updateById(dict) != 1) {
            return false;
        }
        if (!StrUtil.equals(dict.getDictCode(), originalDict.getDictCode())) {
            tenantGlobalDictItemService.updateNewCode(originalDict.getDictCode(), dict.getDictCode());
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long dictId) {
        TenantGlobalDict dict = this.getById(dictId);
        if (dict == null) {
            return false;
        }
        this.removeGlobalDictAllCache(dict);
        if (tenantGlobalDictMapper.deleteById(dictId) == 0) {
            return false;
        }
        TenantGlobalDictItem filter = new TenantGlobalDictItem();
        filter.setDictCode(dict.getDictCode());
        tenantGlobalDictItemService.removeBy(filter);
        return true;
    }

    @Override
    public List<TenantGlobalDict> getGlobalDictList(TenantGlobalDict filter, String orderBy) {
        LambdaQueryWrapper<TenantGlobalDict> queryWrapper = new LambdaQueryWrapper<>(filter);
        if (StrUtil.isNotBlank(orderBy)) {
            queryWrapper.last(" ORDER BY " + orderBy);
        }
        return tenantGlobalDictMapper.selectList(queryWrapper);
    }

    @Override
    public boolean existDictCode(String dictCode) {
        LambdaQueryWrapper<TenantGlobalDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantGlobalDict::getDictCode, dictCode);
        return tenantGlobalDictMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public TenantGlobalDict getTenantGlobalDictByDictCode(String dictCode) {
        LambdaQueryWrapper<TenantGlobalDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantGlobalDict::getDictCode, dictCode);
        return tenantGlobalDictMapper.selectOne(queryWrapper);
    }

    @Override
    public TenantGlobalDict getTenantGlobalDictFromCache(String dictCode) {
        String key = RedisKeyUtil.makeGlobalDictOnlyKey(dictCode);
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (bucket.isExists()) {
            return JSON.parseObject(bucket.get(), TenantGlobalDict.class);
        }
        TenantGlobalDict dict = this.getTenantGlobalDictByDictCode(dictCode);
        if (dict != null) {
            bucket.set(JSON.toJSONString(dict));
        }
        return dict;
    }

    @Override
    public List<TenantGlobalDictItem> getGlobalDictItemListFromCache(TenantGlobalDict dict, Set<Serializable> itemIds) {
        if (CollUtil.isNotEmpty(itemIds) && !(itemIds.iterator().next() instanceof String)) {
            itemIds = itemIds.stream().map(Object::toString).collect(Collectors.toSet());
        }
        String key = RedisKeyUtil.makeGlobalDictKey(dict.getDictCode());
        if (BooleanUtil.isFalse(dict.getTenantCommon())) {
            key = this.appendTenantSuffix(key);
        }
        List<TenantGlobalDictItem> dataList;
        RMap<Serializable, String> cachedMap = redissonClient.getMap(key);
        if (cachedMap.isExists()) {
            Map<Serializable, String> dataMap =
                    CollUtil.isEmpty(itemIds) ? cachedMap.readAllMap() : cachedMap.getAll(itemIds);
            dataList = dataMap.values().stream()
                    .map(c -> JSON.parseObject(c, TenantGlobalDictItem.class)).collect(Collectors.toList());
            dataList.sort(Comparator.comparingInt(TenantGlobalDictItem::getShowOrder));
        } else {
            dataList = tenantGlobalDictItemService.getGlobalDictItemList(dict);
            this.putCache(dict, dataList);
            if (CollUtil.isNotEmpty(itemIds)) {
                Set<Serializable> tmpItemIds = itemIds;
                dataList = dataList.stream()
                        .filter(c -> tmpItemIds.contains(c.getItemId())).collect(Collectors.toList());
            }
        }
        return dataList;
    }

    @Override
    public Map<Serializable, String> getGlobalDictItemDictMapFromCache(
            TenantGlobalDict dict, Set<Serializable> itemIds) {
        List<TenantGlobalDictItem> dataList = this.getGlobalDictItemListFromCache(dict, itemIds);
        return dataList.stream()
                .collect(Collectors.toMap(TenantGlobalDictItem::getItemId, TenantGlobalDictItem::getItemName));
    }

    @Override
    public void reloadCachedData(TenantGlobalDict dict) {
        this.removeCache(dict);
        List<TenantGlobalDictItem> dataList = tenantGlobalDictItemService.getGlobalDictItemList(dict);
        this.putCache(dict, dataList);
    }

    @Override
    public void reloadAllTenantCachedData(TenantGlobalDict dict) {
        if (StrUtil.isBlank(dict.getDictCode())) {
            return;
        }
        String dictCodeKey = RedisKeyUtil.makeGlobalDictKey(dict.getDictCode());
        redissonClient.getKeys().deleteByPattern(dictCodeKey + "*");
        TenantGlobalDictItem filter = new TenantGlobalDictItem();
        filter.setDictCode(dict.getDictCode());
        List<TenantGlobalDictItem> dictItemList =
                tenantGlobalDictItemService.getGlobalDictItemList(filter, null);
        if (CollUtil.isEmpty(dictItemList)) {
            return;
        }
        Map<Serializable, List<TenantGlobalDictItem>> dictItemMap =
                dictItemList.stream().collect(Collectors.groupingBy(TenantGlobalDictItem::getTenantId));
        for (Map.Entry<Serializable, List<TenantGlobalDictItem>> entry : dictItemMap.entrySet()) {
            String key = dictCodeKey + "-" + entry.getKey();
            Map<Serializable, String> dataMap = entry.getValue().stream()
                    .collect(Collectors.toMap(TenantGlobalDictItem::getItemId, JSON::toJSONString));
            RMap<Serializable, String> cachedMap = redissonClient.getMap(key);
            cachedMap.putAll(dataMap);
            cachedMap.expire(1, TimeUnit.DAYS);
        }
    }

    @Override
    public void removeCache(TenantGlobalDict dict) {
        if (StrUtil.isBlank(dict.getDictCode())) {
            return;
        }
        String key = RedisKeyUtil.makeGlobalDictKey(dict.getDictCode());
        if (BooleanUtil.isFalse(dict.getTenantCommon())) {
            key = this.appendTenantSuffix(key);
        }
        redissonClient.getMap(key).delete();
    }

    private void putCache(TenantGlobalDict dict, List<TenantGlobalDictItem> dictItemList) {
        if (CollUtil.isEmpty(dictItemList)) {
            return;
        }
        String key = RedisKeyUtil.makeGlobalDictKey(dict.getDictCode());
        if (BooleanUtil.isFalse(dict.getTenantCommon())) {
            key = this.appendTenantSuffix(key);
        }
        Map<Serializable, String> dataMap = dictItemList.stream()
                .filter(item -> item.getStatus() == GlobalDictItemStatus.NORMAL)
                .collect(Collectors.toMap(TenantGlobalDictItem::getItemId, JSON::toJSONString));
        if (MapUtil.isNotEmpty(dataMap)) {
            RMap<Serializable, String> cachedMap = redissonClient.getMap(key);
            cachedMap.putAll(dataMap);
            cachedMap.expire(1, TimeUnit.DAYS);
        }
    }

    private String appendTenantSuffix(String key) {
        return key + "-" + TokenData.takeFromRequest().getTenantId();
    }

    private void removeGlobalDictAllCache(TenantGlobalDict dict) {
        String dictCode = dict.getDictCode();
        if (StrUtil.isBlank(dictCode)) {
            return;
        }
        String key = RedisKeyUtil.makeGlobalDictOnlyKey(dictCode);
        redissonClient.getBucket(key).delete();
        key = RedisKeyUtil.makeGlobalDictKey(dictCode);
        if (BooleanUtil.isTrue(dict.getTenantCommon())) {
            redissonClient.getMap(key).delete();
        } else {
            redissonClient.getKeys().deleteByPatternAsync(key + "*");
        }
    }
}
