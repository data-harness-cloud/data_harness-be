package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.StandardMain;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据规划-数据标准-主表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface StandardMainMapper extends BaseDaoMapper<StandardMain> {

    /**
     * 批量插入对象列表。
     *
     * @param standardMainList 新增对象列表。
     */
    void insertList(List<StandardMain> standardMainList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param standardMainFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<StandardMain> getGroupedStandardMainList(
            @Param("standardMainFilter") StandardMain standardMainFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param standardMainFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<StandardMain> getStandardMainList(
            @Param("standardMainFilter") StandardMain standardMainFilter, @Param("orderBy") String orderBy);
}
