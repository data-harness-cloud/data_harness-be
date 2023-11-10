package supie.webadmin.app.controller;

import io.swagger.annotations.ApiOperation;
import supie.common.core.annotation.NoAuthInterface;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.app.util.remoteshell.JschUtil;
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
 * 项目中心-基础信息配置-远程主机操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "项目中心-基础信息配置-远程主机管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/remoteHost")
public class RemoteHostController {

    @Autowired
    private RemoteHostService remoteHostService;

    /**
     * 新增项目中心-基础信息配置-远程主机数据。
     *
     * @param remoteHostDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "remoteHostDto.id",
            "remoteHostDto.searchString",
            "remoteHostDto.createTimeStart",
            "remoteHostDto.createTimeEnd",
            "remoteHostDto.updateTimeStart",
            "remoteHostDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody RemoteHostDto remoteHostDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(remoteHostDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        RemoteHost remoteHost = MyModelUtil.copyTo(remoteHostDto, RemoteHost.class);
        // 验证关联Id的数据合法性
        CallResult callResult = remoteHostService.verifyRelatedData(remoteHost, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        remoteHost = remoteHostService.saveNew(remoteHost);
        return ResponseResult.success(remoteHost.getId());
    }

    /**
     * 更新项目中心-基础信息配置-远程主机数据。
     *
     * @param remoteHostDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "remoteHostDto.searchString",
            "remoteHostDto.createTimeStart",
            "remoteHostDto.createTimeEnd",
            "remoteHostDto.updateTimeStart",
            "remoteHostDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody RemoteHostDto remoteHostDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(remoteHostDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        RemoteHost remoteHost = MyModelUtil.copyTo(remoteHostDto, RemoteHost.class);
        RemoteHost originalRemoteHost = remoteHostService.getById(remoteHost.getId());
        if (originalRemoteHost == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = remoteHostService.verifyRelatedData(remoteHost, originalRemoteHost);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!remoteHostService.update(remoteHost, originalRemoteHost)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除项目中心-基础信息配置-远程主机数据。
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
     * 列出符合过滤条件的项目中心-基础信息配置-远程主机列表。
     *
     * @param remoteHostDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<RemoteHostVo>> list(
            @MyRequestBody RemoteHostDto remoteHostDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        RemoteHost remoteHostFilter = MyModelUtil.copyTo(remoteHostDtoFilter, RemoteHost.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, RemoteHost.class);
        List<RemoteHost> remoteHostList = remoteHostService.getRemoteHostListWithRelation(remoteHostFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(remoteHostList, RemoteHost.INSTANCE));
    }

    /**
     * 查看指定项目中心-基础信息配置-远程主机对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<RemoteHostVo> view(@RequestParam Long id) {
        RemoteHost remoteHost = remoteHostService.getByIdWithRelation(id, MyRelationParam.full());
        if (remoteHost == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        RemoteHostVo remoteHostVo = RemoteHost.INSTANCE.fromModel(remoteHost);
        return ResponseResult.success(remoteHostVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        RemoteHost originalRemoteHost = remoteHostService.getById(id);
        if (originalRemoteHost == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!remoteHostService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }


    @PostMapping("/testConnection")
    @ApiOperation("测试shh连接测试")
    public ResponseResult<String> testConnection(@MyRequestBody RemoteHostDto remoteHostDtoFilter,@MyRequestBody String commands) {

        RemoteHost remoteHostFilter = MyModelUtil.copyTo(remoteHostDtoFilter, RemoteHost.class);

        String data= remoteHostService.testConnection("",remoteHostFilter,commands);
        if (data.isEmpty()){
            return ResponseResult.error("500","请联系管理员");
        }
        return ResponseResult.success("成功");
    }
}
