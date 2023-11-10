package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.BaseBusinessFile;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 基础业务附件表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface BaseBusinessFileMapper extends BaseDaoMapper<BaseBusinessFile> {

    /**
     * 批量插入对象列表。
     *
     * @param baseBusinessFileList 新增对象列表。
     */
    void insertList(List<BaseBusinessFile> baseBusinessFileList);

    /**
     * 获取分组计算后的数据对象列表。
     *
     * @param baseBusinessFileFilter 主表过滤对象。
     * @param groupSelect 分组显示字段列表字符串，SELECT从句的参数。
     * @param groupBy 分组字段列表字符串，GROUP BY从句的参数。
     * @param orderBy 排序字符串，ORDER BY从句的参数。
     * @return 对象列表。
     */
    List<BaseBusinessFile> getGroupedBaseBusinessFileList(
            @Param("baseBusinessFileFilter") BaseBusinessFile baseBusinessFileFilter,
            @Param("groupSelect") String groupSelect,
            @Param("groupBy") String groupBy,
            @Param("orderBy") String orderBy);

    /**
     * 获取过滤后的对象列表。
     *
     * @param baseBusinessFileFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<BaseBusinessFile> getBaseBusinessFileList(
            @Param("baseBusinessFileFilter") BaseBusinessFile baseBusinessFileFilter, @Param("orderBy") String orderBy);
}
