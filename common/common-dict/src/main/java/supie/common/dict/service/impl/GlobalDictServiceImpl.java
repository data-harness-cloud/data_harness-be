package supie.common.dict.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.base.service.BaseService;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.TokenData;
import supie.common.core.util.RedisKeyUtil;
import supie.common.dict.constant.GlobalDictItemStatus;
import supie.common.dict.dao.GlobalDictMapper;
import supie.common.dict.model.GlobalDict;
import supie.common.dict.model.GlobalDictItem;
import supie.common.dict.service.GlobalDictItemService;
import supie.common.dict.service.GlobalDictService;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 全局字典数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("globalDictService")
public class GlobalDictServiceImpl extends BaseService<GlobalDict, Long> implements GlobalDictService {

    @Autowired
    private GlobalDictMapper globalDictMapper;
    @Autowired
    private GlobalDictItemService globalDictItemService;
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
    protected BaseDaoMapper<GlobalDict> mapper() {
        return globalDictMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GlobalDict saveNew(GlobalDict globalDict) {
        globalDict.setDictId(idGenerator.nextLongId());
        globalDict.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        globalDict.setCreateUserId(TokenData.takeFromRequest().getUserId());
        globalDict.setUpdateUserId(globalDict.getCreateUserId());
        globalDict.setCreateTime(new Date());
        globalDict.setUpdateTime(globalDict.getCreateTime());
        globalDictMapper.insert(globalDict);
        return globalDict;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(GlobalDict globalDict, GlobalDict originalGlobalDict) {
        this.removeCache(originalGlobalDict.getDictCode());
        globalDict.setCreateUserId(originalGlobalDict.getCreateUserId());
        globalDict.setCreateTime(originalGlobalDict.getCreateTime());
        globalDict.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        globalDict.setUpdateTime(new Date());
        if (globalDictMapper.updateById(globalDict) != 1) {
            return false;
        }
        if (!StrUtil.equals(globalDict.getDictCode(), originalGlobalDict.getDictCode())) {
            globalDictItemService.updateNewCode(originalGlobalDict.getDictCode(), globalDict.getDictCode());
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long dictId) {
        GlobalDict globalDict = this.getById(dictId);
        if (globalDict == null) {
            return false;
        }
        this.removeCache(globalDict.getDictCode());
        if (globalDictMapper.deleteById(dictId) == 0) {
            return false;
        }
        GlobalDictItem filter = new GlobalDictItem();
        filter.setDictCode(globalDict.getDictCode());
        globalDictItemService.removeBy(filter);
        return true;
    }

    @Override
    public List<GlobalDict> getGlobalDictList(GlobalDict filter, String orderBy) {
        LambdaQueryWrapper<GlobalDict> queryWrapper = new LambdaQueryWrapper<>(filter);
        if (StrUtil.isNotBlank(orderBy)) {
            queryWrapper.last(" ORDER BY " + orderBy);
        }
        return globalDictMapper.selectList(queryWrapper);
    }

    @Override
    public boolean existDictCode(String dictCode) {
        LambdaQueryWrapper<GlobalDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GlobalDict::getDictCode, dictCode);
        return globalDictMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean existDictItemFromCache(String dictCode, Serializable itemId) {
        return null != this.getGlobalDictItemListFromCache(dictCode, CollUtil.newHashSet(itemId));
    }

    @Override
    public List<GlobalDictItem> getGlobalDictItemListFromCache(String dictCode, Set<Serializable> itemIds) {
        if (CollUtil.isNotEmpty(itemIds) && !(itemIds.iterator().next() instanceof String)) {
            itemIds = itemIds.stream().map(Object::toString).collect(Collectors.toSet());
        }
        List<GlobalDictItem> dataList;
        RMap<Serializable, String> cachedMap =
                redissonClient.getMap(RedisKeyUtil.makeGlobalDictKey(dictCode));
        if (cachedMap.isExists()) {
            Map<Serializable, String> dataMap =
                    CollUtil.isEmpty(itemIds) ? cachedMap.readAllMap() : cachedMap.getAll(itemIds);
            dataList = dataMap.values().stream()
                    .map(c -> JSON.parseObject(c, GlobalDictItem.class)).collect(Collectors.toList());
            dataList.sort(Comparator.comparingInt(GlobalDictItem::getShowOrder));
        } else {
            dataList = globalDictItemService.getGlobalDictItemListByDictCode(dictCode);
            this.putCache(dictCode, dataList);
            if (CollUtil.isNotEmpty(itemIds)) {
                Set<Serializable> tmpItemIds = itemIds;
                dataList = dataList.stream()
                        .filter(c -> tmpItemIds.contains(c.getItemId())).collect(Collectors.toList());
            }
        }
        return dataList;
    }

    @Override
    public Map<Serializable, String> getGlobalDictItemDictMapFromCache(String dictCode, Set<Serializable> itemIds) {
        List<GlobalDictItem> dataList = this.getGlobalDictItemListFromCache(dictCode, itemIds);
        return dataList.stream().collect(Collectors.toMap(GlobalDictItem::getItemId, GlobalDictItem::getItemName));
    }

    @Override
    public void reloadCachedData(String dictCode) {
        this.removeCache(dictCode);
        List<GlobalDictItem> dataList = globalDictItemService.getGlobalDictItemListByDictCode(dictCode);
        this.putCache(dictCode, dataList);
    }

    @Override
    public void removeCache(String dictCode) {
        if (StrUtil.isNotBlank(dictCode)) {
            redissonClient.getMap(RedisKeyUtil.makeGlobalDictKey(dictCode)).delete();
        }
    }

    private void putCache(String dictCode, List<GlobalDictItem> globalDictItemList) {
        if (CollUtil.isNotEmpty(globalDictItemList)) {
            Map<Serializable, String> dataMap = globalDictItemList.stream()
                    .filter(item -> item.getStatus() == GlobalDictItemStatus.NORMAL)
                    .collect(Collectors.toMap(GlobalDictItem::getItemId, JSON::toJSONString));
            if (MapUtil.isNotEmpty(dataMap)) {
                redissonClient.getMap(RedisKeyUtil.makeGlobalDictKey(dictCode)).putAll(dataMap);
            }
        }
    }
}
