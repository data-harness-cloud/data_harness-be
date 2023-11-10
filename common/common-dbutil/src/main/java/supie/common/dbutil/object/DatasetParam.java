package supie.common.dbutil.object;

import supie.common.core.object.MyOrderParam;
import supie.common.core.object.MyPageParam;
import lombok.Data;

import java.util.List;

/**
 * 数据集查询的各种参数。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class DatasetParam {

    /**
     * SELECT选择的字段名列表。
     */
    private List<String> selectColumnNameList;
    /**
     * 数据集过滤参数。
     */
    private DatasetFilter filter;
    /**
     * SQL结果集的参数。
     */
    private DatasetFilter sqlFilter;
    /**
     * 分页参数。
     */
    private MyPageParam pageParam;
    /**
     * 分组参数。
     */
    private MyOrderParam orderParam;
    /**
     * 排序字符串。
     */
    private String orderBy;
    /**
     * 该值目前仅用于SQL类型的结果集。
     * 如果该值为true，SQL结果集中定义的参数都会被替换为 (1 = 1) 的恒成立过滤。
     * 比如 select * from zz_sys_user where user_status = ${status}，
     * 该值为true的时会被替换为 select * from zz_sys_user where 1 = 1。
     */
    private Boolean disableSqlDatasetFilter = false;
}
