package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
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

/**
 * 数据开发-指标与模型字段关联表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("definitionIndexModelFieldRelationService")
public class DefinitionIndexModelFieldRelationServiceImpl extends BaseService<DefinitionIndexModelFieldRelation, Long> implements DefinitionIndexModelFieldRelationService {

    @Autowired
    private DefinitionIndexModelFieldRelationMapper definitionIndexModelFieldRelationMapper;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<DefinitionIndexModelFieldRelation> mapper() {
        return definitionIndexModelFieldRelationMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param definitionIndexModelFieldRelation 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DefinitionIndexModelFieldRelation saveNew(DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation) {
        definitionIndexModelFieldRelationMapper.insert(this.buildDefaultValue(definitionIndexModelFieldRelation));
        return definitionIndexModelFieldRelation;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param definitionIndexModelFieldRelationList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<DefinitionIndexModelFieldRelation> definitionIndexModelFieldRelationList) {
        if (CollUtil.isNotEmpty(definitionIndexModelFieldRelationList)) {
            definitionIndexModelFieldRelationList.forEach(this::buildDefaultValue);
            definitionIndexModelFieldRelationMapper.insertList(definitionIndexModelFieldRelationList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param definitionIndexModelFieldRelation         更新的对象。
     * @param originalDefinitionIndexModelFieldRelation 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation, DefinitionIndexModelFieldRelation originalDefinitionIndexModelFieldRelation) {
        MyModelUtil.fillCommonsForUpdate(definitionIndexModelFieldRelation, originalDefinitionIndexModelFieldRelation);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<DefinitionIndexModelFieldRelation> uw = this.createUpdateQueryForNullValue(definitionIndexModelFieldRelation, definitionIndexModelFieldRelation.getId());
        return definitionIndexModelFieldRelationMapper.update(definitionIndexModelFieldRelation, uw) == 1;
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
        return definitionIndexModelFieldRelationMapper.deleteById(id) == 1;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getDefinitionIndexModelFieldRelationListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<DefinitionIndexModelFieldRelation> getDefinitionIndexModelFieldRelationList(DefinitionIndexModelFieldRelation filter, String orderBy) {
        return definitionIndexModelFieldRelationMapper.getDefinitionIndexModelFieldRelationList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getDefinitionIndexModelFieldRelationList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<DefinitionIndexModelFieldRelation> getDefinitionIndexModelFieldRelationListWithRelation(DefinitionIndexModelFieldRelation filter, String orderBy) {
        List<DefinitionIndexModelFieldRelation> resultList = definitionIndexModelFieldRelationMapper.getDefinitionIndexModelFieldRelationList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    private DefinitionIndexModelFieldRelation buildDefaultValue(DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation) {
        if (definitionIndexModelFieldRelation.getId() == null) {
            definitionIndexModelFieldRelation.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(definitionIndexModelFieldRelation);
        definitionIndexModelFieldRelation.setIsDelete(GlobalDeletedFlag.NORMAL);
        return definitionIndexModelFieldRelation;
    }
}
