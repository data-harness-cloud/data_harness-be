package supie.webadmin.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import supie.common.core.annotation.MyRequestBody;
import supie.common.core.object.ResponseResult;
import supie.webadmin.app.service.databasemanagement.*;
import supie.webadmin.app.service.databasemanagement.model.DatabaseManagement;
import supie.webadmin.app.service.impl.DatabaseManagementServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 数据库管理模块
 * @author 李涛
 */
@Api(tags = "数据库管理模块")
@Slf4j
@RestController
@RequestMapping("/admin/app/databaseManagement")
public class DatabaseManagementController {

    @Resource
    private DatabaseManagementServiceImpl databaseManagementService;
    @Autowired
    private StrategyFactory strategyFactory;

    /**
     * 数据库连接测试
     *
     * @param databaseManagement 数据库实体类。
     * @return 返回数据连接的信息。
     */
    @PostMapping("/connection")
    @ApiOperation("数据库连接测试")
    public ResponseResult<String> connection(@MyRequestBody DatabaseManagement databaseManagement){
        try {
            Strategy strategy = strategyFactory.getStrategy(
                    databaseManagement.getDatabaseType(), databaseManagement.getIp(), databaseManagement.getPort(),
                    databaseManagement.getDatabase(), databaseManagement.getUser(), databaseManagement.getPassword(),0);
            strategy.closeAll();
        } catch (Exception e) {
            log.error("数据源连接失败", e);
            return ResponseResult.error("500", "连接失败："+e.getMessage());
        }
        return ResponseResult.success("连接成功！");
    }


    /**
     * 数据库连接测试
     *
     * @param databaseManagement 数据库实体类。
     * @return 返回数据连接的信息。
     */
    @PostMapping("/initConnectionAndCreatelibrarypermissions")
    @ApiOperation("数据库连接测试---包含校验用户是否有创建数据库的权限")
    public ResponseResult<String> initConnectionAndCreatelibrarypermissions(@MyRequestBody DatabaseManagement databaseManagement){
        try {
            Strategy strategy = strategyFactory.getStrategy(
                    databaseManagement.getDatabaseType(), databaseManagement.getIp(), databaseManagement.getPort(),
                    databaseManagement.getDatabase(), databaseManagement.getUser(), databaseManagement.getPassword(),1);
            strategy.closeAll();
        } catch (Exception e) {
            log.error("数据源连接失败", e);
            return ResponseResult.error("500", "连接失败："+e.getMessage());
        }
        return ResponseResult.success("连接成功！");
    }

    /**
     * 数据库查询表。
     *
     * @param databaseManagement 数据库实体类。
     * @return 返回数据连接的信息。
     */
    @PostMapping("/queryDatabaseTable")
    @ApiOperation("根据连接信息查询数据库的表")
    public ResponseResult<List<Map<String,Object>>> queryDatabaseTable(@MyRequestBody DatabaseManagement databaseManagement){
        List<Map<String, Object>> price = new ArrayList<>();
        try {
            Strategy strategy = strategyFactory.getStrategy(
                    databaseManagement.getDatabaseType(), databaseManagement.getIp(), databaseManagement.getPort(),
                    databaseManagement.getDatabase(), databaseManagement.getUser(), databaseManagement.getPassword(),0);
            price = strategy.queryDatabaseTable(databaseManagement.getDatabase());
            strategy.closeAll();
        } catch (Exception e) {
            log.error("数据源连接失败", e);
            return ResponseResult.error("500", e.getMessage());
        }
        return ResponseResult.success(price);
    }

    /**
     * 数据库查询表。
     *
     * @param databaseManagement 数据库实体类。
     * @return 返回数据连接的信息。
     */
    @PostMapping("/queryTableFields")
    @ApiOperation("根据连接信息查询数据库表的字段")
    public ResponseResult<List<Map<String,Object>>> queryTableFields(@MyRequestBody DatabaseManagement databaseManagement){
        List<Map<String, Object>> price = new ArrayList<>();
        try {
            Strategy strategy = strategyFactory.getStrategy(
                    databaseManagement.getDatabaseType(), databaseManagement.getIp(), databaseManagement.getPort(),
                    databaseManagement.getDatabase(), databaseManagement.getUser(), databaseManagement.getPassword(),0);
            price = strategy.queryTableFields(databaseManagement.getDatabase(), databaseManagement.getTable());
            strategy.closeAll();
        } catch (Exception e) {
            log.error("数据源连接失败", e);
            return ResponseResult.error("500", e.getMessage());
        }
        return ResponseResult.success(price);
    }

    @PostMapping("/executeSql")
    @ApiOperation("执行sql接口")
    public ResponseResult<List<Map<String, Object>>> executeSql(@MyRequestBody String sql,@MyRequestBody DatabaseManagement databaseManagement){
        List<Map<String, Object>> resultData;
        try {
            Strategy strategy = strategyFactory.getStrategy(
                    databaseManagement.getDatabaseType(), databaseManagement.getIp(), databaseManagement.getPort(),
                    databaseManagement.getDatabase(), databaseManagement.getUser(), databaseManagement.getPassword(),0);
            resultData = strategy.executeSqlList(sql);
            strategy.closeAll();
        } catch (Exception e) {
            log.error("数据源连接失败", e);
            return ResponseResult.error("500", e.getMessage());
        }
        return ResponseResult.success(resultData);
    }

    @PostMapping("/getAllDatabaseName")
    @ApiOperation("获取可操作的所有数据库名称")
    public ResponseResult<List<String>> getAllDatabaseName(@MyRequestBody DatabaseManagement databaseManagement){
        Strategy strategy = strategyFactory.getStrategy(
                databaseManagement.getDatabaseType(), databaseManagement.getIp(), databaseManagement.getPort(),
                databaseManagement.getDatabase(), databaseManagement.getUser(), databaseManagement.getPassword(),0);
        List<String> resultData = strategy.queryAllDatabaseName();
        strategy.closeAll();
        return ResponseResult.success(resultData);
    }

}
