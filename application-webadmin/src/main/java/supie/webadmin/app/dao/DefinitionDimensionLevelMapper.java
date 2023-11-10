package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.DefinitionDimensionLevel;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 业务定义-维度层级表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface DefinitionDimensionLevelMapper extends BaseDaoMapper<DefinitionDimensionLevel> {

    /**
     * 批量插入对象列表。
     *
     * @param definitionDimensionLevelList 新增对象列表。
     */
    void insertList(List<DefinitionDimensionLevel> definitionDimensionLevelList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param definitionDimensionLevelFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<DefinitionDimensionLevel> getGroupedDefinitionDimensionLevelList(
            @Param("definitionDimensionLevelFilter") DefinitionDimensionLevel definitionDimensionLevelFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param definitionDimensionLevelFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<DefinitionDimensionLevel> getDefinitionDimensionLevelList(
            @Param("definitionDimensionLevelFilter") DefinitionDimensionLevel definitionDimensionLevelFilter, @Param("orderBy") String orderBy);
}
