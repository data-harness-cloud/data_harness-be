package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import java.util.stream.Collectors;

/**
 * 数据项目-数据源表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("projectDatasourceService")
public class ProjectDatasourceServiceImpl extends BaseService<ProjectDatasource, Long> implements ProjectDatasourceService {

    @Autowired
    private ProjectDatasourceMapper projectDatasourceMapper;
    @Autowired
    private ProjectDatasourceRelationMapper projectDatasourceRelationMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private ProjectDatasourceRelationService projectDatasourceRelationService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<ProjectDatasource> mapper() {
        return projectDatasourceMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param projectDatasource 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProjectDatasource saveNew(ProjectDatasource projectDatasource) {
        projectDatasourceMapper.insert(this.buildDefaultValue(projectDatasource));
        return projectDatasource;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param projectDatasourceList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<ProjectDatasource> projectDatasourceList) {
        if (CollUtil.isNotEmpty(projectDatasourceList)) {
            projectDatasourceList.forEach(this::buildDefaultValue);
            projectDatasourceMapper.insertList(projectDatasourceList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param projectDatasource         更新的对象。
     * @param originalProjectDatasource 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(ProjectDatasource projectDatasource, ProjectDatasource originalProjectDatasource) {
        MyModelUtil.fillCommonsForUpdate(projectDatasource, originalProjectDatasource);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<ProjectDatasource> uw = this.createUpdateQueryForNullValue(projectDatasource, projectDatasource.getId());
        return projectDatasourceMapper.update(projectDatasource, uw) == 1;
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
        if (projectDatasourceMapper.deleteById(id) == 0) {
            return false;
        }
        // 开始删除多对多父表的关联
        ProjectDatasourceRelation projectDatasourceRelation = new ProjectDatasourceRelation();
        projectDatasourceRelation.setDatasourceId(id);
        projectDatasourceRelationMapper.delete(new QueryWrapper<>(projectDatasourceRelation));
        return true;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getProjectDatasourceListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ProjectDatasource> getProjectDatasourceList(ProjectDatasource filter, String orderBy) {
        return projectDatasourceMapper.getProjectDatasourceList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getProjectDatasourceList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ProjectDatasource> getProjectDatasourceListWithRelation(ProjectDatasource filter, String orderBy) {
        List<ProjectDatasource> resultList = projectDatasourceMapper.getProjectDatasourceList(filter, orderBy);
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
    public List<ProjectDatasource> getGroupedProjectDatasourceListWithRelation(
            ProjectDatasource filter, String groupSelect, String groupBy, String orderBy) {
        List<ProjectDatasource> resultList =
                projectDatasourceMapper.getGroupedProjectDatasourceList(filter, groupSelect, groupBy, orderBy);
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
     * @param projectId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ProjectDatasource> getNotInProjectDatasourceListByProjectId(Long projectId, ProjectDatasource filter, String orderBy) {
        List<ProjectDatasource> resultList;
        if (projectId != null) {
            resultList = projectDatasourceMapper.getNotInProjectDatasourceListByProjectId(projectId, filter, orderBy);
        } else {
            resultList = getProjectDatasourceList(filter, orderBy);
        }
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        return resultList;
    }

    /**
     * 在多对多关系中，当前Service的数据表为从表，返回与指定主表主键Id存在对多对关系的列表。
     *
     * @param projectId 主表主键Id。
     * @param filter 从表的过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ProjectDatasource> getProjectDatasourceListByProjectId(Long projectId, ProjectDatasource filter, String orderBy) {
        List<ProjectDatasource> resultList =
                projectDatasourceMapper.getProjectDatasourceListByProjectId(projectId, filter, orderBy);
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        // 这里是为中间表ProjectDatasourceRelation中关联的静态字典，进行基于注解的数据绑定。
        List<ProjectDatasourceRelation> projectDatasourceRelationList = resultList.stream()
                .filter(c -> c.getProjectDatasourceRelation() != null)
                .map(ProjectDatasource::getProjectDatasourceRelation).collect(Collectors.toList());
        projectDatasourceRelationService.buildRelationForDataList(
                projectDatasourceRelationList, MyRelationParam.dictOnly(), CollUtil.newHashSet("projectId", "datasourceId"));
        return resultList;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param projectDatasource 最新数据对象。
     * @param originalProjectDatasource 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(ProjectDatasource projectDatasource, ProjectDatasource originalProjectDatasource) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是基于字典的验证。
        if (this.needToVerify(projectDatasource, originalProjectDatasource, ProjectDatasource::getDataUserId)
                && !sysUserService.existId(projectDatasource.getDataUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属人"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(projectDatasource, originalProjectDatasource, ProjectDatasource::getDataDeptId)
                && !sysDeptService.existId(projectDatasource.getDataDeptId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属部门"));
        }
        return CallResult.ok();
    }

    private ProjectDatasource buildDefaultValue(ProjectDatasource projectDatasource) {
        if (projectDatasource.getId() == null) {
            projectDatasource.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(projectDatasource);
        projectDatasource.setIsDelete(GlobalDeletedFlag.NORMAL);
        return projectDatasource;
    }
}
