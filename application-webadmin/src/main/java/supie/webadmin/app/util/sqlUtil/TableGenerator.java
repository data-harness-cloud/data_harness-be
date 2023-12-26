package supie.webadmin.app.util.sqlUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：生成建表语句
 *
 * @author 王立宏
 * @date 2023/10/25 18:18
 * @path SDT-supie.webadmin.app.util-TableGenerator
 */
@Slf4j
public class TableGenerator {

    /**
     * 生成 SQL 建表语句
     *
     * @param dbType          数据库类型（mysql、doris）
     * @param tableModel      表名模型
     * @param columnModelList 表字段模型
     * @return 字符串
     * @author 王立宏
     * @date 2023/10/29 10:59
     */
    public static <T, C> String generateCreateTableSQL(String dbType, T tableModel, List<C> columnModelList) {
        if (null == dbType) dbType = "mysql";
        if (dbType.equalsIgnoreCase("mysql")) {
            // 生成 mysql数据库 建表语句
            return buildMySql(tableModel, columnModelList);
        } else if (dbType.equalsIgnoreCase("doris")) {
            // 生成 doris数据库 建表语句
            return buildDoris(tableModel, columnModelList);
        }
        return "暂不支持[" + dbType + "]数据库！";
    }

    /**
     * 构建 Doris 数据库的建表语句
     *
     * @param tableModel      表模型
     * @param columnModelList 色谱柱模型列表
     * @return 字符串
     * @author 王立宏
     * @date 2023/10/29 11:00
     */
    private static <T, C> String buildDoris(T tableModel, List<C> columnModelList) {
        StringBuilder sql = new StringBuilder();
        // In Doris, table creation is slightly different from MySQL
        Map<String, String> tableMap = getTableValue(tableModel);
        if (StrUtil.isBlankIfStr(tableMap.get("tableCode"))) return "表名无效";

        sql.append("DROP TABLE IF EXISTS ").append(tableMap.get("tableCode")).append(";\n");
        // Create a Doris table
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableMap.get("tableCode")).append(" (\n");
        String distributed = "";
        for (int i = 0; i < columnModelList.size(); i++) {
            C columnModel = columnModelList.get(i);
            Class<?> column = columnModel.getClass();
            Map<String, Object> columnMap = new HashMap<>();
            Field[] fields = column.getDeclaredFields();
            for (Field field : fields) {
                columnMap = getColumnValue(columnModel, field, columnMap);
            }
            String columnCode = null;
            String columnName = null;
            String columnType = null;
            String columnDefaultValue = null;
            Integer columnLength = null;
            Integer columnDecimal = null;
            Boolean columnIsNotNullable = null;
            Boolean columnIsKey = null;
            if (columnMap != null) {
                columnCode = (String) columnMap.get("columnCode");
                columnName = (String) columnMap.get("columnName");
                columnType = (String) columnMap.get("columnType");
                columnDefaultValue = (String) columnMap.get("columnDefaultValue");
                columnLength = (Integer) columnMap.get("columnLength");
                columnDecimal = (Integer) columnMap.get("columnDecimal");
                columnIsNotNullable = (Boolean) columnMap.get("columnIsNotNullable");
                columnIsKey = (Boolean) columnMap.get("columnIsKey");
            }

            if (StrUtil.isBlankIfStr(columnCode) || StrUtil.isBlankIfStr(columnType)) continue;
            if (i == 0) distributed = columnCode;
            sql.append("  ").append(columnCode).append(" ").append(columnType);
            if (columnLength != null && columnDecimal != null && columnLength >= columnDecimal) {
                sql.append("(").append(columnLength).append(", ").append(columnDecimal).append(")");
            } else if (columnLength != null) {
                sql.append("(").append(columnLength).append(")");
            } else if (StrUtil.contains(columnType.toUpperCase(), "VARCHAR")) {
                sql.append("(").append(255).append(")");
            }
            if (!columnIsNotNullable) {
                sql.append(" NULL");
            } else {
                sql.append(" NOT NULL");
            }
            if (StrUtil.isNotBlank(columnDefaultValue)) sql.append(" DEFAULT ").append(columnDefaultValue);
            // If there is a column name, append it as a comment
            if (StrUtil.isNotBlank(columnName)) {
                sql.append(" COMMENT '").append(columnName).append("'");
            }
//            if (columnIsKey) {
//                // Doris may have different primary key syntax
//                // Modify this part according to Doris's primary key syntax
//                sql.append(" PRIMARY KEY");
//            }
            if (i < (columnModelList.size() - 1)) sql.append(",\n");
        }
        sql.append("\n").append(") ENGINE=OLAP ").append("COMMENT '").append(tableMap.get("tableName")).append("' \n")
                .append("DISTRIBUTED BY HASH(`").append(distributed).append("`) BUCKETS 1;");
        return sql.toString();
    }

    private static <T, C> String buildMySql(T tableModel, List<C> columnModelList) {
        StringBuilder sql = new StringBuilder();
        // 获取表名
        Map<String, String> tableMap = getTableValue(tableModel);
        if (StrUtil.isBlankIfStr(tableMap.get("tableCode"))) throw new RuntimeException("表名无效");
        // 如果表存在则删除表
        sql.append("DROP TABLE IF EXISTS ").append(tableMap.get("tableCode")).append(";\n");
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableMap.get("tableCode")).append(" (\n");
        for (int i = 0; i < columnModelList.size(); i++) {
            C columnModel = columnModelList.get(i);
            Class<?> column = columnModel.getClass();
            Map<String, Object> columnMap = new HashMap<>();
            Field[] fields = column.getDeclaredFields();
            for (Field field : fields) {
                columnMap = getColumnValue(columnModel, field, columnMap);
            }
            String columnCode = null;
            String columnName = null;
            String columnType = null;
            String columnDefaultValue = null;
            Integer columnLength = null;
            Integer columnDecimal = null;
            Boolean columnIsNotNullable = null;
            Boolean columnIsKey = null;
            if (columnMap != null) {
                columnCode = (String) columnMap.get("columnCode");
                columnName = (String) columnMap.get("columnName");
                columnType = (String) columnMap.get("columnType");
                columnDefaultValue = (String) columnMap.get("columnDefaultValue");
                columnLength = (Integer) columnMap.get("columnLength");
                columnDecimal = (Integer) columnMap.get("columnDecimal");
                columnIsNotNullable = (Boolean) columnMap.get("columnIsNotNullable");
                columnIsKey = (Boolean) columnMap.get("columnIsKey");
            }

            if (StrUtil.isBlankIfStr(columnCode) || StrUtil.isBlankIfStr(columnType)) continue;
            sql.append("  ").append(columnCode).append(" ").append(columnType);
            if (columnLength != null && columnDecimal != null && columnLength >= columnDecimal) {
                sql.append("(").append(columnLength).append(", ").append(columnDecimal).append(")");
            } else if (columnLength != null) {
                sql.append("(").append(columnLength).append(")");
            } else if (StrUtil.contains(columnType.toUpperCase(), "VARCHAR")) {
                sql.append("(").append(255).append(")");
            }
            if (!columnIsNotNullable) {
                sql.append(" NULL");
            } else {
                sql.append(" NOT NULL");
            }
            if (StrUtil.isNotBlank(columnDefaultValue)) sql.append(" DEFAULT ").append(columnDefaultValue);
            // 如果有注释,追加注释
            if (StrUtil.isNotBlank(columnName)) {
                sql.append(" COMMENT ").append("'").append(columnName).append("'");
            }
            if (columnIsKey) sql.append(" PRIMARY KEY");
            if (i < (columnModelList.size() - 1)) sql.append(",\n");
        }
//        // 只在最后的PRIMARY KEY语句设置主键
//        sql.append(" PRIMARY KEY (").append(keyColumnName).append(")\n");
        sql.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '").append(tableMap.get("tableName")).append("';");
        return sql.toString();
    }

    /**
     * 获取表名称相关字段
     *
     * @param tableModel 表模型
     * @return
     * @author 王立宏
     * @date 2023/10/29 11:03
     */
    public static <T> Map<String, String> getTableValue(T tableModel) {
        Map<String, String> tableMap = new HashMap<>();
        Class<?> tableClass = tableModel.getClass();
        Field[] fields = tableClass.getDeclaredFields();
        for (Field field : fields) {
            ColumnField columnField = field.getAnnotation(ColumnField.class);
            if (columnField == null) continue;
            try {
                switch (columnField.value()) {
                    case TABLE_NAME:
                        // 表名(表注释)
                        field.setAccessible(true);
                        tableMap.put("tableName", (String) field.get(tableModel));
                        break;
                    case TABLE_CODE:
                        // 表变量名
                        field.setAccessible(true);
                        tableMap.put("tableCode", (String) field.get(tableModel));
                        break;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return tableMap;
    }

    /**
     * 设置列值
     *
     * @param columnMode 处理的模型变量
     * @param field      字段
     * @param columnMap  columnMap
     * @return
     * @author 王立宏
     * @date 2023/10/26 11:26
     */
    private static <C> Map<String, Object> getColumnValue(C columnMode, Field field, Map<String, Object> columnMap) {
        ColumnField columnField = field.getAnnotation(ColumnField.class);
        if (columnField == null) return columnMap;
        try {
            switch (columnField.value()) {
                case COLUMN_NAME:
                    // 字段名(字段注释)
                    field.setAccessible(true);
                    if (field.get(columnMode) != null) columnMap.put("columnName", (String) field.get(columnMode));
                    break;
                case COLUMN_CODE:
                    // 字段变量名
                    field.setAccessible(true);
                    if (field.get(columnMode) != null) columnMap.put("columnCode", (String) field.get(columnMode));
                    break;
                case COLUMN_TYPE:
                    // 类型
                    field.setAccessible(true);
                    if (field.get(columnMode) != null) columnMap.put("columnType", (String) field.get(columnMode));
                    break;
                case COLUMN_LENGTH:
                    // 长度
                    field.setAccessible(true);
                    if (field.get(columnMode) != null) columnMap.put("columnLength", (Integer) field.get(columnMode));
                    break;
                case COLUMN_DECIMAL:
                    // 小数点(<=COLUMN_LENGTH)
                    field.setAccessible(true);
                    if (field.get(columnMode) != null) columnMap.put("columnDecimal", (Integer) field.get(columnMode));
                    break;
                case COLUMN_IS_NULLABLE:
                    // 是否为空
                    Boolean columnIsNotNullable = false;
                    field.setAccessible(true);
                    if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        columnIsNotNullable = (Boolean) field.get(columnMode);
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        Integer isNull = (Integer) field.get(columnMode);
                        if (null != isNull) {
                            columnIsNotNullable = isNull == 1;
                        }
                    }
                    columnMap.put("columnIsNotNullable", columnIsNotNullable);
                    break;
                case COLUMN_DEFAULT_VALUE:
                    // 默认值
                    field.setAccessible(true);
                    if (field.get(columnMode) != null) columnMap.put("columnDefaultValue", (String) field.get(columnMode));
                    break;
                case COLUMN_IS_KEY:
                    // 是否为主键
                    Boolean columnIsKey = false;
                    field.setAccessible(true);
                    if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        columnIsKey = (Boolean) field.get(columnMode);
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        if (field.get(columnMode) != null) columnIsKey = ((Integer) field.get(columnMode)) == 1;
                    }
                    columnMap.put("columnIsKey", columnIsKey);
                    break;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // 如果字段为主键，则设置其不为空（All parts of a PRIMARY KEY must be NOT NULL; if you need NULL in a key, use UNIQUE instead.）
        if (Boolean.TRUE.equals(columnMap.get("columnIsKey"))) {
            columnMap.put("columnIsNotNullable", true);
        }
        return columnMap;
    }

}
