package supie.common.dict.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.base.service.BaseService;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.TokenData;
import supie.common.dict.constant.GlobalDictItemStatus;
import supie.common.dict.dao.GlobalDictItemMapper;
import supie.common.dict.model.GlobalDictItem;
import supie.common.dict.service.GlobalDictItemService;
import supie.common.dict.service.GlobalDictService;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 全局字典项目数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("globalDictItemService")
public class GlobalDictItemServiceImpl
        extends BaseService<GlobalDictItem, Long> implements GlobalDictItemService {

    @Autowired
    private GlobalDictItemMapper globalDictItemMapper;
    @Autowired
    private GlobalDictService globalDictService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<GlobalDictItem> mapper() {
        return globalDictItemMapper;
    }

    @Override
    public GlobalDictItem saveNew(GlobalDictItem globalDictItem) {
        globalDictService.removeCache(globalDictItem.getDictCode());
        globalDictItem.setId(idGenerator.nextLongId());
        globalDictItem.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        globalDictItem.setStatus(GlobalDictItemStatus.NORMAL);
        globalDictItem.setCreateUserId(TokenData.takeFromRequest().getUserId());
        globalDictItem.setUpdateUserId(globalDictItem.getCreateUserId());
        globalDictItem.setCreateTime(new Date());
        globalDictItem.setUpdateTime(globalDictItem.getCreateTime());
        globalDictItemMapper.insert(globalDictItem);
        return globalDictItem;
    }

    @Override
    public boolean update(GlobalDictItem globalDictItem, GlobalDictItem originalGlobalDictItem) {
        globalDictService.removeCache(globalDictItem.getDictCode());
        // 该方法不能直接修改字典状态。
        globalDictItem.setStatus(originalGlobalDictItem.getStatus());
        globalDictItem.setCreateUserId(originalGlobalDictItem.getCreateUserId());
        globalDictItem.setCreateTime(originalGlobalDictItem.getCreateTime());
        globalDictItem.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        globalDictItem.setUpdateTime(new Date());
        return globalDictItemMapper.updateById(globalDictItem) == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNewCode(String oldCode, String newCode) {
        GlobalDictItem globalDictItem = new GlobalDictItem();
        globalDictItem.setDictCode(newCode);
        LambdaQueryWrapper<GlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GlobalDictItem::getDictCode, oldCode);
        globalDictItemMapper.update(globalDictItem, queryWrapper);
    }

    @Override
    public void updateStatus(GlobalDictItem globalDictItem, Integer status) {
        globalDictService.removeCache(globalDictItem.getDictCode());
        globalDictItem.setStatus(status);
        globalDictItem.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        globalDictItem.setUpdateTime(new Date());
        globalDictItemMapper.updateById(globalDictItem);
    }

    @Override
    public boolean remove(GlobalDictItem globalDictItem) {
        globalDictService.removeCache(globalDictItem.getDictCode());
        return this.removeById(globalDictItem.getId());
    }

    @Override
    public boolean existDictCodeAndItemId(String dictCode, Serializable itemId) {
        LambdaQueryWrapper<GlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GlobalDictItem::getDictCode, dictCode);
        queryWrapper.eq(GlobalDictItem::getItemId, itemId.toString());
        return globalDictItemMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public GlobalDictItem getGlobalDictItemByDictCodeAndItemId(String dictCode, Serializable itemId) {
        LambdaQueryWrapper<GlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GlobalDictItem::getDictCode, dictCode);
        queryWrapper.eq(GlobalDictItem::getItemId, itemId.toString());
        return globalDictItemMapper.selectOne(queryWrapper);
    }

    @Override
    public List<GlobalDictItem> getGlobalDictItemList(GlobalDictItem filter, String orderBy) {
        LambdaQueryWrapper<GlobalDictItem> queryWrapper = new LambdaQueryWrapper<>(filter);
        if (StrUtil.isNotBlank(orderBy)) {
            queryWrapper.last(" ORDER BY " + orderBy);
        } else {
            queryWrapper.orderByAsc(GlobalDictItem::getShowOrder);
        }
        return globalDictItemMapper.selectList(queryWrapper);
    }

    @Override
    public List<GlobalDictItem> getGlobalDictItemListByDictCode(String dictCode) {
        LambdaQueryWrapper<GlobalDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GlobalDictItem::getDictCode, dictCode);
        queryWrapper.orderByAsc(GlobalDictItem::getShowOrder);
        return globalDictItemMapper.selectList(queryWrapper);
    }
}
