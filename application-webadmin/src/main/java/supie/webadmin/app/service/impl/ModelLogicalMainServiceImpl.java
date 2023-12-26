package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;
import supie.webadmin.app.util.sqlUtil.TableGenerator;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 数据规划-模型设计-模型逻辑表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("modelLogicalMainService")
public class ModelLogicalMainServiceImpl extends BaseService<ModelLogicalMain, Long> implements ModelLogicalMainService {

    @Autowired
    private ModelLogicalMainMapper modelLogicalMainMapper;
    @Autowired
    private ModelDesginFieldService modelDesginFieldService;
    @Autowired
    private ModelPhysicsScriptService modelPhysicsScriptService;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private ModelDesginFieldMapper modelDesginFieldMapper;
    @Autowired
    private PlanningWarehouseLayerMapper planningWarehouseLayerMapper;
    @Autowired
    private ProjectMainMapper projectMainMapper;
    @Autowired
    private ProjectEngineMapper projectEngineMapper;
    @Autowired
    private StrategyFactory strategyFactory;

    @Resource
    private  PlanningThemeMapper planningThemeMapper;

    @Resource
    private  PlanningClassificationMapper planningClassificationMapper;

    @Resource
    private DefinitionIndexMapper definitionIndexMapper;
    @Resource
    private  DefinitionDimensionMapper definitionDimensionMapper;
    @Resource
    private  StandardMainMapper standardMainMapper;


    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<ModelLogicalMain> mapper() {
        return modelLogicalMainMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param modelLogicalMain 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ModelLogicalMain saveNew(ModelLogicalMain modelLogicalMain) {
        modelLogicalMainMapper.insert(this.buildDefaultValue(modelLogicalMain));
        return modelLogicalMain;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param modelLogicalMainList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<ModelLogicalMain> modelLogicalMainList) {
        if (CollUtil.isNotEmpty(modelLogicalMainList)) {
            modelLogicalMainList.forEach(this::buildDefaultValue);
            modelLogicalMainMapper.insertList(modelLogicalMainList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ModelLogicalMain saveNewWithRelation(ModelLogicalMain modelLogicalMain, JSONObject relationData) {
        this.saveNew(modelLogicalMain);
        this.saveOrUpdateOneToOneRelationData(modelLogicalMain, relationData);
        this.saveOrUpdateRelationData(modelLogicalMain, relationData);
        return modelLogicalMain;
    }

    /**
     * 更新数据对象。
     *
     * @param modelLogicalMain         更新的对象。
     * @param originalModelLogicalMain 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(ModelLogicalMain modelLogicalMain, ModelLogicalMain originalModelLogicalMain) {
        MyModelUtil.fillCommonsForUpdate(modelLogicalMain, originalModelLogicalMain);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<ModelLogicalMain> uw = this.createUpdateQueryForNullValue(modelLogicalMain, modelLogicalMain.getId());
        return modelLogicalMainMapper.update(modelLogicalMain, uw) == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateWithRelation(
            ModelLogicalMain modelLogicalMain, ModelLogicalMain originalModelLogicalMain, JSONObject relationData) {
        // modelLogicalMain 为空的时候，无需修改主表数据。
        if (modelLogicalMain != null && !this.update(modelLogicalMain, originalModelLogicalMain)) {
            return false;
        }
        this.saveOrUpdateOneToOneRelationData(originalModelLogicalMain, relationData);
        this.saveOrUpdateRelationData(originalModelLogicalMain, relationData);
        return true;
    }

    private void saveOrUpdateOneToOneRelationData(ModelLogicalMain modelLogicalMain, JSONObject relationData) {
        // 对于一对一新增或更新，如果主键值为空就新增，否则就更新，同时更新updateTime和updateUserId。
        ModelPhysicsScript modelPhysicsScript = relationData.getObject("modelPhysicsScript", ModelPhysicsScript.class);
        if (modelPhysicsScript != null) {
            modelPhysicsScript.setModelId(modelLogicalMain.getId());
            modelPhysicsScriptService.saveNewOrUpdate(modelPhysicsScript,
                    modelPhysicsScriptService::saveNew, modelPhysicsScriptService::update);
        }
    }

    private void saveOrUpdateRelationData(ModelLogicalMain modelLogicalMain, JSONObject relationData) {
        List<ModelDesginField> modelDesginFieldList =
                relationData.getObject("modelDesginFieldList", new TypeReference<List<ModelDesginField>>() {});
        // 对于一对多更新，分为以下三步：
        // 1. 在关联从表中，删除掉与主表字段关联，但是又没有出现在本地更新中的数据。我们将这些数据视为需要删除的数据。
        // 2. 在本次更新数据列表中，如果从表的对象没有主键Id，我们视为新数据，可以批量插入。
        // 3. 在本次更新数据列表中，如果从表的对象存在主键Id，我们视为已有数据，逐条更新。
        if (modelDesginFieldList != null) {
            modelDesginFieldService.updateBatchByModelId(modelLogicalMain.getId(), modelDesginFieldList);
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
        if (modelLogicalMainMapper.deleteById(id) == 0) {
            return false;
        }
        modelPhysicsScriptService.removeByModelId(id);
        modelDesginFieldService.removeByModelId(id);
        return true;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getModelLogicalMainListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelLogicalMain> getModelLogicalMainList(ModelLogicalMain filter, String orderBy) {
        return modelLogicalMainMapper.getModelLogicalMainList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getModelLogicalMainList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ModelLogicalMain> getModelLogicalMainListWithRelation(ModelLogicalMain filter, String orderBy) {
        List<ModelLogicalMain> resultList = modelLogicalMainMapper.getModelLogicalMainList(filter, orderBy);
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
    public List<ModelLogicalMain> getGroupedModelLogicalMainListWithRelation(
            ModelLogicalMain filter, String groupSelect, String groupBy, String orderBy) {
        List<ModelLogicalMain> resultList =
                modelLogicalMainMapper.getGroupedModelLogicalMainList(filter, groupSelect, groupBy, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        // NOTE: 这里只是包含了关联数据，聚合计算数据没有包含。
        // 主要原因是，由于聚合字段通常被视为普通字段使用，不会在group by的从句中出现，语义上也不会在此关联。
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    private ModelLogicalMain buildDefaultValue(ModelLogicalMain modelLogicalMain) {
        if (modelLogicalMain.getId() == null) {
            modelLogicalMain.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(modelLogicalMain);
        modelLogicalMain.setIsDelete(GlobalDeletedFlag.NORMAL);
        return modelLogicalMain;
    }

    /**
     * 根据ID生成建表SQL，并存入 ModelLogicalMain.
     *
     * @param modelLogicalMain ModelLogicalMain
     * @return SQL
     * @author 王立宏
     * @date 2023/10/25 06:07
     */
    @Override
    public Map<String, String> buildSql(ModelLogicalMain modelLogicalMain) {
        List<ModelDesginField> modelDesginFieldList = modelDesginFieldMapper.queryByModelId(modelLogicalMain.getId());
        Map<String, String> sqlMapData = new HashMap<>();
        // 生成建表的SQL
        String createTableSql = TableGenerator.generateCreateTableSQL(
                modelLogicalMain.getModelDatasourceType(), modelLogicalMain, modelDesginFieldList);
        modelLogicalMain.setModelSqlScript(createTableSql);
        // 存入SQL
        updateById(this.buildDefaultValue(modelLogicalMain));
        // 生成修改表的SQL
        String updateTableSql = "";
        // TODO 查询到原来的表结构信息，信息不为空才修改，信息为空表明还没有该表
//        Strategy strategy = getModelLogicalMainStrategy(modelLogicalMain.getWarehouseLayerId());
//        List<Map<String, Object>> tableStructure = strategy.queryTableStructure(modelLogicalMain.getOldModelTableName());
//        String oldCreatTableSql = null;
//        if (modelLogicalMain.getModelSqlScript() != null && StrUtil.isNotEmpty(modelLogicalMain.getModelSqlScript())) {
//            updateTableSql = TableGenerator.generateUpdateTableSQL(
//                    modelLogicalMain.getModelDatasourceType(), modelLogicalMain, modelDesginFieldList);
//        }
        Map<String, String> tableValue = TableGenerator.getTableValue(modelLogicalMain);
        sqlMapData.put("tableCode", tableValue.get("tableCode"));
        sqlMapData.put("createTableSql", createTableSql);
        sqlMapData.put("updateTableSql", updateTableSql);
        return sqlMapData;
    }

    /**
     * 获取 ModelLogicalMain 的数据库
     * @param planningWarehouseLayerId
     * @return
     */
    public Strategy getModelLogicalMainStrategy(Long planningWarehouseLayerId) {
        PlanningWarehouseLayer planningWarehouseLayer =
                planningWarehouseLayerMapper.selectById(planningWarehouseLayerId);
        if (null == planningWarehouseLayer) throw new RuntimeException("未查询到[" + planningWarehouseLayerId + "]分层信息！");
        ProjectMain projectMain = projectMainMapper.selectById(planningWarehouseLayer.getProjectId());
        if (null == projectMain) throw new RuntimeException("未查询到[" + planningWarehouseLayerId + "]分层关联的[" + planningWarehouseLayer.getProjectId() + "]项目信息！");
        ProjectEngine projectEngine = projectEngineMapper.selectById(projectMain.getProjectEngineId());
        if (null == projectEngine) throw new RuntimeException("未查询到[ProjectEngine]信息！");
        // 该处使用的数据库为 数据分层的数据库
        Strategy strategy;
        try {
            strategy = strategyFactory.getStrategy(projectEngine.getEngineType(), projectEngine.getEngineHost(), projectEngine.getEnginePort(),
                    planningWarehouseLayer.getHouseLayerCode(), projectEngine.getEngineUsername(), projectEngine.getEnginePassword(),0);
        } catch (Exception e) {
            if (StrUtil.contains(e.getMessage(), "Unknown database")) {
                // 数据库不存在，创建数据库
                strategy = strategyFactory.getStrategy(projectEngine.getEngineType(), projectEngine.getEngineHost(), projectEngine.getEnginePort(),
                        projectEngine.getEngineName(), projectEngine.getEngineUsername(), projectEngine.getEnginePassword(),0);
                strategy.createDatabase(planningWarehouseLayer.getHouseLayerCode());
                strategy.closeAll();
                return strategyFactory.getStrategy(projectEngine.getEngineType(), projectEngine.getEngineHost(), projectEngine.getEnginePort(),
                        planningWarehouseLayer.getHouseLayerCode(), projectEngine.getEngineUsername(), projectEngine.getEnginePassword(),0);
            }
            throw new RuntimeException(e);
        }
        return strategy;
    }

    @Override
    public Map<String, Object> statisticsTableType(String projectId) {
        // 明细表数量
        Long scheduleNumber = modelLogicalMainMapper.selectCount(new LambdaQueryWrapper<ModelLogicalMain>()
                .eq(ModelLogicalMain::getProjectId, projectId)
                .eq(ModelLogicalMain::getModelTableType, "明细表"));

        // 汇总表数量
        Long summaryNumber = modelLogicalMainMapper.selectCount(new LambdaQueryWrapper<ModelLogicalMain>()
                .eq(ModelLogicalMain::getDataDeptId, projectId)
                .eq(ModelLogicalMain::getModelTableType, "汇总表"));

        // 维度表数量
        Long dimensionNumber = modelLogicalMainMapper.selectCount(new LambdaQueryWrapper<ModelLogicalMain>()
                .eq(ModelLogicalMain::getDataDeptId, projectId)
                .eq(ModelLogicalMain::getModelTableType, "维度表"));


        Long planningThemeNumber = planningThemeMapper.selectCount(new LambdaQueryWrapper<PlanningTheme>()
                .eq(PlanningTheme::getProjectId, projectId));

        Long planningClassificationNumber = planningClassificationMapper.selectCount(new LambdaQueryWrapper<PlanningClassification>()
                .eq(PlanningClassification::getProjectId, projectId));

        Long definitionIndexNumber = definitionIndexMapper.selectCount(new LambdaQueryWrapper<DefinitionIndex>()
                .eq(DefinitionIndex::getProjectId, projectId));

        Long  definitionDimensionNumber = definitionDimensionMapper.selectCount(new LambdaQueryWrapper<DefinitionDimension>()
                .eq(DefinitionDimension::getProjectId, projectId));


        Long standardMainNumber = standardMainMapper.selectCount(new LambdaQueryWrapper<StandardMain>()
                .eq(StandardMain::getProjectId, projectId));



        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("scheduleNumber",scheduleNumber);
        dataMap.put("summaryNumber",summaryNumber);
        dataMap.put("planningThemeNumber",planningThemeNumber);
        dataMap.put("dimensionNumber",dimensionNumber);
        dataMap.put("planningClassificationNumber",planningClassificationNumber);
        dataMap.put("definitionIndexNumber",definitionIndexNumber);
        dataMap.put("definitionDimensionNumber",definitionDimensionNumber);
        dataMap.put("standardMainNumber",standardMainNumber);

        return  dataMap;

    }

    @Override
    public Map<String, BigDecimal> houseLayerNameNumber(Long projectId) {
        // 查询ODS、DWS、DWD、ADS、DDS、DIM数量
        List<String> houseLayerNameList = modelLogicalMainMapper.houseLayerNameNumber(projectId);
        HashMap<String, BigDecimal> dataMap = new HashMap<>();
        dataMap.put("odsNumber", new BigDecimal("0"));
        dataMap.put("dwsNumber", new BigDecimal("0"));
        dataMap.put("dwdNumber", new BigDecimal("0"));
        dataMap.put("adsNumber", new BigDecimal("0"));
        dataMap.put("ddsNumber", new BigDecimal("0"));
        dataMap.put("dimNumber", new BigDecimal("0"));
        for (String houseLayerName : houseLayerNameList) {
            // 判断 houseLayerName 中是否存在指定字符串
            if (StrUtil.contains(houseLayerName, "ODS")) {
                dataMap.put("odsNumber", dataMap.get("odsNumber").add(BigDecimal.ONE));
            } else if (StrUtil.contains(houseLayerName, "DWS")) {
                dataMap.put("dwsNumber", dataMap.get("dwsNumber").add(BigDecimal.ONE));
            } else if (StrUtil.contains(houseLayerName, "DWD")) {
                dataMap.put("dwdNumber", dataMap.get("dwdNumber").add(BigDecimal.ONE));
            } else if (StrUtil.contains(houseLayerName, "ADS")) {
                dataMap.put("adsNumber", dataMap.get("adsNumber").add(BigDecimal.ONE));
            } else if (StrUtil.contains(houseLayerName, "DDS")) {
                dataMap.put("ddsNumber", dataMap.get("ddsNumber").add(BigDecimal.ONE));
            } else if (StrUtil.contains(houseLayerName, "DIM")) {
                dataMap.put("dimNumber", dataMap.get("dimNumber").add(BigDecimal.ONE));
            } else {
                throw new RuntimeException("projectId为[" + projectId + "]的houseLayerName 格式不正确");
            }
        }
        return dataMap;
    }
}
