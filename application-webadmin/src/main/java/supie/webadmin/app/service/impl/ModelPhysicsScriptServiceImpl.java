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
import supie.webadmin.app.service.databasemanagement.Strategy;

import java.util.*;

/**
 * 数据规划-模型设计-模型物理表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("modelPhysicsScriptService")
public class ModelPhysicsScriptServiceImpl extends BaseService<ModelPhysicsScript, Long> implements ModelPhysicsScriptService {

    @Autowired
    private ModelPhysicsScriptMapper modelPhysicsScriptMapper;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private ModelLogicalMainService modelLogicalMainService;
	    @Autowired
    private PlanningWarehouseLayerService planningWarehouseLayerService;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<ModelPhysicsScript> mapper() {
        return modelPhysicsScriptMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param modelPhysicsScript 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ModelPhysicsScript saveNew(ModelPhysicsScript modelPhysicsScript) {
        modelPhysicsScriptMapper.insert(this.buildDefaultValue(modelPhysicsScript));
        return modelPhysicsScript;
    }

    /**
     * 新增物理表数据，并且创建物理表
     *
     * @param modelPhysicsScript
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ModelPhysicsScript addAndCreatingTable(ModelPhysicsScript modelPhysicsScript) {
        modelPhysicsScriptMapper.insert(this.buildDefaultValue(modelPhysicsScript));
        // 创建物理表
        if (modelPhysicsScript.getModelPhysicsState() == 2) {
            throw new RuntimeException("表[" + modelPhysicsScript.getModelPhysicsTableName() + "]已经存在于[" + modelPhysicsScript.getModelPhysicsDatabase() + "]数据库,无法使用该功能创建.");
        }
        this.executeCreatingTableSql(modelPhysicsScript);
        return modelPhysicsScript;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param modelPhysicsScriptList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<ModelPhysicsScript> modelPhysicsScriptList) {
        if (CollUtil.isNotEmpty(modelPhysicsScriptList)) {
            modelPhysicsScriptList.forEach(this::buildDefaultValue);
            modelPhysicsScriptMapper.insertList(modelPhysicsScriptList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param modelPhysicsScript         更新的对象。
     * @param originalModelPhysicsScript 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(ModelPhysicsScript modelPhysicsScript, ModelPhysicsScript originalModelPhysicsScript) {
        MyModelUtil.fillCommonsForUpdate(modelPhysicsScript, originalModelPhysicsScript);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<ModelPhysicsScript> uw = this.createUpdateQueryForNullValue(modelPhysicsScript, modelPhysicsScript.getId());
        return modelPhysicsScriptMapper.update(modelPhysicsScript, uw) == 1;
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
        return modelPhysicsScriptMapper.deleteById(id) == 1;
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
        ModelPhysicsScript deletedObject = new ModelPhysicsScript();
        deletedObject.setModelId(modelId);
        return modelPhysicsScriptMapper.delete(new QueryWrapper<>(deletedObject));
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getModelPhysicsScriptListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelPhysicsScript> getModelPhysicsScriptList(ModelPhysicsScript filter, String orderBy) {
        return modelPhysicsScriptMapper.getModelPhysicsScriptList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getModelPhysicsScriptList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelPhysicsScript> getModelPhysicsScriptListWithRelation(ModelPhysicsScript filter, String orderBy) {
        List<ModelPhysicsScript> resultList = modelPhysicsScriptMapper.getModelPhysicsScriptList(filter, orderBy);
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
    public List<ModelPhysicsScript> getGroupedModelPhysicsScriptListWithRelation(
            ModelPhysicsScript filter, String groupSelect, String groupBy, String orderBy) {
        List<ModelPhysicsScript> resultList =
                modelPhysicsScriptMapper.getGroupedModelPhysicsScriptList(filter, groupSelect, groupBy, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        // NOTE: 这里只是包含了关联数据，聚合计算数据没有包含。
        // 主要原因是，由于聚合字段通常被视为普通字段使用，不会在group by的从句中出现，语义上也不会在此关联。
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param modelPhysicsScript 最新数据对象。
     * @param originalModelPhysicsScript 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(ModelPhysicsScript modelPhysicsScript, ModelPhysicsScript originalModelPhysicsScript) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是一对多的验证
        if (this.needToVerify(modelPhysicsScript, originalModelPhysicsScript, ModelPhysicsScript::getPlanningWarehouseLayerId)
                && !planningWarehouseLayerService.existId(modelPhysicsScript.getPlanningWarehouseLayerId())) {
            return CallResult.error(String.format(errorMessageFormat, "分层id"));
        }
        return CallResult.ok();
    }

    private ModelPhysicsScript buildDefaultValue(ModelPhysicsScript modelPhysicsScript) {
        if (modelPhysicsScript.getId() == null) {
            modelPhysicsScript.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(modelPhysicsScript);
        modelPhysicsScript.setIsDelete(GlobalDeletedFlag.NORMAL);
        MyModelUtil.setDefaultValue(modelPhysicsScript, "modelPhysicsTableName", "");
        return modelPhysicsScript;
    }

    /**
     * 执行创建表 SQL
     *
     * @param modelPhysicsScript 模型物理脚本
     * @author 王立宏
     * @date 2023/11/03 05:38
     */
    @Override
    public void executeCreatingTableSql(ModelPhysicsScript modelPhysicsScript) {
        String createTableSql = modelPhysicsScript.getModelPhysicsTable();
        Long planningWarehouseLayerId = modelPhysicsScript.getPlanningWarehouseLayerId();
        if (planningWarehouseLayerId == null) throw new RuntimeException("未关联分层数据，请联系管理员！");
        Strategy strategy = modelLogicalMainService.getModelLogicalMainStrategy(planningWarehouseLayerId);
        List<Map<String, Object>> mapList = strategy.executeSqlList(createTableSql);
        if (mapList.size() == 2) {
            Map<String, Object> createTableResult = mapList.get(1);
            if (Boolean.FALSE.equals(createTableResult.get("success"))) {
                throw new RuntimeException(createTableResult.get("message").toString());
            }
        }
        // 建表成功，状态设置成 已建表
        modelPhysicsScript.setModelPhysicsState(2);
        modelPhysicsScriptMapper.updateById(modelPhysicsScript);
    }
}
