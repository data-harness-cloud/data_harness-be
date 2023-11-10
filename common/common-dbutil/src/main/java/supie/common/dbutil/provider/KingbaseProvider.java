package supie.common.dbutil.provider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import supie.common.core.constant.ObjectFieldType;
import supie.common.dbutil.constant.DblinkType;

/**
 * 人大金仓数据源的提供者实现类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class KingbaseProvider extends DamengProvider {

    @Override
    public int getDblinkType() {
        return DblinkType.KINGBASE;
    }

    @Override
    public JdbcConfig getJdbcConfig(String configuration) {
        return JSON.parseObject(configuration, KingbaseConfig.class);
    }

    @Override
    public String convertColumnTypeToJavaType(String columnType, Integer numericPrecision, Integer numericScale) {
        if (StrUtil.equalsAnyIgnoreCase(columnType, "VARCHAR", "CHAR", "TEXT", "CLOB")) {
            return ObjectFieldType.STRING;
        }
        if (StrUtil.equalsAnyIgnoreCase(columnType, "FLOAT4", "FLOAT8")) {
            return ObjectFieldType.DOUBLE;
        }
        if (StrUtil.equalsAnyIgnoreCase(columnType, "DATE", "TIMESTAMP", "TIME")) {
            return ObjectFieldType.DATE;
        }
        if (StrUtil.equalsAnyIgnoreCase(columnType, "INT", "INTEGER", "INT4", "INT2", "TINYINT")) {
            return ObjectFieldType.INTEGER;
        }
        if (StrUtil.equalsAnyIgnoreCase(columnType, "INT8", "BIGINT")) {
            return ObjectFieldType.LONG;
        }
        if (StrUtil.equalsAnyIgnoreCase(columnType, "BIT", "BOOL", "BOOLEAN")) {
            return ObjectFieldType.BOOLEAN;
        }
        if (StrUtil.equalsIgnoreCase(columnType, "BLOB")) {
            return ObjectFieldType.BYTE_ARRAY;
        }
        if (!"NUMERIC".equalsIgnoreCase(columnType)) {
            return null;
        }
        if (numericScale != null && numericScale > 0) {
            return ObjectFieldType.DOUBLE;
        }
        if (numericPrecision == null) {
            return ObjectFieldType.LONG;
        }
        return numericPrecision > 10 ? ObjectFieldType.LONG : ObjectFieldType.INTEGER;
    }
}
