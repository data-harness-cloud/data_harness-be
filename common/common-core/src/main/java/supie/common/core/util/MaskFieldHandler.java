package supie.common.core.util;

/**
 * 自定义脱敏处理器接口。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public interface MaskFieldHandler {

    /**
     * 处理自定义的脱敏数据。可以根据表名和字段名，使用不同的自定义脱敏规则。
     *
     * @param modelName 脱敏字段所在实体对象名。
     * @param fieldName 脱敏实体对象名中的字段属性名。
     * @param data      待脱敏的数据。
     * @param maskChar   脱敏掩码字符。
     * @return 脱敏后的数据。
     */
    String handleMask(String modelName, String fieldName, String data, char maskChar);
}
