package supie.webadmin.app.liteFlow.node;

import cn.hutool.json.JSONUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supie.webadmin.app.dao.ProjectDatasourceMapper;
import supie.webadmin.app.liteFlow.exception.MyLiteFlowException;
import supie.webadmin.app.liteFlow.model.ErrorMessageModel;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
import supie.webadmin.app.liteFlow.model.SqlAndShellModel;
import supie.webadmin.app.model.ProjectDatasource;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;
import supie.webadmin.app.service.databasemanagement.model.DatabaseManagement;

import java.util.List;
import java.util.Map;

/**
 * 描述:用于执行客户自定义的SQL语句
 *
 * @author 王立宏
 * @date 2023/10/22 14:32
 * @path SDT-supie.webadmin.app.node-SqlNode
 */
@Slf4j
@Component
@LiteflowComponent(id = "sqlNode", name = "sqlNode")
public class SqlNode extends BaseNode {

    private SqlAndShellModel sqlAndShellModel;
    private ProjectDatasource projectDatasource = null;
    private DatabaseManagement databaseManagement;

    @Autowired
    private ProjectDatasourceMapper projectDatasourceMapper;
    @Autowired
    private StrategyFactory strategyFactory;

    @Override
    public void beforeProcess() {
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "进入SqlNode-beforeProcess!"));
        sqlAndShellModel = JSONUtil.toBean(devLiteflowNode.getFieldJsonData(), SqlAndShellModel.class);
        if (sqlAndShellModel.getSourceId() == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未关联数据源!"));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "未关联数据源!"));
        }
        projectDatasource = projectDatasourceMapper.selectById(sqlAndShellModel.getSourceId());
        if (projectDatasource == null) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "未找到数据源!"));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "未找到数据源!"));
        }
        databaseManagement = JSONUtil.toBean(projectDatasource.getDatasourceContent(), DatabaseManagement.class);
    }

    @Override
    public void process() throws Exception {
        nodeLog.add(LiteFlowNodeLogModel.warn(nodeId, nodeTag, "进入SqlNode-process!"));
        Strategy strategy = null;
        try {
            strategy = strategyFactory.getStrategy(
                    databaseManagement.getDatabaseType(), databaseManagement.getIp(), databaseManagement.getPort(),
                    databaseManagement.getDatabase(), databaseManagement.getUser(), databaseManagement.getPassword(),0);
        } catch (Exception e) {
            nodeLog.add(LiteFlowNodeLogModel.error(nodeId, nodeTag, "获取数据库连接失败!" + e));
            throw new MyLiteFlowException(new ErrorMessageModel(getClass(), "获取数据库连接失败!" + e));
        }
        List<Map<String, Object>> resultData = strategy.executeSqlList(sqlAndShellModel.getScript());
        strategy.closeAll();
        nodeLog.add(LiteFlowNodeLogModel.info(nodeId, nodeTag, "SQL执行结果为:" + JSONUtil.toJsonStr(resultData)));
        devLiteflowNodeMapper.setExecutionMessage(rulerId, nodeId, nodeTag, JSONUtil.toJsonStr(resultData));
    }

}
