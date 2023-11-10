package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.DefinitionDimension;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 业务定义-数据维度数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface DefinitionDimensionMapper extends BaseDaoMapper<DefinitionDimension> {

    /**
     * 批量插入对象列表。
     *
     * @param definitionDimensionList 新增对象列表。
     */
    void insertList(List<DefinitionDimension> definitionDimensionList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param definitionDimensionFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<DefinitionDimension> getGroupedDefinitionDimensionList(
            @Param("definitionDimensionFilter") DefinitionDimension definitionDimensionFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param definitionDimensionFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<DefinitionDimension> getDefinitionDimensionList(
            @Param("definitionDimensionFilter") DefinitionDimension definitionDimensionFilter, @Param("orderBy") String orderBy);
}
