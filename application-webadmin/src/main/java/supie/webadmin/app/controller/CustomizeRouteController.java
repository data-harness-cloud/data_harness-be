package supie.webadmin.app.controller;

import io.swagger.annotations.ApiOperation;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.app.controller.dynamicRoutingAPI.MyDynamicController;
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
 * 自定义动态路由操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "自定义动态路由管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/customizeRoute")
public class CustomizeRouteController {

    @Autowired
    private CustomizeRouteService customizeRouteService;
    @Autowired
    private MyDynamicController myDynamicController;

    /**
     * 新增自定义动态路由数据。
     *
     * @param customizeRouteDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "customizeRouteDto.id",
            "customizeRouteDto.searchString",
            "customizeRouteDto.updateTimeStart",
            "customizeRouteDto.updateTimeEnd",
            "customizeRouteDto.createTimeStart",
            "customizeRouteDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody CustomizeRouteDto customizeRouteDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(customizeRouteDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        CustomizeRoute customizeRoute = MyModelUtil.copyTo(customizeRouteDto, CustomizeRoute.class);
        customizeRoute = customizeRouteService.saveNew(customizeRoute);
        return ResponseResult.success(customizeRoute.getId());
    }

    /**
     * 更新自定义动态路由数据。
     *
     * @param customizeRouteDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "customizeRouteDto.searchString",
            "customizeRouteDto.updateTimeStart",
            "customizeRouteDto.updateTimeEnd",
            "customizeRouteDto.createTimeStart",
            "customizeRouteDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody CustomizeRouteDto customizeRouteDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(customizeRouteDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        CustomizeRoute customizeRoute = MyModelUtil.copyTo(customizeRouteDto, CustomizeRoute.class);
        CustomizeRoute originalCustomizeRoute = customizeRouteService.getById(customizeRoute.getId());
        if (originalCustomizeRoute == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!customizeRouteService.update(customizeRoute, originalCustomizeRoute)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除自定义动态路由数据。
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
     * 列出符合过滤条件的自定义动态路由列表。
     *
     * @param customizeRouteDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<CustomizeRouteVo>> list(
            @MyRequestBody CustomizeRouteDto customizeRouteDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        CustomizeRoute customizeRouteFilter = MyModelUtil.copyTo(customizeRouteDtoFilter, CustomizeRoute.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, CustomizeRoute.class);
        List<CustomizeRoute> customizeRouteList =
                customizeRouteService.getCustomizeRouteListWithRelation(customizeRouteFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(customizeRouteList, CustomizeRoute.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的自定义动态路由列表。
     *
     * @param customizeRouteDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<CustomizeRouteVo>> listWithGroup(
            @MyRequestBody CustomizeRouteDto customizeRouteDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, CustomizeRoute.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, CustomizeRoute.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        CustomizeRoute filter = MyModelUtil.copyTo(customizeRouteDtoFilter, CustomizeRoute.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<CustomizeRoute> resultList = customizeRouteService.getGroupedCustomizeRouteListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, CustomizeRoute.INSTANCE));
    }

    /**
     * 查看指定自定义动态路由对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<CustomizeRouteVo> view(@RequestParam Long id) {
        CustomizeRoute customizeRoute = customizeRouteService.getByIdWithRelation(id, MyRelationParam.full());
        if (customizeRoute == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        CustomizeRouteVo customizeRouteVo = CustomizeRoute.INSTANCE.fromModel(customizeRoute);
        return ResponseResult.success(customizeRouteVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        CustomizeRoute originalCustomizeRoute = customizeRouteService.getById(id);
        if (originalCustomizeRoute == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!customizeRouteService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 上线API
     */
    @ApiOperation("上线API")
    @PostMapping("/registerApi")
    public ResponseResult<Void> registerApi(@MyRequestBody Long id) {
        if (id == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        CustomizeRoute originalCustomizeRoute = customizeRouteService.getById(id);
        if (originalCustomizeRoute == null) {
            // NOTE: 修改下面方括号中的话述
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST,
                    "数据验证失败，当前 [数据] 并不存在，请刷新后重试！");
        }
        // 数据存在，注册该路由
        customizeRouteService.registerApi(originalCustomizeRoute);
        return ResponseResult.success();
    }

    /**
     * 下线API
     */
    @ApiOperation("下线API")
    @PostMapping("/unregisterApi")
    public ResponseResult<Void> unregisterApi(@MyRequestBody Long id) {
        if (id == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        CustomizeRoute originalCustomizeRoute = customizeRouteService.getById(id);
        if (originalCustomizeRoute == null) {
            // NOTE: 修改下面方括号中的话述
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST,
                    "数据验证失败，当前 [数据] 并不存在，请刷新后重试！");
        }
        // 数据存在，删除该路由
        customizeRouteService.unregisterApi(originalCustomizeRoute);
        return ResponseResult.success();
    }

    /**
     * 测试动态API
     */
    @ApiOperation("测试动态API")
    @PostMapping("/testCustomizeRoute")
    public ResponseResult<Object> testCustomizeRoute(@MyRequestBody Long id, @MyRequestBody Map<String, Object> params) {
        if (id == null) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        CustomizeRoute originalCustomizeRoute = customizeRouteService.getById(id);
        if (originalCustomizeRoute == null) {
            // NOTE: 修改下面方括号中的话述
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST,
                    "数据验证失败，当前 [数据] 并不存在，请刷新后重试！");
        }
        try {
            return myDynamicController.performCustomizeRouteBusiness(params, originalCustomizeRoute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
