package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ModelDesginField;
import supie.webadmin.app.model.DefinitionIndexModelFieldRelation;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据规划-模型设计-模型字段表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ModelDesginFieldMapper extends BaseDaoMapper<ModelDesginField> {

    /**
     * 批量插入对象列表。
     *
     * @param modelDesginFieldList 新增对象列表。
     */
    void insertList(List<ModelDesginField> modelDesginFieldList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param modelDesginFieldFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<ModelDesginField> getGroupedModelDesginFieldList(
            @Param("modelDesginFieldFilter") ModelDesginField modelDesginFieldFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param modelDesginFieldFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ModelDesginField> getModelDesginFieldList(
            @Param("modelDesginFieldFilter") ModelDesginField modelDesginFieldFilter, @Param("orderBy") String orderBy);

    /**
     * 根据关联主表Id，获取关联从表数据列表。
     *
     * @param modelId 型号编号
     * @return 列表<模型设计字段>
     * @author 王立宏
     * @date 2023/10/25 06:12
     */
    List<ModelDesginField> queryByModelId(@Param("modelId") Long modelId);

   /**
     * 根据关联主表Id，获取关联从表数据列表。
     *
     * @param indexId 关联主表Id。
     * @param modelDesginFieldFilter 从表过滤对象。
     * @param definitionIndexModelFieldRelationFilter 多对多关联表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 从表数据列表。
     */
    List<ModelDesginField> getModelDesginFieldListByIndexId(
            @Param("indexId") Long indexId,
            @Param("modelDesginFieldFilter") ModelDesginField modelDesginFieldFilter,
            @Param("definitionIndexModelFieldRelationFilter") DefinitionIndexModelFieldRelation definitionIndexModelFieldRelationFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据关联主表Id，获取关联从表中没有和主表建立关联关系的数据列表。
     *
     * @param indexId 关联主表Id。
     * @param modelDesginFieldFilter 过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 与主表没有建立关联的从表数据列表。
     */
    List<ModelDesginField> getNotInModelDesginFieldListByIndexId(
            @Param("indexId") Long indexId,
            @Param("modelDesginFieldFilter") ModelDesginField modelDesginFieldFilter,
            @Param("orderBy") String orderBy);
}
