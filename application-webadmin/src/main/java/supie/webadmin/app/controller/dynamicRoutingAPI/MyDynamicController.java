package supie.webadmin.app.controller.dynamicRoutingAPI;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.ObjectMapper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import supie.common.core.constant.ErrorCodeEnum;
import supie.common.core.object.MyPageParam;
import supie.common.core.object.ResponseResult;
import supie.webadmin.app.dao.ProjectEngineMapper;
import supie.webadmin.app.model.CustomizeRoute;
import supie.webadmin.app.model.ProjectEngine;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/11/15 15:17
 * @path SDT-supie.webadmin.app.controller.dynamicRoutingAPI-MyDynamicController
 */
@Slf4j
@Component
public class MyDynamicController {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ProjectEngineMapper projectEngineMapper;
    @Autowired
    private StrategyFactory strategyFactory;

    /**
     * 执行SQL
     */
    @ResponseBody
    public ResponseResult<Object> executeSql(HttpServletRequest request) {
        // 获取请求体格式类型
        String contentType = request.getContentType();
        // 获取请求体内容
        String requestBody;
        try {
            requestBody = request.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> params = new HashMap<>();
        if (StrUtil.isNotBlank(requestBody)) {
            try {
                params = JSONUtil.toBean(requestBody, Map.class);
            } catch (Exception e) {
                return ResponseResult.error(ErrorCodeEnum.NO_ERROR, "请求体请使用正确的JSON格式!");
            }
        }
        String url = request.getRequestURI();
        RBucket<String> customizeRouteData = redissonClient.getBucket("CustomizeRoute:" + url);
        if (!customizeRouteData.isExists()) {
            return ResponseResult.error(ErrorCodeEnum.NO_ERROR, "当前的路由相关信息获取失败，请重试!");
        }
        CustomizeRoute customizeRoute = JSONUtil.toBean(customizeRouteData.get(), CustomizeRoute.class);
        return performCustomizeRouteBusiness(params, customizeRoute);
    }

    /**
     * 执行自定义路线业务
     * @param params 请求参数
     * @param customizeRoute 配置的参数信息
     * @return
     */
    @NotNull
    public ResponseResult<Object> performCustomizeRouteBusiness(Map<String, Object> params, CustomizeRoute customizeRoute) {
        String sqlScript = customizeRoute.getSqlScript();
        List<Parameter> parameterList = JSONUtil.toList(JSONUtil.parseArray(customizeRoute.getParameter()), Parameter.class);
        Set<String> paramsKey = params.keySet();
        String defaultValue;
        for (Parameter parameter : parameterList) {
            if (!parameter.getRequired() && !paramsKey.contains(parameter.getName()) && StrUtil.isBlank(parameter.getDefaultValue())) {
                // 缺少当前变量
                return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST, "缺少必填[" + parameter.getName() + "]变量！");
            }
            String name = "${" + parameter.getName() + "}";
            Object value = params.get(parameter.getName());
            if (value != null) {
                if (Objects.equals(value, "")) {
                    defaultValue = "\"\"";
                } else {
                    defaultValue = value.toString();
                }
            } else {
                if (parameter.getDefaultValue() == null) {
                    defaultValue = "null";
                } else if (Objects.equals(parameter.getDefaultValue(), "")) {
                    defaultValue = "\"\"";
                } else {
                    defaultValue = parameter.getDefaultValue();
                }
            }
            sqlScript = sqlScript.replace(name, defaultValue);
        }
        ProjectEngine projectEngine = projectEngineMapper.selectByProjectId(customizeRoute.getProjectId());
        Strategy strategy = strategyFactory.getStrategy(
                projectEngine.getEngineType(), projectEngine.getEngineHost(), projectEngine.getEnginePort(),
                customizeRoute.getDatabaseName(), projectEngine.getEngineUsername(), projectEngine.getEnginePassword(),0);
        Map<String, Object> resultData;
        Map<String, Object> resultMap = new HashMap<>();
        if (paramsKey.contains("pageParam")) {
            MyPageParam pageParam = JSONUtil.toBean(JSONUtil.toJsonStr(params.get("pageParam")), MyPageParam.class);
            resultData = strategy.executeSql(sqlScript, pageParam);
            strategy.closeAll();
            if (Boolean.FALSE.equals(resultData.get("success"))) {
                return ResponseResult.error(ErrorCodeEnum.NO_ERROR,
                        resultData.get("sql").toString() + " ==> " + resultData.get("message").toString());
            }
            // 判断属于查询还是非查询
            if (resultData.containsKey("queryResultData")) {
                Map<String, Object> queryResultData = (Map<String, Object>) resultData.get("queryResultData");
                List<Map<String, Object>> queryDataList = (List<Map<String, Object>>) queryResultData.get("queryDataList");
                PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(queryDataList);
                resultMap.put("totalCount", mapPageInfo.getTotal());
            } else if (resultData.containsKey("updateResultData")) {
            } else {
                // 系统错误！
            }
        } else {
            resultData = strategy.executeSql(sqlScript, null);
            strategy.closeAll();
            if (Boolean.FALSE.equals(resultData.get("success"))) {
                return ResponseResult.error(ErrorCodeEnum.NO_ERROR,
                        resultData.get("sql").toString() + " ==> " + resultData.get("message").toString());
            }
        }
        resultMap.put("url", customizeRoute.getUrl());
        resultMap.put("resultData", resultData);
        return ResponseResult.success(resultMap);
    }

    @Data
    public class Parameter {
        private String name;        // 参数名
        private String describe;    // 描述
        private String type;        // 类型
        private Boolean required;   // 是否可为空
        private String defaultValue;// 默认值
    }

}
