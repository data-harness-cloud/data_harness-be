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
 * 数据开发-自助开发控制台操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据开发-自助开发控制台管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/devConsole")
public class DevConsoleController {

    @Autowired
    private DevConsoleService devConsoleService;

    /**
     * 新增数据开发-自助开发控制台数据。
     *
     * @param devConsoleDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devConsoleDto.id",
            "devConsoleDto.searchString",
            "devConsoleDto.updateTimeStart",
            "devConsoleDto.updateTimeEnd",
            "devConsoleDto.createTimeStart",
            "devConsoleDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DevConsoleDto devConsoleDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devConsoleDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevConsole devConsole = MyModelUtil.copyTo(devConsoleDto, DevConsole.class);
        devConsole = devConsoleService.saveNew(devConsole);
        return ResponseResult.success(devConsole.getId());
    }

    /**
     * 更新数据开发-自助开发控制台数据。
     *
     * @param devConsoleDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devConsoleDto.searchString",
            "devConsoleDto.updateTimeStart",
            "devConsoleDto.updateTimeEnd",
            "devConsoleDto.createTimeStart",
            "devConsoleDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DevConsoleDto devConsoleDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devConsoleDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevConsole devConsole = MyModelUtil.copyTo(devConsoleDto, DevConsole.class);
        DevConsole originalDevConsole = devConsoleService.getById(devConsole.getId());
        if (originalDevConsole == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!devConsoleService.update(devConsole, originalDevConsole)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据开发-自助开发控制台数据。
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
     * 列出符合过滤条件的数据开发-自助开发控制台列表。
     *
     * @param devConsoleDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DevConsoleVo>> list(
            @MyRequestBody DevConsoleDto devConsoleDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DevConsole devConsoleFilter = MyModelUtil.copyTo(devConsoleDtoFilter, DevConsole.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DevConsole.class);
        List<DevConsole> devConsoleList = devConsoleService.getDevConsoleListWithRelation(devConsoleFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(devConsoleList, DevConsole.INSTANCE));
    }

    /**
     * 查看指定数据开发-自助开发控制台对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DevConsoleVo> view(@RequestParam Long id) {
        DevConsole devConsole = devConsoleService.getByIdWithRelation(id, MyRelationParam.full());
        if (devConsole == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DevConsoleVo devConsoleVo = DevConsole.INSTANCE.fromModel(devConsole);
        return ResponseResult.success(devConsoleVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DevConsole originalDevConsole = devConsoleService.getById(id);
        if (originalDevConsole == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!devConsoleService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
