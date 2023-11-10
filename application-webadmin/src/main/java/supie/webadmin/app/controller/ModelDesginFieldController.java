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
 * 数据规划-模型设计-模型字段表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-模型设计-模型字段表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/modelDesginField")
public class ModelDesginFieldController {

    @Autowired
    private ModelDesginFieldService modelDesginFieldService;

    /**
     * 新增数据规划-模型设计-模型字段表数据。
     *
     * @param modelDesginFieldDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "modelDesginFieldDto.id",
            "modelDesginFieldDto.searchString",
            "modelDesginFieldDto.updateTimeStart",
            "modelDesginFieldDto.updateTimeEnd",
            "modelDesginFieldDto.createTimeStart",
            "modelDesginFieldDto.createTimeEnd",
            "modelDesginFieldDto.modelFieldLengthStart",
            "modelDesginFieldDto.modelFieldLengthEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ModelDesginFieldDto modelDesginFieldDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(modelDesginFieldDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ModelDesginField modelDesginField = MyModelUtil.copyTo(modelDesginFieldDto, ModelDesginField.class);
        // 验证关联Id的数据合法性
        CallResult callResult = modelDesginFieldService.verifyRelatedData(modelDesginField, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        modelDesginField = modelDesginFieldService.saveNew(modelDesginField);
        return ResponseResult.success(modelDesginField.getId());
    }

    /**
     * 更新数据规划-模型设计-模型字段表数据。
     *
     * @param modelDesginFieldDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "modelDesginFieldDto.searchString",
            "modelDesginFieldDto.updateTimeStart",
            "modelDesginFieldDto.updateTimeEnd",
            "modelDesginFieldDto.createTimeStart",
            "modelDesginFieldDto.createTimeEnd",
            "modelDesginFieldDto.modelFieldLengthStart",
            "modelDesginFieldDto.modelFieldLengthEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ModelDesginFieldDto modelDesginFieldDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(modelDesginFieldDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ModelDesginField modelDesginField = MyModelUtil.copyTo(modelDesginFieldDto, ModelDesginField.class);
        ModelDesginField originalModelDesginField = modelDesginFieldService.getById(modelDesginField.getId());
        if (originalModelDesginField == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = modelDesginFieldService.verifyRelatedData(modelDesginField, originalModelDesginField);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!modelDesginFieldService.update(modelDesginField, originalModelDesginField)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-模型设计-模型字段表数据。
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
     * 列出符合过滤条件的数据规划-模型设计-模型字段表列表。
     *
     * @param modelDesginFieldDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ModelDesginFieldVo>> list(
            @MyRequestBody ModelDesginFieldDto modelDesginFieldDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelDesginField modelDesginFieldFilter = MyModelUtil.copyTo(modelDesginFieldDtoFilter, ModelDesginField.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelDesginField.class);
        List<ModelDesginField> modelDesginFieldList =
                modelDesginFieldService.getModelDesginFieldListWithRelation(modelDesginFieldFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(modelDesginFieldList, ModelDesginField.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-模型设计-模型字段表列表。
     *
     * @param modelDesginFieldDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ModelDesginFieldVo>> listWithGroup(
            @MyRequestBody ModelDesginFieldDto modelDesginFieldDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelDesginField.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ModelDesginField.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelDesginField filter = MyModelUtil.copyTo(modelDesginFieldDtoFilter, ModelDesginField.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ModelDesginField> resultList = modelDesginFieldService.getGroupedModelDesginFieldListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ModelDesginField.INSTANCE));
    }

    /**
     * 查看指定数据规划-模型设计-模型字段表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ModelDesginFieldVo> view(@RequestParam Long id) {
        ModelDesginField modelDesginField = modelDesginFieldService.getByIdWithRelation(id, MyRelationParam.full());
        if (modelDesginField == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ModelDesginFieldVo modelDesginFieldVo = ModelDesginField.INSTANCE.fromModel(modelDesginField);
        return ResponseResult.success(modelDesginFieldVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ModelDesginField originalModelDesginField = modelDesginFieldService.getById(id);
        if (originalModelDesginField == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!modelDesginFieldService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
