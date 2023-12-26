package supie.webadmin.app.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;
import supie.webadmin.app.vo.*;
import supie.webadmin.app.dto.*;
import supie.webadmin.app.model.*;
import supie.webadmin.app.service.*;
import supie.common.core.object.*;
import supie.common.core.util.*;
import supie.common.core.constant.*;
import supie.common.core.annotation.MyRequestBody;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 数据项目-存算引擎表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据项目-存算引擎表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/projectEngine")
public class ProjectEngineController {

    @Autowired
    private ProjectEngineService projectEngineService;
    @Autowired
    private ProjectMainService projectMainService;
    @Autowired
    private StrategyFactory strategyFactory;

    /**
     * 新增数据项目-存算引擎表数据。
     *
     * @param projectEngineDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectEngineDto.id",
            "projectEngineDto.searchString",
            "projectEngineDto.updateTimeStart",
            "projectEngineDto.updateTimeEnd",
            "projectEngineDto.createTimeStart",
            "projectEngineDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ProjectEngineDto projectEngineDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectEngineDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectEngine projectEngine = MyModelUtil.copyTo(projectEngineDto, ProjectEngine.class);
        projectEngine = projectEngineService.saveNew(projectEngine);
        return ResponseResult.success(projectEngine.getId());
    }

    /**
     * 更新数据项目-存算引擎表数据。
     *
     * @param projectEngineDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectEngineDto.searchString",
            "projectEngineDto.updateTimeStart",
            "projectEngineDto.updateTimeEnd",
            "projectEngineDto.createTimeStart",
            "projectEngineDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ProjectEngineDto projectEngineDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectEngineDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectEngine projectEngine = MyModelUtil.copyTo(projectEngineDto, ProjectEngine.class);
        ProjectEngine originalProjectEngine = projectEngineService.getById(projectEngine.getId());
        if (originalProjectEngine == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!projectEngineService.update(projectEngine, originalProjectEngine)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据项目-存算引擎表数据。
     *
     * @param id 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long id) {
        if (MyCommonUtil.existBlankArgument(id)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(id);
    }

    /**
     * 列出符合过滤条件的数据项目-存算引擎表列表。
     *
     * @param projectEngineDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ProjectEngineVo>> list(
            @MyRequestBody ProjectEngineDto projectEngineDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectEngine projectEngineFilter = MyModelUtil.copyTo(projectEngineDtoFilter, ProjectEngine.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectEngine.class);
        List<ProjectEngine> projectEngineList =
                projectEngineService.getProjectEngineListWithRelation(projectEngineFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(projectEngineList, ProjectEngine.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据项目-存算引擎表列表。
     *
     * @param projectEngineDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ProjectEngineVo>> listWithGroup(
            @MyRequestBody ProjectEngineDto projectEngineDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectEngine.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ProjectEngine.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectEngine filter = MyModelUtil.copyTo(projectEngineDtoFilter, ProjectEngine.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ProjectEngine> resultList = projectEngineService.getGroupedProjectEngineListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ProjectEngine.INSTANCE));
    }

    /**
     * 查看指定数据项目-存算引擎表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ProjectEngineVo> view(@RequestParam Long id) {
        ProjectEngine projectEngine = projectEngineService.getByIdWithRelation(id, MyRelationParam.full());
        if (projectEngine == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ProjectEngineVo projectEngineVo = ProjectEngine.INSTANCE.fromModel(projectEngine);
        return ResponseResult.success(projectEngineVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ProjectEngine originalProjectEngine = projectEngineService.getById(id);
        if (originalProjectEngine == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // NOTE: 如果该对象的删除前数据一致性验证和实际需求有偏差，可以根据需求调整验证字段，甚至也可以直接删除下面的验证。
        // 删除前，先主动验证是否存在关联的从表数据。
        CallResult callResult = projectEngineService.verifyRelatedDataBeforeDelete(originalProjectEngine);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!projectEngineService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 获取可操作的数据库集
     *
     * @param projectId 项目 ID
     * @return 返回结果
     * @author 王立宏
     * @date 2023/11/14 05:30
     */
    @PostMapping("/getAllDatabaseName")
    @ApiOperation("获取可操作的所有数据库名称")
    public ResponseResult<List<String>> getAllDatabaseName(@MyRequestBody Long projectId){
        if (projectId == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        ProjectMain projectMain = projectMainService.getByIdWithRelation(projectId, MyRelationParam.full());
        if (projectMain == null || projectMain.getProjectEngineId() == null || projectMain.getProjectEngine() == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST,
                    "数据验证失败，当前 [数据] 并不存在，请刷新后重试！");
        }
        ProjectEngine projectEngine = projectMain.getProjectEngine();
        Strategy strategy = strategyFactory.getStrategy(
                projectEngine.getEngineType(), projectEngine.getEngineHost(), projectEngine.getEnginePort(),
                null, projectEngine.getEngineUsername(), projectEngine.getEnginePassword(),0);
        List<String> resultData = strategy.queryAllDatabaseName();
        strategy.closeAll();
        return ResponseResult.success(resultData);
    }

    /**
     * 执行 SQL
     *
     * @param sql          SQL语句
     * @param databaseName 目标数据库名称
     * @param projectId    项目 ID
     * @return 响应结果
     * @author 王立宏
     * @date 2023/11/14 05:30
     */
    @ApiOperation("执行sql接口")
    @PostMapping("/executeSql")
    public ResponseResult<List<Map<String, Object>>> executeSql(
            @MyRequestBody String sql, @MyRequestBody String databaseName, @MyRequestBody Long projectId){
        if (projectId == null || StrUtil.isBlank(sql) || StrUtil.isBlank(databaseName)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        ProjectMain projectMain = projectMainService.getByIdWithRelation(projectId, MyRelationParam.full());
        if (projectMain == null || projectMain.getProjectEngineId() == null || projectMain.getProjectEngine() == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST,
                    "数据验证失败，当前 [数据] 并不存在，请刷新后重试！");
        }
        ProjectEngine projectEngine = projectMain.getProjectEngine();
        Strategy strategy = strategyFactory.getStrategy(
                projectEngine.getEngineType(), projectEngine.getEngineHost(), projectEngine.getEnginePort(),
                databaseName, projectEngine.getEngineUsername(), projectEngine.getEnginePassword(),0);
        List<Map<String, Object>> resultData = strategy.executeSqlList(sql);
        strategy.closeAll();
        return ResponseResult.success(resultData);
    }

}
