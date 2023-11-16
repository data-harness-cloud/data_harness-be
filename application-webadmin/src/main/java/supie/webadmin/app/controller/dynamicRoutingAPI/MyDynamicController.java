package supie.webadmin.app.controller.dynamicRoutingAPI;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import supie.common.core.annotation.MyRequestBody;
import supie.common.core.object.ResponseResult;

import java.util.Map;

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

    private static int sum = 0;

//    @ResponseBody
//    public String test(String name) {
//        sum++;
//        log.error("==============》第[" + sum + "]次调用自定义接口《==============");
//        return DateUtil.now() + "<--[" + sum + "]-->" + name;
//    }

    public ResponseResult<String> test(@MyRequestBody Map<String, Object> params) {
        sum++;
        log.error("==============》第[" + sum + "]次调用自定义接口《==============");
        params.put("nowDate", DateUtil.now());
        params.put("sum", sum);
        return ResponseResult.success(JSONUtil.toJsonStr(params));
    }

}
