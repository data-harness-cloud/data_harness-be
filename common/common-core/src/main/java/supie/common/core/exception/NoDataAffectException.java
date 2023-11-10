package supie.common.core.exception;

/**
 * 没有数据被修改的自定义异常。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class NoDataAffectException extends RuntimeException {

    /**
     * 构造函数。
     */
	public NoDataAffectException() {

	}

    /**
     * 构造函数。
     *
     * @param msg 错误信息。
     */
	public NoDataAffectException(String msg) {
		super(msg);
	}
}
