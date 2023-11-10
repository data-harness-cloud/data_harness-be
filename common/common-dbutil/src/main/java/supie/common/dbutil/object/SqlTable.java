package supie.common.dbutil.object;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据库中的表对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class SqlTable {

    /**
     * 表名称。
     */
    private String tableName;

    /**
     * 表注释。
     */
    private String tableComment;

    /**
     * 创建时间。
     */
    private Date createTime;

    /**
     * 关联的字段列表。
     */
    private List<SqlTableColumn> columnList;

    /**
     * 数据库链接Id。
     */
    private Long dblinkId;
}
