package supie.webadmin.app.controller.dynamicRoutingAPI;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import supie.common.core.constant.ErrorCodeEnum;
import supie.common.core.object.MyPageParam;
import supie.common.core.object.ResponseResult;
import supie.webadmin.app.dao.CustomizeRouteMapper;
import supie.webadmin.app.dao.ProjectEngineMapper;
import supie.webadmin.app.model.CustomizeRoute;
import supie.webadmin.app.model.ProjectEngine;
import supie.webadmin.app.service.databasemanagement.Strategy;
import supie.webadmin.app.service.databasemanagement.StrategyFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private CustomizeRouteMapper customizeRouteMapper;
    @Autowired
    private ProjectEngineMapper projectEngineMapper;
    @Autowired
    private StrategyFactory strategyFactory;

    /**
     * 执行SQL
     */
    @ResponseBody
    public ResponseResult<Object> executeSql(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String url = request.getRequestURI();
        QueryWrapper<CustomizeRoute> customizeRouteQueryWrapper = new QueryWrapper<>();
        customizeRouteQueryWrapper.eq("url", url);
        CustomizeRoute customizeRoute = customizeRouteMapper.selectOne(customizeRouteQueryWrapper);
        return performCustomizeRouteBusiness(params, customizeRoute);
    }

    @NotNull
    public ResponseResult<Object> performCustomizeRouteBusiness(Map<String, Object> params, CustomizeRoute customizeRoute) {
        String sqlScript = customizeRoute.getSqlScript();
        List<Parameter> parameterList = JSONUtil.toList(JSONUtil.parseArray(customizeRoute.getParameter()), Parameter.class);
        Set<String> paramsKey = params.keySet();
        for (Parameter parameter : parameterList) {
            if (parameter.getRequired() && !paramsKey.contains(parameter.getName())) {
                return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST, "缺少[" + parameter.getName() + "]变量！");
            }
            String name = "${" + parameter.getName() + "}";
            String defaultValue = params.get(parameter.getName()).toString();
            if (StrUtil.isBlank(defaultValue)) {
                defaultValue = parameter.getDefaultValue();
            }
            sqlScript = sqlScript.replace(name, defaultValue);
        }
        ProjectEngine projectEngine = projectEngineMapper.selectByProjectId(customizeRoute.getProjectId());
        Strategy strategy = strategyFactory.getStrategy(
                projectEngine.getEngineType(), projectEngine.getEngineHost(), projectEngine.getEnginePort(),
                customizeRoute.getDatabaseName(), projectEngine.getEngineUsername(), projectEngine.getEnginePassword());
        Map<String, Object> resultData;
        Map<String, Object> resultMap = new HashMap<>();
        if (paramsKey.contains("pageParam")) {
            MyPageParam pageParam = JSONUtil.toBean(JSONUtil.toJsonStr(params.get("pageParam")), MyPageParam.class);
            PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
            resultData = strategy.executeSql(sqlScript);
            strategy.closeAll();
            if (Boolean.FALSE.equals(resultData.get("isSuccess"))) {
                return ResponseResult.error(ErrorCodeEnum.NO_ERROR,
                        "(" + resultData.get("sql").toString() + ")" + resultData.get("message").toString());
            }
            Map<String, Object> queryResultData = (Map<String, Object>) resultData.get("queryResultData");
            List<Map<String, Object>> queryDataList = (List<Map<String, Object>>) queryResultData.get("queryDataList");
            PageInfo<Map<String, Object>> mapPageInfo = new PageInfo<>(queryDataList);
            resultMap.put("totalCount", mapPageInfo.getTotal());
        } else {
            resultData = strategy.executeSql(sqlScript);
            strategy.closeAll();
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
