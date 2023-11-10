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
 * 业务定义-数据维度操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "业务定义-数据维度管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/definitionDimension")
public class DefinitionDimensionController {

    @Autowired
    private DefinitionDimensionService definitionDimensionService;

    /**
     * 新增业务定义-数据维度数据。
     *
     * @param definitionDimensionDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "definitionDimensionDto.id",
            "definitionDimensionDto.searchString",
            "definitionDimensionDto.updateTimeStart",
            "definitionDimensionDto.updateTimeEnd",
            "definitionDimensionDto.createTimeStart",
            "definitionDimensionDto.createTimeEnd",
            "definitionDimensionDto.dimensionPeriodStartDateStart",
            "definitionDimensionDto.dimensionPeriodStartDateEnd",
            "definitionDimensionDto.dimensionPeriodEndDateStart",
            "definitionDimensionDto.dimensionPeriodEndDateEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DefinitionDimensionDto definitionDimensionDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(definitionDimensionDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DefinitionDimension definitionDimension = MyModelUtil.copyTo(definitionDimensionDto, DefinitionDimension.class);
        definitionDimension = definitionDimensionService.saveNew(definitionDimension);
        return ResponseResult.success(definitionDimension.getId());
    }

    /**
     * 更新业务定义-数据维度数据。
     *
     * @param definitionDimensionDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "definitionDimensionDto.searchString",
            "definitionDimensionDto.updateTimeStart",
            "definitionDimensionDto.updateTimeEnd",
            "definitionDimensionDto.createTimeStart",
            "definitionDimensionDto.createTimeEnd",
            "definitionDimensionDto.dimensionPeriodStartDateStart",
            "definitionDimensionDto.dimensionPeriodStartDateEnd",
            "definitionDimensionDto.dimensionPeriodEndDateStart",
            "definitionDimensionDto.dimensionPeriodEndDateEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DefinitionDimensionDto definitionDimensionDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(definitionDimensionDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DefinitionDimension definitionDimension = MyModelUtil.copyTo(definitionDimensionDto, DefinitionDimension.class);
        DefinitionDimension originalDefinitionDimension = definitionDimensionService.getById(definitionDimension.getId());
        if (originalDefinitionDimension == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!definitionDimensionService.update(definitionDimension, originalDefinitionDimension)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除业务定义-数据维度数据。
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
     * 列出符合过滤条件的业务定义-数据维度列表。
     *
     * @param definitionDimensionDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DefinitionDimensionVo>> list(
            @MyRequestBody DefinitionDimensionDto definitionDimensionDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DefinitionDimension definitionDimensionFilter = MyModelUtil.copyTo(definitionDimensionDtoFilter, DefinitionDimension.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DefinitionDimension.class);
        List<DefinitionDimension> definitionDimensionList =
                definitionDimensionService.getDefinitionDimensionListWithRelation(definitionDimensionFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(definitionDimensionList, DefinitionDimension.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的业务定义-数据维度列表。
     *
     * @param definitionDimensionDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<DefinitionDimensionVo>> listWithGroup(
            @MyRequestBody DefinitionDimensionDto definitionDimensionDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DefinitionDimension.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, DefinitionDimension.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DefinitionDimension filter = MyModelUtil.copyTo(definitionDimensionDtoFilter, DefinitionDimension.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<DefinitionDimension> resultList = definitionDimensionService.getGroupedDefinitionDimensionListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, DefinitionDimension.INSTANCE));
    }

    /**
     * 查看指定业务定义-数据维度对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DefinitionDimensionVo> view(@RequestParam Long id) {
        DefinitionDimension definitionDimension = definitionDimensionService.getByIdWithRelation(id, MyRelationParam.full());
        if (definitionDimension == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DefinitionDimensionVo definitionDimensionVo = DefinitionDimension.INSTANCE.fromModel(definitionDimension);
        return ResponseResult.success(definitionDimensionVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DefinitionDimension originalDefinitionDimension = definitionDimensionService.getById(id);
        if (originalDefinitionDimension == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!definitionDimensionService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
