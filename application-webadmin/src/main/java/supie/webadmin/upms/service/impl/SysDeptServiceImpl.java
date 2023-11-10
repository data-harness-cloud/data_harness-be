package supie.webadmin.upms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.upms.service.*;
import supie.webadmin.upms.dao.*;
import supie.webadmin.upms.model.*;
import supie.common.ext.util.BizWidgetDatasourceExtHelper;
import supie.common.ext.base.BizWidgetDatasource;
import supie.common.ext.constant.BizWidgetDatasourceType;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.*;
import supie.common.core.base.service.BaseService;
import supie.common.core.util.MyModelUtil;
import supie.common.core.util.MyPageUtil;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("sysDeptService")
public class SysDeptServiceImpl extends BaseService<SysDept, Long> implements SysDeptService, BizWidgetDatasource {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysDeptRelationMapper sysDeptRelationMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptPostMapper sysDeptPostMapper;
    @Autowired
    private SysDataPermDeptMapper sysDataPermDeptMapper;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private BizWidgetDatasourceExtHelper bizWidgetDatasourceExtHelper;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<SysDept> mapper() {
        return sysDeptMapper;
    }

    @PostConstruct
    private void registerBizWidgetDatasource() {
        bizWidgetDatasourceExtHelper.registerDatasource(BizWidgetDatasourceType.UPMS_DEPT_TYPE, this);
    }

    @Override
    public MyPageData<Map<String, Object>> getDataList(
            String type, Map<String, Object> filter, MyOrderParam orderParam, MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        String orderBy = orderParam == null ? null : MyOrderParam.buildOrderBy(orderParam, SysDept.class);
        SysDept deptFilter = filter == null ? null : BeanUtil.toBean(filter, SysDept.class);
        List<SysDept> deptList = this.getSysDeptList(deptFilter, orderBy);
        this.buildRelationForDataList(deptList, MyRelationParam.dictOnly());
        return MyPageUtil.makeResponseData(deptList, BeanUtil::beanToMap);
    }

    @Override
    public List<Map<String, Object>> getDataListWithInList(String type, String fieldName, List<String> fieldValues) {
        List<SysDept> deptList;
        if (StrUtil.isBlank(fieldName)) {
            deptList = this.getInList(fieldValues.stream().map(Long::valueOf).collect(Collectors.toSet()));
        } else {
            deptList = this.getInList(fieldName, MyModelUtil.convertToTypeValues(SysDept.class, fieldName, fieldValues));
        }
        this.buildRelationForDataList(deptList, MyRelationParam.dictOnly());
        return MyModelUtil.beanToMapList(deptList);
    }

    /**
     * 保存新增的部门对象。
     *
     * @param sysDept       新增的部门对象。
     * @param parentSysDept 上级部门对象。
     * @return 新增后的部门对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysDept saveNew(SysDept sysDept, SysDept parentSysDept) {
        sysDept.setDeptId(idGenerator.nextLongId());
        sysDept.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        MyModelUtil.fillCommonsForInsert(sysDept);
        sysDeptMapper.insert(sysDept);
        // 同步插入部门关联关系数据
        if (parentSysDept == null) {
            sysDeptRelationMapper.insert(new SysDeptRelation(sysDept.getDeptId(), sysDept.getDeptId()));
        } else {
            sysDeptRelationMapper.insertParentList(parentSysDept.getDeptId(), sysDept.getDeptId());
        }
        return sysDept;
    }

    /**
     * 更新部门对象。
     *
     * @param sysDept         更新的部门对象。
     * @param originalSysDept 原有的部门对象。
     * @return 更新成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SysDept sysDept, SysDept originalSysDept) {
        MyModelUtil.fillCommonsForUpdate(sysDept, originalSysDept);
        UpdateWrapper<SysDept> uw = this.createUpdateQueryForNullValue(sysDept, sysDept.getDeptId());
        if (sysDeptMapper.update(sysDept, uw) == 0) {
            return false;
        }
        if (ObjectUtil.notEqual(sysDept.getParentId(), originalSysDept.getParentId())) {
            this.updateParentRelation(sysDept, originalSysDept);
        }
        return true;
    }

    private void updateParentRelation(SysDept sysDept, SysDept originalSysDept) {
        List<Long> originalParentIdList = null;
        // 1. 因为层级关系变化了，所以要先遍历出，当前部门的原有父部门Id列表。
        if (originalSysDept.getParentId() != null) {
            LambdaQueryWrapper<SysDeptRelation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysDeptRelation::getDeptId, sysDept.getDeptId());
            List<SysDeptRelation> relationList = sysDeptRelationMapper.selectList(queryWrapper);
            originalParentIdList = relationList.stream()
                    .filter(c -> !c.getParentDeptId().equals(sysDept.getDeptId()))
                    .map(SysDeptRelation::getParentDeptId).collect(Collectors.toList());
        }
        // 2. 毕竟当前部门的上级部门变化了，所以当前部门和他的所有子部门，与当前部门的原有所有上级部门
        // 之间的关联关系就要被移除。
        // 这里先移除当前部门的所有子部门，与当前部门的所有原有上级部门之间的关联关系。
        if (CollUtil.isNotEmpty(originalParentIdList)) {
            sysDeptRelationMapper.removeBetweenChildrenAndParents(originalParentIdList, sysDept.getDeptId());
        }
        // 这里更进一步，将当前部门Id与其原有所有上级部门Id之间的关联关系删除。
        SysDeptRelation filter = new SysDeptRelation();
        filter.setDeptId(sysDept.getDeptId());
        sysDeptRelationMapper.delete(new QueryWrapper<>(filter));
        // 3. 重新计算当前部门的新上级部门列表。
        List<Long> newParentIdList = new LinkedList<>();
        // 这里要重新计算出当前部门所有新的上级部门Id列表。
        if (sysDept.getParentId() != null) {
            LambdaQueryWrapper<SysDeptRelation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysDeptRelation::getDeptId, sysDept.getParentId());
            List<SysDeptRelation> relationList = sysDeptRelationMapper.selectList(queryWrapper);
            newParentIdList = relationList.stream()
                    .map(SysDeptRelation::getParentDeptId).collect(Collectors.toList());
        }
        // 4. 先查询出当前部门的所有下级子部门Id列表。
        LambdaQueryWrapper<SysDeptRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDeptRelation::getParentDeptId, sysDept.getDeptId());
        List<SysDeptRelation> childRelationList = sysDeptRelationMapper.selectList(queryWrapper);
        // 5. 将当前部门及其所有子部门Id与其新的所有上级部门Id之间，建立关联关系。
        List<SysDeptRelation> deptRelationList = new LinkedList<>();
        deptRelationList.add(new SysDeptRelation(sysDept.getDeptId(), sysDept.getDeptId()));
        for (Long newParentId : newParentIdList) {
            deptRelationList.add(new SysDeptRelation(newParentId, sysDept.getDeptId()));
            for (SysDeptRelation childDeptRelation : childRelationList) {
                deptRelationList.add(new SysDeptRelation(newParentId, childDeptRelation.getDeptId()));
            }
        }
        // 6. 执行批量插入SQL语句，插入当前部门Id及其所有下级子部门Id，与所有新上级部门Id之间的关联关系。
        sysDeptRelationMapper.insertList(deptRelationList);
    }

    /**
     * 删除指定数据。
     *
     * @param deptId 主键Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long deptId) {
        if (sysDeptMapper.deleteById(deptId) == 0) {
            return false;
        }
        // 这里删除当前部门及其父部门的关联关系。
        // 当前部门和子部门的关系无需在这里删除，因为包含子部门时不能删除父部门。
        SysDeptRelation deptRelation = new SysDeptRelation();
        deptRelation.setDeptId(deptId);
        sysDeptRelationMapper.delete(new QueryWrapper<>(deptRelation));
        SysDataPermDept dataPermDept = new SysDataPermDept();
        dataPermDept.setDeptId(deptId);
        sysDataPermDeptMapper.delete(new QueryWrapper<>(dataPermDept));
        return true;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getSysDeptListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<SysDept> getSysDeptList(SysDept filter, String orderBy) {
        return sysDeptMapper.getSysDeptList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getSysDeptList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<SysDept> getSysDeptListWithRelation(SysDept filter, String orderBy) {
        List<SysDept> resultList = sysDeptMapper.getSysDeptList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 判断指定对象是否包含下级对象。
     *
     * @param deptId 主键Id。
     * @return 存在返回true，否则false。
     */
    @Override
    public boolean hasChildren(Long deptId) {
        SysDept filter = new SysDept();
        filter.setParentId(deptId);
        return getCountByFilter(filter) > 0;
    }

    /**
     * 判断指定部门Id是否包含用户对象。
     *
     * @param deptId 部门主键Id。
     * @return 存在返回true，否则false。
     */
    @Override
    public boolean hasChildrenUser(Long deptId) {
        SysUser sysUser = new SysUser();
        sysUser.setDeptId(deptId);
        return sysUserService.getCountByFilter(sysUser) > 0;
    }

    /**
     * 批量添加多对多关联关系。
     *
     * @param sysDeptPostList 多对多关联表对象集合。
     * @param deptId 主表Id。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSysDeptPostList(List<SysDeptPost> sysDeptPostList, Long deptId) {
        for (SysDeptPost sysDeptPost : sysDeptPostList) {
            sysDeptPost.setDeptPostId(idGenerator.nextLongId());
            sysDeptPost.setDeptId(deptId);
            sysDeptPostMapper.insert(sysDeptPost);
        }
    }

    /**
     * 更新中间表数据。
     *
     * @param sysDeptPost 中间表对象。
     * @return 更新成功与否。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysDeptPost(SysDeptPost sysDeptPost) {
        SysDeptPost filter = new SysDeptPost();
        filter.setDeptPostId(sysDeptPost.getDeptPostId());
        filter.setDeptId(sysDeptPost.getDeptId());
        filter.setPostId(sysDeptPost.getPostId());
        UpdateWrapper<SysDeptPost> uw =
                BaseService.createUpdateQueryForNullValue(sysDeptPost, SysDeptPost.class);
        uw.setEntity(filter);
        return sysDeptPostMapper.update(sysDeptPost, uw) > 0;
    }

    /**
     * 移除单条多对多关系。
     *
     * @param deptId 主表Id。
     * @param postId 从表Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeSysDeptPost(Long deptId, Long postId) {
        SysDeptPost filter = new SysDeptPost();
        filter.setDeptId(deptId);
        filter.setPostId(postId);
        return sysDeptPostMapper.delete(new QueryWrapper<>(filter)) > 0;
    }

    /**
     * 获取中间表数据。
     *
     * @param deptId 主表Id。
     * @param postId 从表Id。
     * @return 中间表对象。
     */
    @Override
    public SysDeptPost getSysDeptPost(Long deptId, Long postId) {
        SysDeptPost filter = new SysDeptPost();
        filter.setDeptId(deptId);
        filter.setPostId(postId);
        return sysDeptPostMapper.selectOne(new QueryWrapper<>(filter));
    }

    @Override
    public SysDeptPost getSysDeptPost(Long deptPostId) {
        return sysDeptPostMapper.selectById(deptPostId);
    }

    @Override
    public List<Map<String, Object>> getSysDeptPostListWithRelationByDeptId(Long deptId) {
        return sysDeptPostMapper.getSysDeptPostListWithRelationByDeptId(deptId);
    }

    @Override
    public List<SysDeptPost> getSysDeptPostList(Long deptId, Set<Long> postIdSet) {
        LambdaQueryWrapper<SysDeptPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDeptPost::getDeptId, deptId);
        queryWrapper.in(SysDeptPost::getPostId, postIdSet);
        return sysDeptPostMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysDeptPost> getSiblingSysDeptPostList(Long deptId, Set<Long> postIdSet) {
        SysDept sysDept = this.getById(deptId);
        if (sysDept == null) {
            return new LinkedList<>();
        }
        List<SysDept> deptList = this.getListByParentId("parentId", sysDept.getParentId());
        Set<Long> deptIdSet = deptList.stream().map(SysDept::getDeptId).collect(Collectors.toSet());
        LambdaQueryWrapper<SysDeptPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDeptPost::getDeptId, deptIdSet);
        queryWrapper.in(SysDeptPost::getPostId, postIdSet);
        return sysDeptPostMapper.selectList(queryWrapper);
    }

    @Override
    public List<Long> getLeaderDeptPostIdList(Long deptId) {
        List<SysDeptPost> resultList = sysDeptPostMapper.getLeaderDeptPostList(deptId);
        return resultList.stream().map(SysDeptPost::getDeptPostId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getUpLeaderDeptPostIdList(Long deptId) {
        SysDept sysDept = this.getById(deptId);
        if (sysDept.getParentId() == null) {
            return new LinkedList<>();
        }
        return this.getLeaderDeptPostIdList(sysDept.getParentId());
    }

    @Override
    public List<Long> getAllChildDeptIdByParentIds(List<Long> parentIds) {
        LambdaQueryWrapper<SysDeptRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDeptRelation::getParentDeptId, parentIds);
        return sysDeptRelationMapper.selectList(queryWrapper)
                .stream().map(SysDeptRelation::getDeptId).collect(Collectors.toList());
    }
}
