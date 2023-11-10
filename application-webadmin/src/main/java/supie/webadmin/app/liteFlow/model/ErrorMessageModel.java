package supie.webadmin.app.liteFlow.model;

import cn.hutool.core.date.DateUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;

/**
 * 描述:
 *
 * @author 王立宏
 * @date 2023/10/25 15:36
 * @path SDT-supie.webadmin.app.liteFlow.model-ErrorMessageModel
 */
public class ErrorMessageModel {


    private String code;
    private String errorMessage;

    public ErrorMessageModel() {
        this.errorMessage = "无错误信息！";
    }

    /**
     * 错误信息
     *
     * @param errorMessage 错误信息
     * @author 王立宏
     * @date 2023/10/25 03:28
     */
    public ErrorMessageModel(String errorMessage) {
        this.errorMessage = "错误时间:" + DateUtil.now() + "\n" +
                "错误信息:" + errorMessage;
    }

    /**
     * 错误信息
     *
     * @param errorNodeClass 错误节点类
     * @param errorMessage   错误信息
     * @author 王立宏
     * @date 2023/10/25 03:28
     */
    public ErrorMessageModel(Class<?> errorNodeClass, String errorMessage) {
        LiteflowComponent liteflowComponent = errorNodeClass.getAnnotation(LiteflowComponent.class);
        String nodeMessage = "";
        if (liteflowComponent != null && liteflowComponent.id() != null) {
            nodeMessage = liteflowComponent.id() + "[" + liteflowComponent.name() + "]";
        } else if (liteflowComponent != null) {
            nodeMessage = liteflowComponent.value();
        }
        this.errorMessage = "错误的组件为:" + nodeMessage + "\n" +
                "错误时间:" + DateUtil.now() + "\n" +
                "错误信息:" + errorMessage;
    }

    /**
     * 错误信息
     *
     * @param code         错误代码
     * @param errorMessage 错误信息
     * @author 王立宏
     * @date 2023/10/25 03:28
     */
    public ErrorMessageModel(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = "错误时间:" + DateUtil.now() + "\n" +
                "错误信息:" + errorMessage;
    }

    /**
     * 错误信息
     *
     * @param code           错误代码
     * @param errorNodeClass 错误节点类
     * @param errorMessage   错误信息
     * @author 王立宏
     * @date 2023/10/25 03:28
     */
    public ErrorMessageModel(String code, Class<?> errorNodeClass, String errorMessage) {
        this.code = code;
        LiteflowComponent liteflowComponent = errorNodeClass.getAnnotation(LiteflowComponent.class);
        String nodeMessage = "";
        if (liteflowComponent != null && liteflowComponent.id() != null) {
            nodeMessage = liteflowComponent.id() + "[" + liteflowComponent.name() + "]";
        } else if (liteflowComponent != null) {
            nodeMessage = liteflowComponent.value();
        }
        this.errorMessage = "错误的组件为:" + nodeMessage + "\n" +
                "错误时间:" + DateUtil.now() + "\n" +
                "错误信息:" + errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
