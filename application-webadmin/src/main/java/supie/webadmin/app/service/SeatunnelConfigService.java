package supie.webadmin.app.service;

import com.alibaba.fastjson.JSONObject;
import supie.webadmin.app.model.*;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 项目中心-数据传输引擎配置数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface SeatunnelConfigService extends IBaseService<SeatunnelConfig, Long> {

    /**
     * 保存新增对象。
     *
     * @param seatunnelConfig 新增对象。
     * @return 返回新增对象。
     */
    SeatunnelConfig saveNew(SeatunnelConfig seatunnelConfig);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param seatunnelConfigList 新增对象列表。
     */
    void saveNewBatch(List<SeatunnelConfig> seatunnelConfigList);

    /**
     * 保存新增主表对象及关联对象。
     *
     * @param seatunnelConfig 新增主表对象。
     * @param relationData 全部关联从表数据。
     * @return 返回新增主表对象。
     */
    SeatunnelConfig saveNewWithRelation(SeatunnelConfig seatunnelConfig, JSONObject relationData);

    /**
     * 更新数据对象。
     *
     * @param seatunnelConfig         更新的对象。
     * @param originalSeatunnelConfig 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(SeatunnelConfig seatunnelConfig, SeatunnelConfig originalSeatunnelConfig);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 当前服务的支持表为从表，根据主表的关联Id，删除一对多的从表数据。
     *
     * @param projectId 从表关联字段。
     * @return 删除数量。
     */
    int removeByProjectId(Long projectId);

    /**
     * 批量更新一对多从表的数据。
     *
     * @param projectId 从表关联字段。
     * @param dataList 本次批量更新的一对多从表数据。
     */
    void updateBatchByProjectId(Long projectId, List<SeatunnelConfig> dataList);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getSeatunnelConfigListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<SeatunnelConfig> getSeatunnelConfigList(SeatunnelConfig filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getSeatunnelConfigList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<SeatunnelConfig> getSeatunnelConfigListWithRelation(SeatunnelConfig filter, String orderBy);
}
