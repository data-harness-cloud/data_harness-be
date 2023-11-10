package supie.webadmin.app.service;

import supie.webadmin.app.model.*;
import supie.common.core.object.CallResult;
import supie.common.core.base.service.IBaseService;

import java.util.*;

/**
 * 数据项目-存算引擎表数据操作服务接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface ProjectEngineService extends IBaseService<ProjectEngine, Long> {

    /**
     * 保存新增对象。
     *
     * @param projectEngine 新增对象。
     * @return 返回新增对象。
     */
    ProjectEngine saveNew(ProjectEngine projectEngine);

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param projectEngineList 新增对象列表。
     */
    void saveNewBatch(List<ProjectEngine> projectEngineList);

    /**
     * 更新数据对象。
     *
     * @param projectEngine         更新的对象。
     * @param originalProjectEngine 原有数据对象。
     * @return 成功返回true，否则false。
     */
    boolean update(ProjectEngine projectEngine, ProjectEngine originalProjectEngine);

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    boolean remove(Long id);

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getProjectEngineListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ProjectEngine> getProjectEngineList(ProjectEngine filter, String orderBy);

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getProjectEngineList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    List<ProjectEngine> getProjectEngineListWithRelation(ProjectEngine filter, String orderBy);

    /**
     * 判断从表数据是否存在，如果存在就不能删除主对象，否则可以删除主对象。
     * 适用于主表对从表不是强制级联删除的场景。
     *
     * @param projectEngine 主表对象。
     * @return 没有关联数据返回true，否则false，同时返回具体的提示信息。
     */
    CallResult verifyRelatedDataBeforeDelete(ProjectEngine projectEngine);

    /**
     * 获取分组过滤后的数据查询结果，以及关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     *
     * @param filter      过滤对象。
     * @param groupSelect 分组显示列表参数。位于SQL语句SELECT的后面。
     * @param groupBy     分组参数。位于SQL语句的GROUP BY后面。
     * @param orderBy     排序字符串，ORDER BY从句的参数。
     * @return 分组过滤结果集。
     */
    List<ProjectEngine> getGroupedProjectEngineListWithRelation(
            ProjectEngine filter, String groupSelect, String groupBy, String orderBy);
}
