package supie.webadmin.app.dao;

import supie.common.core.annotation.EnableDataPerm;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.webadmin.app.model.DevLiteflowNode;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 数据开发-liteflow节点表数据操作访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableDataPerm
public interface DevLiteflowNodeMapper extends BaseDaoMapper<DevLiteflowNode> {

    /**
     * 批量插入对象列表。
     *
     * @param devLiteflowNodeList 新增对象列表。
     */
    void insertList(List<DevLiteflowNode> devLiteflowNodeList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param devLiteflowNodeFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<DevLiteflowNode> getDevLiteflowNodeList(
            @Param("devLiteflowNodeFilter") DevLiteflowNode devLiteflowNodeFilter, @Param("orderBy") String orderBy);

    /**
     * 查询组件自身属性值
     *
     * @param rulerId 规则链ID
     * @param nodeId   组件ID
     * @param nodeTag  组件标签
     * @return 组件配置信息
     * @author 王立宏
     * @date 2023/10/22 09:21
     */
    DevLiteflowNode queryNode(
            @Param("rulerId") String rulerId, @Param("nodeId") String nodeId, @Param("nodeTag") String nodeTag);

    /**
     * 存储节点执行结果信息
     *
     * @param rulerId 规则链ID
     * @param nodeId   节点 ID
     * @param nodeTag  节点 标签
     * @param executionMessage 执行结果信息
     * @return int
     * @author 王立宏
     * @date 2023/10/27 05:53
     */
    int setExecutionMessage(
            @Param("rulerId") String rulerId, @Param("nodeId") String nodeId, @Param("nodeTag") String nodeTag,
            @Param("executionMessage") String executionMessage);
}
