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
 * 外部APP与动态路由关联表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据服务-外部APP与动态路由关联表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/externalAppCustomizeRoute")
public class ExternalAppCustomizeRouteController {

    @Autowired
    private ExternalAppCustomizeRouteService externalAppCustomizeRouteService;

    /**
     * 新增外部APP与动态路由关联表数据。
     *
     * @param externalAppCustomizeRouteDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "externalAppCustomizeRouteDto.id",
            "externalAppCustomizeRouteDto.updateTimeStart",
            "externalAppCustomizeRouteDto.updateTimeEnd",
            "externalAppCustomizeRouteDto.createTimeStart",
            "externalAppCustomizeRouteDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ExternalAppCustomizeRouteDto externalAppCustomizeRouteDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(externalAppCustomizeRouteDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ExternalAppCustomizeRoute externalAppCustomizeRoute = MyModelUtil.copyTo(externalAppCustomizeRouteDto, ExternalAppCustomizeRoute.class);
        externalAppCustomizeRoute = externalAppCustomizeRouteService.saveNew(externalAppCustomizeRoute);
        return ResponseResult.success(externalAppCustomizeRoute.getId());
    }

    /**
     * 更新外部APP与动态路由关联表数据。
     *
     * @param externalAppCustomizeRouteDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "externalAppCustomizeRouteDto.updateTimeStart",
            "externalAppCustomizeRouteDto.updateTimeEnd",
            "externalAppCustomizeRouteDto.createTimeStart",
            "externalAppCustomizeRouteDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ExternalAppCustomizeRouteDto externalAppCustomizeRouteDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(externalAppCustomizeRouteDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ExternalAppCustomizeRoute externalAppCustomizeRoute = MyModelUtil.copyTo(externalAppCustomizeRouteDto, ExternalAppCustomizeRoute.class);
        ExternalAppCustomizeRoute originalExternalAppCustomizeRoute = externalAppCustomizeRouteService.getById(externalAppCustomizeRoute.getId());
        if (originalExternalAppCustomizeRoute == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!externalAppCustomizeRouteService.update(externalAppCustomizeRoute, originalExternalAppCustomizeRoute)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除外部APP与动态路由关联表数据。
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
     * 列出符合过滤条件的外部APP与动态路由关联表列表。
     *
     * @param externalAppCustomizeRouteDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ExternalAppCustomizeRouteVo>> list(
            @MyRequestBody ExternalAppCustomizeRouteDto externalAppCustomizeRouteDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ExternalAppCustomizeRoute externalAppCustomizeRouteFilter = MyModelUtil.copyTo(externalAppCustomizeRouteDtoFilter, ExternalAppCustomizeRoute.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ExternalAppCustomizeRoute.class);
        List<ExternalAppCustomizeRoute> externalAppCustomizeRouteList =
                externalAppCustomizeRouteService.getExternalAppCustomizeRouteListWithRelation(externalAppCustomizeRouteFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(externalAppCustomizeRouteList, ExternalAppCustomizeRoute.INSTANCE));
    }

    /**
     * 查看指定外部APP与动态路由关联表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ExternalAppCustomizeRouteVo> view(@RequestParam Long id) {
        ExternalAppCustomizeRoute externalAppCustomizeRoute = externalAppCustomizeRouteService.getByIdWithRelation(id, MyRelationParam.full());
        if (externalAppCustomizeRoute == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ExternalAppCustomizeRouteVo externalAppCustomizeRouteVo = ExternalAppCustomizeRoute.INSTANCE.fromModel(externalAppCustomizeRoute);
        return ResponseResult.success(externalAppCustomizeRouteVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ExternalAppCustomizeRoute originalExternalAppCustomizeRoute = externalAppCustomizeRouteService.getById(id);
        if (originalExternalAppCustomizeRoute == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!externalAppCustomizeRouteService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
