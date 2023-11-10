package supie.common.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 无效的数据库链接类型自定义异常。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidDblinkTypeException extends RuntimeException {

    /**
     * 构造函数。
     *
     * @param dblinkType 数据库链接类型。
     */
    public InvalidDblinkTypeException(int dblinkType) {
        super("Invalid Dblink Type [" + dblinkType + "].");
    }
}
