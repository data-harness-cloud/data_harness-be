package supie.webadmin.app.controller;

import cn.hutool.json.JSONUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import io.swagger.annotations.ApiOperation;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.app.liteFlow.model.LiteFlowNodeLogModel;
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

import javax.annotation.Resource;
import java.util.*;

/**
 * 数据开发-liteflow表达式规则表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据开发-liteflow表达式规则表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/devLiteflowRuler")
public class DevLiteflowRulerController {

    @Autowired
    private DevLiteflowRulerService devLiteflowRulerService;
    @Resource
    private FlowExecutor flowExecutor;

    @ApiOperation("执行LiteFlow")
    @PostMapping("/executeLiteFlow")
    public ResponseResult<String> executeLiteFlow(@MyRequestBody Long rulerId) {
        if (rulerId == null) return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp(String.valueOf(rulerId), null, new LinkedList<LiteFlowNodeLogModel>());
        LinkedList<LiteFlowNodeLogModel> contextBean = liteflowResponse.getContextBean(LinkedList.class);
        if (liteflowResponse.isSuccess()) {
            return ResponseResult.success(JSONUtil.toJsonStr(contextBean));
        }
        return ResponseResult.error(liteflowResponse.getCode(), JSONUtil.toJsonStr(contextBean) + liteflowResponse.getMessage());
    }

    /**
     * 新增数据开发-liteflow表达式规则表数据。
     *
     * @param devLiteflowRulerDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devLiteflowRulerDto.id",
            "devLiteflowRulerDto.searchString",
            "devLiteflowRulerDto.updateTimeStart",
            "devLiteflowRulerDto.updateTimeEnd",
            "devLiteflowRulerDto.createTimeStart",
            "devLiteflowRulerDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DevLiteflowRulerDto devLiteflowRulerDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devLiteflowRulerDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevLiteflowRuler devLiteflowRuler = MyModelUtil.copyTo(devLiteflowRulerDto, DevLiteflowRuler.class);
        // 验证关联Id的数据合法性
        CallResult callResult = devLiteflowRulerService.verifyRelatedData(devLiteflowRuler, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        devLiteflowRuler = devLiteflowRulerService.saveNew(devLiteflowRuler);
        return ResponseResult.success(devLiteflowRuler.getId());
    }

    /**
     * 更新数据开发-liteflow表达式规则表数据。
     *
     * @param devLiteflowRulerDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devLiteflowRulerDto.searchString",
            "devLiteflowRulerDto.updateTimeStart",
            "devLiteflowRulerDto.updateTimeEnd",
            "devLiteflowRulerDto.createTimeStart",
            "devLiteflowRulerDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DevLiteflowRulerDto devLiteflowRulerDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devLiteflowRulerDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevLiteflowRuler devLiteflowRuler = MyModelUtil.copyTo(devLiteflowRulerDto, DevLiteflowRuler.class);
        DevLiteflowRuler originalDevLiteflowRuler = devLiteflowRulerService.getById(devLiteflowRuler.getId());
        if (originalDevLiteflowRuler == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = devLiteflowRulerService.verifyRelatedData(devLiteflowRuler, originalDevLiteflowRuler);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!devLiteflowRulerService.update(devLiteflowRuler, originalDevLiteflowRuler)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据开发-liteflow表达式规则表数据。
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
     * 列出符合过滤条件的数据开发-liteflow表达式规则表列表。
     *
     * @param devLiteflowRulerDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DevLiteflowRulerVo>> list(
            @MyRequestBody DevLiteflowRulerDto devLiteflowRulerDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DevLiteflowRuler devLiteflowRulerFilter = MyModelUtil.copyTo(devLiteflowRulerDtoFilter, DevLiteflowRuler.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DevLiteflowRuler.class);
        List<DevLiteflowRuler> devLiteflowRulerList =
                devLiteflowRulerService.getDevLiteflowRulerListWithRelation(devLiteflowRulerFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(devLiteflowRulerList, DevLiteflowRuler.INSTANCE));
    }

    /**
     * 查看指定数据开发-liteflow表达式规则表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DevLiteflowRulerVo> view(@RequestParam Long id) {
        DevLiteflowRuler devLiteflowRuler = devLiteflowRulerService.getByIdWithRelation(id, MyRelationParam.full());
        if (devLiteflowRuler == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DevLiteflowRulerVo devLiteflowRulerVo = DevLiteflowRuler.INSTANCE.fromModel(devLiteflowRuler);
        return ResponseResult.success(devLiteflowRulerVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DevLiteflowRuler originalDevLiteflowRuler = devLiteflowRulerService.getById(id);
        if (originalDevLiteflowRuler == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // NOTE: 如果该对象的删除前数据一致性验证和实际需求有偏差，可以根据需求调整验证字段，甚至也可以直接删除下面的验证。
        // 删除前，先主动验证是否存在关联的从表数据。
        CallResult callResult = devLiteflowRulerService.verifyRelatedDataBeforeDelete(originalDevLiteflowRuler);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!devLiteflowRulerService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

}
