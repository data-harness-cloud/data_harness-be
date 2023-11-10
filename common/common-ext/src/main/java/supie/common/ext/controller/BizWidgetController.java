package supie.common.ext.controller;

import com.alibaba.fastjson.JSONObject;
import supie.common.core.object.*;
import supie.common.ext.util.BizWidgetDatasourceExtHelper;
import supie.common.core.annotation.MyRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务组件获取数据的访问接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@RestController
@RequestMapping("${common-ext.urlPrefix}/bizwidget")
public class BizWidgetController {

    @Autowired
    private BizWidgetDatasourceExtHelper bizWidgetDatasourceExtHelper;

    @PostMapping("/list")
    public ResponseResult<MyPageData<Map<String, Object>>> list(
            @MyRequestBody(required = true) String widgetType,
            @MyRequestBody JSONObject filter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String appCode = TokenData.takeFromRequest().getAppCode();
        MyPageData<Map<String, Object>> pageData =
                bizWidgetDatasourceExtHelper.getDataList(appCode, widgetType, filter, orderParam, pageParam);
        return ResponseResult.success(pageData);
    }

    /**
     * 查看指定多条数据的详情。
     *
     * @param widgetType  组件类型。
     * @param fieldName   字段名，如果为空则默认为主键过滤。
     * @param fieldValues 字段值。多个值之间逗号分割。
     * @return 详情数据。
     */
    @PostMapping("/view")
    public ResponseResult<List<Map<String, Object>>> view(
            @MyRequestBody(required = true) String widgetType,
            @MyRequestBody String fieldName,
            @MyRequestBody(required = true) String fieldValues) {
        String appCode = TokenData.takeFromRequest().getAppCode();
        List<Map<String, Object>> dataMapList =
                bizWidgetDatasourceExtHelper.getDataListWithInList(appCode, widgetType, fieldName, fieldValues);
        return ResponseResult.success(dataMapList);
    }
}
