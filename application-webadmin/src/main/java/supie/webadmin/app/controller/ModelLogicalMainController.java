package supie.webadmin.app.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 数据规划-模型设计-模型逻辑表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-模型设计-模型逻辑表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/modelLogicalMain")
public class ModelLogicalMainController {

    private static final String SQL_PREFIX = "SQL:";

    @Autowired
    private ModelLogicalMainService modelLogicalMainService;
    @Autowired
    private ModelDesginFieldService modelDesginFieldService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ProjectMainService projectMainService;

    /**
     * 新增数据规划-模型设计-模型逻辑表数据，及其关联的从表数据。
     *
     * @param modelLogicalMainDto 新增主表对象。
     * @param modelPhysicsScriptDto 一对一数据规划-模型设计-模型物理表从表Dto。
     * @param modelDesginFieldDtoList 一对多数据规划-模型设计-模型字段表从表列表。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "modelLogicalMainDto.id",
            "modelLogicalMainDto.searchString",
            "modelLogicalMainDto.updateTimeStart",
            "modelLogicalMainDto.updateTimeEnd",
            "modelLogicalMainDto.createTimeStart",
            "modelLogicalMainDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(
            @MyRequestBody ModelLogicalMainDto modelLogicalMainDto,
            @MyRequestBody ModelPhysicsScriptDto modelPhysicsScriptDto,
            @MyRequestBody List<ModelDesginFieldDto> modelDesginFieldDtoList) {
        ResponseResult<Tuple2<ModelLogicalMain, JSONObject>> verifyResult = this.doBusinessDataVerifyAndConvert(
                modelLogicalMainDto, false, modelPhysicsScriptDto, modelDesginFieldDtoList);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        Tuple2<ModelLogicalMain, JSONObject> bizData = verifyResult.getData();
        ModelLogicalMain modelLogicalMain = bizData.getFirst();
        modelLogicalMain = modelLogicalMainService.saveNewWithRelation(modelLogicalMain, bizData.getSecond());
        return ResponseResult.success(modelLogicalMain.getId());
    }

    /**
     * 修改数据规划-模型设计-模型逻辑表数据，及其关联的从表数据。
     *
     * @param modelLogicalMainDto 修改后的对象。
     * @param modelPhysicsScriptDto 一对一数据规划-模型设计-模型物理表从表Dto。
     * @param modelDesginFieldDtoList 一对多数据规划-模型设计-模型字段表从表列表。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "modelLogicalMainDto.id",
            "modelLogicalMainDto.searchString",
            "modelLogicalMainDto.updateTimeStart",
            "modelLogicalMainDto.updateTimeEnd",
            "modelLogicalMainDto.createTimeStart",
            "modelLogicalMainDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Long> update(
            @MyRequestBody ModelLogicalMainDto modelLogicalMainDto,
            @MyRequestBody ModelPhysicsScriptDto modelPhysicsScriptDto,
            @MyRequestBody List<ModelDesginFieldDto> modelDesginFieldDtoList) {
        String errorMessage;
        ResponseResult<Tuple2<ModelLogicalMain, JSONObject>> verifyResult = this.doBusinessDataVerifyAndConvert(
                modelLogicalMainDto, true, modelPhysicsScriptDto, modelDesginFieldDtoList);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        Tuple2<ModelLogicalMain, JSONObject> bizData = verifyResult.getData();
        ModelLogicalMain originalModelLogicalMain = bizData.getSecond().getObject("originalData", ModelLogicalMain.class);
        ModelLogicalMain modelLogicalMain = bizData.getFirst();
        if (!modelLogicalMainService.updateWithRelation(modelLogicalMain, originalModelLogicalMain, bizData.getSecond())) {
            errorMessage = "数据验证失败，[ModelLogicalMain] 数据不存在!";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success(modelLogicalMain.getId());
    }

    /**
     * 删除数据规划-模型设计-模型逻辑表数据。
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
     * 列出符合过滤条件的数据规划-模型设计-模型逻辑表列表。
     *
     * @param modelLogicalMainDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ModelLogicalMainVo>> list(
            @MyRequestBody ModelLogicalMainDto modelLogicalMainDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelLogicalMain modelLogicalMainFilter = MyModelUtil.copyTo(modelLogicalMainDtoFilter, ModelLogicalMain.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelLogicalMain.class);
        List<ModelLogicalMain> modelLogicalMainList =
                modelLogicalMainService.getModelLogicalMainListWithRelation(modelLogicalMainFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(modelLogicalMainList, ModelLogicalMain.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-模型设计-模型逻辑表列表。
     *
     * @param modelLogicalMainDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ModelLogicalMainVo>> listWithGroup(
            @MyRequestBody ModelLogicalMainDto modelLogicalMainDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ModelLogicalMain.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ModelLogicalMain.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ModelLogicalMain filter = MyModelUtil.copyTo(modelLogicalMainDtoFilter, ModelLogicalMain.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ModelLogicalMain> resultList = modelLogicalMainService.getGroupedModelLogicalMainListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ModelLogicalMain.INSTANCE));
    }

    /**
     * 查看指定数据规划-模型设计-模型逻辑表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ModelLogicalMainVo> view(@RequestParam Long id) {
        ModelLogicalMain modelLogicalMain = modelLogicalMainService.getByIdWithRelation(id, MyRelationParam.full());
        if (modelLogicalMain == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ModelLogicalMainVo modelLogicalMainVo = ModelLogicalMain.INSTANCE.fromModel(modelLogicalMain);
        return ResponseResult.success(modelLogicalMainVo);
    }

    private ResponseResult<Tuple2<ModelLogicalMain, JSONObject>> doBusinessDataVerifyAndConvert(
            ModelLogicalMainDto modelLogicalMainDto,
            boolean forUpdate,
            ModelPhysicsScriptDto modelPhysicsScriptDto,
            List<ModelDesginFieldDto> modelDesginFieldDtoList) {
        ErrorCodeEnum errorCode = ErrorCodeEnum.DATA_VALIDATED_FAILED;
        String errorMessage = MyCommonUtil.getModelValidationError(modelLogicalMainDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(errorCode, errorMessage);
        }
        errorMessage = MyCommonUtil.getModelValidationError(modelPhysicsScriptDto);
        if (errorMessage != null) {
            return ResponseResult.error(errorCode, "参数 [modelPhysicsScriptDto] " + errorMessage);
        }
        errorMessage = MyCommonUtil.getModelValidationError(modelDesginFieldDtoList);
        if (errorMessage != null) {
            return ResponseResult.error(errorCode, "参数 [modelDesginFieldDtoList] " + errorMessage);
        }
        // 全部关联从表数据的验证和转换
        JSONObject relationData = new JSONObject();
        CallResult verifyResult;
        // 下面是输入参数中，主表关联数据的验证。
        ModelLogicalMain modelLogicalMain = MyModelUtil.copyTo(modelLogicalMainDto, ModelLogicalMain.class);
        ModelLogicalMain originalData;
        if (forUpdate && modelLogicalMain != null) {
            originalData = modelLogicalMainService.getById(modelLogicalMain.getId());
            if (originalData == null) {
                return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
            }
            relationData.put("originalData", originalData);
        }
        // 处理主表的一对一关联 [ModelPhysicsScript]
        ModelPhysicsScript modelPhysicsScript = MyModelUtil.copyTo(modelPhysicsScriptDto, ModelPhysicsScript.class);
        relationData.put("modelPhysicsScript", modelPhysicsScript);
        // 处理主表的一对多关联 [ModelDesginField]
        List<ModelDesginField> modelDesginFieldList =
                MyModelUtil.copyCollectionTo(modelDesginFieldDtoList, ModelDesginField.class);
        verifyResult = modelDesginFieldService.verifyRelatedData(modelDesginFieldList);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        relationData.put("modelDesginFieldList", modelDesginFieldList);
        return ResponseResult.success(new Tuple2<>(modelLogicalMain, relationData));
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ModelLogicalMain originalModelLogicalMain = modelLogicalMainService.getById(id);
        if (originalModelLogicalMain == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!modelLogicalMainService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 根据ID生成建表SQL
     *
     * @param modelLogicalMainId 模型ID
     * @return 生成的SQL
     * @author 王立宏
     * @date 2023/10/29 11:24
     */
    @ApiOperation("根据ID生成建表SQL")
    @PostMapping("/buildSql")
    public ResponseResult<Map<String, String>> buildSql(@MyRequestBody Long modelLogicalMainId) {
        if (modelLogicalMainId == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        ModelLogicalMain modelLogicalMain = modelLogicalMainService.getById(modelLogicalMainId);
        if (modelLogicalMain == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success(modelLogicalMainService.buildSql(modelLogicalMain));
    }


    @ApiOperation("统计statisticsTableType数量")
    @PostMapping("/statisticsTableTypeNumber")
    public ResponseResult<Map<String, Object>> statisticsTableType(@MyRequestBody String projectId) {
        if (projectId == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        Map<String, Object> dataMap = modelLogicalMainService.statisticsTableType(projectId);

        return ResponseResult.success(dataMap);
    }

    @ApiOperation("统计houseLayerNameNumbere数量")
    @PostMapping("/houseLayerNameNumber")
    public ResponseResult<Map<String, BigDecimal>> houseLayerNameNumber(@MyRequestBody Long projectId) {
        if (projectId == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        // 判断该 projectId 是否存在
        if (projectMainService.getById(projectId) == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, "该项目ID不存在!");
        }
        return ResponseResult.success(modelLogicalMainService.houseLayerNameNumber(projectId));
    }
}
