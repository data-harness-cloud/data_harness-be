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
 * 数据开发-liteflow节点表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据开发-liteflow节点表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/devLiteflowNode")
public class DevLiteflowNodeController {

    @Autowired
    private DevLiteflowNodeService devLiteflowNodeService;

    /**
     * 新增数据开发-liteflow节点表数据。
     *
     * @param devLiteflowNodeDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devLiteflowNodeDto.id",
            "devLiteflowNodeDto.searchString",
            "devLiteflowNodeDto.createTimeStart",
            "devLiteflowNodeDto.createTimeEnd",
            "devLiteflowNodeDto.updateTimeStart",
            "devLiteflowNodeDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DevLiteflowNodeDto devLiteflowNodeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devLiteflowNodeDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevLiteflowNode devLiteflowNode = MyModelUtil.copyTo(devLiteflowNodeDto, DevLiteflowNode.class);
        // 验证关联Id的数据合法性
        CallResult callResult = devLiteflowNodeService.verifyRelatedData(devLiteflowNode, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        devLiteflowNode = devLiteflowNodeService.saveNew(devLiteflowNode);
        return ResponseResult.success(devLiteflowNode.getId());
    }

    /**
     * 更新数据开发-liteflow节点表数据。
     *
     * @param devLiteflowNodeDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devLiteflowNodeDto.searchString",
            "devLiteflowNodeDto.createTimeStart",
            "devLiteflowNodeDto.createTimeEnd",
            "devLiteflowNodeDto.updateTimeStart",
            "devLiteflowNodeDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DevLiteflowNodeDto devLiteflowNodeDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devLiteflowNodeDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevLiteflowNode devLiteflowNode = MyModelUtil.copyTo(devLiteflowNodeDto, DevLiteflowNode.class);
        DevLiteflowNode originalDevLiteflowNode = devLiteflowNodeService.getById(devLiteflowNode.getId());
        if (originalDevLiteflowNode == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = devLiteflowNodeService.verifyRelatedData(devLiteflowNode, originalDevLiteflowNode);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!devLiteflowNodeService.update(devLiteflowNode, originalDevLiteflowNode)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据开发-liteflow节点表数据。
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
     * 列出符合过滤条件的数据开发-liteflow节点表列表。
     *
     * @param devLiteflowNodeDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DevLiteflowNodeVo>> list(
            @MyRequestBody DevLiteflowNodeDto devLiteflowNodeDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DevLiteflowNode devLiteflowNodeFilter = MyModelUtil.copyTo(devLiteflowNodeDtoFilter, DevLiteflowNode.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DevLiteflowNode.class);
        List<DevLiteflowNode> devLiteflowNodeList =
                devLiteflowNodeService.getDevLiteflowNodeListWithRelation(devLiteflowNodeFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(devLiteflowNodeList, DevLiteflowNode.INSTANCE));
    }

    /**
     * 查看指定数据开发-liteflow节点表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DevLiteflowNodeVo> view(@RequestParam Long id) {
        DevLiteflowNode devLiteflowNode = devLiteflowNodeService.getByIdWithRelation(id, MyRelationParam.full());
        if (devLiteflowNode == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DevLiteflowNodeVo devLiteflowNodeVo = DevLiteflowNode.INSTANCE.fromModel(devLiteflowNode);
        return ResponseResult.success(devLiteflowNodeVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DevLiteflowNode originalDevLiteflowNode = devLiteflowNodeService.getById(id);
        if (originalDevLiteflowNode == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!devLiteflowNodeService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
