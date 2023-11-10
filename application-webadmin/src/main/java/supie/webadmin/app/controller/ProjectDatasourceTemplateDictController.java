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
 * 数据项目-项目数据源模板字典表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据项目-项目数据源模板字典表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/projectDatasourceTemplateDict")
public class ProjectDatasourceTemplateDictController {

    @Autowired
    private ProjectDatasourceTemplateDictService projectDatasourceTemplateDictService;

    /**
     * 新增数据项目-项目数据源模板字典表数据。
     *
     * @param projectDatasourceTemplateDictDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectDatasourceTemplateDictDto.id",
            "projectDatasourceTemplateDictDto.searchString",
            "projectDatasourceTemplateDictDto.updateTimeStart",
            "projectDatasourceTemplateDictDto.updateTimeEnd",
            "projectDatasourceTemplateDictDto.createTimeStart",
            "projectDatasourceTemplateDictDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ProjectDatasourceTemplateDictDto projectDatasourceTemplateDictDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectDatasourceTemplateDictDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectDatasourceTemplateDict projectDatasourceTemplateDict = MyModelUtil.copyTo(projectDatasourceTemplateDictDto, ProjectDatasourceTemplateDict.class);
        // 验证关联Id的数据合法性
        CallResult callResult = projectDatasourceTemplateDictService.verifyRelatedData(projectDatasourceTemplateDict, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        projectDatasourceTemplateDict = projectDatasourceTemplateDictService.saveNew(projectDatasourceTemplateDict);
        return ResponseResult.success(projectDatasourceTemplateDict.getId());
    }

    /**
     * 更新数据项目-项目数据源模板字典表数据。
     *
     * @param projectDatasourceTemplateDictDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectDatasourceTemplateDictDto.searchString",
            "projectDatasourceTemplateDictDto.updateTimeStart",
            "projectDatasourceTemplateDictDto.updateTimeEnd",
            "projectDatasourceTemplateDictDto.createTimeStart",
            "projectDatasourceTemplateDictDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ProjectDatasourceTemplateDictDto projectDatasourceTemplateDictDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectDatasourceTemplateDictDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectDatasourceTemplateDict projectDatasourceTemplateDict = MyModelUtil.copyTo(projectDatasourceTemplateDictDto, ProjectDatasourceTemplateDict.class);
        ProjectDatasourceTemplateDict originalProjectDatasourceTemplateDict = projectDatasourceTemplateDictService.getById(projectDatasourceTemplateDict.getId());
        if (originalProjectDatasourceTemplateDict == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = projectDatasourceTemplateDictService.verifyRelatedData(projectDatasourceTemplateDict, originalProjectDatasourceTemplateDict);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!projectDatasourceTemplateDictService.update(projectDatasourceTemplateDict, originalProjectDatasourceTemplateDict)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据项目-项目数据源模板字典表数据。
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
     * 列出符合过滤条件的数据项目-项目数据源模板字典表列表。
     *
     * @param projectDatasourceTemplateDictDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ProjectDatasourceTemplateDictVo>> list(
            @MyRequestBody ProjectDatasourceTemplateDictDto projectDatasourceTemplateDictDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectDatasourceTemplateDict projectDatasourceTemplateDictFilter = MyModelUtil.copyTo(projectDatasourceTemplateDictDtoFilter, ProjectDatasourceTemplateDict.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectDatasourceTemplateDict.class);
        List<ProjectDatasourceTemplateDict> projectDatasourceTemplateDictList =
                projectDatasourceTemplateDictService.getProjectDatasourceTemplateDictListWithRelation(projectDatasourceTemplateDictFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(projectDatasourceTemplateDictList, ProjectDatasourceTemplateDict.INSTANCE));
    }

    /**
     * 查看指定数据项目-项目数据源模板字典表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ProjectDatasourceTemplateDictVo> view(@RequestParam Long id) {
        ProjectDatasourceTemplateDict projectDatasourceTemplateDict = projectDatasourceTemplateDictService.getByIdWithRelation(id, MyRelationParam.full());
        if (projectDatasourceTemplateDict == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ProjectDatasourceTemplateDictVo projectDatasourceTemplateDictVo = ProjectDatasourceTemplateDict.INSTANCE.fromModel(projectDatasourceTemplateDict);
        return ResponseResult.success(projectDatasourceTemplateDictVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ProjectDatasourceTemplateDict originalProjectDatasourceTemplateDict = projectDatasourceTemplateDictService.getById(id);
        if (originalProjectDatasourceTemplateDict == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!projectDatasourceTemplateDictService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
