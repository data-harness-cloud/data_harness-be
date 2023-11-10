package supie.common.dbutil.object;

import supie.common.core.constant.FieldFilterType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 数据集过滤对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DatasetFilter extends ArrayList<DatasetFilter.FilterInfo> {

    @Data
    public static class FilterInfo {
        /**
         * 过滤的数据集Id。
         */
        private Long datasetId;
        /**
         * 过滤参数名称。
         */
        private String paramName;
        /**
         * 过滤参数值是单值时。使用该字段值。
         */
        private Object paramValue;
        /**
         * 过滤参数值是集合时，使用该字段值。
         */
        private Collection<Serializable> paramValueList;
        /**
         * 过滤类型。参考常量类 FieldFilterType。
         */
        private Integer filterType = FieldFilterType.EQUAL;
        /**
         * 是否为日期值的过滤。
         */
        private Boolean dateValueFilter = false;
        /**
         * 日期精确到。year/month/week/day
         */
        private String dateRange;
    }
}
