package supie.common.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 无效的类对象字段的自定义异常。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidClassFieldException extends RuntimeException {

    private final String className;
    private final String fieldName;

    /**
     * 构造函数。
     *
     * @param className 对象名。
     * @param fieldName 字段名。
     */
    public InvalidClassFieldException(String className, String fieldName) {
        super("Invalid FieldName [" + fieldName + "] in Class [" + className + "].");
        this.className = className;
        this.fieldName = fieldName;
    }
}
