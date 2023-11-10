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
 * 数据项目-项目数据源关联表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据项目-项目数据源关联表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/projectDatasourceRelation")
public class ProjectDatasourceRelationController {

    @Autowired
    private ProjectDatasourceRelationService projectDatasourceRelationService;

    /**
     * 新增数据项目-项目数据源关联表数据。
     *
     * @param projectDatasourceRelationDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectDatasourceRelationDto.id",
            "projectDatasourceRelationDto.updateTimeStart",
            "projectDatasourceRelationDto.updateTimeEnd",
            "projectDatasourceRelationDto.createTimeStart",
            "projectDatasourceRelationDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ProjectDatasourceRelationDto projectDatasourceRelationDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectDatasourceRelationDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectDatasourceRelation projectDatasourceRelation = MyModelUtil.copyTo(projectDatasourceRelationDto, ProjectDatasourceRelation.class);
        // 验证关联Id的数据合法性
        CallResult callResult = projectDatasourceRelationService.verifyRelatedData(projectDatasourceRelation, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        projectDatasourceRelation = projectDatasourceRelationService.saveNew(projectDatasourceRelation);
        return ResponseResult.success(projectDatasourceRelation.getId());
    }

    /**
     * 更新数据项目-项目数据源关联表数据。
     *
     * @param projectDatasourceRelationDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectDatasourceRelationDto.updateTimeStart",
            "projectDatasourceRelationDto.updateTimeEnd",
            "projectDatasourceRelationDto.createTimeStart",
            "projectDatasourceRelationDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ProjectDatasourceRelationDto projectDatasourceRelationDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectDatasourceRelationDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectDatasourceRelation projectDatasourceRelation = MyModelUtil.copyTo(projectDatasourceRelationDto, ProjectDatasourceRelation.class);
        ProjectDatasourceRelation originalProjectDatasourceRelation = projectDatasourceRelationService.getById(projectDatasourceRelation.getId());
        if (originalProjectDatasourceRelation == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = projectDatasourceRelationService.verifyRelatedData(projectDatasourceRelation, originalProjectDatasourceRelation);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!projectDatasourceRelationService.update(projectDatasourceRelation, originalProjectDatasourceRelation)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据项目-项目数据源关联表数据。
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
     * 列出符合过滤条件的数据项目-项目数据源关联表列表。
     *
     * @param projectDatasourceRelationDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ProjectDatasourceRelationVo>> list(
            @MyRequestBody ProjectDatasourceRelationDto projectDatasourceRelationDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectDatasourceRelation projectDatasourceRelationFilter = MyModelUtil.copyTo(projectDatasourceRelationDtoFilter, ProjectDatasourceRelation.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectDatasourceRelation.class);
        List<ProjectDatasourceRelation> projectDatasourceRelationList =
                projectDatasourceRelationService.getProjectDatasourceRelationListWithRelation(projectDatasourceRelationFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(projectDatasourceRelationList, ProjectDatasourceRelation.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据项目-项目数据源关联表列表。
     *
     * @param projectDatasourceRelationDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ProjectDatasourceRelationVo>> listWithGroup(
            @MyRequestBody ProjectDatasourceRelationDto projectDatasourceRelationDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectDatasourceRelation.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ProjectDatasourceRelation.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectDatasourceRelation filter = MyModelUtil.copyTo(projectDatasourceRelationDtoFilter, ProjectDatasourceRelation.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ProjectDatasourceRelation> resultList = projectDatasourceRelationService.getGroupedProjectDatasourceRelationListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ProjectDatasourceRelation.INSTANCE));
    }

    /**
     * 查看指定数据项目-项目数据源关联表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ProjectDatasourceRelationVo> view(@RequestParam Long id) {
        ProjectDatasourceRelation projectDatasourceRelation = projectDatasourceRelationService.getByIdWithRelation(id, MyRelationParam.full());
        if (projectDatasourceRelation == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ProjectDatasourceRelationVo projectDatasourceRelationVo = ProjectDatasourceRelation.INSTANCE.fromModel(projectDatasourceRelation);
        return ResponseResult.success(projectDatasourceRelationVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ProjectDatasourceRelation originalProjectDatasourceRelation = projectDatasourceRelationService.getById(id);
        if (originalProjectDatasourceRelation == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!projectDatasourceRelationService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
