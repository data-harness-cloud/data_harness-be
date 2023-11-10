package supie.webadmin.app.controller;

import cn.hutool.core.util.ObjectUtil;
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
 * 数据规划-数据标准-目录表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据规划-数据标准-目录表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/standardDirectory")
public class StandardDirectoryController {

    @Autowired
    private StandardDirectoryService standardDirectoryService;

    /**
     * 新增数据规划-数据标准-目录表数据。
     *
     * @param standardDirectoryDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "standardDirectoryDto.id",
            "standardDirectoryDto.searchString",
            "standardDirectoryDto.updateTimeStart",
            "standardDirectoryDto.updateTimeEnd",
            "standardDirectoryDto.createTimeStart",
            "standardDirectoryDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody StandardDirectoryDto standardDirectoryDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(standardDirectoryDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        StandardDirectory standardDirectory = MyModelUtil.copyTo(standardDirectoryDto, StandardDirectory.class);
        // 验证父Id的数据合法性
        StandardDirectory parentStandardDirectory;
        if (MyCommonUtil.isNotBlankOrNull(standardDirectory.getDirectoryParentId())) {
            parentStandardDirectory = standardDirectoryService.getById(standardDirectory.getDirectoryParentId());
            if (parentStandardDirectory == null) {
                errorMessage = "数据验证失败，关联的父节点并不存在，请刷新后重试！";
                return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        standardDirectory = standardDirectoryService.saveNew(standardDirectory);
        return ResponseResult.success(standardDirectory.getId());
    }

    /**
     * 更新数据规划-数据标准-目录表数据。
     *
     * @param standardDirectoryDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "standardDirectoryDto.searchString",
            "standardDirectoryDto.updateTimeStart",
            "standardDirectoryDto.updateTimeEnd",
            "standardDirectoryDto.createTimeStart",
            "standardDirectoryDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody StandardDirectoryDto standardDirectoryDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(standardDirectoryDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        StandardDirectory standardDirectory = MyModelUtil.copyTo(standardDirectoryDto, StandardDirectory.class);
        StandardDirectory originalStandardDirectory = standardDirectoryService.getById(standardDirectory.getId());
        if (originalStandardDirectory == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证父Id的数据合法性
        if (MyCommonUtil.isNotBlankOrNull(standardDirectory.getDirectoryParentId())
                && ObjectUtil.notEqual(standardDirectory.getDirectoryParentId(), originalStandardDirectory.getDirectoryParentId())) {
            StandardDirectory parentStandardDirectory = standardDirectoryService.getById(standardDirectory.getDirectoryParentId());
            if (parentStandardDirectory == null) {
                // NOTE: 修改下面方括号中的话述
                errorMessage = "数据验证失败，关联的 [父节点] 并不存在，请刷新后重试！";
                return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        if (!standardDirectoryService.update(standardDirectory, originalStandardDirectory)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据规划-数据标准-目录表数据。
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
     * 列出符合过滤条件的数据规划-数据标准-目录表列表。
     *
     * @param standardDirectoryDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<StandardDirectoryVo>> list(
            @MyRequestBody StandardDirectoryDto standardDirectoryDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardDirectory standardDirectoryFilter = MyModelUtil.copyTo(standardDirectoryDtoFilter, StandardDirectory.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardDirectory.class);
        List<StandardDirectory> standardDirectoryList =
                standardDirectoryService.getStandardDirectoryListWithRelation(standardDirectoryFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(standardDirectoryList, StandardDirectory.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据规划-数据标准-目录表列表。
     *
     * @param standardDirectoryDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<StandardDirectoryVo>> listWithGroup(
            @MyRequestBody StandardDirectoryDto standardDirectoryDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, StandardDirectory.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, StandardDirectory.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        StandardDirectory filter = MyModelUtil.copyTo(standardDirectoryDtoFilter, StandardDirectory.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<StandardDirectory> resultList = standardDirectoryService.getGroupedStandardDirectoryListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, StandardDirectory.INSTANCE));
    }

    /**
     * 查看指定数据规划-数据标准-目录表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<StandardDirectoryVo> view(@RequestParam Long id) {
        StandardDirectory standardDirectory = standardDirectoryService.getByIdWithRelation(id, MyRelationParam.full());
        if (standardDirectory == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        StandardDirectoryVo standardDirectoryVo = StandardDirectory.INSTANCE.fromModel(standardDirectory);
        return ResponseResult.success(standardDirectoryVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        StandardDirectory originalStandardDirectory = standardDirectoryService.getById(id);
        if (originalStandardDirectory == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (standardDirectoryService.hasChildren(id)) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象存在子对象] ，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.HAS_CHILDREN_DATA, errorMessage);
        }
        if (!standardDirectoryService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
