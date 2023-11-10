package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.DevConsole;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据开发-自助开发控制台数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface DevConsoleMapper extends BaseDaoMapper<DevConsole> {

    /**
     * 批量插入对象列表。
     *
     * @param devConsoleList 新增对象列表。
     */
    void insertList(List<DevConsole> devConsoleList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param devConsoleFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<DevConsole> getDevConsoleList(
            @Param("devConsoleFilter") DevConsole devConsoleFilter, @Param("orderBy") String orderBy);
}
