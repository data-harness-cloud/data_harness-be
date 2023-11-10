package supie.common.core.util;

import org.springframework.stereotype.Component;

/**
 * 缺省的自定义脱敏处理器的实现类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Component
public class MyCustomMaskFieldHandler implements MaskFieldHandler {

    @Override
    public String handleMask(String modelName, String fieldName, String data, char maskChar) {
        // 这里是我们默认提供的躺平实现方式。
        // 在默认生成的代码中，如果脱敏字段的处理类型为CUSTOM的时候，就会暂时使用
        // 该类为默认实现，其实这里就是一个占位符实现类。用户可根据需求自行实现自己所需的脱敏处理器实现类。
        // 实现后，可在脱敏字段的MaskField注解的handler参数中，改为自己的实现类。
        // 最后一句很重要，实现类必须是bean对象，如当前类用@Component注解标记。
        throw new UnsupportedOperationException("请仔细阅读上面的代码注解，并实现自己的处理类，以替代默认生成的自定义实现类！！");
    }
}
