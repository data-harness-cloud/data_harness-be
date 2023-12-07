package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import supie.webadmin.app.service.*;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.model.*;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.MyRelationParam;
import supie.common.core.object.CallResult;
import supie.common.core.base.service.BaseService;
import supie.common.core.util.MyModelUtil;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 数据规划-数据标准-数据字段标准数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("standardFieldService")
public class StandardFieldServiceImpl extends BaseService<StandardField, Long> implements StandardFieldService {

    @Autowired
    private StandardFieldMapper standardFieldMapper;
    @Autowired
    private StandardFieldQualityMapper standardFieldQualityMapper;
    @Autowired
    private StandardMainService standardMainService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<StandardField> mapper() {
        return standardFieldMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param standardField 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StandardField saveNew(StandardField standardField) {
        standardFieldMapper.insert(this.buildDefaultValue(standardField));
        return standardField;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param standardFieldList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<StandardField> standardFieldList) {
        if (CollUtil.isNotEmpty(standardFieldList)) {
            standardFieldList.forEach(this::buildDefaultValue);
            standardFieldMapper.insertList(standardFieldList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StandardField saveNewWithRelation(StandardField standardField, JSONObject relationData) {
        this.saveNew(standardField);
        this.saveOrUpdateRelationData(standardField, relationData);
        return standardField;
    }

    /**
     * 更新数据对象。
     *
     * @param standardField         更新的对象。
     * @param originalStandardField 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(StandardField standardField, StandardField originalStandardField) {
        MyModelUtil.fillCommonsForUpdate(standardField, originalStandardField);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<StandardField> uw = this.createUpdateQueryForNullValue(standardField, standardField.getId());
        return standardFieldMapper.update(standardField, uw) == 1;
    }

    private void saveOrUpdateRelationData(StandardField standardField, JSONObject relationData) {
        List<StandardFieldQuality> standardFieldQualityList =
                relationData.getObject("standardFieldQualityList", new TypeReference<List<StandardFieldQuality>>() {});
        // 对于多对多更新，因为中间表没有其他业务字段，这里就走先删除后插入的高效处理方式了。
        if (standardFieldQualityList != null) {
            StandardFieldQuality standardFieldQuality = new StandardFieldQuality();
            standardFieldQuality.setStaidardFieldId(standardField.getId());
            standardFieldQualityMapper.delete(new QueryWrapper<>(standardFieldQuality));
            if (CollUtil.isNotEmpty(standardFieldQualityList)) {
                standardFieldQualityList.forEach(o -> {
                    if (o.getId() == null) {
                        o.setId(idGenerator.nextLongId());
                    }
                    o.setStaidardFieldId(standardField.getId());
                });
                standardFieldQualityMapper.insertList(standardFieldQualityList);
            }
        }
    }

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long id) {
        if (standardFieldMapper.deleteById(id) == 0) {
            return false;
        }
        // 开始删除多对多子表的关联
        StandardFieldQuality standardFieldQuality = new StandardFieldQuality();
        standardFieldQuality.setStaidardFieldId(id);
        standardFieldQualityMapper.delete(new QueryWrapper<>(standardFieldQuality));
        return true;
    }

    /**
     * 当前服务的支持表为从表，根据主表的关联Id，删除一对多的从表数据。
     *
     * @param staidardId 从表关联字段。
     * @return 删除数量。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeByStaidardId(Long staidardId) {
        StandardField deletedObject = new StandardField();
        deletedObject.setStaidardId(staidardId);
        return standardFieldMapper.delete(new QueryWrapper<>(deletedObject));
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getStandardFieldListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<StandardField> getStandardFieldList(StandardField filter, String orderBy) {
        return standardFieldMapper.getStandardFieldList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getStandardFieldList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<StandardField> getStandardFieldListWithRelation(StandardField filter, String orderBy) {
        List<StandardField> resultList = standardFieldMapper.getStandardFieldList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    @Override
    public List<StandardField> getGroupedStandardFieldListWithRelation(
            StandardField filter, String groupSelect, String groupBy, String orderBy) {
        List<StandardField> resultList =
                standardFieldMapper.getGroupedStandardFieldList(filter, groupSelect, groupBy, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        // NOTE: 这里只是包含了关联数据，聚合计算数据没有包含。
        // 主要原因是，由于聚合字段通常被视为普通字段使用，不会在group by的从句中出现，语义上也不会在此关联。
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 批量添加多对多关联关系。
     *
     * @param standardFieldQualityList 多对多关联表对象集合。
     * @param staidardFieldId 主表Id。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addStandardFieldQualityList(List<StandardFieldQuality> standardFieldQualityList, Long staidardFieldId) {
        for (StandardFieldQuality standardFieldQuality : standardFieldQualityList) {
            standardFieldQuality.setId(idGenerator.nextLongId());
            standardFieldQuality.setStaidardFieldId(staidardFieldId);
            standardFieldQualityMapper.insert(standardFieldQuality);
        }
    }

    /**
     * 更新中间表数据。
     *
     * @param standardFieldQuality 中间表对象。
     * @return 更新成功与否。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStandardFieldQuality(StandardFieldQuality standardFieldQuality) {
        StandardFieldQuality filter = new StandardFieldQuality();
        filter.setStaidardFieldId(standardFieldQuality.getStaidardFieldId());
        filter.setStaidardQualityId(standardFieldQuality.getStaidardQualityId());
        UpdateWrapper<StandardFieldQuality> uw =
                BaseService.createUpdateQueryForNullValue(standardFieldQuality, StandardFieldQuality.class);
        uw.setEntity(filter);
        return standardFieldQualityMapper.update(standardFieldQuality, uw) > 0;
    }

    /**
     * 获取中间表数据。
     *
     * @param staidardFieldId 主表Id。
     * @param staidardQualityId 从表Id。
     * @return 中间表对象。
     */
    @Override
    public StandardFieldQuality getStandardFieldQuality(Long staidardFieldId, Long staidardQualityId) {
        StandardFieldQuality filter = new StandardFieldQuality();
        filter.setStaidardFieldId(staidardFieldId);
        filter.setStaidardQualityId(staidardQualityId);
        return standardFieldQualityMapper.selectOne(new QueryWrapper<>(filter));
    }

    /**
     * 移除单条多对多关系。
     *
     * @param staidardFieldId 主表Id。
     * @param staidardQualityId 从表Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeStandardFieldQuality(Long staidardFieldId, Long staidardQualityId) {
        StandardFieldQuality filter = new StandardFieldQuality();
        filter.setStaidardFieldId(staidardFieldId);
        filter.setStaidardQualityId(staidardQualityId);
        return standardFieldQualityMapper.delete(new QueryWrapper<>(filter)) > 0;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param standardField 最新数据对象。
     * @param originalStandardField 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(StandardField standardField, StandardField originalStandardField) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是一对多的验证
        if (this.needToVerify(standardField, originalStandardField, StandardField::getStaidardId)
                && !standardMainService.existId(standardField.getStaidardId())) {
            return CallResult.error(String.format(errorMessageFormat, "标准主表id"));
        }
        return CallResult.ok();
    }

    private StandardField buildDefaultValue(StandardField standardField) {
        if (standardField.getId() == null) {
            standardField.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(standardField);
        standardField.setIsDelete(GlobalDeletedFlag.NORMAL);
        return standardField;
    }
}
