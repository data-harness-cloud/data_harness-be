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
 * 业务定义-数据维度发布记录表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "业务定义-数据维度发布记录表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/definitionDimensionPublishRecord")
public class DefinitionDimensionPublishRecordController {

    @Autowired
    private DefinitionDimensionPublishRecordService definitionDimensionPublishRecordService;

    /**
     * 新增业务定义-数据维度发布记录表数据。
     *
     * @param definitionDimensionPublishRecordDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "definitionDimensionPublishRecordDto.id",
            "definitionDimensionPublishRecordDto.searchString",
            "definitionDimensionPublishRecordDto.updateTimeStart",
            "definitionDimensionPublishRecordDto.updateTimeEnd",
            "definitionDimensionPublishRecordDto.createTimeStart",
            "definitionDimensionPublishRecordDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DefinitionDimensionPublishRecordDto definitionDimensionPublishRecordDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(definitionDimensionPublishRecordDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DefinitionDimensionPublishRecord definitionDimensionPublishRecord = MyModelUtil.copyTo(definitionDimensionPublishRecordDto, DefinitionDimensionPublishRecord.class);
        definitionDimensionPublishRecord = definitionDimensionPublishRecordService.saveNew(definitionDimensionPublishRecord);
        return ResponseResult.success(definitionDimensionPublishRecord.getId());
    }

    /**
     * 更新业务定义-数据维度发布记录表数据。
     *
     * @param definitionDimensionPublishRecordDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "definitionDimensionPublishRecordDto.searchString",
            "definitionDimensionPublishRecordDto.updateTimeStart",
            "definitionDimensionPublishRecordDto.updateTimeEnd",
            "definitionDimensionPublishRecordDto.createTimeStart",
            "definitionDimensionPublishRecordDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DefinitionDimensionPublishRecordDto definitionDimensionPublishRecordDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(definitionDimensionPublishRecordDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DefinitionDimensionPublishRecord definitionDimensionPublishRecord = MyModelUtil.copyTo(definitionDimensionPublishRecordDto, DefinitionDimensionPublishRecord.class);
        DefinitionDimensionPublishRecord originalDefinitionDimensionPublishRecord = definitionDimensionPublishRecordService.getById(definitionDimensionPublishRecord.getId());
        if (originalDefinitionDimensionPublishRecord == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!definitionDimensionPublishRecordService.update(definitionDimensionPublishRecord, originalDefinitionDimensionPublishRecord)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除业务定义-数据维度发布记录表数据。
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
     * 列出符合过滤条件的业务定义-数据维度发布记录表列表。
     *
     * @param definitionDimensionPublishRecordDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DefinitionDimensionPublishRecordVo>> list(
            @MyRequestBody DefinitionDimensionPublishRecordDto definitionDimensionPublishRecordDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DefinitionDimensionPublishRecord definitionDimensionPublishRecordFilter = MyModelUtil.copyTo(definitionDimensionPublishRecordDtoFilter, DefinitionDimensionPublishRecord.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DefinitionDimensionPublishRecord.class);
        List<DefinitionDimensionPublishRecord> definitionDimensionPublishRecordList =
                definitionDimensionPublishRecordService.getDefinitionDimensionPublishRecordListWithRelation(definitionDimensionPublishRecordFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(definitionDimensionPublishRecordList, DefinitionDimensionPublishRecord.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的业务定义-数据维度发布记录表列表。
     *
     * @param definitionDimensionPublishRecordDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<DefinitionDimensionPublishRecordVo>> listWithGroup(
            @MyRequestBody DefinitionDimensionPublishRecordDto definitionDimensionPublishRecordDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DefinitionDimensionPublishRecord.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, DefinitionDimensionPublishRecord.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DefinitionDimensionPublishRecord filter = MyModelUtil.copyTo(definitionDimensionPublishRecordDtoFilter, DefinitionDimensionPublishRecord.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<DefinitionDimensionPublishRecord> resultList = definitionDimensionPublishRecordService.getGroupedDefinitionDimensionPublishRecordListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, DefinitionDimensionPublishRecord.INSTANCE));
    }

    /**
     * 查看指定业务定义-数据维度发布记录表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DefinitionDimensionPublishRecordVo> view(@RequestParam Long id) {
        DefinitionDimensionPublishRecord definitionDimensionPublishRecord = definitionDimensionPublishRecordService.getByIdWithRelation(id, MyRelationParam.full());
        if (definitionDimensionPublishRecord == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DefinitionDimensionPublishRecordVo definitionDimensionPublishRecordVo = DefinitionDimensionPublishRecord.INSTANCE.fromModel(definitionDimensionPublishRecord);
        return ResponseResult.success(definitionDimensionPublishRecordVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DefinitionDimensionPublishRecord originalDefinitionDimensionPublishRecord = definitionDimensionPublishRecordService.getById(id);
        if (originalDefinitionDimensionPublishRecord == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!definitionDimensionPublishRecordService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
