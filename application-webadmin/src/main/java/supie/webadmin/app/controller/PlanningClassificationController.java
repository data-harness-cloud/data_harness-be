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
 * 数据规划-数据架构-业务分类表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-数据架构-业务分类表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/planningClassification")
public class PlanningClassificationController {

    @Autowired
    private PlanningClassificationService planningClassificationService;

    /**
     * 新增数据规划-数据架构-业务分类表数据。
     *
     * @param planningClassificationDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "planningClassificationDto.id",
            "planningClassificationDto.searchString",
            "planningClassificationDto.updateTimeStart",
            "planningClassificationDto.updateTimeEnd",
            "planningClassificationDto.createTimeStart",
            "planningClassificationDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody PlanningClassificationDto planningClassificationDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(planningClassificationDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PlanningClassification planningClassification = MyModelUtil.copyTo(planningClassificationDto, PlanningClassification.class);
        // 验证关联Id的数据合法性
        CallResult callResult = planningClassificationService.verifyRelatedData(planningClassification, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        planningClassification = planningClassificationService.saveNew(planningClassification);
        return ResponseResult.success(planningClassification.getId());
    }

    /**
     * 更新数据规划-数据架构-业务分类表数据。
     *
     * @param planningClassificationDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "planningClassificationDto.searchString",
            "planningClassificationDto.updateTimeStart",
            "planningClassificationDto.updateTimeEnd",
            "planningClassificationDto.createTimeStart",
            "planningClassificationDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody PlanningClassificationDto planningClassificationDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(planningClassificationDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PlanningClassification planningClassification = MyModelUtil.copyTo(planningClassificationDto, PlanningClassification.class);
        PlanningClassification originalPlanningClassification = planningClassificationService.getById(planningClassification.getId());
        if (originalPlanningClassification == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = planningClassificationService.verifyRelatedData(planningClassification, originalPlanningClassification);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!planningClassificationService.update(planningClassification, originalPlanningClassification)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-数据架构-业务分类表数据。
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
     * 列出符合过滤条件的数据规划-数据架构-业务分类表列表。
     *
     * @param planningClassificationDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<PlanningClassificationVo>> list(
            @MyRequestBody PlanningClassificationDto planningClassificationDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        PlanningClassification planningClassificationFilter = MyModelUtil.copyTo(planningClassificationDtoFilter, PlanningClassification.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, PlanningClassification.class);
        List<PlanningClassification> planningClassificationList =
                planningClassificationService.getPlanningClassificationListWithRelation(planningClassificationFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(planningClassificationList, PlanningClassification.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-数据架构-业务分类表列表。
     *
     * @param planningClassificationDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<PlanningClassificationVo>> listWithGroup(
            @MyRequestBody PlanningClassificationDto planningClassificationDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, PlanningClassification.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, PlanningClassification.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        PlanningClassification filter = MyModelUtil.copyTo(planningClassificationDtoFilter, PlanningClassification.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<PlanningClassification> resultList = planningClassificationService.getGroupedPlanningClassificationListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, PlanningClassification.INSTANCE));
    }

    /**
     * 查看指定数据规划-数据架构-业务分类表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<PlanningClassificationVo> view(@RequestParam Long id) {
        PlanningClassification planningClassification = planningClassificationService.getByIdWithRelation(id, MyRelationParam.full());
        if (planningClassification == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        PlanningClassificationVo planningClassificationVo = PlanningClassification.INSTANCE.fromModel(planningClassification);
        return ResponseResult.success(planningClassificationVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        PlanningClassification originalPlanningClassification = planningClassificationService.getById(id);
        if (originalPlanningClassification == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // NOTE: 如果该对象的删除前数据一致性验证和实际需求有偏差，可以根据需求调整验证字段，甚至也可以直接删除下面的验证。
        // 删除前，先主动验证是否存在关联的从表数据。
        CallResult callResult = planningClassificationService.verifyRelatedDataBeforeDelete(originalPlanningClassification);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!planningClassificationService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
