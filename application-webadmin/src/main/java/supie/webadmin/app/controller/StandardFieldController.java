package supie.webadmin.app.controller;

import com.alibaba.fastjson.JSONObject;
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
import java.util.stream.Collectors;

/**
 * 数据规划-数据标准-数据字段标准操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-数据标准-数据字段标准管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/standardField")
public class StandardFieldController {

    @Autowired
    private StandardFieldService standardFieldService;
    @Autowired
    private StandardQuatityService standardQuatityService;

    /**
     * 新增数据规划-数据标准-数据字段标准数据，及其关联的从表数据。
     *
     * @param standardFieldDto 新增主表对象。
     * @param standardFieldQuatityDtoList 多对多数据规划-数据标准-数据字段质量关联表中间表列表。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "standardFieldDto.id",
            "standardFieldDto.searchString",
            "standardFieldDto.updateTimeStart",
            "standardFieldDto.updateTimeEnd",
            "standardFieldDto.createTimeStart",
            "standardFieldDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(
            @MyRequestBody StandardFieldDto standardFieldDto,
            @MyRequestBody List<StandardFieldQuatityDto> standardFieldQuatityDtoList) {
        ResponseResult<Tuple2<StandardField, JSONObject>> verifyResult =
                this.doBusinessDataVerifyAndConvert(standardFieldDto, false, standardFieldQuatityDtoList);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        Tuple2<StandardField, JSONObject> bizData = verifyResult.getData();
        StandardField standardField = bizData.getFirst();
        standardField = standardFieldService.saveNewWithRelation(standardField, bizData.getSecond());
        return ResponseResult.success(standardField.getId());
    }

    /**
     * 更新数据规划-数据标准-数据字段标准数据。
     *
     * @param standardFieldDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "standardFieldDto.searchString",
            "standardFieldDto.updateTimeStart",
            "standardFieldDto.updateTimeEnd",
            "standardFieldDto.createTimeStart",
            "standardFieldDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody StandardFieldDto standardFieldDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(standardFieldDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        StandardField standardField = MyModelUtil.copyTo(standardFieldDto, StandardField.class);
        StandardField originalStandardField = standardFieldService.getById(standardField.getId());
        if (originalStandardField == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = standardFieldService.verifyRelatedData(standardField, originalStandardField);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!standardFieldService.update(standardField, originalStandardField)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-数据标准-数据字段标准数据。
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
     * 列出符合过滤条件的数据规划-数据标准-数据字段标准列表。
     *
     * @param standardFieldDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<StandardFieldVo>> list(
            @MyRequestBody StandardFieldDto standardFieldDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardField standardFieldFilter = MyModelUtil.copyTo(standardFieldDtoFilter, StandardField.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardField.class);
        List<StandardField> standardFieldList =
                standardFieldService.getStandardFieldListWithRelation(standardFieldFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(standardFieldList, StandardField.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-数据标准-数据字段标准列表。
     *
     * @param standardFieldDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<StandardFieldVo>> listWithGroup(
            @MyRequestBody StandardFieldDto standardFieldDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardField.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, StandardField.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardField filter = MyModelUtil.copyTo(standardFieldDtoFilter, StandardField.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<StandardField> resultList = standardFieldService.getGroupedStandardFieldListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, StandardField.INSTANCE));
    }

    /**
     * 查看指定数据规划-数据标准-数据字段标准对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<StandardFieldVo> view(@RequestParam Long id) {
        StandardField standardField = standardFieldService.getByIdWithRelation(id, MyRelationParam.full());
        if (standardField == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        StandardFieldVo standardFieldVo = StandardField.INSTANCE.fromModel(standardField);
        return ResponseResult.success(standardFieldVo);
    }

    /**
     * 列出不与指定数据规划-数据标准-数据字段标准存在多对多关系的 [数据规划-数据标准-数据质量表] 列表数据。通常用于查看添加新 [数据规划-数据标准-数据质量表] 对象的候选列表。
     *
     * @param id 主表关联字段。
     * @param standardQuatityDtoFilter [数据规划-数据标准-数据质量表] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listNotInStandardFieldQuatity")
    public ResponseResult<MyPageData<StandardQuatityVo>> listNotInStandardFieldQuatity(
            @MyRequestBody Long id,
            @MyRequestBody StandardQuatityDto standardQuatityDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (MyCommonUtil.isNotBlankOrNull(id) && !standardFieldService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardQuatity filter = MyModelUtil.copyTo(standardQuatityDtoFilter, StandardQuatity.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardQuatity.class);
        List<StandardQuatity> standardQuatityList =
                standardQuatityService.getNotInStandardQuatityListByStaidardFieldId(id, filter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(standardQuatityList, StandardQuatity.INSTANCE));
    }

    /**
     * 列出与指定数据规划-数据标准-数据字段标准存在多对多关系的 [数据规划-数据标准-数据质量表] 列表数据。
     *
     * @param id 主表关联字段。
     * @param standardQuatityDtoFilter [数据规划-数据标准-数据质量表] 过滤对象。
     * @param standardFieldQuatityDtoFilter 多对多关联表过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listStandardFieldQuatity")
    public ResponseResult<MyPageData<StandardQuatityVo>> listStandardFieldQuatity(
            @MyRequestBody(required = true) Long id,
            @MyRequestBody StandardQuatityDto standardQuatityDtoFilter,
            @MyRequestBody StandardFieldQuatityDto standardFieldQuatityDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (!standardFieldService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardQuatity filter = MyModelUtil.copyTo(standardQuatityDtoFilter, StandardQuatity.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardQuatity.class);
        StandardFieldQuatity standardFieldQuatityFilter =
                MyModelUtil.copyTo(standardFieldQuatityDtoFilter, StandardFieldQuatity.class);
        List<StandardQuatity> standardQuatityList =
                standardQuatityService.getStandardQuatityListByStaidardFieldId(id, filter, standardFieldQuatityFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(standardQuatityList, StandardQuatity.INSTANCE));
    }

    /**
     * 批量添加数据规划-数据标准-数据字段标准和 [数据规划-数据标准-数据质量表] 对象的多对多关联关系数据。
     *
     * @param staidardFieldId 主表主键Id。
     * @param standardFieldQuatityDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.ADD_M2M)
    @PostMapping("/addStandardFieldQuatity")
    public ResponseResult<Void> addStandardFieldQuatity(
            @MyRequestBody Long staidardFieldId,
            @MyRequestBody List<StandardFieldQuatityDto> standardFieldQuatityDtoList) {
        if (MyCommonUtil.existBlankArgument(staidardFieldId, standardFieldQuatityDtoList)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        Set<Long> staidardQualityIdSet =
                standardFieldQuatityDtoList.stream().map(StandardFieldQuatityDto::getStaidardQualityId).collect(Collectors.toSet());
        if (!standardFieldService.existId(staidardFieldId)
                || !standardQuatityService.existUniqueKeyList("id", staidardQualityIdSet)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        List<StandardFieldQuatity> standardFieldQuatityList =
                MyModelUtil.copyCollectionTo(standardFieldQuatityDtoList, StandardFieldQuatity.class);
        standardFieldService.addStandardFieldQuatityList(standardFieldQuatityList, staidardFieldId);
        return ResponseResult.success();
    }

    /**
     * 更新指定数据规划-数据标准-数据字段标准和指定 [数据规划-数据标准-数据质量表] 的多对多关联数据。
     *
     * @param standardFieldQuatityDto 对多对中间表对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/updateStandardFieldQuatity")
    public ResponseResult<Void> updateStandardFieldQuatity(
            @MyRequestBody StandardFieldQuatityDto standardFieldQuatityDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(standardFieldQuatityDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        StandardFieldQuatity standardFieldQuatity = MyModelUtil.copyTo(standardFieldQuatityDto, StandardFieldQuatity.class);
        if (!standardFieldService.updateStandardFieldQuatity(standardFieldQuatity)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 显示数据规划-数据标准-数据字段标准和指定 [数据规划-数据标准-数据质量表] 的多对多关联详情数据。
     *
     * @param staidardFieldId 主表主键Id。
     * @param staidardQualityId 从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @GetMapping("/viewStandardFieldQuatity")
    public ResponseResult<StandardFieldQuatityVo> viewStandardFieldQuatity(
            @RequestParam Long staidardFieldId, @RequestParam Long staidardQualityId) {
        StandardFieldQuatity standardFieldQuatity = standardFieldService.getStandardFieldQuatity(staidardFieldId, staidardQualityId);
        if (standardFieldQuatity == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        StandardFieldQuatityVo standardFieldQuatityVo = MyModelUtil.copyTo(standardFieldQuatity, StandardFieldQuatityVo.class);
        return ResponseResult.success(standardFieldQuatityVo);
    }

    /**
     * 移除指定数据规划-数据标准-数据字段标准和指定 [数据规划-数据标准-数据质量表] 的多对多关联关系。
     *
     * @param staidardFieldId 主表主键Id。
     * @param staidardQualityId 从表主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE_M2M)
    @PostMapping("/deleteStandardFieldQuatity")
    public ResponseResult<Void> deleteStandardFieldQuatity(
            @MyRequestBody Long staidardFieldId, @MyRequestBody Long staidardQualityId) {
        if (MyCommonUtil.existBlankArgument(staidardFieldId, staidardQualityId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        if (!standardFieldService.removeStandardFieldQuatity(staidardFieldId, staidardQualityId)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    private ResponseResult<Tuple2<StandardField, JSONObject>> doBusinessDataVerifyAndConvert(
            StandardFieldDto standardFieldDto,
            boolean forUpdate,
            List<StandardFieldQuatityDto> standardFieldQuatityDtoList) {
        ErrorCodeEnum errorCode = ErrorCodeEnum.DATA_VALIDATED_FAILED;
        String errorMessage = MyCommonUtil.getModelValidationError(standardFieldDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(errorCode, errorMessage);
        }
        errorMessage = MyCommonUtil.getModelValidationError(standardFieldQuatityDtoList);
        if (errorMessage != null) {
            return ResponseResult.error(errorCode, "参数 [standardFieldQuatityDtoList] " + errorMessage);
        }
        // 全部关联从表数据的验证和转换
        JSONObject relationData = new JSONObject();
        CallResult verifyResult;
        // 下面是输入参数中，主表关联数据的验证。
        StandardField standardField = MyModelUtil.copyTo(standardFieldDto, StandardField.class);
        StandardField originalData = null;
        if (forUpdate && standardField != null) {
            originalData = standardFieldService.getById(standardField.getId());
            if (originalData == null) {
                return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
            }
            relationData.put("originalData", originalData);
        }
        verifyResult = standardFieldService.verifyRelatedData(standardField, originalData);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        // 处理主表的多对多关联 [StandardFieldQuatity]
        List<StandardFieldQuatity> standardFieldQuatityList =
                MyModelUtil.copyCollectionTo(standardFieldQuatityDtoList, StandardFieldQuatity.class);
        relationData.put("standardFieldQuatityList", standardFieldQuatityList);
        return ResponseResult.success(new Tuple2<>(standardField, relationData));
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        StandardField originalStandardField = standardFieldService.getById(id);
        if (originalStandardField == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!standardFieldService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
