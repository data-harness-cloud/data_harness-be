package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.base.service.BaseService;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.CallResult;
import supie.common.core.object.MyRelationParam;
import supie.common.core.util.MyModelUtil;
import supie.common.dict.service.GlobalDictService;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import supie.webadmin.app.dao.ProjectDatasourceRelationMapper;
import supie.webadmin.app.dao.ProjectEngineMapper;
import supie.webadmin.app.dao.ProjectMainMapper;
import supie.webadmin.app.dao.ProjectMemberMapper;
import supie.webadmin.app.model.*;
import supie.webadmin.app.service.*;
import supie.webadmin.upms.service.SysDeptService;
import supie.webadmin.upms.service.SysUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据项目—项目管理主表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("projectMainService")
public class ProjectMainServiceImpl extends BaseService<ProjectMain, Long> implements ProjectMainService {

    @Autowired
    private ProjectMainMapper projectMainMapper;
    @Autowired
    private ProjectMemberMapper projectMemberMapper;
    @Autowired
    private ProjectDatasourceRelationMapper projectDatasourceRelationMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private ProjectDatasourceService projectDatasourceService;
    @Autowired
    private ProjectEngineService projectEngineService;
    @Autowired
    private DevLiteflowRulerService devLiteflowRulerService;
    @Autowired
    private SchedulingTasksService schedulingTasksService;
    @Autowired
    private RemoteHostService remoteHostService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;
    @Autowired
    private GlobalDictService globalDictService;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private ProjectMemberService projectMemberService;
    @Autowired
    private PlanningWarehouseLayerService planningWarehouseLayerService;
    @Autowired
    private ProjectEngineMapper projectEngineMapper;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<ProjectMain> mapper() {
        return projectMainMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param projectMain 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProjectMain saveNew(ProjectMain projectMain) {
        projectMainMapper.insert(this.buildDefaultValue(projectMain));
        return projectMain;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param projectMainList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<ProjectMain> projectMainList) {
        if (CollUtil.isNotEmpty(projectMainList)) {
            projectMainList.forEach(this::buildDefaultValue);
            projectMainMapper.insertList(projectMainList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param projectMain         更新的对象。
     * @param originalProjectMain 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(ProjectMain projectMain, ProjectMain originalProjectMain) {
        MyModelUtil.fillCommonsForUpdate(projectMain, originalProjectMain);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<ProjectMain> uw = this.createUpdateQueryForNullValue(projectMain, projectMain.getId());
        return projectMainMapper.update(projectMain, uw) == 1;
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
        if (projectMainMapper.deleteById(id) == 0) {
            return false;
        }
        remoteHostService.removeByProjectId(id);
        seatunnelConfigService.removeByProjectId(id);
        // 开始删除多对多子表的关联
        ProjectMember projectMember = new ProjectMember();
        projectMember.setMemberProjectId(id);
        projectMemberMapper.delete(new QueryWrapper<>(projectMember));
        ProjectDatasourceRelation projectDatasourceRelation = new ProjectDatasourceRelation();
        projectDatasourceRelation.setProjectId(id);
        projectDatasourceRelationMapper.delete(new QueryWrapper<>(projectDatasourceRelation));
        // 删除相关的分层信息
        planningWarehouseLayerService.removeByProjectId(id);
        return true;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getProjectMainListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ProjectMain> getProjectMainList(ProjectMain filter, String orderBy) {
        return projectMainMapper.getProjectMainList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getProjectMainList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param remoteHostFilter 一对多从表过滤对象。
     * @param seatunnelConfigFilter 一对多从表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ProjectMain> getProjectMainListWithRelation(ProjectMain filter, RemoteHost remoteHostFilter, SeatunnelConfig seatunnelConfigFilter, String orderBy) {
        List<ProjectMain> resultList =
                projectMainMapper.getProjectMainListEx(filter, remoteHostFilter, seatunnelConfigFilter, orderBy);
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
     * @param projectMain 主表对象。
     * @return 没有关联数据返回true，否则false，同时返回具体的提示信息。
     */
    @Override
    public CallResult verifyRelatedDataBeforeDelete(ProjectMain projectMain) {
        DevLiteflowRuler devLiteflowRuler = new DevLiteflowRuler();
        devLiteflowRuler.setProjectId(projectMain.getId());
        if (devLiteflowRulerService.getCountByFilter(devLiteflowRuler) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[DevLiteflowRuler] 存在关联数据！");
        }
        SchedulingTasks schedulingTasks = new SchedulingTasks();
        schedulingTasks.setProjectId(projectMain.getId());
        if (schedulingTasksService.getCountByFilter(schedulingTasks) > 0) {
            // NOTE: 可以根据需求修改下面方括号中的提示信息。
            return CallResult.error("数据验证失败，[SchedulingTasks] 存在关联数据！");
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
    public List<ProjectMain> getGroupedProjectMainListWithRelation(
            ProjectMain filter, String groupSelect, String groupBy, String orderBy) {
        List<ProjectMain> resultList =
                projectMainMapper.getGroupedProjectMainList(filter, groupSelect, groupBy, orderBy);
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
     * @param projectMemberList 多对多关联表对象集合。
     * @param memberProjectId 主表Id。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProjectMemberList(List<ProjectMember> projectMemberList, Long memberProjectId) {
        for (ProjectMember projectMember : projectMemberList) {
            projectMember.setId(idGenerator.nextLongId());
            projectMember.setMemberProjectId(memberProjectId);
            projectMemberMapper.insert(projectMember);
        }
    }

    /**
     * 更新中间表数据。
     *
     * @param projectMember 中间表对象。
     * @return 更新成功与否。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateProjectMember(ProjectMember projectMember) {
        ProjectMember filter = new ProjectMember();
        filter.setMemberProjectId(projectMember.getMemberProjectId());
        filter.setMemberUserId(projectMember.getMemberUserId());
        UpdateWrapper<ProjectMember> uw =
                BaseService.createUpdateQueryForNullValue(projectMember, ProjectMember.class);
        uw.setEntity(filter);
        return projectMemberMapper.update(projectMember, uw) > 0;
    }

    /**
     * 获取中间表数据。
     *
     * @param memberProjectId 主表Id。
     * @param memberUserId 从表Id。
     * @return 中间表对象。
     */
    @Override
    public ProjectMember getProjectMember(Long memberProjectId, Long memberUserId) {
        ProjectMember filter = new ProjectMember();
        filter.setMemberProjectId(memberProjectId);
        filter.setMemberUserId(memberUserId);
        return projectMemberMapper.selectOne(new QueryWrapper<>(filter));
    }

    /**
     * 移除单条多对多关系。
     *
     * @param memberProjectId 主表Id。
     * @param memberUserId 从表Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeProjectMember(Long memberProjectId, Long memberUserId) {
        ProjectMember filter = new ProjectMember();
        filter.setMemberProjectId(memberProjectId);
        filter.setMemberUserId(memberUserId);
        return projectMemberMapper.delete(new QueryWrapper<>(filter)) > 0;
    }

    /**
     * 批量添加多对多关联关系。
     *
     * @param projectDatasourceRelationList 多对多关联表对象集合。
     * @param projectId 主表Id。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProjectDatasourceRelationList(List<ProjectDatasourceRelation> projectDatasourceRelationList, Long projectId) {
        for (ProjectDatasourceRelation projectDatasourceRelation : projectDatasourceRelationList) {
            projectDatasourceRelation.setId(idGenerator.nextLongId());
            projectDatasourceRelation.setProjectId(projectId);
            projectDatasourceRelationMapper.insert(projectDatasourceRelation);
        }
    }

    /**
     * 更新中间表数据。
     *
     * @param projectDatasourceRelation 中间表对象。
     * @return 更新成功与否。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateProjectDatasourceRelation(ProjectDatasourceRelation projectDatasourceRelation) {
        ProjectDatasourceRelation filter = new ProjectDatasourceRelation();
        filter.setProjectId(projectDatasourceRelation.getProjectId());
        filter.setDatasourceId(projectDatasourceRelation.getDatasourceId());
        UpdateWrapper<ProjectDatasourceRelation> uw =
                BaseService.createUpdateQueryForNullValue(projectDatasourceRelation, ProjectDatasourceRelation.class);
        uw.setEntity(filter);
        return projectDatasourceRelationMapper.update(projectDatasourceRelation, uw) > 0;
    }

    /**
     * 获取中间表数据。
     *
     * @param projectId 主表Id。
     * @param datasourceId 从表Id。
     * @return 中间表对象。
     */
    @Override
    public ProjectDatasourceRelation getProjectDatasourceRelation(Long projectId, Long datasourceId) {
        ProjectDatasourceRelation filter = new ProjectDatasourceRelation();
        filter.setProjectId(projectId);
        filter.setDatasourceId(datasourceId);
        return projectDatasourceRelationMapper.selectOne(new QueryWrapper<>(filter));
    }

    /**
     * 移除单条多对多关系。
     *
     * @param projectId 主表Id。
     * @param datasourceId 从表Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeProjectDatasourceRelation(Long projectId, Long datasourceId) {
        ProjectDatasourceRelation filter = new ProjectDatasourceRelation();
        filter.setProjectId(projectId);
        filter.setDatasourceId(datasourceId);
        return projectDatasourceRelationMapper.delete(new QueryWrapper<>(filter)) > 0;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param projectMain 最新数据对象。
     * @param originalProjectMain 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(ProjectMain projectMain, ProjectMain originalProjectMain) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是基于字典的验证。
        if (this.needToVerify(projectMain, originalProjectMain, ProjectMain::getDataUserId)
                && !sysUserService.existId(projectMain.getDataUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属人"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(projectMain, originalProjectMain, ProjectMain::getDataDeptId)
                && !sysDeptService.existId(projectMain.getDataDeptId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属部门"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(projectMain, originalProjectMain, ProjectMain::getProjectCurrentsStatus)
                && !globalDictService.existDictItemFromCache("ProjectStatus", projectMain.getProjectCurrentsStatus())) {
            return CallResult.error(String.format(errorMessageFormat, "项目状态"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(projectMain, originalProjectMain, ProjectMain::getProjectHeaderId)
                && !sysUserService.existId(projectMain.getProjectHeaderId())) {
            return CallResult.error(String.format(errorMessageFormat, "项目负责人"));
        }
        //这里是一对多的验证
        if (this.needToVerify(projectMain, originalProjectMain, ProjectMain::getProjectEngineId)
                && !projectEngineService.existId(projectMain.getProjectEngineId())) {
            return CallResult.error(String.format(errorMessageFormat, "项目存算引擎"));
        }
        return CallResult.ok();
    }

    private ProjectMain buildDefaultValue(ProjectMain projectMain) {
        if (projectMain.getId() == null) {
            projectMain.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(projectMain);
        projectMain.setIsDelete(GlobalDeletedFlag.NORMAL);
        MyModelUtil.setDefaultValue(projectMain, "projectCode", "");
        return projectMain;
    }

    /**
     * 新增项目。
     *
     * @param projectMain      项目主表对象。
     * @param memberUserIdList 项目成员用户Id列表。
     * @return 新增结果。
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Long addProject(ProjectMain projectMain, List<Long> memberUserIdList) throws Exception {
        // 新增项目
        ProjectMain saveProjectMain = saveNew(projectMain);
        Long projectMainId = saveProjectMain.getId();
        // 项目与成员关系关联
        for (Long memberUserId : memberUserIdList) {
            ProjectMember projectMember = new ProjectMember();
            projectMember.setMemberProjectId(projectMainId);
            projectMember.setMemberUserId(memberUserId);
            projectMemberService.saveNew(projectMember);
        }
        // 新增分层
        List<PlanningWarehouseLayer> planningWarehouseLayerList = new ArrayList<>();
        planningWarehouseLayerList.add(new PlanningWarehouseLayer(projectMainId, projectMain.getProjectCode() + "_ODS", "原始层", 1));
        planningWarehouseLayerList.add(new PlanningWarehouseLayer(projectMainId, projectMain.getProjectCode() + "_DWD", "明细层", 1));
        planningWarehouseLayerList.add(new PlanningWarehouseLayer(projectMainId, projectMain.getProjectCode() + "_DWS", "汇总层", 1));
        planningWarehouseLayerList.add(new PlanningWarehouseLayer(projectMainId, projectMain.getProjectCode() + "_ADS", "结果层", 1));
        planningWarehouseLayerList.add(new PlanningWarehouseLayer(projectMainId, projectMain.getProjectCode() + "_DIM", "维度层", 1));
        planningWarehouseLayerService.saveNewBatch(planningWarehouseLayerList);
        // 创建分层相关的数据库
        List<String> databaseNameList = new ArrayList<>();
        for (PlanningWarehouseLayer planningWarehouseLayer : planningWarehouseLayerList) {
            databaseNameList.add(planningWarehouseLayer.getHouseLayerCode());
        }
        ProjectEngine projectEngine = projectEngineMapper.selectById(projectMain.getProjectEngineId());
        planningWarehouseLayerService.createDatabaseByProjectEngine(projectEngine, databaseNameList.toArray(new String[0]));
        return projectMainId;
    }
}
