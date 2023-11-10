package supie.common.dbutil.object;

import lombok.Data;

/**
 * 数据库中的表字段对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class SqlTableColumn {

    /**
     * 表字段名。
     */
    private String columnName;

    /**
     * 字段注释。
     */
    private String columnComment;

    /**
     * 表字段类型。
     */
    private String columnType;

    /**
     * 表字段全类型。
     */
    private String fullColumnType;

    /**
     * 是否自动增长。
     */
    private Boolean autoIncrement;

    /**
     * 是否为主键。
     */
    private Boolean primaryKey;

    /**
     * 是否可以为空值。
     */
    private Boolean nullable;

    /**
     * 字段顺序。
     */
    private Integer columnShowOrder;

    /**
     * 附加信息。
     */
    private String extra;

    /**
     * 数值型字段精度。
     */
    private Integer numericPrecision;

    /**
     * 数值型字段刻度。
     */
    private Integer numericScale;

    /**
     * 字符型字段精度。
     */
    private Long stringPrecision;

    /**
     * 缺省值。
     */
    private Object columnDefault;

    /**
     * 数据库链接类型。该值为冗余字段，只是为了提升运行时效率。
     */
    private int dblinkType;
}
