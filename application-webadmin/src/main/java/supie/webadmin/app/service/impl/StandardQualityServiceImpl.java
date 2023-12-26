package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import supie.webadmin.app.service.*;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.model.*;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.MyRelationParam;
import supie.common.core.base.service.BaseService;
import supie.common.core.util.MyModelUtil;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据规划-数据标准-数据质量表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("standardQualityService")
public class StandardQualityServiceImpl extends BaseService<StandardQuality, Long> implements StandardQualityService {

    @Autowired
    private StandardQualityMapper standardQualityMapper;
    @Autowired
    private StandardFieldQualityMapper standardFieldQualityMapper;
    @Autowired
    private StandardFieldQualityService standardFieldQualityService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<StandardQuality> mapper() {
        return standardQualityMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param standardQuality 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StandardQuality saveNew(StandardQuality standardQuality) {
        standardQualityMapper.insert(this.buildDefaultValue(standardQuality));
        return standardQuality;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param standardQualityList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<StandardQuality> standardQualityList) {
        if (CollUtil.isNotEmpty(standardQualityList)) {
            standardQualityList.forEach(this::buildDefaultValue);
            standardQualityMapper.insertList(standardQualityList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param standardQuality         更新的对象。
     * @param originalStandardQuality 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(StandardQuality standardQuality, StandardQuality originalStandardQuality) {
        MyModelUtil.fillCommonsForUpdate(standardQuality, originalStandardQuality);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<StandardQuality> uw = this.createUpdateQueryForNullValue(standardQuality, standardQuality.getId());
        return standardQualityMapper.update(standardQuality, uw) == 1;
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
        if (standardQualityMapper.deleteById(id) == 0) {
            return false;
        }
        // 开始删除多对多父表的关联
        StandardFieldQuality standardFieldQuality = new StandardFieldQuality();
        standardFieldQuality.setStaidardQualityId(id);
        standardFieldQualityMapper.delete(new QueryWrapper<>(standardFieldQuality));
        return true;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getStandardQualityListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<StandardQuality> getStandardQualityList(StandardQuality filter, String orderBy) {
        return standardQualityMapper.getStandardQualityList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getStandardQualityList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<StandardQuality> getStandardQualityListWithRelation(StandardQuality filter, String orderBy) {
        List<StandardQuality> resultList = standardQualityMapper.getStandardQualityList(filter, orderBy);
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
    public List<StandardQuality> getGroupedStandardQualityListWithRelation(
            StandardQuality filter, String groupSelect, String groupBy, String orderBy) {
        List<StandardQuality> resultList =
                standardQualityMapper.getGroupedStandardQualityList(filter, groupSelect, groupBy, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        // NOTE: 这里只是包含了关联数据，聚合计算数据没有包含。
        // 主要原因是，由于聚合字段通常被视为普通字段使用，不会在group by的从句中出现，语义上也不会在此关联。
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 在多对多关系中，当前Service的数据表为从表，返回不与指定主表主键Id存在对多对关系的列表。
     *
     * @param staidardFieldId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<StandardQuality> getNotInStandardQualityListByStaidardFieldId(Long staidardFieldId, StandardQuality filter, String orderBy) {
        List<StandardQuality> resultList;
        if (staidardFieldId != null) {
            resultList = standardQualityMapper.getNotInStandardQualityListByStaidardFieldId(staidardFieldId, filter, orderBy);
        } else {
            resultList = getStandardQualityList(filter, orderBy);
        }
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        return resultList;
    }

    /**
     * 在多对多关系中，当前Service的数据表为从表，返回与指定主表主键Id存在对多对关系的列表。
     *
     * @param staidardFieldId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param standardFieldQualityFilter 多对多关联表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<StandardQuality> getStandardQualityListByStaidardFieldId(
            Long staidardFieldId, StandardQuality filter, StandardFieldQuality standardFieldQualityFilter, String orderBy) {
        List<StandardQuality> resultList =
                standardQualityMapper.getStandardQualityListByStaidardFieldId(staidardFieldId, filter, standardFieldQualityFilter, orderBy);
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        // 这里是为中间表StandardFieldQuality中关联的静态字典，进行基于注解的数据绑定。
        List<StandardFieldQuality> standardFieldQualityList = resultList.stream()
                .filter(c -> c.getStandardFieldQuality() != null)
                .map(StandardQuality::getStandardFieldQuality).collect(Collectors.toList());
        standardFieldQualityService.buildRelationForDataList(
                standardFieldQualityList, MyRelationParam.dictOnly(), CollUtil.newHashSet("staidardFieldId", "staidardQualityId"));
        return resultList;
    }

    private StandardQuality buildDefaultValue(StandardQuality standardQuality) {
        if (standardQuality.getId() == null) {
            standardQuality.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(standardQuality);
        standardQuality.setIsDelete(GlobalDeletedFlag.NORMAL);
        return standardQuality;
    }
}
