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
 * 数据规划-数据架构-主题域表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-数据架构-主题域表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/planningTheme")
public class PlanningThemeController {

    @Autowired
    private PlanningThemeService planningThemeService;

    /**
     * 新增数据规划-数据架构-主题域表数据。
     *
     * @param planningThemeDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "planningThemeDto.id",
            "planningThemeDto.searchString",
            "planningThemeDto.updateTimeStart",
            "planningThemeDto.updateTimeEnd",
            "planningThemeDto.createTimeStart",
            "planningThemeDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody PlanningThemeDto planningThemeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(planningThemeDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PlanningTheme planningTheme = MyModelUtil.copyTo(planningThemeDto, PlanningTheme.class);
        planningTheme = planningThemeService.saveNew(planningTheme);
        return ResponseResult.success(planningTheme.getId());
    }

    /**
     * 更新数据规划-数据架构-主题域表数据。
     *
     * @param planningThemeDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "planningThemeDto.searchString",
            "planningThemeDto.updateTimeStart",
            "planningThemeDto.updateTimeEnd",
            "planningThemeDto.createTimeStart",
            "planningThemeDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody PlanningThemeDto planningThemeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(planningThemeDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PlanningTheme planningTheme = MyModelUtil.copyTo(planningThemeDto, PlanningTheme.class);
        PlanningTheme originalPlanningTheme = planningThemeService.getById(planningTheme.getId());
        if (originalPlanningTheme == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!planningThemeService.update(planningTheme, originalPlanningTheme)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-数据架构-主题域表数据。
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
     * 列出符合过滤条件的数据规划-数据架构-主题域表列表。
     *
     * @param planningThemeDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<PlanningThemeVo>> list(
            @MyRequestBody PlanningThemeDto planningThemeDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        PlanningTheme planningThemeFilter = MyModelUtil.copyTo(planningThemeDtoFilter, PlanningTheme.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, PlanningTheme.class);
        List<PlanningTheme> planningThemeList =
                planningThemeService.getPlanningThemeListWithRelation(planningThemeFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(planningThemeList, PlanningTheme.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-数据架构-主题域表列表。
     *
     * @param planningThemeDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<PlanningThemeVo>> listWithGroup(
            @MyRequestBody PlanningThemeDto planningThemeDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, PlanningTheme.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, PlanningTheme.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        PlanningTheme filter = MyModelUtil.copyTo(planningThemeDtoFilter, PlanningTheme.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<PlanningTheme> resultList = planningThemeService.getGroupedPlanningThemeListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, PlanningTheme.INSTANCE));
    }

    /**
     * 查看指定数据规划-数据架构-主题域表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<PlanningThemeVo> view(@RequestParam Long id) {
        PlanningTheme planningTheme = planningThemeService.getByIdWithRelation(id, MyRelationParam.full());
        if (planningTheme == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        PlanningThemeVo planningThemeVo = PlanningTheme.INSTANCE.fromModel(planningTheme);
        return ResponseResult.success(planningThemeVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        PlanningTheme originalPlanningTheme = planningThemeService.getById(id);
        if (originalPlanningTheme == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // NOTE: 如果该对象的删除前数据一致性验证和实际需求有偏差，可以根据需求调整验证字段，甚至也可以直接删除下面的验证。
        // 删除前，先主动验证是否存在关联的从表数据。
        CallResult callResult = planningThemeService.verifyRelatedDataBeforeDelete(originalPlanningTheme);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!planningThemeService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
