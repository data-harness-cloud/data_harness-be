package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.redisson.api.RedissonClient;
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

import java.util.*;

/**
 * 外部APP与动态路由关联表数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("externalAppCustomizeRouteService")
public class ExternalAppCustomizeRouteServiceImpl extends BaseService<ExternalAppCustomizeRoute, Long> implements ExternalAppCustomizeRouteService {

    @Autowired
    private ExternalAppCustomizeRouteMapper externalAppCustomizeRouteMapper;
    @Autowired
    private IdGeneratorWrapper idGenerator;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CustomizeRouteService customizeRouteService;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<ExternalAppCustomizeRoute> mapper() {
        return externalAppCustomizeRouteMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param externalAppCustomizeRoute 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExternalAppCustomizeRoute saveNew(ExternalAppCustomizeRoute externalAppCustomizeRoute) {
        unregisterDynamicRouteFromRedis(
                externalAppCustomizeRoute.getId(), externalAppCustomizeRoute.getExternalAppId(), externalAppCustomizeRoute.getCustomizeRouteId());
        externalAppCustomizeRouteMapper.insert(this.buildDefaultValue(externalAppCustomizeRoute));
        return externalAppCustomizeRoute;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param externalAppCustomizeRouteList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<ExternalAppCustomizeRoute> externalAppCustomizeRouteList) {
        if (CollUtil.isNotEmpty(externalAppCustomizeRouteList)) {
            externalAppCustomizeRouteList.forEach(this::buildDefaultValue);
            externalAppCustomizeRouteMapper.insertList(externalAppCustomizeRouteList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param externalAppCustomizeRoute         更新的对象。
     * @param originalExternalAppCustomizeRoute 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(ExternalAppCustomizeRoute externalAppCustomizeRoute, ExternalAppCustomizeRoute originalExternalAppCustomizeRoute) {
        unregisterDynamicRouteFromRedis(
                originalExternalAppCustomizeRoute.getId(),
                originalExternalAppCustomizeRoute.getExternalAppId(),
                originalExternalAppCustomizeRoute.getCustomizeRouteId());
        MyModelUtil.fillCommonsForUpdate(externalAppCustomizeRoute, originalExternalAppCustomizeRoute);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<ExternalAppCustomizeRoute> uw = this.createUpdateQueryForNullValue(externalAppCustomizeRoute, externalAppCustomizeRoute.getId());
        return externalAppCustomizeRouteMapper.update(externalAppCustomizeRoute, uw) == 1;
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
        unregisterDynamicRouteFromRedis(id, null, null);
        return externalAppCustomizeRouteMapper.deleteById(id) == 1;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getExternalAppCustomizeRouteListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ExternalAppCustomizeRoute> getExternalAppCustomizeRouteList(ExternalAppCustomizeRoute filter, String orderBy) {
        return externalAppCustomizeRouteMapper.getExternalAppCustomizeRouteList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getExternalAppCustomizeRouteList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<ExternalAppCustomizeRoute> getExternalAppCustomizeRouteListWithRelation(ExternalAppCustomizeRoute filter, String orderBy) {
        List<ExternalAppCustomizeRoute> resultList = externalAppCustomizeRouteMapper.getExternalAppCustomizeRouteList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    @Override
    public ExternalAppCustomizeRoute buildDefaultValue(ExternalAppCustomizeRoute externalAppCustomizeRoute) {
        if (externalAppCustomizeRoute.getId() == null) {
            externalAppCustomizeRoute.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(externalAppCustomizeRoute);
        externalAppCustomizeRoute.setIsDelete(GlobalDeletedFlag.NORMAL);
        return externalAppCustomizeRoute;
    }

    /**
     * 下线redis中相关的路由权限信息
     */
    private void unregisterDynamicRouteFromRedis(Long externalAppCustomizeRouteId, Long externalAppId, Long customizeRouteId) {
        if (externalAppCustomizeRouteId == null && externalAppId == null && customizeRouteId == null) return;
        ExternalAppCustomizeRoute externalAppCustomizeRoute = new ExternalAppCustomizeRoute();
        if (externalAppCustomizeRouteId != null) externalAppCustomizeRoute.setId(externalAppCustomizeRouteId);
        if (externalAppId != null) externalAppCustomizeRoute.setExternalAppId(externalAppId);
        if (customizeRouteId != null) externalAppCustomizeRoute.setCustomizeRouteId(customizeRouteId);
        List<CustomizeRoute> customizeRouteList = customizeRouteService.queryCustomizeRouteByExternalAppCustomizeRoute(externalAppCustomizeRoute);
        for (CustomizeRoute customizeRoute : customizeRouteList) {
            String url = customizeRoute.getUrl();
            redissonClient.getBucket("CustomizeRoute:" + url).delete();
            redissonClient.getBucket("CustomizeRouteVerificationInfo:" + url).delete();
        }
    }
}
