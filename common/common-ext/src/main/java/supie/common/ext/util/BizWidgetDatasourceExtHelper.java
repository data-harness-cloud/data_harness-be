package supie.common.ext.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import supie.common.core.exception.MyRuntimeException;
import supie.common.core.object.*;
import supie.common.ext.base.BizWidgetDatasource;
import supie.common.ext.config.CommonExtProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高级通用业务组件的扩展帮助实现类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Component
public class BizWidgetDatasourceExtHelper {

    @Autowired
    private CommonExtProperties properties;
    /**
     * 全部框架使用橙单框架，同时组件所在模块，如在线表单，报表等和业务服务位于同一服务内是使用。
     */
    private static final String DEFAULT_ORANGE_APP = "__DEFAULT_ORANGE_APP__";
    /**
     * Map的数据结构为：Map<AppCode, Map<widgetDatasourceType, DatasourceWrapper>>
     */
    private Map<String, Map<String, DatasourceWrapper>> dataExtractorMap = MapUtil.newHashMap();

    @PostConstruct
    private void laodThirdPartyAppConfig() {
        Map<String, CommonExtProperties.AppProperties> appPropertiesMap = properties.getApplicationMap();
        if (MapUtil.isEmpty(appPropertiesMap)) {
            return;
        }
        for (Map.Entry<String, CommonExtProperties.AppProperties> entry : appPropertiesMap.entrySet()) {
            String appCode = entry.getKey();
            List<CommonExtProperties.BizWidgetDatasourceProperties> datasources = entry.getValue().getBizWidgetDatasources();
            Map<String, DatasourceWrapper> m = new HashMap<>(datasources.size());
            for (CommonExtProperties.BizWidgetDatasourceProperties datasource : datasources) {
                List<String> types = StrUtil.split(datasource.getTypes(), ",");
                DatasourceWrapper w = new DatasourceWrapper();
                w.setListUrl(datasource.getListUrl());
                w.setViewUrl(datasource.getViewUrl());
                for (String type : types) {
                    m.put(type, w);
                }
            }
            dataExtractorMap.put(appCode, m);
        }
    }

    /**
     * 为默认APP注册基础组件数据源对象。
     *
     * @param type       数据源类型。
     * @param datasource 业务通用组件的数据源接口。
     */
    public void registerDatasource(String type, BizWidgetDatasource datasource) {
        Assert.notBlank(type);
        Assert.notNull(datasource);
        Map<String, DatasourceWrapper> datasourceWrapperMap =
                dataExtractorMap.computeIfAbsent(DEFAULT_ORANGE_APP, k -> new HashMap<>(2));
        datasourceWrapperMap.put(type, new DatasourceWrapper(datasource));
    }

    /**
     * 根据过滤条件获取指定通用业务组件的数据列表。
     *
     * @param appCode    接入应用编码。如果为空，则使用默认的 DEFAULT_ORANGE_APP。
     * @param type       组件数据源类型。
     * @param filter     过滤参数。不同的数据源参数不同。这里我们以键值对的方式传递。
     * @param orderParam 排序参数。
     * @param pageParam  分页参数。
     * @return 查询后的分页数据列表。
     */
    public MyPageData<Map<String, Object>> getDataList(
            String appCode, String type, Map<String, Object> filter, MyOrderParam orderParam, MyPageParam pageParam) {
        if (StrUtil.isBlank(type)) {
            throw new MyRuntimeException("Argument [types] can't be BLANK");
        }
        if (StrUtil.isBlank(appCode)) {
            return this.getDataList(type, filter, orderParam, pageParam);
        }
        DatasourceWrapper wrapper = this.getDatasourceWrapper(appCode, type);
        JSONObject body = new JSONObject();
        body.put("type", type);
        if (MapUtil.isNotEmpty(filter)) {
            body.put("filter", filter);
        }
        if (orderParam != null) {
            body.put("orderParam", orderParam);
        }
        if (pageParam != null) {
            body.put("pageParam", pageParam);
        }
        String response = this.invokeThirdPartyUrlWithPost(wrapper.getListUrl(), body.toJSONString());
        ResponseResult<MyPageData<Map<String, Object>>> responseResult =
                JSON.parseObject(response, new TypeReference<ResponseResult<MyPageData<Map<String, Object>>>>() {
                });
        if (!responseResult.isSuccess()) {
            throw new MyRuntimeException(responseResult.getErrorMessage());
        }
        return responseResult.getData();
    }

    /**
     * 根据指定字段的集合获取指定通用业务组件的数据对象列表。
     *
     * @param appCode     接入应用Id。如果为空，则使用默认的 DEFAULT_ORANGE_APP。
     * @param type        组件数据源类型。
     * @param fieldName   字段名称。
     * @param fieldValues 字段值结合。
     * @return 指定字段数据集合的数据对象列表。
     */
    public List<Map<String, Object>> getDataListWithInList(
            String appCode, String type, String fieldName, String fieldValues) {
        if (StrUtil.isBlank(fieldValues)) {
            throw new MyRuntimeException("Argument [fieldValues] can't be BLANK");
        }
        if (StrUtil.isBlank(type)) {
            throw new MyRuntimeException("Argument [types] can't be BLANK");
        }
        if (StrUtil.isBlank(appCode)) {
            return this.getDataListWithInList(type, fieldName, fieldValues);
        }
        DatasourceWrapper wrapper = this.getDatasourceWrapper(appCode, type);
        JSONObject body = new JSONObject();
        body.put("type", type);
        if (StrUtil.isNotBlank(fieldName)) {
            body.put("fieldName", fieldName);
        }
        body.put("fieldValues", fieldValues);
        String response = this.invokeThirdPartyUrlWithPost(wrapper.getViewUrl(), body.toJSONString());
        ResponseResult<List<Map<String, Object>>> responseResult =
                JSON.parseObject(response, new TypeReference<ResponseResult<List<Map<String, Object>>>>() {
                });
        if (!responseResult.isSuccess()) {
            throw new MyRuntimeException(responseResult.getErrorMessage());
        }
        return responseResult.getData();
    }

    private MyPageData<Map<String, Object>> getDataList(
            String type, Map<String, Object> filter, MyOrderParam orderParam, MyPageParam pageParam) {
        DatasourceWrapper wrapper = this.getDatasourceWrapper(DEFAULT_ORANGE_APP, type);
        return wrapper.getBizWidgetDataSource().getDataList(type, filter, orderParam, pageParam);
    }

    private List<Map<String, Object>> getDataListWithInList(String type, String fieldName, String fieldValues) {
        DatasourceWrapper wrapper = this.getDatasourceWrapper(DEFAULT_ORANGE_APP, type);
        return wrapper.getBizWidgetDataSource().getDataListWithInList(type, fieldName, StrUtil.split(fieldValues, ","));
    }

    private String invokeThirdPartyUrlWithPost(String url, String body) {
        String token = TokenData.takeFromRequest().getToken();
        Map<String, String> headerMap = new HashMap<>(1);
        headerMap.put("Authorization", token);
        StringBuilder fullUrl = new StringBuilder(128);
        fullUrl.append(url).append("?token=").append(token);
        HttpResponse httpResponse = HttpUtil.createPost(fullUrl.toString()).body(body).addHeaders(headerMap).execute();
        if (!httpResponse.isOk()) {
            String msg = StrFormatter.format(
                    "Failed to call [{}] with ERROR HTTP Status [{}] and [{}].",
                    url, httpResponse.getStatus(), httpResponse.body());
            log.error(msg);
            throw new MyRuntimeException(msg);
        }
        return httpResponse.body();
    }

    private DatasourceWrapper getDatasourceWrapper(String appCode, String type) {
        Map<String, DatasourceWrapper> datasourceWrapperMap = dataExtractorMap.get(appCode);
        Assert.notNull(datasourceWrapperMap);
        DatasourceWrapper wrapper = datasourceWrapperMap.get(type);
        Assert.notNull(wrapper);
        return wrapper;
    }

    @NoArgsConstructor
    @Data
    public static class DatasourceWrapper {
        private BizWidgetDatasource bizWidgetDataSource;
        private String listUrl;
        private String viewUrl;

        public DatasourceWrapper(BizWidgetDatasource bizWidgetDataSource) {
            this.bizWidgetDataSource = bizWidgetDataSource;
        }
    }
}
