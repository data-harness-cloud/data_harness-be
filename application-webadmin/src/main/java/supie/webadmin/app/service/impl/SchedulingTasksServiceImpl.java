package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import supie.webadmin.app.liteFlow.QuartzJobCheckCycle;
import supie.webadmin.app.service.*;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.model.*;
import supie.webadmin.upms.service.SysUserService;
import supie.webadmin.upms.service.SysDeptService;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.TokenData;
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
 * 数据开发-任务调度-任务表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("schedulingTasksService")
public class SchedulingTasksServiceImpl extends BaseService<SchedulingTasks, Long> implements SchedulingTasksService {

    public static final Class<QuartzJobCheckCycle> TASK_CLASS = QuartzJobCheckCycle.class;

    @Autowired
    private SchedulingTasksMapper schedulingTasksMapper;
    @Autowired
    private ProjectMainService projectMainService;
    @Autowired
    private PlanningClassificationService planningClassificationService;
    @Autowired
    private PlanningProcessService planningProcessService;
    @Autowired
    private PlanningThemeService planningThemeService;
    @Autowired
    private DevLiteflowRulerService devLiteflowRulerService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<SchedulingTasks> mapper() {
        return schedulingTasksMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param schedulingTasks 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SchedulingTasks saveNew(SchedulingTasks schedulingTasks) {
        schedulingTasksMapper.insert(this.buildDefaultValue(schedulingTasks));
        return schedulingTasks;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param schedulingTasksList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<SchedulingTasks> schedulingTasksList) {
        if (CollUtil.isNotEmpty(schedulingTasksList)) {
            schedulingTasksList.forEach(this::buildDefaultValue);
            schedulingTasksMapper.insertList(schedulingTasksList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param schedulingTasks         更新的对象。
     * @param originalSchedulingTasks 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SchedulingTasks schedulingTasks, SchedulingTasks originalSchedulingTasks) {
        schedulingTasks.setUpdateUserId(TokenData.takeFromRequest().getUserId());
        schedulingTasks.setUpdateTime(new Date());
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<SchedulingTasks> uw = this.createUpdateQueryForNullValue(schedulingTasks, schedulingTasks.getId());
        return schedulingTasksMapper.update(schedulingTasks, uw) == 1;
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
        return schedulingTasksMapper.deleteById(id) == 1;
    }

    /**
     * 当前服务的支持表为从表，根据主表的关联Id，删除一对多的从表数据。
     *
     * @param projectId 从表关联字段。
     * @return 删除数量。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeByProjectId(Long projectId) {
        SchedulingTasks deletedObject = new SchedulingTasks();
        deletedObject.setProjectId(projectId);
        return schedulingTasksMapper.delete(new QueryWrapper<>(deletedObject));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBatchByProjectId(Long projectId, List<SchedulingTasks> dataList) {
        this.updateBatchOneToManyRelation("projectId", projectId,
                null, null, dataList, this::saveNewBatch);
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getSchedulingTasksListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<SchedulingTasks> getSchedulingTasksList(SchedulingTasks filter, String orderBy) {
        return schedulingTasksMapper.getSchedulingTasksList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getSchedulingTasksList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<SchedulingTasks> getSchedulingTasksListWithRelation(SchedulingTasks filter, String orderBy) {
        List<SchedulingTasks> resultList = schedulingTasksMapper.getSchedulingTasksList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param schedulingTasks 最新数据对象。
     * @param originalSchedulingTasks 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(SchedulingTasks schedulingTasks, SchedulingTasks originalSchedulingTasks) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是基于字典的验证。
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getCreateUserId)
                && !sysUserService.existId(schedulingTasks.getCreateUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "创建者ID"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getDataDeptId)
                && !sysDeptService.existId(schedulingTasks.getDataDeptId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属部门ID"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getDataUserId)
                && !sysUserService.existId(schedulingTasks.getDataUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属人ID"));
        }
        //这里是一对多的验证
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getProjectId)
                && !projectMainService.existId(schedulingTasks.getProjectId())) {
            return CallResult.error(String.format(errorMessageFormat, "所属项目ID"));
        }
        //这里是一对多的验证
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getClassificationId)
                && !planningClassificationService.existId(schedulingTasks.getClassificationId())) {
            return CallResult.error(String.format(errorMessageFormat, "业务分类ID"));
        }
        //这里是一对多的验证
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getProcessId)
                && !planningProcessService.existId(schedulingTasks.getProcessId())) {
            return CallResult.error(String.format(errorMessageFormat, "过程ID"));
        }
        //这里是一对多的验证
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getProcessThemeId)
                && !planningThemeService.existId(schedulingTasks.getProcessThemeId())) {
            return CallResult.error(String.format(errorMessageFormat, "主题域ID"));
        }
        //这里是一对多的验证
        if (this.needToVerify(schedulingTasks, originalSchedulingTasks, SchedulingTasks::getRulerId)
                && !devLiteflowRulerService.existId(schedulingTasks.getRulerId())) {
            return CallResult.error(String.format(errorMessageFormat, "任务规则链ID"));
        }
        return CallResult.ok();
    }

    private SchedulingTasks buildDefaultValue(SchedulingTasks schedulingTasks) {
        if (schedulingTasks.getId() == null) {
            schedulingTasks.setId(idGenerator.nextLongId());
        }
        TokenData tokenData = TokenData.takeFromRequest();
        schedulingTasks.setUpdateUserId(tokenData.getUserId());
        schedulingTasks.setUpdateTime(new Date());
        schedulingTasks.setIsDelete(GlobalDeletedFlag.NORMAL);
        MyModelUtil.setDefaultValue(schedulingTasks, "taskGroup", "");
        MyModelUtil.setDefaultValue(schedulingTasks, "taskDescription", "");
        MyModelUtil.setDefaultValue(schedulingTasks, "taskClassName", "");
        MyModelUtil.setDefaultValue(schedulingTasks, "cronExpression", "");
        MyModelUtil.setDefaultValue(schedulingTasks, "startTime", new Date());
        MyModelUtil.setDefaultValue(schedulingTasks, "taskDataMap", "");
        MyModelUtil.setDefaultValue(schedulingTasks, "endTime", new Date());
        MyModelUtil.setDefaultValue(schedulingTasks, "errorMsg", "");
        Map<String, Long> dataMap = new HashMap<>();
        dataMap.put("schedulingTasksId", schedulingTasks.getId());
        schedulingTasks.setTaskDataMap(JSONUtil.toJsonStr(dataMap));
        schedulingTasks.setTaskClassName(TASK_CLASS.getCanonicalName());
        return schedulingTasks;
    }
}
