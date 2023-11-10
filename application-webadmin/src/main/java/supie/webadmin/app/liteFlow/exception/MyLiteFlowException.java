package supie.webadmin.app.liteFlow.exception;

import cn.hutool.core.date.DateUtil;
import com.yomahub.liteflow.exception.LiteFlowException;
import supie.webadmin.app.liteFlow.model.ErrorMessageModel;

/**
 * 描述:LiteFlow自定义异常处理类
 *
 * @author 王立宏
 * @date 2023/10/25 14:39
 * @path SDT-supie.webadmin.app.liteFlow.exception-MyLiteFlowException
 */
public class MyLiteFlowException extends LiteFlowException {

    /**
     * 构建一个异常
     *
     * @param code    异常状态码
     * @param message 异常描述信息
     */
    public MyLiteFlowException(String code, String message) {
        super(code, "错误时间:" + DateUtil.now() + "\n" + "错误信息:" + message);
    }

    /**
     * 构建一个异常
     *
     * @param code         错误代码
     * @param errorMessageModel 错误信息对象
     * @author 王立宏
     * @date 2023/10/25 03:27
     */
    public MyLiteFlowException(String code, ErrorMessageModel errorMessageModel) {
        super(code, errorMessageModel.getErrorMessage());
    }

    /**
     * 构建一个异常
     *
     * @param errorMessageModel 错误信息对象
     */
    public MyLiteFlowException(ErrorMessageModel errorMessageModel) {
        super(errorMessageModel.getCode(), errorMessageModel.getErrorMessage());
    }

}
