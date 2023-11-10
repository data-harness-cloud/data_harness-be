package supie.webadmin.app.controller;

import cn.hutool.core.util.ReflectUtil;
import supie.common.core.upload.BaseUpDownloader;
import supie.common.core.upload.UpDownloaderFactory;
import supie.common.core.upload.UploadResponseInfo;
import supie.common.core.upload.UploadStoreInfo;
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
import supie.common.redis.cache.SessionCacheHelper;
import supie.webadmin.config.ApplicationConfig;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 数据开发-liteflow-日志表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据开发-liteflow-日志表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/devLiteflowLog")
public class DevLiteflowLogController {

    @Autowired
    private DevLiteflowLogService devLiteflowLogService;
    @Autowired
    private ApplicationConfig appConfig;
    @Autowired
    private SessionCacheHelper cacheHelper;
    @Autowired
    private UpDownloaderFactory upDownloaderFactory;

    /**
     * 新增数据开发-liteflow-日志表数据。
     *
     * @param devLiteflowLogDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devLiteflowLogDto.id",
            "devLiteflowLogDto.searchString",
            "devLiteflowLogDto.runTimeStart",
            "devLiteflowLogDto.runTimeEnd",
            "devLiteflowLogDto.logFileSizeStart",
            "devLiteflowLogDto.logFileSizeEnd",
            "devLiteflowLogDto.createTimeStart",
            "devLiteflowLogDto.createTimeEnd",
            "devLiteflowLogDto.updateTimeStart",
            "devLiteflowLogDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody DevLiteflowLogDto devLiteflowLogDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devLiteflowLogDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevLiteflowLog devLiteflowLog = MyModelUtil.copyTo(devLiteflowLogDto, DevLiteflowLog.class);
        // 验证关联Id的数据合法性
        CallResult callResult = devLiteflowLogService.verifyRelatedData(devLiteflowLog, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        devLiteflowLog = devLiteflowLogService.saveNew(devLiteflowLog);
        return ResponseResult.success(devLiteflowLog.getId());
    }

    /**
     * 更新数据开发-liteflow-日志表数据。
     *
     * @param devLiteflowLogDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "devLiteflowLogDto.searchString",
            "devLiteflowLogDto.runTimeStart",
            "devLiteflowLogDto.runTimeEnd",
            "devLiteflowLogDto.logFileSizeStart",
            "devLiteflowLogDto.logFileSizeEnd",
            "devLiteflowLogDto.createTimeStart",
            "devLiteflowLogDto.createTimeEnd",
            "devLiteflowLogDto.updateTimeStart",
            "devLiteflowLogDto.updateTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody DevLiteflowLogDto devLiteflowLogDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(devLiteflowLogDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        DevLiteflowLog devLiteflowLog = MyModelUtil.copyTo(devLiteflowLogDto, DevLiteflowLog.class);
        DevLiteflowLog originalDevLiteflowLog = devLiteflowLogService.getById(devLiteflowLog.getId());
        if (originalDevLiteflowLog == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = devLiteflowLogService.verifyRelatedData(devLiteflowLog, originalDevLiteflowLog);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!devLiteflowLogService.update(devLiteflowLog, originalDevLiteflowLog)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据开发-liteflow-日志表数据。
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
     * 列出符合过滤条件的数据开发-liteflow-日志表列表。
     *
     * @param devLiteflowLogDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<DevLiteflowLogVo>> list(
            @MyRequestBody DevLiteflowLogDto devLiteflowLogDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        DevLiteflowLog devLiteflowLogFilter = MyModelUtil.copyTo(devLiteflowLogDtoFilter, DevLiteflowLog.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, DevLiteflowLog.class);
        List<DevLiteflowLog> devLiteflowLogList =
                devLiteflowLogService.getDevLiteflowLogListWithRelation(devLiteflowLogFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(devLiteflowLogList, DevLiteflowLog.INSTANCE));
    }

    /**
     * 查看指定数据开发-liteflow-日志表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<DevLiteflowLogVo> view(@RequestParam Long id) {
        DevLiteflowLog devLiteflowLog = devLiteflowLogService.getByIdWithRelation(id, MyRelationParam.full());
        if (devLiteflowLog == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        DevLiteflowLogVo devLiteflowLogVo = DevLiteflowLog.INSTANCE.fromModel(devLiteflowLog);
        return ResponseResult.success(devLiteflowLogVo);
    }

    /**
     * 附件文件下载。
     * 这里将图片和其他类型的附件文件放到不同的父目录下，主要为了便于今后图片文件的迁移。
     *
     * @param id 附件所在记录的主键Id。
     * @param fieldName 附件所属的字段名。
     * @param filename  文件名。如果没有提供该参数，就从当前记录的指定字段中读取。
     * @param asImage   下载文件是否为图片。
     * @param response  Http 应答对象。
     */
    @OperationLog(type = SysOperationLogType.DOWNLOAD, saveResponse = false)
    @GetMapping("/download")
    public void download(
            @RequestParam(required = false) Long id,
            @RequestParam String fieldName,
            @RequestParam String filename,
            @RequestParam Boolean asImage,
            HttpServletResponse response) {
        if (MyCommonUtil.existBlankArgument(fieldName, filename, asImage)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // 使用try来捕获异常，是为了保证一旦出现异常可以返回500的错误状态，便于调试。
        // 否则有可能给前端返回的是200的错误码。
        try {
            // 如果请求参数中没有包含主键Id，就判断该文件是否为当前session上传的。
            if (id == null) {
                if (!cacheHelper.existSessionUploadFile(filename)) {
                    ResponseResult.output(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } else {
                DevLiteflowLog devLiteflowLog = devLiteflowLogService.getById(id);
                if (devLiteflowLog == null) {
                    ResponseResult.output(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                String fieldJsonData = (String) ReflectUtil.getFieldValue(devLiteflowLog, fieldName);
                if (fieldJsonData == null && !cacheHelper.existSessionUploadFile(filename)) {
                    ResponseResult.output(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                if (!BaseUpDownloader.containFile(fieldJsonData, filename)
                        && !cacheHelper.existSessionUploadFile(filename)) {
                    ResponseResult.output(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
            UploadStoreInfo storeInfo = MyModelUtil.getUploadStoreInfo(DevLiteflowLog.class, fieldName);
            if (!storeInfo.isSupportUpload()) {
                ResponseResult.output(HttpServletResponse.SC_NOT_IMPLEMENTED,
                        ResponseResult.error(ErrorCodeEnum.INVALID_UPLOAD_FIELD));
                return;
            }
            BaseUpDownloader upDownloader = upDownloaderFactory.get(storeInfo.getStoreType());
            upDownloader.doDownload(appConfig.getUploadFileBaseDir(),
                    DevLiteflowLog.class.getSimpleName(), fieldName, filename, asImage, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 文件上传操作。
     *
     * @param fieldName  上传文件名。
     * @param asImage    是否作为图片上传。如果是图片，今后下载的时候无需权限验证。否则就是附件上传，下载时需要权限验证。
     * @param uploadFile 上传文件对象。
     */
    @OperationLog(type = SysOperationLogType.UPLOAD, saveResponse = false)
    @PostMapping("/upload")
    public void upload(
            @RequestParam String fieldName,
            @RequestParam Boolean asImage,
            @RequestParam("uploadFile") MultipartFile uploadFile) throws IOException {
        UploadStoreInfo storeInfo = MyModelUtil.getUploadStoreInfo(DevLiteflowLog.class, fieldName);
        // 这里就会判断参数中指定的字段，是否支持上传操作。
        if (!storeInfo.isSupportUpload()) {
            ResponseResult.output(HttpServletResponse.SC_FORBIDDEN,
                    ResponseResult.error(ErrorCodeEnum.INVALID_UPLOAD_FIELD));
            return;
        }
        // 根据字段注解中的存储类型，通过工厂方法获取匹配的上传下载实现类，从而解耦。
        BaseUpDownloader upDownloader = upDownloaderFactory.get(storeInfo.getStoreType());
        UploadResponseInfo responseInfo = upDownloader.doUpload(null,
                appConfig.getUploadFileBaseDir(), DevLiteflowLog.class.getSimpleName(), fieldName, asImage, uploadFile);
        if (Boolean.TRUE.equals(responseInfo.getUploadFailed())) {
            ResponseResult.output(HttpServletResponse.SC_FORBIDDEN,
                    ResponseResult.error(ErrorCodeEnum.UPLOAD_FAILED, responseInfo.getErrorMessage()));
            return;
        }
        cacheHelper.putSessionUploadFile(responseInfo.getFilename());
        ResponseResult.output(ResponseResult.success(responseInfo));
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        DevLiteflowLog originalDevLiteflowLog = devLiteflowLogService.getById(id);
        if (originalDevLiteflowLog == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!devLiteflowLogService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
