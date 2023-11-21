package supie.webadmin.app.controller;

import io.swagger.annotations.ApiOperation;
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
 * 外部App表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据服务-外部App表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/externalApp")
public class ExternalAppController {

    @Autowired
    private ExternalAppService externalAppService;
    @Autowired
    private CustomizeRouteService customizeRouteService;

    /**
     * 新增外部App表数据。
     *
     * @param externalAppDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "externalAppDto.id",
            "externalAppDto.searchString",
            "externalAppDto.updateTimeStart",
            "externalAppDto.updateTimeEnd",
            "externalAppDto.createTimeStart",
            "externalAppDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ExternalAppDto externalAppDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(externalAppDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ExternalApp externalApp = MyModelUtil.copyTo(externalAppDto, ExternalApp.class);
        externalApp = externalAppService.saveNew(externalApp);
        return ResponseResult.success(externalApp.getId());
    }

    /**
     * 更新外部App表数据。
     *
     * @param externalAppDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "externalAppDto.searchString",
            "externalAppDto.updateTimeStart",
            "externalAppDto.updateTimeEnd",
            "externalAppDto.createTimeStart",
            "externalAppDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ExternalAppDto externalAppDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(externalAppDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ExternalApp externalApp = MyModelUtil.copyTo(externalAppDto, ExternalApp.class);
        ExternalApp originalExternalApp = externalAppService.getById(externalApp.getId());
        if (originalExternalApp == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!externalAppService.update(externalApp, originalExternalApp)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除外部App表数据。
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
     * 列出符合过滤条件的外部App表列表。
     *
     * @param externalAppDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ExternalAppVo>> list(
            @MyRequestBody ExternalAppDto externalAppDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ExternalApp externalAppFilter = MyModelUtil.copyTo(externalAppDtoFilter, ExternalApp.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ExternalApp.class);
        List<ExternalApp> externalAppList =
                externalAppService.getExternalAppListWithRelation(externalAppFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(externalAppList, ExternalApp.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的外部App表列表。
     *
     * @param externalAppDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ExternalAppVo>> listWithGroup(
            @MyRequestBody ExternalAppDto externalAppDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ExternalApp.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ExternalApp.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ExternalApp externalAppFilter = MyModelUtil.copyTo(externalAppDtoFilter, ExternalApp.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ExternalApp> resultList = externalAppService.getGroupedExternalAppListWithRelation(
                externalAppFilter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ExternalApp.INSTANCE));
    }

    /**
     * 查看指定外部App表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ExternalAppVo> view(@RequestParam Long id) {
        ExternalApp externalApp = externalAppService.getByIdWithRelation(id, MyRelationParam.full());
        if (externalApp == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ExternalAppVo externalAppVo = ExternalApp.INSTANCE.fromModel(externalApp);
        return ResponseResult.success(externalAppVo);
    }

    /**
     * 列出不与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。通常用于查看添加新 [自定义动态路由] 对象的候选列表。
     *
     * @param externalAppId 主表关联字段。
     * @param customizeRouteDtoFilter [自定义动态路由] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @ApiOperation(value = "列出不与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。通常用于查看添加新 [自定义动态路由] 对象的候选列表。")
    @PostMapping("/listNotInExternalAppCustomizeRoute")
    public ResponseResult<MyPageData<CustomizeRouteVo>> listNotInExternalAppCustomizeRoute(
            @MyRequestBody Long externalAppId,
            @MyRequestBody CustomizeRouteDto customizeRouteDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (MyCommonUtil.isNotBlankOrNull(externalAppId) && !externalAppService.existId(externalAppId)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        CustomizeRoute customizeRouteFilter = MyModelUtil.copyTo(customizeRouteDtoFilter, CustomizeRoute.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, CustomizeRoute.class);
        List<CustomizeRoute> customizeRouteList =
                customizeRouteService.getNotInCustomizeRouteListByExternalAppId(externalAppId, customizeRouteFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(customizeRouteList, CustomizeRoute.INSTANCE));
    }

    /**
     * 列出与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。
     *
     * @param externalAppId 主表关联字段。
     * @param customizeRouteDtoFilter [自定义动态路由] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @ApiOperation("列出与指定外部App表存在多对多关系的 [自定义动态路由] 列表数据。")
    @PostMapping("/listExternalAppCustomizeRoute")
    public ResponseResult<MyPageData<CustomizeRouteVo>> listExternalAppCustomizeRoute(
            @MyRequestBody(required = true) Long externalAppId,
            @MyRequestBody CustomizeRouteDto customizeRouteDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (!externalAppService.existId(externalAppId)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        CustomizeRoute customizeRouteFilter = MyModelUtil.copyTo(customizeRouteDtoFilter, CustomizeRoute.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, CustomizeRoute.class);
        List<CustomizeRoute> customizeRouteList =
                customizeRouteService.getCustomizeRouteListByExternalAppId(externalAppId, customizeRouteFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(customizeRouteList, CustomizeRoute.INSTANCE));
    }

    /**
     * 批量添加外部App表和 [自定义动态路由] 对象的多对多关联关系数据。
     *
     * @param externalAppId 主表主键Id。
     * @param externalAppCustomizeRouteDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.ADD_M2M)
    @ApiOperation("批量添加外部App表和 [自定义动态路由] 对象的多对多关联关系数据。")
    @PostMapping("/addExternalAppCustomizeRoute")
    public ResponseResult<Void> addExternalAppCustomizeRoute(
            @MyRequestBody Long externalAppId,
            @MyRequestBody List<ExternalAppCustomizeRouteDto> externalAppCustomizeRouteDtoList) {
        if (MyCommonUtil.existBlankArgument(externalAppId, externalAppCustomizeRouteDtoList)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        Set<Long> customizeRouteIdSet =
                externalAppCustomizeRouteDtoList.stream().map(ExternalAppCustomizeRouteDto::getCustomizeRouteId).collect(Collectors.toSet());
        if (!externalAppService.existId(externalAppId)
                || !customizeRouteService.existUniqueKeyList("id", customizeRouteIdSet)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        List<ExternalAppCustomizeRoute> externalAppCustomizeRouteList =
                MyModelUtil.copyCollectionTo(externalAppCustomizeRouteDtoList, ExternalAppCustomizeRoute.class);
        externalAppService.addExternalAppCustomizeRouteList(externalAppCustomizeRouteList, externalAppId);
        return ResponseResult.success();
    }

    /**
     * 更新指定外部App表和指定 [自定义动态路由] 的多对多关联数据。
     *
     * @param externalAppCustomizeRouteDto 对多对中间表对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @ApiOperation("更新指定外部App表和指定 [自定义动态路由] 的多对多关联数据。")
    @PostMapping("/updateExternalAppCustomizeRoute")
    public ResponseResult<Void> updateExternalAppCustomizeRoute(
            @MyRequestBody ExternalAppCustomizeRouteDto externalAppCustomizeRouteDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(externalAppCustomizeRouteDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ExternalAppCustomizeRoute externalAppCustomizeRoute = MyModelUtil.copyTo(externalAppCustomizeRouteDto, ExternalAppCustomizeRoute.class);
        if (!externalAppService.updateExternalAppCustomizeRoute(externalAppCustomizeRoute)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 显示外部App表和指定 [自定义动态路由] 的多对多关联详情数据。
     *
     * @param externalAppId 主表主键Id。
     * @param customizeRouteId 从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @ApiOperation("显示外部App表和指定 [自定义动态路由] 的多对多关联详情数据。")
    @GetMapping("/viewExternalAppCustomizeRoute")
    public ResponseResult<ExternalAppCustomizeRouteVo> viewExternalAppCustomizeRoute(
            @RequestParam Long externalAppId, @RequestParam Long customizeRouteId) {
        ExternalAppCustomizeRoute externalAppCustomizeRoute = externalAppService.getExternalAppCustomizeRoute(externalAppId, customizeRouteId);
        if (externalAppCustomizeRoute == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ExternalAppCustomizeRouteVo externalAppCustomizeRouteVo = MyModelUtil.copyTo(externalAppCustomizeRoute, ExternalAppCustomizeRouteVo.class);
        return ResponseResult.success(externalAppCustomizeRouteVo);
    }

    /**
     * 移除指定外部App表和指定 [自定义动态路由] 的多对多关联关系。
     *
     * @param externalAppId 主表主键Id。
     * @param customizeRouteId 从表主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE_M2M)
    @ApiOperation("移除指定外部App表和指定 [自定义动态路由] 的多对多关联关系。")
    @PostMapping("/deleteExternalAppCustomizeRoute")
    public ResponseResult<Void> deleteExternalAppCustomizeRoute(
            @MyRequestBody Long externalAppId, @MyRequestBody Long customizeRouteId) {
        if (MyCommonUtil.existBlankArgument(externalAppId, customizeRouteId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        if (!externalAppService.removeExternalAppCustomizeRoute(externalAppId, customizeRouteId)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ExternalApp originalExternalApp = externalAppService.getById(id);
        if (originalExternalApp == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!externalAppService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 生成 AppKey
     */
    @ApiOperation("生成AppKey")
    @GetMapping("/generateAppKey")
    public ResponseResult<ExternalApp> generateAppKey(@RequestParam Long externalAppId) {
        ExternalApp externalApp = externalAppService.getByIdWithRelation(externalAppId, MyRelationParam.full());
        if (externalApp == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        externalApp = externalAppService.generateAppKey(externalApp);
        if (externalApp == null) return ResponseResult.error(ErrorCodeEnum.NO_ERROR, "生成失败，请重试！");
        return ResponseResult.success(externalApp);
    }

}
