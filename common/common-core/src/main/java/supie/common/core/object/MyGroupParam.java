package supie.common.core.object;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import supie.common.core.config.CoreProperties;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.exception.InvalidClassFieldException;
import supie.common.core.exception.InvalidDataFieldException;
import supie.common.core.exception.InvalidDataModelException;
import supie.common.core.util.ApplicationContextHolder;
import supie.common.core.util.MyModelUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 查询分组参数请求对象。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class MyGroupParam extends ArrayList<MyGroupParam.GroupInfo> {

    private final transient CoreProperties coreProperties =
            ApplicationContextHolder.getBean(CoreProperties.class);

    /**
     * SQL语句的SELECT LIST中，分组字段的返回字段名称列表。
     */
    private List<String> selectGroupFieldList;
    /**
     * 分组参数解析后构建的SQL语句中所需的分组数据，如GROUP BY的字段列表和SELECT LIST中的分组字段显示列表。
     */
    private transient MyGroupCriteria groupCriteria;
    /**
     * 基于分组参数对象中的数据，构建SQL中select list和group by从句可以直接使用的分组对象。
     *
     * @param groupParam 分组参数对象。
     * @param modelClazz 查询表对应的主对象的Class。
     * @return SQL中所需的GROUP对象。详见MyGroupCriteria类定义。
     */
    public static MyGroupParam buildGroupBy(MyGroupParam groupParam, Class<?> modelClazz) {
        if (groupParam == null) {
            return null;
        }
        if (modelClazz == null) {
            throw new IllegalArgumentException("modelClazz Argument can't be NULL");
        }
        groupParam.selectGroupFieldList = new LinkedList<>();
        StringBuilder groupByBuilder = new StringBuilder(128);
        StringBuilder groupSelectBuilder = new StringBuilder(128);
        int i = 0;
        for (GroupInfo groupInfo : groupParam) {
            GroupBaseData groupBaseData = groupParam.parseGroupBaseData(groupInfo, modelClazz);
            if (StrUtil.isBlank(groupBaseData.tableName)) {
                throw new InvalidDataModelException(groupBaseData.modelName);
            }
            if (StrUtil.isBlank(groupBaseData.columnName)) {
                throw new InvalidDataFieldException(groupBaseData.modelName, groupBaseData.fieldName);
            }
            groupParam.processGroupInfo(groupInfo, groupBaseData, groupByBuilder, groupSelectBuilder);
            String aliasName = StrUtil.isBlank(groupInfo.aliasName) ? groupInfo.fieldName : groupInfo.aliasName;
            // selectGroupFieldList中的元素，目前只是被export操作使用。会根据集合中的元素名称匹配导出表头。
            groupParam.selectGroupFieldList.add(aliasName);
            if (++i < groupParam.size()) {
                groupByBuilder.append(", ");
                groupSelectBuilder.append(", ");
            }
        }
        groupParam.groupCriteria = new MyGroupCriteria(groupByBuilder.toString(), groupSelectBuilder.toString());
        return groupParam;
    }

    private GroupBaseData parseGroupBaseData(GroupInfo groupInfo, Class<?> modelClazz) {
        GroupBaseData baseData = new GroupBaseData();
        if (StrUtil.isBlank(groupInfo.fieldName)) {
            throw new IllegalArgumentException("GroupInfo.fieldName can't be EMPTY");
        }
        String[] stringArray = StrUtil.splitToArray(groupInfo.fieldName, '.');
        if (stringArray.length == 1) {
            baseData.modelName = modelClazz.getSimpleName();
            baseData.fieldName = groupInfo.fieldName;
            baseData.tableName = MyModelUtil.mapToTableName(modelClazz);
            baseData.columnName = MyModelUtil.mapToColumnName(groupInfo.fieldName, modelClazz);
        } else {
            Field field = ReflectUtil.getField(modelClazz, stringArray[0]);
            if (field == null) {
                throw new InvalidClassFieldException(modelClazz.getSimpleName(), stringArray[0]);
            }
            Class<?> fieldClazz = field.getType();
            baseData.modelName = fieldClazz.getSimpleName();
            baseData.fieldName = stringArray[1];
            baseData.tableName = MyModelUtil.mapToTableName(fieldClazz);
            baseData.columnName = MyModelUtil.mapToColumnName(baseData.fieldName, fieldClazz);
        }
        return baseData;
    }

    private void processGroupInfo(
            GroupInfo groupInfo, GroupBaseData baseData, StringBuilder groupBy, StringBuilder groupSelect) {
        String tableName = baseData.tableName;
        String columnName = baseData.columnName;
        if (StrUtil.isBlank(groupInfo.dateAggregateBy)) {
            groupBy.append(tableName).append(".").append(columnName);
            groupSelect.append(tableName).append(".").append(columnName);
            if (StrUtil.isNotBlank(groupInfo.aliasName)) {
                groupSelect.append(" ").append(groupInfo.aliasName);
            }
            return;
        }
        if (coreProperties.isMySql() || coreProperties.isDm()) {
            this.processMySqlGroupInfoWithDateAggregation(groupInfo, baseData, groupBy, groupSelect);
        } else if (coreProperties.isPostgresql()) {
            this.processPostgreSqlGroupInfoWithDateAggregation(groupInfo, baseData, groupBy, groupSelect);
        } else if (coreProperties.isOracle() || coreProperties.isKingbase()) {
            this.processOracleGroupInfoWithDateAggregation(groupInfo, baseData, groupBy, groupSelect);
        } else {
            throw new UnsupportedOperationException("Unsupport Database Type.");
        }
        if (StrUtil.isNotBlank(groupInfo.aliasName)) {
            groupSelect.append(" ").append(groupInfo.aliasName);
        } else {
            groupSelect.append(" ").append(columnName);
        }
    }

    private void processMySqlGroupInfoWithDateAggregation(
            GroupInfo groupInfo, GroupBaseData baseData, StringBuilder groupBy, StringBuilder groupSelect) {
        groupBy.append("DATE_FORMAT(")
                .append(baseData.tableName).append(".").append(baseData.columnName);
        groupSelect.append("DATE_FORMAT(")
                .append(baseData.tableName).append(".").append(baseData.columnName);
        if (ApplicationConstant.DAY_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(", '%Y-%m-%d')");
            groupSelect.append(", '%Y-%m-%d')");
        } else if (ApplicationConstant.MONTH_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(", '%Y-%m-01')");
            groupSelect.append(", '%Y-%m-01')");
        } else if (ApplicationConstant.YEAR_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(", '%Y-01-01')");
            groupSelect.append(", '%Y-01-01')");
        } else {
            throw new IllegalArgumentException("Illegal DATE_FORMAT for GROUP ID list.");
        }
    }

    private void processPostgreSqlGroupInfoWithDateAggregation(
            GroupInfo groupInfo, GroupBaseData baseData, StringBuilder groupBy, StringBuilder groupSelect) {
        String toCharFunc = "TO_CHAR(";
        String dateFormat = ", 'YYYY-MM-dd')";
        groupBy.append(toCharFunc)
                .append(baseData.tableName).append(".").append(baseData.columnName);
        groupSelect.append(toCharFunc)
                .append(baseData.tableName).append(".").append(baseData.columnName);
        if (ApplicationConstant.DAY_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(dateFormat);
            groupSelect.append(dateFormat);
        } else if (ApplicationConstant.YEAR_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(", 'YYYY-01-01')");
            groupSelect.append(", 'YYYY-01-01')");
        } else if (ApplicationConstant.MONTH_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(", 'YYYY-MM-01')");
            groupSelect.append(", 'YYYY-MM-01')");
        } else {
            throw new IllegalArgumentException("Illegal TO_CHAR for GROUP ID list.");
        }
    }

    private void processOracleGroupInfoWithDateAggregation(
            GroupInfo groupInfo, GroupBaseData baseData, StringBuilder groupBy, StringBuilder groupSelect) {
        String toCharFunc = "TO_CHAR(";
        String dateFormat = ", 'YYYY-MM-dd')";
        groupBy.append(toCharFunc)
                .append(baseData.tableName).append(".").append(baseData.columnName);
        groupSelect.append(toCharFunc)
                .append(baseData.tableName).append(".").append(baseData.columnName);
        if (ApplicationConstant.DAY_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(dateFormat);
            groupSelect.append(dateFormat);
        } else if (ApplicationConstant.MONTH_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(", 'YYYY-MM') || '-01'");
            groupSelect.append(", 'YYYY-MM') || '-01'");
        } else if (ApplicationConstant.YEAR_AGGREGATION.equals(groupInfo.dateAggregateBy)) {
            groupBy.append(", 'YYYY') || '-01-01'");
            groupSelect.append(", 'YYYY') || '-01-01'");
        } else {
            throw new IllegalArgumentException("Illegal TO_CHAR for GROUP ID list.");
        }
    }

    /**
     * 分组信息对象。
     */
    @Data
    public static class GroupInfo {
        /**
         * Java对象的字段名。目前主要包含三种格式：
         * 1. 简单的属性名称，如userId，将会直接映射到与其关联的数据库字段。表名为当前ModelClazz所对应的表名。
         *    映射结果或为 my_main_table.user_id
         * 2. 一对一关联表属性，如user.userId，这里将先获取user属性的对象类型并映射到对应的表名，后面的userId为
         *    user所在实体的属性。映射结果或为：my_sys_user.user_id
         */
        private String fieldName;
        /**
         * SQL语句的Select List中，分组字段的别名。如果别名为NULL，直接取fieldName。
         */
        private String aliasName;
        /**
         * 如果该值不为NULL，则会对分组字段进行DATE_FORMAT函数的计算，并根据具体的值，将日期数据截取到指定的位。
         * day:   表示按照天聚合，将会截取到天。DATE_FORMAT(columnName, '%Y-%m-%d')
         * month: 表示按照月聚合，将会截取到月。DATE_FORMAT(columnName, '%Y-%m-01')
         * year:  表示按照年聚合，将会截取到年。DATE_FORMAT(columnName, '%Y-01-01')
         */
        private String dateAggregateBy;
    }

    private static class GroupBaseData {
        private String modelName;
        private String fieldName;
        private String tableName;
        private String columnName;
    }
}
