package supie.common.core.object;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 业务方法调用结果对象。可以同时返回具体的错误和JSON类型的数据对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class CallResult {

    /**
     * 为了优化性能，所有没有携带数据的正确结果，均可用该对象表示。
     */
    private static final CallResult OK = new CallResult();
    /**
     * 是否成功标记。
     */
    private boolean success = true;
    /**
     * 错误信息描述。
     */
    private String errorMessage = null;
    /**
     * 在验证同时，仍然需要附加的关联数据对象。
     */
    private JSONObject data;

    /**
     * 创建验证结果对象。
     *
     * @param errorMessage 错误描述信息。
     * @return 如果参数为空，表示成功，否则返回代码错误信息的错误对象实例。
     */
    public static CallResult create(String errorMessage) {
        return errorMessage == null ? ok() : error(errorMessage);
    }

    /**
     * 创建验证结果对象。
     *
     * @param errorMessage 错误描述信息。
     * @param data         附带的数据对象。
     * @return 如果参数为空，表示成功，否则返回代码错误信息的错误对象实例。
     */
    public static CallResult create(String errorMessage, JSONObject data) {
        return errorMessage == null ? ok(data) : error(errorMessage);
    }

    /**
     * 创建表示验证成功的对象实例。
     *
     * @return 验证成功对象实例。
     */
    public static CallResult ok() {
        return OK;
    }

    /**
     * 创建表示验证成功的对象实例。
     *
     * @param data 附带的数据对象。
     * @return 验证成功对象实例。
     */
    public static CallResult ok(JSONObject data) {
        CallResult result = new CallResult();
        result.data = data;
        return result;
    }

    /**
     * 创建表示验证失败的对象实例。
     *
     * @param errorMessage 错误描述。
     * @return 验证失败对象实例。
     */
    public static CallResult error(String errorMessage) {
        CallResult result = new CallResult();
        result.success = false;
        result.errorMessage = errorMessage;
        return result;
    }

    /**
     * 创建表示验证失败的对象实例。
     *
     * @param errorMessage 错误描述。
     * @param data         附带的数据对象。
     * @return 验证失败对象实例。
     */
    public static <T> CallResult error(String errorMessage, T data) {
        CallResult result = new CallResult();
        result.success = false;
        result.errorMessage = errorMessage;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorData", data);
        result.data = jsonObject;
        return result;
    }
}
