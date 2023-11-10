package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.BaseBusinessDict;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 基础业务字典数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface BaseBusinessDictMapper extends BaseDaoMapper<BaseBusinessDict> {

    /**
     * 批量插入对象列表。
     *
     * @param baseBusinessDictList 新增对象列表。
     */
    void insertList(List<BaseBusinessDict> baseBusinessDictList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param baseBusinessDictFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<BaseBusinessDict> getGroupedBaseBusinessDictList(
            @Param("baseBusinessDictFilter") BaseBusinessDict baseBusinessDictFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param baseBusinessDictFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<BaseBusinessDict> getBaseBusinessDictList(
            @Param("baseBusinessDictFilter") BaseBusinessDict baseBusinessDictFilter, @Param("orderBy") String orderBy);
}
