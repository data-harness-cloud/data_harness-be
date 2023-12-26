package supie.webadmin.app.controller;

import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBucket;
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
 * 数据规划-模型设计-模型物理表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-模型设计-模型物理表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/modelPhysicsScript")
public class ModelPhysicsScriptController {

    @Autowired
    private ModelPhysicsScriptService modelPhysicsScriptService;

    /**
     * 新增数据规划-模型设计-模型物理表数据。
     *
     * @param modelPhysicsScriptDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "modelPhysicsScriptDto.id",
            "modelPhysicsScriptDto.searchString",
            "modelPhysicsScriptDto.updateTimeStart",
            "modelPhysicsScriptDto.updateTimeEnd",
            "modelPhysicsScriptDto.createTimeStart",
            "modelPhysicsScriptDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ModelPhysicsScriptDto modelPhysicsScriptDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(modelPhysicsScriptDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ModelPhysicsScript modelPhysicsScript = MyModelUtil.copyTo(modelPhysicsScriptDto, ModelPhysicsScript.class);
        // 验证关联Id的数据合法性
        CallResult callResult = modelPhysicsScriptService.verifyRelatedData(modelPhysicsScript, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        modelPhysicsScript = modelPhysicsScriptService.saveNew(modelPhysicsScript);
        return ResponseResult.success(modelPhysicsScript.getId());
    }

    /**
     * 新增物理表数据，并且创建物理表。-模型设计-模型物理表数据。
     *
     * @param modelPhysicsScriptDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperation(value = "新增物理表数据，并且创建物理表。")
    @ApiOperationSupport(ignoreParameters = {
            "modelPhysicsScriptDto.id",
            "modelPhysicsScriptDto.searchString",
            "modelPhysicsScriptDto.updateTimeStart",
            "modelPhysicsScriptDto.updateTimeEnd",
            "modelPhysicsScriptDto.createTimeStart",
            "modelPhysicsScriptDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/addAndCreatingTable")
    public ResponseResult<Long> addAndCreatingTable(@MyRequestBody ModelPhysicsScriptDto modelPhysicsScriptDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(modelPhysicsScriptDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ModelPhysicsScript modelPhysicsScript = MyModelUtil.copyTo(modelPhysicsScriptDto, ModelPhysicsScript.class);
        // 验证关联Id的数据合法性
        CallResult callResult = modelPhysicsScriptService.verifyRelatedData(modelPhysicsScript, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        modelPhysicsScript = modelPhysicsScriptService.addAndCreatingTable(modelPhysicsScript);
        return ResponseResult.success(modelPhysicsScript.getId());
    }

    /**
     * 更新数据规划-模型设计-模型物理表数据。
     *
     * @param modelPhysicsScriptDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "modelPhysicsScriptDto.searchString",
            "modelPhysicsScriptDto.updateTimeStart",
            "modelPhysicsScriptDto.updateTimeEnd",
            "modelPhysicsScriptDto.createTimeStart",
            "modelPhysicsScriptDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ModelPhysicsScriptDto modelPhysicsScriptDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(modelPhysicsScriptDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ModelPhysicsScript modelPhysicsScript = MyModelUtil.copyTo(modelPhysicsScriptDto, ModelPhysicsScript.class);
        ModelPhysicsScript originalModelPhysicsScript = modelPhysicsScriptService.getById(modelPhysicsScript.getId());
        if (originalModelPhysicsScript == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = modelPhysicsScriptService.verifyRelatedData(modelPhysicsScript, originalModelPhysicsScript);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!modelPhysicsScriptService.update(modelPhysicsScript, originalModelPhysicsScript)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-模型设计-模型物理表数据。
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
     * 列出符合过滤条件的数据规划-模型设计-模型物理表列表。
     *
     * @param modelPhysicsScriptDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ModelPhysicsScriptVo>> list(
            @MyRequestBody ModelPhysicsScriptDto modelPhysicsScriptDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelPhysicsScript modelPhysicsScriptFilter = MyModelUtil.copyTo(modelPhysicsScriptDtoFilter, ModelPhysicsScript.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelPhysicsScript.class);
        List<ModelPhysicsScript> modelPhysicsScriptList =
                modelPhysicsScriptService.getModelPhysicsScriptListWithRelation(modelPhysicsScriptFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(modelPhysicsScriptList, ModelPhysicsScript.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-模型设计-模型物理表列表。
     *
     * @param modelPhysicsScriptDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ModelPhysicsScriptVo>> listWithGroup(
            @MyRequestBody ModelPhysicsScriptDto modelPhysicsScriptDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelPhysicsScript.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ModelPhysicsScript.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelPhysicsScript filter = MyModelUtil.copyTo(modelPhysicsScriptDtoFilter, ModelPhysicsScript.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ModelPhysicsScript> resultList = modelPhysicsScriptService.getGroupedModelPhysicsScriptListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ModelPhysicsScript.INSTANCE));
    }

    /**
     * 查看指定数据规划-模型设计-模型物理表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ModelPhysicsScriptVo> view(@RequestParam Long id) {
        ModelPhysicsScript modelPhysicsScript = modelPhysicsScriptService.getByIdWithRelation(id, MyRelationParam.full());
        if (modelPhysicsScript == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ModelPhysicsScriptVo modelPhysicsScriptVo = ModelPhysicsScript.INSTANCE.fromModel(modelPhysicsScript);
        return ResponseResult.success(modelPhysicsScriptVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ModelPhysicsScript originalModelPhysicsScript = modelPhysicsScriptService.getById(id);
        if (originalModelPhysicsScript == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!modelPhysicsScriptService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 执行创建表 SQL
     *
     * @param modelPhysicsScriptId 模型逻辑主表 ID
     * @return 响应结果<模型物理脚本 VO>
     * @author 王立宏
     * @date 2023/11/03 03:55
     */
    @ApiOperation("执行建表 SQL")
    @PostMapping("/executeCreatingTableSql")
    public ResponseResult<Void> executeCreatingTableSql(
            @MyRequestBody Long modelPhysicsScriptId) {
        if (modelPhysicsScriptId == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        ModelPhysicsScript modelPhysicsScript = modelPhysicsScriptService.getById(modelPhysicsScriptId);
        if (modelPhysicsScript == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        if (modelPhysicsScript.getModelPhysicsTable() == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, "建表语句不能为空！");
        }
        modelPhysicsScriptService.executeCreatingTableSql(modelPhysicsScript);
        return ResponseResult.success();
    }

}
