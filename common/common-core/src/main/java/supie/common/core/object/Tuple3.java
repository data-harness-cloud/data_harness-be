package supie.common.core.object;

/**
 * 三元组对象。主要用于可以一次返回多个结果的场景，同时还能避免强制转换。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class Tuple3<T1, T2, T3> {

    /**
     * 第一个变量。
     */
    private final T1 first;
    /**
     * 第二个变量。
     */
    private final T2 second;

    /**
     * 第三个变量。
     */
    private final T3 third;

    /**
     * 构造函数。
     *
     * @param first  第一个变量。
     * @param second 第二个变量。
     * @param third  第三个变量。
     */
    public Tuple3(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * 获取第一个变量。
     *
     * @return 返回第一个变量。
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * 获取第二个变量。
     *
     * @return 返回第二个变量。
     */
    public T2 getSecond() {
        return second;
    }

    /**
     * 获取第三个变量。
     *
     * @return 返回第三个变量。
     */
    public T3 getThird() {
        return third;
    }
}

