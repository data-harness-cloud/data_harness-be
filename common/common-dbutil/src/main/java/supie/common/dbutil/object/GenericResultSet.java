package supie.common.dbutil.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报表通用的查询结果集对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericResultSet<M, T> {

    /**
     * 查询结果集的字段meta数据列表。
     */
    private List<M> columnMetaList;

    /**
     * 查询数据集。如果当前结果集为分页查询，将只包含分页数据。
     */
    private List<T> dataList;

    /**
     * 查询数据总数。如果当前结果集为分页查询，该值为分页前的数据总数，否则为0。
     */
    private Long totalCount = 0L;

    public GenericResultSet(List<M> columnMetaList, List<T> dataList) {
        this.columnMetaList = columnMetaList;
        this.dataList = dataList;
    }
}
