package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.DevLiteflowLog;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据开发-liteflow-日志表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface DevLiteflowLogMapper extends BaseDaoMapper<DevLiteflowLog> {

    /**
     * 批量插入对象列表。
     *
     * @param devLiteflowLogList 新增对象列表。
     */
    void insertList(List<DevLiteflowLog> devLiteflowLogList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param devLiteflowLogFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<DevLiteflowLog> getDevLiteflowLogList(
            @Param("devLiteflowLogFilter") DevLiteflowLog devLiteflowLogFilter, @Param("orderBy") String orderBy);
}
