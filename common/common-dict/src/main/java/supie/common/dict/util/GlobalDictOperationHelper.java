package supie.common.dict.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import supie.common.core.object.ResponseResult;
import supie.common.core.object.MyPageData;
import supie.common.core.object.MyPageParam;
import supie.common.core.util.MyModelUtil;
import supie.common.core.util.MyPageUtil;
import supie.common.dict.dto.GlobalDictDto;
import supie.common.dict.model.GlobalDict;
import supie.common.dict.service.GlobalDictService;
import supie.common.dict.vo.GlobalDictVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 全局编码字典操作的通用帮助对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Component
public class GlobalDictOperationHelper {

    @Autowired
    private GlobalDictService globalDictService;

    /**
     * 获取全部编码字典列表。
     *
     * @param globalDictDtoFilter 过滤对象。
     * @param pageParam           分页参数。
     * @return 字典的数据列表。
     */
    public ResponseResult<MyPageData<GlobalDictVo>> listAllGlobalDict(
            GlobalDictDto globalDictDtoFilter, MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        GlobalDict filter = MyModelUtil.copyTo(globalDictDtoFilter, GlobalDict.class);
        List<GlobalDict> dictList = globalDictService.getGlobalDictList(filter, null);
        List<GlobalDictVo> dictVoList = MyModelUtil.copyCollectionTo(dictList, GlobalDictVo.class);
        long totalCount = 0L;
        if (dictList instanceof Page) {
            totalCount = ((Page<GlobalDict>) dictList).getTotal();
        }
        return ResponseResult.success(MyPageUtil.makeResponseData(dictVoList, totalCount));
    }
}
