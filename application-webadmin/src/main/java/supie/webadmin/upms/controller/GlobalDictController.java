package supie.webadmin.upms.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import supie.common.core.annotation.MyRequestBody;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.constant.ErrorCodeEnum;
import supie.common.core.object.MyOrderParam;
import supie.common.core.object.MyPageData;
import supie.common.core.object.MyPageParam;
import supie.common.core.object.ResponseResult;
import supie.common.core.util.MyCommonUtil;
import supie.common.core.util.MyModelUtil;
import supie.common.core.util.MyPageUtil;
import supie.common.core.validator.UpdateGroup;
import supie.common.dict.dto.GlobalDictDto;
import supie.common.dict.dto.GlobalDictItemDto;
import supie.common.dict.model.GlobalDict;
import supie.common.dict.model.GlobalDictItem;
import supie.common.dict.service.GlobalDictItemService;
import supie.common.dict.service.GlobalDictService;
import supie.common.dict.vo.GlobalDictVo;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 全局通用字典操作接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "全局字典管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/upms/globalDict")
public class GlobalDictController {

    @Autowired
    private GlobalDictService globalDictService;
    @Autowired
    private GlobalDictItemService globalDictItemService;

    /**
     * 新增全局字典接口。
     *
     * @param globalDictDto 新增字典对象。
     * @return 保存后的字典对象。
     */
    @ApiOperationSupport(ignoreParameters = {"globalDictDto.dictId"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody GlobalDictDto globalDictDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(globalDictDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        // 这里必须手动校验字典编码是否存在，因为我们缺省的实现是逻辑删除，所以字典编码字段没有设置为唯一索引。
        if (globalDictService.existDictCode(globalDictDto.getDictCode())) {
            errorMessage = "数据验证失败，字典编码已经存在！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        GlobalDict globalDict = MyModelUtil.copyTo(globalDictDto, GlobalDict.class);
        globalDictService.saveNew(globalDict);
        return ResponseResult.success(globalDict.getDictId());
    }

    /**
     * 更新全局字典操作。
     *
     * @param globalDictDto 更新全局字典对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody GlobalDictDto globalDictDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(globalDictDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        GlobalDict originalGlobalDict = globalDictService.getById(globalDictDto.getDictId());
        if (originalGlobalDict == null) {
            errorMessage = "数据验证失败，当前全局字典并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        GlobalDict globalDict = MyModelUtil.copyTo(globalDictDto, GlobalDict.class);
        if (ObjectUtil.notEqual(globalDict.getDictCode(), originalGlobalDict.getDictCode())
                && globalDictService.existDictCode(globalDict.getDictCode())) {
            errorMessage = "数据验证失败，字典编码已经存在！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!globalDictService.update(globalDict, originalGlobalDict)) {
            errorMessage = "更新失败，数据不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 删除指定的全局字典。
     *
     * @param dictId 指定全局字典主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody(required = true) Long dictId) {
        if (!globalDictService.remove(dictId)) {
            String errorMessage = "数据操作失败，全局字典Id不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 查看全局字典列表。
     *
     * @param globalDictDtoFilter 过滤对象。
     * @param orderParam          排序参数。
     * @param pageParam           分页参数。
     * @return 应答结果对象，包含角色列表。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<GlobalDictVo>> list(
            @MyRequestBody GlobalDictDto globalDictDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        GlobalDict filter = MyModelUtil.copyTo(globalDictDtoFilter, GlobalDict.class);
        List<GlobalDict> globalDictList =
                globalDictService.getGlobalDictList(filter, MyOrderParam.buildOrderBy(orderParam, GlobalDict.class));
        List<GlobalDictVo> globalDictVoList =
                MyModelUtil.copyCollectionTo(globalDictList, GlobalDictVo.class);
        long totalCount = 0L;
        if (globalDictList instanceof Page) {
            totalCount = ((Page<GlobalDict>) globalDictList).getTotal();
        }
        return ResponseResult.success(MyPageUtil.makeResponseData(globalDictVoList, totalCount));
    }

    /**
     * 新增全局字典项目接口。
     *
     * @param globalDictItemDto 新增字典项目对象。
     * @return 保存后的字典对象。
     */
    @ApiOperationSupport(ignoreParameters = {"globalDictItemDto.id"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/addItem")
    public ResponseResult<Long> addItem(@MyRequestBody GlobalDictItemDto globalDictItemDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(globalDictItemDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!globalDictService.existDictCode(globalDictItemDto.getDictCode())) {
            errorMessage = "数据验证失败，字典编码不存在！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (globalDictItemService.existDictCodeAndItemId(
                globalDictItemDto.getDictCode(), globalDictItemDto.getItemId())) {
            errorMessage = "数据验证失败，该字典编码的项目Id已存在！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        GlobalDictItem globalDictItem = MyModelUtil.copyTo(globalDictItemDto, GlobalDictItem.class);
        globalDictItemService.saveNew(globalDictItem);
        return ResponseResult.success(globalDictItem.getId());
    }

    /**
     * 更新全局字典项目。
     *
     * @param globalDictItemDto 更新全局字典项目对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/updateItem")
    public ResponseResult<Void> updateItem(@MyRequestBody GlobalDictItemDto globalDictItemDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(globalDictItemDto, Default.class, UpdateGroup.class);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        GlobalDictItem originalGlobalDictItem = globalDictItemService.getById(globalDictItemDto.getId());
        if (originalGlobalDictItem == null) {
            errorMessage = "数据验证失败，当前全局字典项目并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        GlobalDictItem globalDictItem = MyModelUtil.copyTo(globalDictItemDto, GlobalDictItem.class);
        if (ObjectUtil.notEqual(globalDictItem.getDictCode(), originalGlobalDictItem.getDictCode())) {
            errorMessage = "数据验证失败，字典项目的字典编码不能修改！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (ObjectUtil.notEqual(globalDictItem.getItemId(), originalGlobalDictItem.getItemId())
                && globalDictItemService.existDictCodeAndItemId(globalDictItem.getDictCode(), globalDictItem.getItemId())) {
            errorMessage = "数据验证失败，该字典编码已经包含了该项目Id！";
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        if (!globalDictItemService.update(globalDictItem, originalGlobalDictItem)) {
            errorMessage = "更新失败，数据不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 更新全局字典项目的状态。
     *
     * @param id 更新全局字典项目主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/updateItemStatus")
    public ResponseResult<Void> updateItemStatus(
            @MyRequestBody(required = true) Long id, @MyRequestBody(required = true) Integer status) {
        String errorMessage;
        GlobalDictItem dictItem = globalDictItemService.getById(id);
        if (dictItem == null) {
            errorMessage = "数据操作失败，全局字典项目Id不存在，请刷新后重试!";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (ObjectUtil.notEqual(dictItem.getStatus(), status)) {
            globalDictItemService.updateStatus(dictItem, status);
        }
        return ResponseResult.success();
    }

    /**
     * 删除指定编码的全局字典项目。
     *
     * @param id 主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/deleteItem")
    public ResponseResult<Void> deleteItem(@MyRequestBody(required = true) Long id) {
        String errorMessage;
        GlobalDictItem dictItem = globalDictItemService.getById(id);
        if (dictItem == null) {
            errorMessage = "数据操作失败，全局字典项目Id不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        if (!globalDictItemService.remove(dictItem)) {
            errorMessage = "数据操作失败，全局字典项目Id不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 获取指定字典编码的全局字典项目。字典的键值为[itemId, itemName]。
     * NOTE: 白名单接口。
     *
     * @param dictCode 字典编码。
     * @return 应答结果对象。
     */
    @GetMapping("/listDict")
    public ResponseResult<List<Map<String, Object>>> listDict(@RequestParam String dictCode) {
        List<GlobalDictItem> resultList =
                globalDictService.getGlobalDictItemListFromCache(dictCode, null);
        resultList = resultList.stream()
                .sorted(Comparator.comparing(GlobalDictItem::getStatus))
                .sorted(Comparator.comparing(GlobalDictItem::getShowOrder))
                .collect(Collectors.toList());
        return ResponseResult.success(this.toDictDataList(resultList));
    }

    /**
     * 根据字典Id集合，获取查询后的字典数据。
     * NOTE: 白名单接口。
     *
     * @param dictCode 字典编码。
     * @param itemIds  字典项目Id集合。
     * @return 应答结果对象，包含字典形式的数据集合。
     */
    @PostMapping("/listDictByIds")
    public ResponseResult<List<Map<String, Object>>> listDictByIds(
            @MyRequestBody(required = true) String dictCode, @MyRequestBody List<String> itemIds) {
        List<GlobalDictItem> resultList =
                globalDictService.getGlobalDictItemListFromCache(dictCode, new HashSet<>(itemIds));
        return ResponseResult.success(this.toDictDataList(resultList));
    }

    /**
     * 白名单接口，登录用户均可访问。以字典形式返回全部字典数据集合。
     * fullResultList中的字典列表全部取自于数据库，而cachedResultList全部取自于缓存，前端负责比对。
     *
     * @return 应答结果对象，包含字典形式的数据集合。
     */
    @GetMapping("/listAll")
    public ResponseResult<JSONObject> listAll(@RequestParam String dictCode) {
        List<GlobalDictItem> fullResultList =
                globalDictItemService.getGlobalDictItemListByDictCode(dictCode);
        List<GlobalDictItem> cachedList =
                globalDictService.getGlobalDictItemListFromCache(dictCode, null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullResultList", this.toDictDataList2(fullResultList));
        jsonObject.put("cachedResultList", this.toDictDataList2(cachedList));
        return ResponseResult.success(jsonObject);
    }

    /**
     * 将当前字典表的数据重新加载到缓存中。
     * 由于缓存的数据更新，在add/update/delete等接口均有同步处理。因此该接口仅当同步过程中出现问题时，
     * 可手工调用，或者每天晚上定时同步一次。
     */
    @OperationLog(type = SysOperationLogType.RELOAD_CACHE)
    @GetMapping("/reloadCachedData")
    public ResponseResult<Boolean> reloadCachedData(@RequestParam String dictCode) {
        globalDictService.reloadCachedData(dictCode);
        return ResponseResult.success(true);
    }

    private List<Map<String, Object>> toDictDataList(List<GlobalDictItem> resultList) {
        return resultList.stream().map(item -> {
            Map<String, Object> dataMap = new HashMap<>(2);
            dataMap.put(ApplicationConstant.DICT_ID, item.getItemId());
            dataMap.put(ApplicationConstant.DICT_NAME, item.getItemName());
            dataMap.put("showOrder", item.getShowOrder());
            dataMap.put("status", item.getStatus());
            return dataMap;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> toDictDataList2(List<GlobalDictItem> resultList) {
        return resultList.stream().map(item -> {
            Map<String, Object> dataMap = new HashMap<>(2);
            dataMap.put(ApplicationConstant.DICT_ID, item.getId());
            dataMap.put("itemId", item.getItemId());
            dataMap.put(ApplicationConstant.DICT_NAME, item.getItemName());
            dataMap.put("showOrder", item.getShowOrder());
            dataMap.put("status", item.getStatus());
            return dataMap;
        }).collect(Collectors.toList());
    }
}
