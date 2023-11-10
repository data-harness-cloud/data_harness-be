package supie.common.core.object;

import lombok.Data;

/**
 * 业务方法调用结果对象。可以同时返回具体的错误和自定义类型的数据对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class TypedCallResult<T> {

    /**
     * 为了优化性能，所有没有携带数据的正确结果，均可用该对象表示。
     */
    private static final TypedCallResult<Void> OK = new TypedCallResult<>();
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
    private T data;

    /**
     * 创建验证结果对象。
     *
     * @param errorMessage 错误描述信息。
     * @return 如果参数为空，表示成功，否则返回代码错误信息的错误对象实例。
     */
    public static TypedCallResult<Void> create(String errorMessage) {
        return errorMessage == null ? ok() : error(errorMessage);
    }

    /**
     * 创建验证结果对象。
     *
     * @param errorMessage 错误描述信息。
     * @param data         附带的数据对象。
     * @return 如果参数为空，表示成功，否则返回代码错误信息的错误对象实例。
     */
    public static <T> TypedCallResult<T> create(String errorMessage, T data) {
        return errorMessage == null ? ok(data) : error(errorMessage, data);
    }

    /**
     * 创建表示验证成功的对象实例。
     *
     * @return 验证成功对象实例。
     */
    public static TypedCallResult<Void> ok() {
        return OK;
    }

    /**
     * 创建表示验证成功的对象实例。
     *
     * @param data 附带的数据对象。
     * @return 验证成功对象实例。
     */
    public static <T> TypedCallResult<T> ok(T data) {
        TypedCallResult<T> result = new TypedCallResult<>();
        result.data = data;
        return result;
    }

    /**
     * 创建表示验证失败的对象实例。
     *
     * @param errorMessage 错误描述。
     * @return 验证失败对象实例。
     */
    public static <T> TypedCallResult<T> error(String errorMessage) {
        TypedCallResult<T> result = new TypedCallResult<>();
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
    public static <T> TypedCallResult<T> error(String errorMessage, T data) {
        TypedCallResult<T> result = new TypedCallResult<>();
        result.success = false;
        result.errorMessage = errorMessage;
        result.data = data;
        return result;
    }

    /**
     * 根据参数中出错的TypedCallResult，创建新的错误调用结果对象。
     * @param result 错误调用结果对象。
     * @return 新的错误调用结果对象。
     */
    public static <T, E> TypedCallResult<T> errorFrom(TypedCallResult<E> result) {
        return error(result.getErrorMessage());
    }
}
