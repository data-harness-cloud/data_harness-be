package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.DevLiteflowScript;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据开发-liteflow脚本表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface DevLiteflowScriptMapper extends BaseDaoMapper<DevLiteflowScript> {

    /**
     * 批量插入对象列表。
     *
     * @param devLiteflowScriptList 新增对象列表。
     */
    void insertList(List<DevLiteflowScript> devLiteflowScriptList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param devLiteflowScriptFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<DevLiteflowScript> getDevLiteflowScriptList(
            @Param("devLiteflowScriptFilter") DevLiteflowScript devLiteflowScriptFilter, @Param("orderBy") String orderBy);
}
