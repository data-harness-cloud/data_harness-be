package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Value;
import supie.webadmin.app.service.*;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.model.*;
import supie.webadmin.upms.service.SysUserService;
import supie.webadmin.upms.service.SysDeptService;
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
 * 数据开发-liteflow表达式规则表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("devLiteflowRulerService")
public class DevLiteflowRulerServiceImpl extends BaseService<DevLiteflowRuler, Long> implements DevLiteflowRulerService {

    @Value("${liteflow.rule-source-ext-data-map.applicationName}")
    private String liteFlowApplicationName;

    @Autowired
    private DevLiteflowRulerMapper devLiteflowRulerMapper;
    @Autowired
    private ProjectMainService projectMainService;
    @Autowired
    private PlanningProcessService planningProcessService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private DevLiteflowNodeService devLiteflowNodeService;
    @Autowired
    private SchedulingTasksService schedulingTasksService;
    @Autowired
    private DevLiteflowLogService devLiteflowLogService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<DevLiteflowRuler> mapper() {
        return devLiteflowRulerMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param devLiteflowRuler 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DevLiteflowRuler saveNew(DevLiteflowRuler devLiteflowRuler) {
        devLiteflowRuler.setApplicationName(liteFlowApplicationName);
        devLiteflowRulerMapper.insert(this.buildDefaultValue(devLiteflowRuler));
        return devLiteflowRuler;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param devLiteflowRulerList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<DevLiteflowRuler> devLiteflowRulerList) {
        if (CollUtil.isNotEmpty(devLiteflowRulerList)) {
            devLiteflowRulerList.forEach(this::buildDefaultValue);
            devLiteflowRulerMapper.insertList(devLiteflowRulerList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param devLiteflowRuler         更新的对象。
     * @param originalDevLiteflowRuler 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(DevLiteflowRuler devLiteflowRuler, DevLiteflowRuler originalDevLiteflowRuler) {
        MyModelUtil.fillCommonsForUpdate(devLiteflowRuler, originalDevLiteflowRuler);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<DevLiteflowRuler> uw = this.createUpdateQueryForNullValue(devLiteflowRuler, devLiteflowRuler.getId());
        return devLiteflowRulerMapper.update(devLiteflowRuler, uw) == 1;
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
        return devLiteflowRulerMapper.deleteById(id) == 1;
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
        DevLiteflowRuler deletedObject = new DevLiteflowRuler();
        deletedObject.setProjectId(projectId);
        return devLiteflowRulerMapper.delete(new QueryWrapper<>(deletedObject));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBatchByProjectId(Long projectId, List<DevLiteflowRuler> dataList) {
        this.updateBatchOneToManyRelation("projectId", projectId,
                null, null, dataList, this::saveNewBatch);
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getDevLiteflowRulerListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<DevLiteflowRuler> getDevLiteflowRulerList(DevLiteflowRuler filter, String orderBy) {
        return devLiteflowRulerMapper.getDevLiteflowRulerList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getDevLiteflowRulerList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<DevLiteflowRuler> getDevLiteflowRulerListWithRelation(DevLiteflowRuler filter, String orderBy) {
        List<DevLiteflowRuler> resultList = devLiteflowRulerMapper.getDevLiteflowRulerList(filter, orderBy);
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
     * @param devLiteflowRuler 主表对象。
     * @return 没有关联数据返回true，否则false，同时返回具体的提示信息。
     */
    @Override
    public CallResult verifyRelatedDataBeforeDelete(DevLiteflowRuler devLiteflowRuler) {
        DevLiteflowNode devLiteflowNode = new DevLiteflowNode();
        devLiteflowNode.setRulerId(devLiteflowRuler.getId());
        if (devLiteflowNodeService.getCountByFilter(devLiteflowNode) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[DevLiteflowNode] 存在关联数据！");
        }
        SchedulingTasks schedulingTasks = new SchedulingTasks();
        schedulingTasks.setRulerId(devLiteflowRuler.getId());
        if (schedulingTasksService.getCountByFilter(schedulingTasks) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[SchedulingTasks] 存在关联数据！");
        }
        DevLiteflowLog devLiteflowLog = new DevLiteflowLog();
        devLiteflowLog.setRulerId(devLiteflowRuler.getId());
        if (devLiteflowLogService.getCountByFilter(devLiteflowLog) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[DevLiteflowLog] 存在关联数据！");
        }
        return CallResult.ok();
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param devLiteflowRuler 最新数据对象。
     * @param originalDevLiteflowRuler 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(DevLiteflowRuler devLiteflowRuler, DevLiteflowRuler originalDevLiteflowRuler) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是基于字典的验证。
        if (this.needToVerify(devLiteflowRuler, originalDevLiteflowRuler, DevLiteflowRuler::getCreateUserId)
                && !sysUserService.existId(devLiteflowRuler.getCreateUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "创建人"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(devLiteflowRuler, originalDevLiteflowRuler, DevLiteflowRuler::getDataUserId)
                && !sysUserService.existId(devLiteflowRuler.getDataUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属人"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(devLiteflowRuler, originalDevLiteflowRuler, DevLiteflowRuler::getDataDeptId)
                && !sysDeptService.existId(devLiteflowRuler.getDataDeptId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属部门"));
        }
        //这里是一对多的验证
        if (this.needToVerify(devLiteflowRuler, originalDevLiteflowRuler, DevLiteflowRuler::getProjectId)
                && !projectMainService.existId(devLiteflowRuler.getProjectId())) {
            return CallResult.error(String.format(errorMessageFormat, "所属项目ID"));
        }
        //这里是一对多的验证
        if (this.needToVerify(devLiteflowRuler, originalDevLiteflowRuler, DevLiteflowRuler::getProcessId)
                && !planningProcessService.existId(devLiteflowRuler.getProcessId())) {
            return CallResult.error(String.format(errorMessageFormat, "过程ID"));
        }
        return CallResult.ok();
    }

    private DevLiteflowRuler buildDefaultValue(DevLiteflowRuler devLiteflowRuler) {
        if (devLiteflowRuler.getId() == null) {
            devLiteflowRuler.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(devLiteflowRuler);
        devLiteflowRuler.setIsDelete(GlobalDeletedFlag.NORMAL);
        return devLiteflowRuler;
    }
}
