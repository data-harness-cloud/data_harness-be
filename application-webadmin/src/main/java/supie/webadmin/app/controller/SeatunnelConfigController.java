package supie.webadmin.app.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import supie.common.core.annotation.NoAuthInterface;
import supie.common.core.upload.BaseUpDownloader;
import supie.common.core.upload.UpDownloaderFactory;
import supie.common.core.upload.UploadStoreInfo;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.common.minio.util.MinioUpDownloader;
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
import supie.webadmin.config.ApplicationConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 项目中心-数据传输引擎配置操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "项目中心-数据传输引擎配置管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/seatunnelConfig")
public class SeatunnelConfigController {

    @Autowired
    private UpDownloaderFactory upDownloaderFactory;

    @Autowired
    private SeatunnelConfigService seatunnelConfigService;
    @Autowired
    private RemoteHostService remoteHostService;

    @Resource
    private MinioUpDownloader  minioUpDownloader;


    @Resource
    private ApplicationConfig appConfig;



    // 本地配置文件生成路径
    //  private final String folderPath ="E:\\gongsixm\\SDT\\application-webadmin\\src\\main\\java\\supie\\webadmin\\app\\controller\\";

    // 服务器配置文件生成路径
    private final String folderPath ="/app/";

    /**
     * 新增项目中心-数据传输引擎配置数据，及其关联的从表数据。
     *
     * @param seatunnelConfigDto 新增主表对象。
     * @param remoteHostDto 一对一项目中心-基础信息配置-远程主机从表Dto。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "seatunnelConfigDto.id",
            "seatunnelConfigDto.searchString",
            "seatunnelConfigDto.createTimeStart",
            "seatunnelConfigDto.createTimeEnd",
            "seatunnelConfigDto.updateTimeStart",
            "seatunnelConfigDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(
            @MyRequestBody SeatunnelConfigDto seatunnelConfigDto,
            @MyRequestBody RemoteHostDto remoteHostDto) {
        ResponseResult<Tuple2<SeatunnelConfig, JSONObject>> verifyResult =
                this.doBusinessDataVerifyAndConvert(seatunnelConfigDto, false, remoteHostDto);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        Tuple2<SeatunnelConfig, JSONObject> bizData = verifyResult.getData();
        SeatunnelConfig seatunnelConfig = bizData.getFirst();
        seatunnelConfig = seatunnelConfigService.saveNewWithRelation(seatunnelConfig, bizData.getSecond());
        return ResponseResult.success(seatunnelConfig.getId());
    }

    /**
     * 更新项目中心-数据传输引擎配置数据。
     *
     * @param seatunnelConfigDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "seatunnelConfigDto.searchString",
            "seatunnelConfigDto.createTimeStart",
            "seatunnelConfigDto.createTimeEnd",
            "seatunnelConfigDto.updateTimeStart",
            "seatunnelConfigDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody SeatunnelConfigDto seatunnelConfigDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(seatunnelConfigDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SeatunnelConfig seatunnelConfig = MyModelUtil.copyTo(seatunnelConfigDto, SeatunnelConfig.class);
        SeatunnelConfig originalSeatunnelConfig = seatunnelConfigService.getById(seatunnelConfig.getId());
        if (originalSeatunnelConfig == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = seatunnelConfigService.verifyRelatedData(seatunnelConfig, originalSeatunnelConfig);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!seatunnelConfigService.update(seatunnelConfig, originalSeatunnelConfig)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除项目中心-数据传输引擎配置数据。
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
     * 列出符合过滤条件的项目中心-数据传输引擎配置列表。
     *
     * @param seatunnelConfigDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<SeatunnelConfigVo>> list(
            @MyRequestBody SeatunnelConfigDto seatunnelConfigDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SeatunnelConfig seatunnelConfigFilter = MyModelUtil.copyTo(seatunnelConfigDtoFilter, SeatunnelConfig.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SeatunnelConfig.class);
        List<SeatunnelConfig> seatunnelConfigList =
                seatunnelConfigService.getSeatunnelConfigListWithRelation(seatunnelConfigFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(seatunnelConfigList, SeatunnelConfig.INSTANCE));
    }

    /**
     * 查看指定项目中心-数据传输引擎配置对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<SeatunnelConfigVo> view(@RequestParam Long id) {
        SeatunnelConfig seatunnelConfig = seatunnelConfigService.getByIdWithRelation(id, MyRelationParam.full());
        if (seatunnelConfig == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SeatunnelConfigVo seatunnelConfigVo = SeatunnelConfig.INSTANCE.fromModel(seatunnelConfig);
        return ResponseResult.success(seatunnelConfigVo);
    }

    private ResponseResult<Tuple2<SeatunnelConfig, JSONObject>> doBusinessDataVerifyAndConvert(
            SeatunnelConfigDto seatunnelConfigDto,
            boolean forUpdate,
            RemoteHostDto remoteHostDto) {
        ErrorCodeEnum errorCode = ErrorCodeEnum.DATA_VALIDATED_FAILED;
        String errorMessage = MyCommonUtil.getModelValidationError(seatunnelConfigDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(errorCode, errorMessage);
        }
        errorMessage = MyCommonUtil.getModelValidationError(remoteHostDto);
        if (errorMessage != null) {
            return ResponseResult.error(errorCode, "参数 [remoteHostDto] " + errorMessage);
        }
        // 全部关联从表数据的验证和转换
        JSONObject relationData = new JSONObject();
        CallResult verifyResult;
        // 下面是输入参数中，主表关联数据的验证。
        SeatunnelConfig seatunnelConfig = MyModelUtil.copyTo(seatunnelConfigDto, SeatunnelConfig.class);
        SeatunnelConfig originalData = null;
        if (forUpdate && seatunnelConfig != null) {
            originalData = seatunnelConfigService.getById(seatunnelConfig.getId());
            if (originalData == null) {
                return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
            }
            relationData.put("originalData", originalData);
        }
        verifyResult = seatunnelConfigService.verifyRelatedData(seatunnelConfig, originalData);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        // 处理主表的一对一关联 [RemoteHost]
        RemoteHost remoteHost = MyModelUtil.copyTo(remoteHostDto, RemoteHost.class);
        verifyResult = remoteHostService.verifyRelatedData(remoteHost);
        if (!verifyResult.isSuccess()) {
            return ResponseResult.errorFrom(verifyResult);
        }
        relationData.put("remoteHost", remoteHost);
        return ResponseResult.success(new Tuple2<>(seatunnelConfig, relationData));
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        SeatunnelConfig originalSeatunnelConfig = seatunnelConfigService.getById(id);
        if (originalSeatunnelConfig == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!seatunnelConfigService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }



    /**
     *   该接口还有很多地方不足，待后面进行优化
     * @param seatunnelEnvDto   seatunnelEnvDto
     * @param seatunnelSourceDto seatunnelSourceDto
     * @param seatunnelSinkDto  seatunnelSinkDto
     * @param seatunnelTransformDto  seatunnelTransformDto
     * @return
     */
    @PostMapping("/assemblyProfile")
    @ApiOperation("组装配置文件")
    @NoAuthInterface
    public ResponseResult<Object> assemblyProfile(
            @MyRequestBody SeatunnelEnvDto seatunnelEnvDto,
            @MyRequestBody SeatunnelSourceDto seatunnelSourceDto,
            @MyRequestBody SeatunnelSinkDto seatunnelSinkDto,
            @MyRequestBody SeatunnelTransformDto seatunnelTransformDto) throws IOException {

        UploadStoreInfo storeInfo = MyModelUtil.getUploadStoreInfo(BaseBusinessFile.class, "fileJson");
        if (!storeInfo.isSupportUpload()) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_UPLOAD_FIELD);

        }
        BaseUpDownloader upDownloader = upDownloaderFactory.get(storeInfo.getStoreType());

        String s = upDownloader.obtainTheConfigurationFileContent(appConfig.getUploadFileBaseDir(), BaseBusinessFile.class.getSimpleName(), "fileJson", "d3e69633eb5640f2811e479d04d3d67c.template",false);
//        String uploadPath = minioUpDownloader.makeFullPath(appConfig.getUploadFileBaseDir(), BaseBusinessFile.class.getSimpleName(), "fileJson", false);
//        String uploadPath = minioUpDownloader.makeFullPath(appConfig.getUploadFileBaseDir(), BaseBusinessFile.class.getSimpleName(), "fileJson", false);

//        String fullFileanme = s + "/" +"v2.batch.config.template";
        List<String> strings = FileUtil.readLines(s, "UTF-8");
//        String s = seatunnelConfigService.assemblyProfile(seatunnelEnvDto, seatunnelSourceDto, seatunnelSinkDto,fullFileanme);
        return ResponseResult.success(strings);

    }
}
