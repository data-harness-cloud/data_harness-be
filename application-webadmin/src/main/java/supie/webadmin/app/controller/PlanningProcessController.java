package supie.webadmin.app.controller;

import cn.hutool.core.util.ObjectUtil;
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
 * 数据规划-数据架构-业务过程表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-数据架构-业务过程表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/planningProcess")
public class PlanningProcessController {

    @Autowired
    private PlanningProcessService planningProcessService;

    /**
     * 新增数据规划-数据架构-业务过程表数据。
     *
     * @param planningProcessDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "planningProcessDto.id",
            "planningProcessDto.searchString",
            "planningProcessDto.updateTimeStart",
            "planningProcessDto.updateTimeEnd",
            "planningProcessDto.createTimeStart",
            "planningProcessDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody PlanningProcessDto planningProcessDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(planningProcessDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PlanningProcess planningProcess = MyModelUtil.copyTo(planningProcessDto, PlanningProcess.class);
        // 验证父Id的数据合法性
        PlanningProcess parentPlanningProcess;
        if (MyCommonUtil.isNotBlankOrNull(planningProcess.getProcessParentId())) {
            parentPlanningProcess = planningProcessService.getById(planningProcess.getProcessParentId());
            if (parentPlanningProcess == null) {
                errorMessage = "数据验证失败，关联的父节点并不存在，请刷新后重试！";
                return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        planningProcess = planningProcessService.saveNew(planningProcess);
        return ResponseResult.success(planningProcess.getId());
    }

    /**
     * 更新数据规划-数据架构-业务过程表数据。
     *
     * @param planningProcessDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "planningProcessDto.searchString",
            "planningProcessDto.updateTimeStart",
            "planningProcessDto.updateTimeEnd",
            "planningProcessDto.createTimeStart",
            "planningProcessDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody PlanningProcessDto planningProcessDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(planningProcessDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        PlanningProcess planningProcess = MyModelUtil.copyTo(planningProcessDto, PlanningProcess.class);
        PlanningProcess originalPlanningProcess = planningProcessService.getById(planningProcess.getId());
        if (originalPlanningProcess == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证父Id的数据合法性
        if (MyCommonUtil.isNotBlankOrNull(planningProcess.getProcessParentId())
                && ObjectUtil.notEqual(planningProcess.getProcessParentId(), originalPlanningProcess.getProcessParentId())) {
            PlanningProcess parentPlanningProcess = planningProcessService.getById(planningProcess.getProcessParentId());
            if (parentPlanningProcess == null) {
                // NOTE: 修改下面方括号中的话述
                errorMessage = "数据验证失败，关联的 [父节点] 并不存在，请刷新后重试！";
                return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        if (!planningProcessService.update(planningProcess, originalPlanningProcess)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-数据架构-业务过程表数据。
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
     * 列出符合过滤条件的数据规划-数据架构-业务过程表列表。
     *
     * @param planningProcessDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<PlanningProcessVo>> list(
            @MyRequestBody PlanningProcessDto planningProcessDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        PlanningProcess planningProcessFilter = MyModelUtil.copyTo(planningProcessDtoFilter, PlanningProcess.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, PlanningProcess.class);
        List<PlanningProcess> planningProcessList =
                planningProcessService.getPlanningProcessListWithRelation(planningProcessFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(planningProcessList, PlanningProcess.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-数据架构-业务过程表列表。
     *
     * @param planningProcessDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<PlanningProcessVo>> listWithGroup(
            @MyRequestBody PlanningProcessDto planningProcessDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, PlanningProcess.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, PlanningProcess.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        PlanningProcess filter = MyModelUtil.copyTo(planningProcessDtoFilter, PlanningProcess.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<PlanningProcess> resultList = planningProcessService.getGroupedPlanningProcessListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, PlanningProcess.INSTANCE));
    }

    /**
     * 查看指定数据规划-数据架构-业务过程表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<PlanningProcessVo> view(@RequestParam Long id) {
        PlanningProcess planningProcess = planningProcessService.getByIdWithRelation(id, MyRelationParam.full());
        if (planningProcess == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        PlanningProcessVo planningProcessVo = PlanningProcess.INSTANCE.fromModel(planningProcess);
        return ResponseResult.success(planningProcessVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        PlanningProcess originalPlanningProcess = planningProcessService.getById(id);
        if (originalPlanningProcess == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (planningProcessService.hasChildren(id)) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象存在子对象] ，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.HAS_CHILDREN_DATA, errorMessage);
        }
        // NOTE: 如果该对象的删除前数据一致性验证和实际需求有偏差，可以根据需求调整验证字段，甚至也可以直接删除下面的验证。
        // 删除前，先主动验证是否存在关联的从表数据。
        CallResult callResult = planningProcessService.verifyRelatedDataBeforeDelete(originalPlanningProcess);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!planningProcessService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
