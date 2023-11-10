package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ProjectDatasourceTemplateDict;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据项目-项目数据源模板字典表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ProjectDatasourceTemplateDictMapper extends BaseDaoMapper<ProjectDatasourceTemplateDict> {

    /**
     * 批量插入对象列表。
     *
     * @param projectDatasourceTemplateDictList 新增对象列表。
     */
    void insertList(List<ProjectDatasourceTemplateDict> projectDatasourceTemplateDictList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param projectDatasourceTemplateDictFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ProjectDatasourceTemplateDict> getProjectDatasourceTemplateDictList(
            @Param("projectDatasourceTemplateDictFilter") ProjectDatasourceTemplateDict projectDatasourceTemplateDictFilter, @Param("orderBy") String orderBy);
}
