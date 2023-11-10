package supie.common.core.object;

/**
 * 哑元对象，主要用于注解中的缺省对象占位符。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public final class DummyClass {

    private static final Object EMPTY_OBJECT = new Object();

    /**
     * 可以忽略的空对象。避免sonarqube的各种警告。
     *
     * @return 空对象。
     */
    public static Object emptyObject() {
        return EMPTY_OBJECT;
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private DummyClass() {
    }
}
