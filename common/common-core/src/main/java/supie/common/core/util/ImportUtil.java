package supie.common.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import supie.common.core.annotation.RelationConstDict;
import supie.common.core.annotation.RelationDict;
import supie.common.core.annotation.RelationGlobalDict;
import supie.common.core.base.service.BaseService;
import supie.common.core.exception.MyRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 导入工具类，目前支持xlsx和xls两种类型。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class ImportUtil {

    /**
     * 根据实体类的Class类型，生成导入的头信息。
     *
     * @param modelClazz   实体对象的Class类型。
     * @param ignoreFields 忽略的字段名集合，如创建时间、创建人、更新时间、更新人等。
     * @param <T> 实体对象类型。
     * @return 创建后的导入头信息列表。
     */
    public static <T> List<ImportHeaderInfo> makeHeaderInfoList(Class<T> modelClazz, Set<String> ignoreFields) {
        List<ImportHeaderInfo> resultList = new LinkedList<>();
        Field[] fields = ReflectUtil.getFields(modelClazz);
        int index = 0;
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            // transient类型的字段不能作为查询条件，静态字段和逻辑删除都不考虑。需要忽略的字段也要跳过。
            int transientMask = 128;
            if ((modifiers & transientMask) == 1
                    || Modifier.isStatic(modifiers)
                    || field.getAnnotation(TableId.class) != null
                    || field.getAnnotation(TableLogic.class) != null
                    || CollUtil.contains(ignoreFields, field.getName())) {
                continue;
            }
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField == null || tableField.exist()) {
                ImportHeaderInfo headerInfo = new ImportHeaderInfo();
                headerInfo.fieldName = field.getName();
                headerInfo.index = index++;
                makeHeaderInfoFieldTypeByField(field, headerInfo);
                resultList.add(headerInfo);
            }
        }
        return resultList;
    }

    /**
     * 保存导入文件。
     *
     * @param baseDir    导入文件本地缓存的根目录。
     * @param subDir     导入文件本地缓存的子目录。
     * @param importFile 导入的文件。
     * @return 保存的本地文件名。
     */
    public static String saveImportFile(
            String baseDir, String subDir, MultipartFile importFile) throws IOException {
        StringBuilder sb = new StringBuilder(256);
        sb.append(baseDir);
        if (!StrUtil.endWith(baseDir, "/")) {
            sb.append("/");
        }
        sb.append("importedFile/");
        if (StrUtil.isNotBlank(subDir)) {
            sb.append(subDir);
            if (!StrUtil.endWith(subDir, "/")) {
                sb.append("/");
            }
        }
        String pathname = sb.toString();
        sb.append(new DateTime().toString("yyyy-MM-dd-HH-mm-"));
        sb.append(MyCommonUtil.generateUuid())
                .append(".").append(FileNameUtil.getSuffix(importFile.getOriginalFilename()));
        String fullname = sb.toString();
        try {
            byte[] bytes = importFile.getBytes();
            Path path = Paths.get(fullname);
            // 如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(pathname));
            }
            // 文件写入指定路径
            Files.write(path, bytes);
        } catch (IOException e) {
            log.error("Failed to write imported file [" + importFile.getOriginalFilename() + " ].", e);
            throw e;
        }
        return fullname;
    }

    /**
     * 导入指定的excel，基于SAX方式解析后返回数据列表。
     *
     * @param headers    头信息数组。
     * @param skipHeader 是否跳过第一行，通常改行为头信息。
     * @param filename   文件名。
     * @return 解析后数据列表。
     */
    public static List<Map<String, Object>> doImport(
            ImportHeaderInfo[] headers, boolean skipHeader, String filename) {
        Assert.notNull(headers);
        Assert.isTrue(StrUtil.isNotBlank(filename));
        List<Map<String, Object>> resultList = new LinkedList<>();
        ExcelUtil.readBySax(new File(filename), 0, createRowHandler(headers, skipHeader, resultList));
        return resultList;
    }

    /**
     * 导入指定的excel，基于SAX方式解析后返回Bean类型的数据列表。
     *
     * @param headers               头信息数组。
     * @param skipHeader            是否跳过第一行，通常改行为头信息。
     * @param filename              文件名。
     * @param clazz                 Bean的Class类型。
     * @param translateDictFieldSet 需要进行反向翻译的字典字段集合。
     * @return 解析后数据列表。
     */
    public static <T> List<T> doImport(
            ImportHeaderInfo[] headers,
            boolean skipHeader,
            String filename,
            Class<T> clazz,
            Set<String> translateDictFieldSet) {
        // 这里将需要进行字典反向翻译的字段类型改为String，否则使用原有的字典Id类型时，无法正确执行下面的doImport方法。
        if (CollUtil.isNotEmpty(translateDictFieldSet)) {
            for (ImportHeaderInfo header : headers) {
                if (translateDictFieldSet.contains(header.fieldName)) {
                    header.fieldType = STRING_TYPE;
                }
            }
        }
        List<Map<String, Object>> resultList = doImport(headers, skipHeader, filename);
        if (CollUtil.isNotEmpty(translateDictFieldSet)) {
            translateDictFieldSet.forEach(c -> doTranslateDict(resultList, clazz, c));
        }
        return MyModelUtil.mapToBeanList(resultList, clazz);
    }

    /**
     * 转换数据列表中，需要进行反向字典翻译的字段。
     *
     * @param dataList   数据列表。
     * @param modelClass 对象模型。
     * @param fieldName  需要进行字典反向翻译的字段名。注意，该字段为需要翻译替换的Java字段名，与此同时，
     *                   该字段 + DictMap后缀的字段名，必须被RelationConstDict和RelationDict注解标记。
     */
    @SuppressWarnings("unchecked")
    public static void doTranslateDict(List<Map<String, Object>> dataList, Class<?> modelClass, String fieldName) {
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        Field field = ReflectUtil.getField(modelClass, fieldName + "DictMap");
        Assert.notNull(field);
        Map<String, Object> inversedDictMap;
        if (field.isAnnotationPresent(RelationConstDict.class)) {
            RelationConstDict r = field.getAnnotation(RelationConstDict.class);
            Field f = ReflectUtil.getField(r.constantDictClass(), "DICT_MAP");
            Map<Object, String> dictMap = (Map<Object, String>) ReflectUtil.getStaticFieldValue(f);
            inversedDictMap = MapUtil.inverse(dictMap);
        } else if (field.isAnnotationPresent(RelationDict.class)) {
            RelationDict r = field.getAnnotation(RelationDict.class);
            String slaveServiceName = r.slaveServiceName();
            if (StrUtil.isBlank(slaveServiceName)) {
                slaveServiceName = r.slaveModelClass().getSimpleName() + "Service";
            }
            BaseService<Object, Serializable> service =
                    ApplicationContextHolder.getBean(StrUtil.lowerFirst(slaveServiceName));
            List<Object> dictDataList = service.getAllList();
            List<Map<String, Object>> dataMapList = MyModelUtil.beanToMapList(dictDataList);
            inversedDictMap = new HashMap<>(dataMapList.size());
            dataMapList.forEach(d ->
                    inversedDictMap.put(d.get(r.slaveNameField()).toString(), d.get(r.slaveIdField())));
        } else if (field.isAnnotationPresent(RelationGlobalDict.class)) {
            RelationGlobalDict r = field.getAnnotation(RelationGlobalDict.class);
            BaseService<Object, Serializable> s = ApplicationContextHolder.getBean("globalDictService");
            Method m = ReflectUtil.getMethodByName(s.getClass(), "getGlobalDictItemDictMapFromCache");
            Map<Object, String> dictMap = ReflectUtil.invoke(s, m, r.dictCode(), null);
            inversedDictMap = MapUtil.inverse(dictMap);
        } else {
            throw new UnsupportedOperationException("Only Support RelationConstDict and RelationDict Field");
        }
        if (MapUtil.isEmpty(inversedDictMap)) {
            log.warn("Dict Data List is EMPTY.");
            return;
        }
        for (Map<String, Object> data : dataList) {
            Object value = data.get(fieldName);
            if (value != null) {
                Object newValue = inversedDictMap.get(value.toString());
                if (newValue != null) {
                    data.put(fieldName, newValue);
                }
            }
        }
    }

    private static void makeHeaderInfoFieldTypeByField(Field field, ImportHeaderInfo headerInfo) {
        if (field.getType().equals(Integer.class)) {
            headerInfo.fieldType = INT_TYPE;
        } else if (field.getType().equals(Long.class)) {
            headerInfo.fieldType = LONG_TYPE;
        } else if (field.getType().equals(String.class)) {
            headerInfo.fieldType = STRING_TYPE;
        } else if (field.getType().equals(Boolean.class)) {
            headerInfo.fieldType = BOOLEAN_TYPE;
        } else if (field.getType().equals(Date.class)) {
            headerInfo.fieldType = DATE_TYPE;
        } else if (field.getType().equals(Double.class)) {
            headerInfo.fieldType = DOUBLE_TYPE;
        } else if (field.getType().equals(Float.class)) {
            headerInfo.fieldType = FLOAT_TYPE;
        } else if (field.getType().equals(BigDecimal.class)) {
            headerInfo.fieldType = BIG_DECIMAL_TYPE;
        } else {
            throw new MyRuntimeException("Unsupport Import FieldType");
        }
    }

    private static RowHandler createRowHandler(
            ImportHeaderInfo[] headers, boolean skipHeader, List<Map<String, Object>> resultList) {
        return new MyRowHandler(headers, skipHeader, resultList);
    }

    public static final int INT_TYPE = 0;
    public static final int LONG_TYPE = 1;
    public static final int STRING_TYPE = 2;
    public static final int BOOLEAN_TYPE = 3;
    public static final int DATE_TYPE = 4;
    public static final int DOUBLE_TYPE = 5;
    public static final int FLOAT_TYPE = 6;
    public static final int BIG_DECIMAL_TYPE = 7;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ImportHeaderInfo {
        /**
         * 对应的Java实体对象属性名。
         */
        private String fieldName;
        /**
         * 对应的Java实体对象类型。
         */
        private Integer fieldType;
        /**
         * 0 表示excel中的第一列。
         */
        private Integer index;
    }

    private static class MyRowHandler implements RowHandler {
        private ImportHeaderInfo[] headers;
        private Map<Integer, ImportHeaderInfo> headerInfoMap;
        private boolean skipHeader;
        private List<Map<String, Object>> resultList;

        public MyRowHandler(ImportHeaderInfo[] headers, boolean skipHeader, List<Map<String, Object>> resultList) {
            this.headers = headers;
            this.skipHeader = skipHeader;
            this.resultList = resultList;
            this.headerInfoMap = Arrays.stream(headers)
                    .collect(Collectors.toMap(ImportHeaderInfo::getIndex, c -> c));
        }

        @Override
        public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
            if (this.skipHeader && rowIndex == 0) {
                return;
            }
            int i = 0;
            Map<String, Object> data = new HashMap<>(headers.length);
            for (Object rowData : rowList) {
                ImportHeaderInfo headerInfo = this.headerInfoMap.get(i++);
                if (headerInfo == null) {
                    continue;
                }
                switch (headerInfo.fieldType) {
                    case INT_TYPE:
                        data.put(headerInfo.fieldName, Convert.toInt(rowData));
                        break;
                    case LONG_TYPE:
                        data.put(headerInfo.fieldName, Convert.toLong(rowData));
                        break;
                    case STRING_TYPE:
                        data.put(headerInfo.fieldName, Convert.toStr(rowData));
                        break;
                    case BOOLEAN_TYPE:
                        data.put(headerInfo.fieldName, Convert.toBool(rowData));
                        break;
                    case DATE_TYPE:
                        data.put(headerInfo.fieldName, Convert.toDate(rowData));
                        break;
                    case DOUBLE_TYPE:
                        data.put(headerInfo.fieldName, Convert.toDouble(rowData));
                        break;
                    case FLOAT_TYPE:
                        data.put(headerInfo.fieldName, Convert.toFloat(rowData));
                        break;
                    case BIG_DECIMAL_TYPE:
                        data.put(headerInfo.fieldName, Convert.toBigDecimal(rowData));
                        break;
                    default:
                        throw new MyRuntimeException(
                                "Invalid ImportHeaderInfo.fieldType [" + headerInfo.fieldType + "].");
                }
            }
            resultList.add(data);
        }
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private ImportUtil() {
    }
}
