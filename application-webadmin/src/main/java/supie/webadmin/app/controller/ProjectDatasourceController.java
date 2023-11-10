package supie.webadmin.app.controller;

import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
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
 * 数据项目-数据源表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据项目-数据源表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/projectDatasource")
public class ProjectDatasourceController {

    @Autowired
    private ProjectDatasourceService projectDatasourceService;

    /**
     * 新增数据项目-数据源表数据。
     *
     * @param projectDatasourceDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectDatasourceDto.id",
            "projectDatasourceDto.searchString",
            "projectDatasourceDto.updateTimeStart",
            "projectDatasourceDto.updateTimeEnd",
            "projectDatasourceDto.createTimeStart",
            "projectDatasourceDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ProjectDatasourceDto projectDatasourceDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectDatasourceDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectDatasource projectDatasource = MyModelUtil.copyTo(projectDatasourceDto, ProjectDatasource.class);
        // 验证关联Id的数据合法性
        CallResult callResult = projectDatasourceService.verifyRelatedData(projectDatasource, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        projectDatasource = projectDatasourceService.saveNew(projectDatasource);
        return ResponseResult.success(projectDatasource.getId());
    }

    /**
     * 更新数据项目-数据源表数据。
     *
     * @param projectDatasourceDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectDatasourceDto.searchString",
            "projectDatasourceDto.updateTimeStart",
            "projectDatasourceDto.updateTimeEnd",
            "projectDatasourceDto.createTimeStart",
            "projectDatasourceDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ProjectDatasourceDto projectDatasourceDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectDatasourceDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectDatasource projectDatasource = MyModelUtil.copyTo(projectDatasourceDto, ProjectDatasource.class);
        ProjectDatasource originalProjectDatasource = projectDatasourceService.getById(projectDatasource.getId());
        if (originalProjectDatasource == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = projectDatasourceService.verifyRelatedData(projectDatasource, originalProjectDatasource);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!projectDatasourceService.update(projectDatasource, originalProjectDatasource)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据项目-数据源表数据。
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
     * 列出符合过滤条件的数据项目-数据源表列表。
     *
     * @param projectDatasourceDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ProjectDatasourceVo>> list(
            @MyRequestBody ProjectDatasourceDto projectDatasourceDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectDatasource projectDatasourceFilter = MyModelUtil.copyTo(projectDatasourceDtoFilter, ProjectDatasource.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectDatasource.class);
        List<ProjectDatasource> projectDatasourceList =
                projectDatasourceService.getProjectDatasourceListWithRelation(projectDatasourceFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(projectDatasourceList, ProjectDatasource.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据项目-数据源表列表。
     *
     * @param projectDatasourceDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ProjectDatasourceVo>> listWithGroup(
            @MyRequestBody ProjectDatasourceDto projectDatasourceDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectDatasource.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ProjectDatasource.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectDatasource filter = MyModelUtil.copyTo(projectDatasourceDtoFilter, ProjectDatasource.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ProjectDatasource> resultList = projectDatasourceService.getGroupedProjectDatasourceListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ProjectDatasource.INSTANCE));
    }

    /**
     * 查看指定数据项目-数据源表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ProjectDatasourceVo> view(@RequestParam Long id) {
        ProjectDatasource projectDatasource = projectDatasourceService.getByIdWithRelation(id, MyRelationParam.full());
        if (projectDatasource == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ProjectDatasourceVo projectDatasourceVo = ProjectDatasource.INSTANCE.fromModel(projectDatasource);
        return ResponseResult.success(projectDatasourceVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ProjectDatasource originalProjectDatasource = projectDatasourceService.getById(id);
        if (originalProjectDatasource == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!projectDatasourceService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
