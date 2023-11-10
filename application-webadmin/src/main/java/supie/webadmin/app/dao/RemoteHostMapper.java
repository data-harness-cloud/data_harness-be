package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.RemoteHost;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 项目中心-基础信息配置-远程主机数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface RemoteHostMapper extends BaseDaoMapper<RemoteHost> {

    /**
     * 批量插入对象列表。
     *
     * @param remoteHostList 新增对象列表。
     */
    void insertList(List<RemoteHost> remoteHostList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param remoteHostFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<RemoteHost> getRemoteHostList(
            @Param("remoteHostFilter") RemoteHost remoteHostFilter, @Param("orderBy") String orderBy);
}
