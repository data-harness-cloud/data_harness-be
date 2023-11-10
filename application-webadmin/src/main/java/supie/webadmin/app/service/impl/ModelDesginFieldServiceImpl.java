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
import java.util.stream.Collectors;

/**
 * 数据规划-模型设计-模型字段表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("modelDesginFieldService")
public class ModelDesginFieldServiceImpl extends BaseService<ModelDesginField, Long> implements ModelDesginFieldService {

    @Autowired
    private ModelDesginFieldMapper modelDesginFieldMapper;
    @Autowired
    private DefinitionIndexModelFieldRelationMapper definitionIndexModelFieldRelationMapper;
    @Autowired
    private ModelLogicalMainService modelLogicalMainService;
    @Autowired
    private DefinitionIndexModelFieldRelationService definitionIndexModelFieldRelationService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<ModelDesginField> mapper() {
        return modelDesginFieldMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param modelDesginField 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ModelDesginField saveNew(ModelDesginField modelDesginField) {
        modelDesginFieldMapper.insert(this.buildDefaultValue(modelDesginField));
        return modelDesginField;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param modelDesginFieldList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<ModelDesginField> modelDesginFieldList) {
        if (CollUtil.isNotEmpty(modelDesginFieldList)) {
            modelDesginFieldList.forEach(this::buildDefaultValue);
            modelDesginFieldMapper.insertList(modelDesginFieldList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param modelDesginField         更新的对象。
     * @param originalModelDesginField 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(ModelDesginField modelDesginField, ModelDesginField originalModelDesginField) {
        MyModelUtil.fillCommonsForUpdate(modelDesginField, originalModelDesginField);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<ModelDesginField> uw = this.createUpdateQueryForNullValue(modelDesginField, modelDesginField.getId());
        return modelDesginFieldMapper.update(modelDesginField, uw) == 1;
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
        if (modelDesginFieldMapper.deleteById(id) == 0) {
            return false;
        }
        // 开始删除多对多父表的关联
        DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation = new DefinitionIndexModelFieldRelation();
        definitionIndexModelFieldRelation.setModelFieldId(id);
        definitionIndexModelFieldRelationMapper.delete(new QueryWrapper<>(definitionIndexModelFieldRelation));
        return true;
    }

    /**
     * 当前服务的支持表为从表，根据主表的关联Id，删除一对多的从表数据。
     *
     * @param modelId 从表关联字段。
     * @return 删除数量。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeByModelId(Long modelId) {
        ModelDesginField deletedObject = new ModelDesginField();
        deletedObject.setModelId(modelId);
        return modelDesginFieldMapper.delete(new QueryWrapper<>(deletedObject));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBatchByModelId(Long modelId, List<ModelDesginField> dataList) {
        this.updateBatchOneToManyRelation("modelId", modelId,
                null, null, dataList, this::saveNewBatch);
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getModelDesginFieldListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelDesginField> getModelDesginFieldList(ModelDesginField filter, String orderBy) {
        return modelDesginFieldMapper.getModelDesginFieldList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getModelDesginFieldList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelDesginField> getModelDesginFieldListWithRelation(ModelDesginField filter, String orderBy) {
        List<ModelDesginField> resultList = modelDesginFieldMapper.getModelDesginFieldList(filter, orderBy);
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
    public List<ModelDesginField> getGroupedModelDesginFieldListWithRelation(
            ModelDesginField filter, String groupSelect, String groupBy, String orderBy) {
        List<ModelDesginField> resultList =
                modelDesginFieldMapper.getGroupedModelDesginFieldList(filter, groupSelect, groupBy, orderBy);
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
     * @param indexId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelDesginField> getNotInModelDesginFieldListByIndexId(Long indexId, ModelDesginField filter, String orderBy) {
        List<ModelDesginField> resultList;
        if (indexId != null) {
            resultList = modelDesginFieldMapper.getNotInModelDesginFieldListByIndexId(indexId, filter, orderBy);
        } else {
            resultList = getModelDesginFieldList(filter, orderBy);
        }
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        return resultList;
    }

    /**
     * 在多对多关系中，当前Service的数据表为从表，返回与指定主表主键Id存在对多对关系的列表。
     *
     * @param indexId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param definitionIndexModelFieldRelationFilter 多对多关联表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelDesginField> getModelDesginFieldListByIndexId(
            Long indexId, ModelDesginField filter, DefinitionIndexModelFieldRelation definitionIndexModelFieldRelationFilter, String orderBy) {
        List<ModelDesginField> resultList =
                modelDesginFieldMapper.getModelDesginFieldListByIndexId(indexId, filter, definitionIndexModelFieldRelationFilter, orderBy);
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        // 这里是为中间表DefinitionIndexModelFieldRelation中关联的静态字典，进行基于注解的数据绑定。
        List<DefinitionIndexModelFieldRelation> definitionIndexModelFieldRelationList = resultList.stream()
                .filter(c -> c.getDefinitionIndexModelFieldRelation() != null)
                .map(ModelDesginField::getDefinitionIndexModelFieldRelation).collect(Collectors.toList());
        definitionIndexModelFieldRelationService.buildRelationForDataList(
                definitionIndexModelFieldRelationList, MyRelationParam.dictOnly(), CollUtil.newHashSet("indexId", "modelFieldId"));
        return resultList;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param modelDesginField 最新数据对象。
     * @param originalModelDesginField 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(ModelDesginField modelDesginField, ModelDesginField originalModelDesginField) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是一对多的验证
        if (this.needToVerify(modelDesginField, originalModelDesginField, ModelDesginField::getModelId)
                && !modelLogicalMainService.existId(modelDesginField.getModelId())) {
            return CallResult.error(String.format(errorMessageFormat, "模型id"));
        }
        return CallResult.ok();
    }

    private ModelDesginField buildDefaultValue(ModelDesginField modelDesginField) {
        if (modelDesginField.getId() == null) {
            modelDesginField.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(modelDesginField);
        modelDesginField.setIsDelete(GlobalDeletedFlag.NORMAL);
        MyModelUtil.setDefaultValue(modelDesginField, "modelFieldDefaultValue", "");
        return modelDesginField;
    }
}
