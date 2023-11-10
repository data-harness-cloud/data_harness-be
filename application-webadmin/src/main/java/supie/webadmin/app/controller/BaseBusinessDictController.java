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
 * 基础业务字典操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "基础业务字典管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/baseBusinessDict")
public class BaseBusinessDictController {

    @Autowired
    private BaseBusinessDictService baseBusinessDictService;

    /**
     * 新增基础业务字典数据。
     *
     * @param baseBusinessDictDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "baseBusinessDictDto.id",
            "baseBusinessDictDto.searchString",
            "baseBusinessDictDto.updateTimeStart",
            "baseBusinessDictDto.updateTimeEnd",
            "baseBusinessDictDto.createTimeStart",
            "baseBusinessDictDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody BaseBusinessDictDto baseBusinessDictDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(baseBusinessDictDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        BaseBusinessDict baseBusinessDict = MyModelUtil.copyTo(baseBusinessDictDto, BaseBusinessDict.class);
        // 验证父Id的数据合法性
        BaseBusinessDict parentBaseBusinessDict;
        if (MyCommonUtil.isNotBlankOrNull(baseBusinessDict.getParentId())) {
            parentBaseBusinessDict = baseBusinessDictService.getById(baseBusinessDict.getParentId());
            if (parentBaseBusinessDict == null) {
                errorMessage = "数据验证失败，关联的父节点并不存在，请刷新后重试！";
                return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        baseBusinessDict = baseBusinessDictService.saveNew(baseBusinessDict);
        return ResponseResult.success(baseBusinessDict.getId());
    }

    /**
     * 更新基础业务字典数据。
     *
     * @param baseBusinessDictDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "baseBusinessDictDto.searchString",
            "baseBusinessDictDto.updateTimeStart",
            "baseBusinessDictDto.updateTimeEnd",
            "baseBusinessDictDto.createTimeStart",
            "baseBusinessDictDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody BaseBusinessDictDto baseBusinessDictDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(baseBusinessDictDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        BaseBusinessDict baseBusinessDict = MyModelUtil.copyTo(baseBusinessDictDto, BaseBusinessDict.class);
        BaseBusinessDict originalBaseBusinessDict = baseBusinessDictService.getById(baseBusinessDict.getId());
        if (originalBaseBusinessDict == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证父Id的数据合法性
        if (MyCommonUtil.isNotBlankOrNull(baseBusinessDict.getParentId())
                && ObjectUtil.notEqual(baseBusinessDict.getParentId(), originalBaseBusinessDict.getParentId())) {
            BaseBusinessDict parentBaseBusinessDict = baseBusinessDictService.getById(baseBusinessDict.getParentId());
            if (parentBaseBusinessDict == null) {
                // NOTE: 修改下面方括号中的话述
                errorMessage = "数据验证失败，关联的 [父节点] 并不存在，请刷新后重试！";
                return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
            }
        }
        if (!baseBusinessDictService.update(baseBusinessDict, originalBaseBusinessDict)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除基础业务字典数据。
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
     * 列出符合过滤条件的基础业务字典列表。
     *
     * @param baseBusinessDictDtoFilter 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<BaseBusinessDictVo>> list(
            @MyRequestBody BaseBusinessDictDto baseBusinessDictDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        BaseBusinessDict baseBusinessDictFilter = MyModelUtil.copyTo(baseBusinessDictDtoFilter, BaseBusinessDict.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, BaseBusinessDict.class);
        List<BaseBusinessDict> baseBusinessDictList =
                baseBusinessDictService.getBaseBusinessDictListWithRelation(baseBusinessDictFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(baseBusinessDictList, BaseBusinessDict.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的基础业务字典列表。
     *
     * @param baseBusinessDictDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<BaseBusinessDictVo>> listWithGroup(
            @MyRequestBody BaseBusinessDictDto baseBusinessDictDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, BaseBusinessDict.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, BaseBusinessDict.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        BaseBusinessDict filter = MyModelUtil.copyTo(baseBusinessDictDtoFilter, BaseBusinessDict.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<BaseBusinessDict> resultList = baseBusinessDictService.getGroupedBaseBusinessDictListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, BaseBusinessDict.INSTANCE));
    }

    /**
     * 查看指定基础业务字典对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<BaseBusinessDictVo> view(@RequestParam Long id) {
        BaseBusinessDict baseBusinessDict = baseBusinessDictService.getByIdWithRelation(id, MyRelationParam.full());
        if (baseBusinessDict == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        BaseBusinessDictVo baseBusinessDictVo = BaseBusinessDict.INSTANCE.fromModel(baseBusinessDict);
        return ResponseResult.success(baseBusinessDictVo);
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        BaseBusinessDict originalBaseBusinessDict = baseBusinessDictService.getById(id);
        if (originalBaseBusinessDict == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (baseBusinessDictService.hasChildren(id)) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象存在子对象] ，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.HAS_CHILDREN_DATA, errorMessage);
        }
        if (!baseBusinessDictService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }
}
