package supie.webadmin.app.util.sqlUtil;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/26 9:08
 * @path SDT-supie.webadmin.app.util.sqlUtil-ColumnType
 */
public enum ColumnType {

    /**
     * 表名（表注释）
     */
    TABLE_NAME("表名（表注释）"),
    /**
     * 表变量名
     */
    TABLE_CODE("表变量名"),

    /**
     * 字段名
     */
    COLUMN_NAME("字段名（字段注释）"),
    /**
     * 字段变量名
     */
    COLUMN_CODE("字段变量名"),
    /**
     * 字段类型
     */
    COLUMN_TYPE("类型"),
    /**
     * 长度
     */
    COLUMN_LENGTH("长度"),
    /**
     * 小数点（<=COLUMN_LENGTH）
     */
    COLUMN_DECIMAL("小数点"),
    /**
     * 是否为空
     */
    COLUMN_IS_NULLABLE("是否为空"),
    /**
     * 默认值
     */
    COLUMN_DEFAULT_VALUE("默认值"),
    /**
     * 是否为主键
     */
    COLUMN_IS_KEY("主键");

    private String value;

    ColumnType(String value) {
        this.value = value;
    }

}
