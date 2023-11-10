package supie.webadmin.upms.controller;

import com.alibaba.fastjson.TypeReference;
import cn.hutool.core.util.ReflectUtil;
import supie.common.core.upload.BaseUpDownloader;
import supie.common.core.upload.UpDownloaderFactory;
import supie.common.core.upload.UploadResponseInfo;
import supie.common.core.upload.UploadStoreInfo;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.upms.vo.*;
import supie.webadmin.upms.dto.*;
import supie.webadmin.upms.model.*;
import supie.webadmin.upms.service.*;
import supie.common.core.object.*;
import supie.common.core.util.*;
import supie.common.core.constant.*;
import supie.common.core.annotation.MyRequestBody;
import supie.common.redis.cache.SessionCacheHelper;
import supie.webadmin.config.ApplicationConfig;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 用户管理操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "用户管理管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/upms/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationConfig appConfig;
    @Autowired
    private SessionCacheHelper cacheHelper;
    @Autowired
    private UpDownloaderFactory upDownloaderFactory;

    /**
     * 新增用户操作。
     *
     * @param sysUserDto           新增用户对象。
     * @param deptPostIdListString 逗号分隔的部门岗位Id列表。
     * @param dataPermIdListString 逗号分隔的数据权限Id列表。
     * @param roleIdListString     逗号分隔的角色Id列表。
     * @return 应答结果对象，包含新增用户的主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "sysUserDto.userId",
            "sysUserDto.createTimeStart",
            "sysUserDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(
            @MyRequestBody SysUserDto sysUserDto,
            @MyRequestBody String deptPostIdListString,
            @MyRequestBody String dataPermIdListString,
            @MyRequestBody String roleIdListString) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysUserDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysUser sysUser = MyModelUtil.copyTo(sysUserDto, SysUser.class);
        CallResult result = sysUserService.verifyRelatedData(
                sysUser, null, roleIdListString, deptPostIdListString, dataPermIdListString);
        if (!result.isSuccess()) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> deptPostIdSet = result.getData().getObject("deptPostIdSet", new TypeReference<Set<Long>>() {});
        Set<Long> roleIdSet = result.getData().getObject("roleIdSet", new TypeReference<Set<Long>>() {});
        Set<Long> dataPermIdSet = result.getData().getObject("dataPermIdSet", new TypeReference<Set<Long>>() {});
        sysUserService.saveNew(sysUser, roleIdSet, deptPostIdSet, dataPermIdSet);
        return ResponseResult.success(sysUser.getUserId());
    }

    /**
     * 更新用户操作。
     *
     * @param sysUserDto           更新用户对象。
     * @param deptPostIdListString 逗号分隔的部门岗位Id列表。
     * @param dataPermIdListString 逗号分隔的数据权限Id列表。
     * @param roleIdListString     逗号分隔的角色Id列表。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "sysUserDto.createTimeStart",
            "sysUserDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(
            @MyRequestBody SysUserDto sysUserDto,
            @MyRequestBody String deptPostIdListString,
            @MyRequestBody String dataPermIdListString,
            @MyRequestBody String roleIdListString) {
        String errorMessage = MyCommonUtil.getModelValidationError(sysUserDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        SysUser originalUser = sysUserService.getById(sysUserDto.getUserId());
        if (originalUser == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SysUser sysUser = MyModelUtil.copyTo(sysUserDto, SysUser.class);
        CallResult result = sysUserService.verifyRelatedData(
                sysUser, originalUser, roleIdListString, deptPostIdListString, dataPermIdListString);
        if (!result.isSuccess()) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, result.getErrorMessage());
        }
        Set<Long> roleIdSet = result.getData().getObject("roleIdSet", new TypeReference<Set<Long>>() {});
        Set<Long> deptPostIdSet = result.getData().getObject("deptPostIdSet", new TypeReference<Set<Long>>() {});
        Set<Long> dataPermIdSet = result.getData().getObject("dataPermIdSet", new TypeReference<Set<Long>>() {});
        if (!sysUserService.update(sysUser, originalUser, roleIdSet, deptPostIdSet, dataPermIdSet)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 重置密码操作。
     *
     * @param userId 指定用户主键Id。
     * @return 应答结果对象。
     */
    @PostMapping("/resetPassword")
    public ResponseResult<Void> resetPassword(@MyRequestBody Long userId) {
        if (MyCommonUtil.existBlankArgument(userId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        if (!sysUserService.changePassword(userId, appConfig.getDefaultUserPassword())) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除用户管理数据。
     *
     * @param userId 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long userId) {
        if (MyCommonUtil.existBlankArgument(userId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(userId);
    }

    /**
     * 列出符合过滤条件的用户管理列表。
     *
     * @param sysUserDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<SysUserVo>> list(
            @MyRequestBody SysUserDto sysUserDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysUser sysUserFilter = MyModelUtil.copyTo(sysUserDtoFilter, SysUser.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysUser.class);
        List<SysUser> sysUserList = sysUserService.getSysUserListWithRelation(sysUserFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(sysUserList, SysUser.INSTANCE));
    }

    /**
     * 查看指定用户管理对象详情。
     *
     * @param userId 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<SysUserVo> view(@RequestParam Long userId) {
        // 这里查看用户数据时候，需要把用户多对多关联的角色和数据权限Id一并查出。
        SysUser sysUser = sysUserService.getByIdWithRelation(userId, MyRelationParam.full());
        if (sysUser == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        SysUserVo sysUserVo = SysUser.INSTANCE.fromModel(sysUser);
        return ResponseResult.success(sysUserVo);
    }

    /**
     * 附件文件下载。
     * 这里将图片和其他类型的附件文件放到不同的父目录下，主要为了便于今后图片文件的迁移。
     *
     * @param userId 附件所在记录的主键Id。
     * @param fieldName 附件所属的字段名。
     * @param filename  文件名。如果没有提供该参数，就从当前记录的指定字段中读取。
     * @param asImage   下载文件是否为图片。
     * @param response  Http 应答对象。
     */
    @OperationLog(type = SysOperationLogType.DOWNLOAD, saveResponse = false)
    @GetMapping("/download")
    public void download(
            @RequestParam(required = false) Long userId,
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
            if (userId == null) {
                if (!cacheHelper.existSessionUploadFile(filename)) {
                    ResponseResult.output(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } else {
                SysUser sysUser = sysUserService.getById(userId);
                if (sysUser == null) {
                    ResponseResult.output(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                String fieldJsonData = (String) ReflectUtil.getFieldValue(sysUser, fieldName);
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
            UploadStoreInfo storeInfo = MyModelUtil.getUploadStoreInfo(SysUser.class, fieldName);
            if (!storeInfo.isSupportUpload()) {
                ResponseResult.output(HttpServletResponse.SC_NOT_IMPLEMENTED,
                        ResponseResult.error(ErrorCodeEnum.INVALID_UPLOAD_FIELD));
                return;
            }
            BaseUpDownloader upDownloader = upDownloaderFactory.get(storeInfo.getStoreType());
            upDownloader.doDownload(appConfig.getUploadFileBaseDir(),
                    SysUser.class.getSimpleName(), fieldName, filename, asImage, response);
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
        UploadStoreInfo storeInfo = MyModelUtil.getUploadStoreInfo(SysUser.class, fieldName);
        // 这里就会判断参数中指定的字段，是否支持上传操作。
        if (!storeInfo.isSupportUpload()) {
            ResponseResult.output(HttpServletResponse.SC_FORBIDDEN,
                    ResponseResult.error(ErrorCodeEnum.INVALID_UPLOAD_FIELD));
            return;
        }
        // 根据字段注解中的存储类型，通过工厂方法获取匹配的上传下载实现类，从而解耦。
        BaseUpDownloader upDownloader = upDownloaderFactory.get(storeInfo.getStoreType());
        UploadResponseInfo responseInfo = upDownloader.doUpload(null,
                appConfig.getUploadFileBaseDir(), SysUser.class.getSimpleName(), fieldName, asImage, uploadFile);
        if (Boolean.TRUE.equals(responseInfo.getUploadFailed())) {
            ResponseResult.output(HttpServletResponse.SC_FORBIDDEN,
                    ResponseResult.error(ErrorCodeEnum.UPLOAD_FAILED, responseInfo.getErrorMessage()));
            return;
        }
        cacheHelper.putSessionUploadFile(responseInfo.getFilename());
        ResponseResult.output(ResponseResult.success(responseInfo));
    }

    /**
     * 以字典形式返回全部用户管理数据集合。字典的键值为[userId, loginName]。
     * 白名单接口，登录用户均可访问。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含的数据为 List<Map<String, String>>，map中包含两条记录，key的值分别是id和name，value对应具体数据。
     */
    @GetMapping("/listDict")
    public ResponseResult<List<Map<String, Object>>> listDict(SysUser filter) {
        List<SysUser> resultList = sysUserService.getListByFilter(filter);
        return ResponseResult.success(
                MyCommonUtil.toDictDataList(resultList, SysUser::getUserId, SysUser::getLoginName));
    }

    /**
     * 根据字典Id集合，获取查询后的字典数据。
     *
     * @param dictIds 字典Id集合。
     * @return 应答结果对象，包含字典形式的数据集合。
     */
    @PostMapping("/listDictByIds")
    public ResponseResult<List<Map<String, Object>>> listDictByIds(@MyRequestBody List<Long> dictIds) {
        List<SysUser> resultList = sysUserService.getInList(new HashSet<>(dictIds));
        return ResponseResult.success(
                MyCommonUtil.toDictDataList(resultList, SysUser::getUserId, SysUser::getLoginName));
    }

    /**
     * 查询用户的权限资源地址列表。同时返回详细的分配路径。
     *
     * @param userId 用户Id。
     * @param url    url过滤条件。
     * @return 应答对象，包含从用户到权限资源的完整权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysPermWithDetail")
    public ResponseResult<List<Map<String, Object>>> listSysPermWithDetail(Long userId, String url) {
        if (MyCommonUtil.isBlankOrNull(userId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return ResponseResult.success(sysUserService.getSysPermListWithDetail(userId, url));
    }

    /**
     * 查询用户的权限字列表。同时返回详细的分配路径。
     *
     * @param userId   用户Id。
     * @param permCode 权限字名称过滤条件。
     * @return 应答对象，包含从用户到权限字的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysPermCodeWithDetail")
    public ResponseResult<List<Map<String, Object>>> listSysPermCodeWithDetail(Long userId, String permCode) {
        if (MyCommonUtil.isBlankOrNull(userId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return ResponseResult.success(sysUserService.getSysPermCodeListWithDetail(userId, permCode));
    }

    /**
     * 查询用户的菜单列表。同时返回详细的分配路径。
     *
     * @param userId   用户Id。
     * @param menuName 菜单名称过滤条件。
     * @return 应答对象，包含从用户到菜单的权限分配路径信息的查询结果列表。
     */
    @GetMapping("/listSysMenuWithDetail")
    public ResponseResult<List<Map<String, Object>>> listSysMenuWithDetail(Long userId, String menuName) {
        if (MyCommonUtil.isBlankOrNull(userId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return ResponseResult.success(sysUserService.getSysMenuListWithDetail(userId, menuName));
    }

    private ResponseResult<Void> doDelete(Long userId) {
        String errorMessage;
        // 验证关联Id的数据合法性
        SysUser originalSysUser = sysUserService.getById(userId);
        if (originalSysUser == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!sysUserService.remove(userId)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
