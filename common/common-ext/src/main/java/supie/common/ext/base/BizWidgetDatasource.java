package supie.common.ext.base;

import supie.common.core.object.MyOrderParam;
import supie.common.core.object.MyPageData;
import supie.common.core.object.MyPageParam;

import java.util.List;
import java.util.Map;

/**
 * 业务组件获取数据的数据源接口。
 * 如果业务服务集成了common-ext组件，可以通过实现该接口的方式，为BizWidgetController访问提供数据。
 * 对于没有集成common-ext组件的服务，可以通过http方式，为BizWidgetController访问提供数据。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface BizWidgetDatasource {

    /**
     * 获取指定通用业务组件的数据。
     *
     * @param widgetType 业务组件类型。
     * @param filter     过滤参数。不同的数据源参数不同。这里我们以键值对的方式传递。
     * @param orderParam 排序参数。
     * @param pageParam  分页参数。
     * @return 查询后的分页数据列表。
     */
    MyPageData<Map<String, Object>> getDataList(
            String widgetType, Map<String, Object> filter, MyOrderParam orderParam, MyPageParam pageParam);

    /**
     * 获取指定主键Id的数据对象。
     *
     * @param widgetType  业务组件类型。
     * @param fieldName   字段名，如果为空，则使用主键字段名。
     * @param fieldValues 字段值集合。
     * @return 指定主键Id的数据对象。
     */
    List<Map<String, Object>> getDataListWithInList(String widgetType, String fieldName, List<String> fieldValues);
}
