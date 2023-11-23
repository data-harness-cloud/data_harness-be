package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ExternalApp;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 外部App表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ExternalAppMapper extends BaseDaoMapper<ExternalApp> {

    /**
     * 批量插入对象列表。
     *
     * @param externalAppList 新增对象列表。
     */
    void insertList(List<ExternalApp> externalAppList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param externalAppFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<ExternalApp> getGroupedExternalAppList(
            @Param("externalAppFilter") ExternalApp externalAppFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param externalAppFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<ExternalApp> getExternalAppList(
            @Param("externalAppFilter") ExternalApp externalAppFilter, @Param("orderBy") String orderBy);

    /**
     * 按 动态路由地址(URL) 查询外部应用信息
     *
     * @param url 动态路由地址(URL)
     * @return 列表<外部应用>
     * @author 王立宏
     * @date 2023/11/22 11:11
     */
    List<ExternalApp> queryExternalAppByUrl(@Param("url") String url);
}
