package supie.webadmin.upms.dao;

import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.upms.model.SysPermModule;

import java.util.List;

/**
 * 权限资源模块数据访问操作接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface SysPermModuleMapper extends BaseDaoMapper<SysPermModule> {

    /**
     * 获取整个权限模块和权限关联后的全部数据。
     *
     * @return 关联的权限模块和权限资源列表。
     */
    List<SysPermModule> getPermModuleAndPermList();
}
