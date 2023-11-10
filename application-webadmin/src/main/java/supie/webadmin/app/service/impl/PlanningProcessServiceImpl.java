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

/**
 * 数据规划-数据架构-业务过程表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("planningProcessService")
public class PlanningProcessServiceImpl extends BaseService<PlanningProcess, Long> implements PlanningProcessService {

    @Autowired
    private PlanningProcessMapper planningProcessMapper;
    @Autowired
    private DevLiteflowRulerService devLiteflowRulerService;
    @Autowired
    private SchedulingTasksService schedulingTasksService;
    @Autowired
    private ModelLogicalMainService modelLogicalMainService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<PlanningProcess> mapper() {
        return planningProcessMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param planningProcess 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PlanningProcess saveNew(PlanningProcess planningProcess) {
        planningProcessMapper.insert(this.buildDefaultValue(planningProcess));
        return planningProcess;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param planningProcessList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<PlanningProcess> planningProcessList) {
        if (CollUtil.isNotEmpty(planningProcessList)) {
            planningProcessList.forEach(this::buildDefaultValue);
            planningProcessMapper.insertList(planningProcessList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param planningProcess         更新的对象。
     * @param originalPlanningProcess 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(PlanningProcess planningProcess, PlanningProcess originalPlanningProcess) {
        MyModelUtil.fillCommonsForUpdate(planningProcess, originalPlanningProcess);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<PlanningProcess> uw = this.createUpdateQueryForNullValue(planningProcess, planningProcess.getId());
        return planningProcessMapper.update(planningProcess, uw) == 1;
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
        return planningProcessMapper.deleteById(id) == 1;
    }

    /**
     * 当前服务的支持表为从表，根据主表的关联Id，删除一对多的从表数据。
     *
     * @param processThemeId 从表关联字段。
     * @return 删除数量。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeByProcessThemeId(Long processThemeId) {
        PlanningProcess deletedObject = new PlanningProcess();
        deletedObject.setProcessThemeId(processThemeId);
        return planningProcessMapper.delete(new QueryWrapper<>(deletedObject));
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getPlanningProcessListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<PlanningProcess> getPlanningProcessList(PlanningProcess filter, String orderBy) {
        return planningProcessMapper.getPlanningProcessList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getPlanningProcessList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<PlanningProcess> getPlanningProcessListWithRelation(PlanningProcess filter, String orderBy) {
        List<PlanningProcess> resultList = planningProcessMapper.getPlanningProcessList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 判断指定对象是否包含下级对象。
     *
     * @param id 主键Id。
     * @return 存在返回true，否则false。
     */
    @Override
    public boolean hasChildren(Long id) {
        PlanningProcess filter = new PlanningProcess();
        filter.setProcessParentId(id);
        return getCountByFilter(filter) > 0;
    }

    /**
     * 判断从表数据是否存在，如果存在就不能删除主对象，否则可以删除主对象。
     * 适用于主表对从表不是强制级联删除的场景。
     *
     * @param planningProcess 主表对象。
     * @return 没有关联数据返回true，否则false，同时返回具体的提示信息。
     */
    @Override
    public CallResult verifyRelatedDataBeforeDelete(PlanningProcess planningProcess) {
        DevLiteflowRuler devLiteflowRuler = new DevLiteflowRuler();
        devLiteflowRuler.setProcessId(planningProcess.getId());
        if (devLiteflowRulerService.getCountByFilter(devLiteflowRuler) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[DevLiteflowRuler] 存在关联数据！");
        }
        SchedulingTasks schedulingTasks = new SchedulingTasks();
        schedulingTasks.setProjectId(planningProcess.getId());
        if (schedulingTasksService.getCountByFilter(schedulingTasks) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[SchedulingTasks] 存在关联数据！");
        }
        ModelLogicalMain modelLogicalMain = new ModelLogicalMain();
        modelLogicalMain.setProcessId(planningProcess.getId());
        if (modelLogicalMainService.getCountByFilter(modelLogicalMain) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[ModelLogicalMain] 存在关联数据！");
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
    public List<PlanningProcess> getGroupedPlanningProcessListWithRelation(
            PlanningProcess filter, String groupSelect, String groupBy, String orderBy) {
        List<PlanningProcess> resultList =
                planningProcessMapper.getGroupedPlanningProcessList(filter, groupSelect, groupBy, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        // NOTE: 这里只是包含了关联数据，聚合计算数据没有包含。
        // 主要原因是，由于聚合字段通常被视为普通字段使用，不会在group by的从句中出现，语义上也不会在此关联。
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    private PlanningProcess buildDefaultValue(PlanningProcess planningProcess) {
        if (planningProcess.getId() == null) {
            planningProcess.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(planningProcess);
        planningProcess.setIsDelete(GlobalDeletedFlag.NORMAL);
        return planningProcess;
    }
}
