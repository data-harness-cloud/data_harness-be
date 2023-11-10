package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.ProjectHostRelation;

import java.util.*;

/**
 * 数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface ProjectHostRelationMapper extends BaseDaoMapper<ProjectHostRelation> {

    /**
     * 批量插入对象列表。
     *
     * @param projectHostRelationList 新增对象列表。
     */
    void insertList(List<ProjectHostRelation> projectHostRelationList);
}
