package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import supie.webadmin.app.service.databasemanagement.*;

import java.util.*;

/**
 * 数据规划-数据仓库分层表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("planningWarehouseLayerService")
public class PlanningWarehouseLayerServiceImpl extends BaseService<PlanningWarehouseLayer, Long> implements PlanningWarehouseLayerService {

    @Autowired
    private PlanningWarehouseLayerMapper planningWarehouseLayerMapper;
    @Autowired
    private ModelLogicalMainService modelLogicalMainService;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private ProjectMainMapper projectMainMapper;
    @Autowired
    private ProjectEngineMapper projectEngineMapper;
    @Autowired
    private ModelPhysicsScriptService modelPhysicsScriptService;
    @Autowired
    private StrategyFactory strategyFactory;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<PlanningWarehouseLayer> mapper() {
        return planningWarehouseLayerMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param planningWarehouseLayer 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PlanningWarehouseLayer saveNew(PlanningWarehouseLayer planningWarehouseLayer) {
        planningWarehouseLayerMapper.insert(this.buildDefaultValue(planningWarehouseLayer));
        // 创建数据库
        ProjectEngine projectEngine = getProjectEngineByProjectId(planningWarehouseLayer.getProjectId());
        createDatabaseByProjectEngine(projectEngine, planningWarehouseLayer.getHouseLayerCode());
        return planningWarehouseLayer;
    }

    /**
     * 根据项目id获取项目引擎
     * @param projectId
     * @return
     */
    private ProjectEngine getProjectEngineByProjectId(Long projectId) {
        ProjectMain projectMain = projectMainMapper.selectById(projectId);
        return projectEngineMapper.selectById(projectMain.getProjectEngineId());
    }
    public void createDatabaseByProjectEngine(ProjectEngine projectEngine, String... createDatabaseNameList) {
        String databaseType = projectEngine.getEngineType();
        String hostIp = projectEngine.getEngineHost();
        String hostPort = projectEngine.getEnginePort();
        String userName = projectEngine.getEngineUsername();
        String password = projectEngine.getEnginePassword();
        Strategy strategy = strategyFactory.getStrategy(databaseType, hostIp, hostPort, null, userName, password,0);
        for (String createDatabaseName : createDatabaseNameList) {
            strategy.createDatabase(createDatabaseName); // 创建数据库
        }
        strategy.closeAll();
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param planningWarehouseLayerList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<PlanningWarehouseLayer> planningWarehouseLayerList) {
        if (CollUtil.isNotEmpty(planningWarehouseLayerList)) {
            planningWarehouseLayerList.forEach(this::buildDefaultValue);
            planningWarehouseLayerMapper.insertList(planningWarehouseLayerList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param planningWarehouseLayer         更新的对象。
     * @param originalPlanningWarehouseLayer 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(PlanningWarehouseLayer planningWarehouseLayer, PlanningWarehouseLayer originalPlanningWarehouseLayer) {
        MyModelUtil.fillCommonsForUpdate(planningWarehouseLayer, originalPlanningWarehouseLayer);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<PlanningWarehouseLayer> uw = this.createUpdateQueryForNullValue(planningWarehouseLayer, planningWarehouseLayer.getId());
        return planningWarehouseLayerMapper.update(planningWarehouseLayer, uw) == 1;
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
        return planningWarehouseLayerMapper.deleteById(id) == 1;
    }

    /**
     * 根据项目ID删除数据。
     *
     * @param projectId 项目ID。
     */
    @Override
    public boolean removeByProjectId(Long projectId) {
        return planningWarehouseLayerMapper.deleteByProjectId(projectId) > 0;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getPlanningWarehouseLayerListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<PlanningWarehouseLayer> getPlanningWarehouseLayerList(PlanningWarehouseLayer filter, String orderBy) {
        return planningWarehouseLayerMapper.getPlanningWarehouseLayerList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getPlanningWarehouseLayerList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param modelPhysicsScriptFilter 一对多从表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<PlanningWarehouseLayer> getPlanningWarehouseLayerListWithRelation(PlanningWarehouseLayer filter, ModelPhysicsScript modelPhysicsScriptFilter, String orderBy) {
        List<PlanningWarehouseLayer> resultList =
                planningWarehouseLayerMapper.getPlanningWarehouseLayerListEx(filter, modelPhysicsScriptFilter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 判断从表数据是否存在，如果存在就不能删除主对象，否则可以删除主对象。
     * 适用于主表对从表不是强制级联删除的场景。
     *
     * @param planningWarehouseLayer 主表对象。
     * @return 没有关联数据返回true，否则false，同时返回具体的提示信息。
     */
    @Override
    public CallResult verifyRelatedDataBeforeDelete(PlanningWarehouseLayer planningWarehouseLayer) {
        ModelLogicalMain modelLogicalMain = new ModelLogicalMain();
        modelLogicalMain.setWarehouseLayerId(planningWarehouseLayer.getId());
        if (modelLogicalMainService.getCountByFilter(modelLogicalMain) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[ModelLogicalMain] 存在关联数据！");
        }
        ModelPhysicsScript modelPhysicsScript = new ModelPhysicsScript();
        modelPhysicsScript.setPlanningWarehouseLayerId(planningWarehouseLayer.getId());
        if (modelPhysicsScriptService.getCountByFilter(modelPhysicsScript) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[ModelPhysicsScript] 存在关联数据！");
        }
        return CallResult.ok();
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
    public List<PlanningWarehouseLayer> getGroupedPlanningWarehouseLayerListWithRelation(
            PlanningWarehouseLayer filter, String groupSelect, String groupBy, String orderBy) {
        List<PlanningWarehouseLayer> resultList =
                planningWarehouseLayerMapper.getGroupedPlanningWarehouseLayerList(filter, groupSelect, groupBy, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        // NOTE: 这里只是包含了关联数据，聚合计算数据没有包含。
        // 主要原因是，由于聚合字段通常被视为普通字段使用，不会在group by的从句中出现，语义上也不会在此关联。
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    private PlanningWarehouseLayer buildDefaultValue(PlanningWarehouseLayer planningWarehouseLayer) {
        if (planningWarehouseLayer.getId() == null) {
            planningWarehouseLayer.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(planningWarehouseLayer);
        planningWarehouseLayer.setIsDelete(GlobalDeletedFlag.NORMAL);
        return planningWarehouseLayer;
    }
}
