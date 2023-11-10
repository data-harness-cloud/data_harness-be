package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.SeatunnelConfig;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 项目中心-数据传输引擎配置数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface SeatunnelConfigMapper extends BaseDaoMapper<SeatunnelConfig> {

    /**
     * 批量插入对象列表。
     *
     * @param seatunnelConfigList 新增对象列表。
     */
    void insertList(List<SeatunnelConfig> seatunnelConfigList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param seatunnelConfigFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<SeatunnelConfig> getSeatunnelConfigList(
            @Param("seatunnelConfigFilter") SeatunnelConfig seatunnelConfigFilter, @Param("orderBy") String orderBy);
}
