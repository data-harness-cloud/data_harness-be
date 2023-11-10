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
import java.util.stream.Collectors;

/**
 * 业务定义-指标管理操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "业务定义-指标管理管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/definitionIndex")
public class DefinitionIndexController {

    @Autowired
    private DefinitionIndexService definitionIndexService;
    @Autowired
    private ModelDesginFieldService modelDesginFieldService;

    /**
     * 新增业务定义-指标管理数据。
     *
     * @param definitionIndexDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "definitionIndexDto.id",
            "definitionIndexDto.searchString",
            "definitionIndexDto.updateTimeStart",
            "definitionIndexDto.updateTimeEnd",
            "definitionIndexDto.createTimeStart",
            "definitionIndexDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DefinitionIndexDto definitionIndexDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(definitionIndexDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DefinitionIndex definitionIndex = MyModelUtil.copyTo(definitionIndexDto, DefinitionIndex.class);
        definitionIndex = definitionIndexService.saveNew(definitionIndex);
        return ResponseResult.success(definitionIndex.getId());
    }

    /**
     * 更新业务定义-指标管理数据。
     *
     * @param definitionIndexDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "definitionIndexDto.searchString",
            "definitionIndexDto.updateTimeStart",
            "definitionIndexDto.updateTimeEnd",
            "definitionIndexDto.createTimeStart",
            "definitionIndexDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DefinitionIndexDto definitionIndexDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(definitionIndexDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DefinitionIndex definitionIndex = MyModelUtil.copyTo(definitionIndexDto, DefinitionIndex.class);
        DefinitionIndex originalDefinitionIndex = definitionIndexService.getById(definitionIndex.getId());
        if (originalDefinitionIndex == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!definitionIndexService.update(definitionIndex, originalDefinitionIndex)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除业务定义-指标管理数据。
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
     * 列出符合过滤条件的业务定义-指标管理列表。
     *
     * @param definitionIndexDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DefinitionIndexVo>> list(
            @MyRequestBody DefinitionIndexDto definitionIndexDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DefinitionIndex definitionIndexFilter = MyModelUtil.copyTo(definitionIndexDtoFilter, DefinitionIndex.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DefinitionIndex.class);
        List<DefinitionIndex> definitionIndexList =
                definitionIndexService.getDefinitionIndexListWithRelation(definitionIndexFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(definitionIndexList, DefinitionIndex.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的业务定义-指标管理列表。
     *
     * @param definitionIndexDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<DefinitionIndexVo>> listWithGroup(
            @MyRequestBody DefinitionIndexDto definitionIndexDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DefinitionIndex.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, DefinitionIndex.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DefinitionIndex filter = MyModelUtil.copyTo(definitionIndexDtoFilter, DefinitionIndex.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<DefinitionIndex> resultList = definitionIndexService.getGroupedDefinitionIndexListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, DefinitionIndex.INSTANCE));
    }

    /**
     * 查看指定业务定义-指标管理对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DefinitionIndexVo> view(@RequestParam Long id) {
        DefinitionIndex definitionIndex = definitionIndexService.getByIdWithRelation(id, MyRelationParam.full());
        if (definitionIndex == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DefinitionIndexVo definitionIndexVo = DefinitionIndex.INSTANCE.fromModel(definitionIndex);
        return ResponseResult.success(definitionIndexVo);
    }

    /**
     * 列出不与指定业务定义-指标管理存在多对多关系的 [数据规划-模型设计-模型字段表] 列表数据。通常用于查看添加新 [数据规划-模型设计-模型字段表] 对象的候选列表。
     *
     * @param id 主表关联字段。
     * @param modelDesginFieldDtoFilter [数据规划-模型设计-模型字段表] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listNotInDefinitionIndexModelFieldRelation")
    public ResponseResult<MyPageData<ModelDesginFieldVo>> listNotInDefinitionIndexModelFieldRelation(
            @MyRequestBody Long id,
            @MyRequestBody ModelDesginFieldDto modelDesginFieldDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (MyCommonUtil.isNotBlankOrNull(id) && !definitionIndexService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelDesginField filter = MyModelUtil.copyTo(modelDesginFieldDtoFilter, ModelDesginField.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelDesginField.class);
        List<ModelDesginField> modelDesginFieldList =
                modelDesginFieldService.getNotInModelDesginFieldListByIndexId(id, filter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(modelDesginFieldList, ModelDesginField.INSTANCE));
    }

    /**
     * 列出与指定业务定义-指标管理存在多对多关系的 [数据规划-模型设计-模型字段表] 列表数据。
     *
     * @param id 主表关联字段。
     * @param modelDesginFieldDtoFilter [数据规划-模型设计-模型字段表] 过滤对象。
     * @param definitionIndexModelFieldRelationDtoFilter 多对多关联表过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listDefinitionIndexModelFieldRelation")
    public ResponseResult<MyPageData<ModelDesginFieldVo>> listDefinitionIndexModelFieldRelation(
            @MyRequestBody(required = true) Long id,
            @MyRequestBody ModelDesginFieldDto modelDesginFieldDtoFilter,
            @MyRequestBody DefinitionIndexModelFieldRelationDto definitionIndexModelFieldRelationDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (!definitionIndexService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelDesginField filter = MyModelUtil.copyTo(modelDesginFieldDtoFilter, ModelDesginField.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelDesginField.class);
        DefinitionIndexModelFieldRelation definitionIndexModelFieldRelationFilter =
                MyModelUtil.copyTo(definitionIndexModelFieldRelationDtoFilter, DefinitionIndexModelFieldRelation.class);
        List<ModelDesginField> modelDesginFieldList =
                modelDesginFieldService.getModelDesginFieldListByIndexId(id, filter, definitionIndexModelFieldRelationFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(modelDesginFieldList, ModelDesginField.INSTANCE));
    }

    /**
     * 批量添加业务定义-指标管理和 [数据规划-模型设计-模型字段表] 对象的多对多关联关系数据。
     *
     * @param indexId 主表主键Id。
     * @param definitionIndexModelFieldRelationDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.ADD_M2M)
    @PostMapping("/addDefinitionIndexModelFieldRelation")
    public ResponseResult<Void> addDefinitionIndexModelFieldRelation(
            @MyRequestBody Long indexId,
            @MyRequestBody List<DefinitionIndexModelFieldRelationDto> definitionIndexModelFieldRelationDtoList) {
        if (MyCommonUtil.existBlankArgument(indexId, definitionIndexModelFieldRelationDtoList)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        Set<Long> modelFieldIdSet =
                definitionIndexModelFieldRelationDtoList.stream().map(DefinitionIndexModelFieldRelationDto::getModelFieldId).collect(Collectors.toSet());
        if (!definitionIndexService.existId(indexId)
                || !modelDesginFieldService.existUniqueKeyList("id", modelFieldIdSet)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        List<DefinitionIndexModelFieldRelation> definitionIndexModelFieldRelationList =
                MyModelUtil.copyCollectionTo(definitionIndexModelFieldRelationDtoList, DefinitionIndexModelFieldRelation.class);
        definitionIndexService.addDefinitionIndexModelFieldRelationList(definitionIndexModelFieldRelationList, indexId);
        return ResponseResult.success();
    }

    /**
     * 更新指定业务定义-指标管理和指定 [数据规划-模型设计-模型字段表] 的多对多关联数据。
     *
     * @param definitionIndexModelFieldRelationDto 对多对中间表对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/updateDefinitionIndexModelFieldRelation")
    public ResponseResult<Void> updateDefinitionIndexModelFieldRelation(
            @MyRequestBody DefinitionIndexModelFieldRelationDto definitionIndexModelFieldRelationDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(definitionIndexModelFieldRelationDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation = MyModelUtil.copyTo(definitionIndexModelFieldRelationDto, DefinitionIndexModelFieldRelation.class);
        if (!definitionIndexService.updateDefinitionIndexModelFieldRelation(definitionIndexModelFieldRelation)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 显示业务定义-指标管理和指定 [数据规划-模型设计-模型字段表] 的多对多关联详情数据。
     *
     * @param indexId 主表主键Id。
     * @param modelFieldId 从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @GetMapping("/viewDefinitionIndexModelFieldRelation")
    public ResponseResult<DefinitionIndexModelFieldRelationVo> viewDefinitionIndexModelFieldRelation(
            @RequestParam Long indexId, @RequestParam Long modelFieldId) {
        DefinitionIndexModelFieldRelation definitionIndexModelFieldRelation = definitionIndexService.getDefinitionIndexModelFieldRelation(indexId, modelFieldId);
        if (definitionIndexModelFieldRelation == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DefinitionIndexModelFieldRelationVo definitionIndexModelFieldRelationVo = MyModelUtil.copyTo(definitionIndexModelFieldRelation, DefinitionIndexModelFieldRelationVo.class);
        return ResponseResult.success(definitionIndexModelFieldRelationVo);
    }

    /**
     * 移除指定业务定义-指标管理和指定 [数据规划-模型设计-模型字段表] 的多对多关联关系。
     *
     * @param indexId 主表主键Id。
     * @param modelFieldId 从表主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE_M2M)
    @PostMapping("/deleteDefinitionIndexModelFieldRelation")
    public ResponseResult<Void> deleteDefinitionIndexModelFieldRelation(
            @MyRequestBody Long indexId, @MyRequestBody Long modelFieldId) {
        if (MyCommonUtil.existBlankArgument(indexId, modelFieldId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        if (!definitionIndexService.removeDefinitionIndexModelFieldRelation(indexId, modelFieldId)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DefinitionIndex originalDefinitionIndex = definitionIndexService.getById(id);
        if (originalDefinitionIndex == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!definitionIndexService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
