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
 * 数据规划-数据标准-数据字段质量关联表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-数据标准-数据字段质量关联表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/standardFieldQuality")
public class StandardFieldQualityController {

    @Autowired
    private StandardFieldQualityService standardFieldQualityService;

    /**
     * 新增数据规划-数据标准-数据字段质量关联表数据。
     *
     * @param standardFieldQualityDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "standardFieldQualityDto.id",
            "standardFieldQualityDto.updateTimeStart",
            "standardFieldQualityDto.updateTimeEnd",
            "standardFieldQualityDto.createTimeStart",
            "standardFieldQualityDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody StandardFieldQualityDto standardFieldQualityDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(standardFieldQualityDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        StandardFieldQuality standardFieldQuality = MyModelUtil.copyTo(standardFieldQualityDto, StandardFieldQuality.class);
        standardFieldQuality = standardFieldQualityService.saveNew(standardFieldQuality);
        return ResponseResult.success(standardFieldQuality.getId());
    }

    /**
     * 更新数据规划-数据标准-数据字段质量关联表数据。
     *
     * @param standardFieldQualityDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "standardFieldQualityDto.updateTimeStart",
            "standardFieldQualityDto.updateTimeEnd",
            "standardFieldQualityDto.createTimeStart",
            "standardFieldQualityDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody StandardFieldQualityDto standardFieldQualityDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(standardFieldQualityDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        StandardFieldQuality standardFieldQuality = MyModelUtil.copyTo(standardFieldQualityDto, StandardFieldQuality.class);
        StandardFieldQuality originalStandardFieldQuality = standardFieldQualityService.getById(standardFieldQuality.getId());
        if (originalStandardFieldQuality == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!standardFieldQualityService.update(standardFieldQuality, originalStandardFieldQuality)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-数据标准-数据字段质量关联表数据。
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
     * 列出符合过滤条件的数据规划-数据标准-数据字段质量关联表列表。
     *
     * @param standardFieldQualityDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<StandardFieldQualityVo>> list(
            @MyRequestBody StandardFieldQualityDto standardFieldQualityDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardFieldQuality standardFieldQualityFilter = MyModelUtil.copyTo(standardFieldQualityDtoFilter, StandardFieldQuality.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardFieldQuality.class);
        List<StandardFieldQuality> standardFieldQualityList =
                standardFieldQualityService.getStandardFieldQualityListWithRelation(standardFieldQualityFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(standardFieldQualityList, StandardFieldQuality.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-数据标准-数据字段质量关联表列表。
     *
     * @param standardFieldQualityDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<StandardFieldQualityVo>> listWithGroup(
            @MyRequestBody StandardFieldQualityDto standardFieldQualityDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardFieldQuality.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, StandardFieldQuality.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardFieldQuality filter = MyModelUtil.copyTo(standardFieldQualityDtoFilter, StandardFieldQuality.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<StandardFieldQuality> resultList = standardFieldQualityService.getGroupedStandardFieldQualityListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, StandardFieldQuality.INSTANCE));
    }

    /**
     * 查看指定数据规划-数据标准-数据字段质量关联表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<StandardFieldQualityVo> view(@RequestParam Long id) {
        StandardFieldQuality standardFieldQuality = standardFieldQualityService.getByIdWithRelation(id, MyRelationParam.full());
        if (standardFieldQuality == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        StandardFieldQualityVo standardFieldQualityVo = StandardFieldQuality.INSTANCE.fromModel(standardFieldQuality);
        return ResponseResult.success(standardFieldQualityVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        StandardFieldQuality originalStandardFieldQuality = standardFieldQualityService.getById(id);
        if (originalStandardFieldQuality == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!standardFieldQualityService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
