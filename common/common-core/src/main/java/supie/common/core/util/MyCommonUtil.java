package supie.common.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import supie.common.core.constant.AppDeviceType;
import supie.common.core.constant.ApplicationConstant;
import supie.common.core.validator.AddGroup;
import supie.common.core.validator.UpdateGroup;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 脚手架中常用的基本工具方法集合，一般而言工程内部使用的方法。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class MyCommonUtil {

    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 创建uuid。
     *
     * @return 返回uuid。
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 对用户密码进行加盐后加密。
     *
     * @param password     明文密码。
     * @param passwordSalt 盐值。
     * @return 加密后的密码。
     */
    public static String encrptedPassword(String password, String passwordSalt) {
        return DigestUtil.md5Hex(password + passwordSalt);
    }

    /**
     * 这个方法一般用于Controller对于入口参数的基本验证。
     * 对于字符串，如果为空字符串，也将视为Blank，同时返回true。
     *
     * @param objs 一组参数。
     * @return 返回是否存在null或空字符串的参数。
     */
    public static boolean existBlankArgument(Object...objs) {
        for (Object obj : objs) {
            if (MyCommonUtil.isBlankOrNull(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结果和 existBlankArgument 相反。
     *
     * @param objs 一组参数。
     * @return 返回是否存在null或空字符串的参数。
     */
    public static boolean existNotBlankArgument(Object...objs) {
        for (Object obj : objs) {
            if (!MyCommonUtil.isBlankOrNull(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证参数是否为空。
     *
     * @param obj 待判断的参数。
     * @return 空或者null返回true，否则false。
     */
    public static boolean isBlankOrNull(Object obj) {
        if (obj instanceof Collection) {
            return CollUtil.isEmpty((Collection<?>) obj);
        }
        return obj == null || (obj instanceof CharSequence && StrUtil.isBlank((CharSequence) obj));
    }

    /**
     * 验证参数是否为非空。
     *
     * @param obj 待判断的参数。
     * @return 空或者null返回false，否则true。
     */
    public static boolean isNotBlankOrNull(Object obj) {
        return !isBlankOrNull(obj);
    }

    /**
     * 判断source是否等于其中任何一个对象值。
     *
     * @param source 源对象。
     * @param others 其他对象。
     * @return 等于其中任何一个返回true，否则false。
     */
    public static boolean equalsAny(Object source, Object...others) {
        for (Object one : others) {
            if (ObjectUtil.equal(source, one)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param model  带校验的model。
     * @param groups Validate绑定的校验组。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(T model, Class<?>...groups) {
        if (model != null) {
            Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(model, groups);
            if (!constraintViolations.isEmpty()) {
                Iterator<ConstraintViolation<T>> it = constraintViolations.iterator();
                ConstraintViolation<T> constraint = it.next();
                return constraint.getMessage();
            }
        }
        return null;
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param model     带校验的model。
     * @param forUpdate 是否为更新。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(T model, boolean forUpdate) {
        if (model != null) {
            Set<ConstraintViolation<T>> constraintViolations;
            if (forUpdate) {
                constraintViolations = VALIDATOR.validate(model, Default.class, UpdateGroup.class);
            } else {
                constraintViolations = VALIDATOR.validate(model, Default.class, AddGroup.class);
            }
            if (!constraintViolations.isEmpty()) {
                Iterator<ConstraintViolation<T>> it = constraintViolations.iterator();
                ConstraintViolation<T> constraint = it.next();
                return constraint.getMessage();
            }
        }
        return null;
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param modelList 带校验的model列表。
     * @param groups    Validate绑定的校验组。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(List<T> modelList, Class<?>... groups) {
        if (CollUtil.isNotEmpty(modelList)) {
            for (T model : modelList) {
                String errorMessage = getModelValidationError(model, groups);
                if (StrUtil.isNotBlank(errorMessage)) {
                    return errorMessage;
                }
            }
        }
        return null;
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param modelList 带校验的model列表。
     * @param forUpdate 是否为更新。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(List<T> modelList, boolean forUpdate) {
        if (CollUtil.isNotEmpty(modelList)) {
            for (T model : modelList) {
                String errorMessage = getModelValidationError(model, forUpdate);
                if (StrUtil.isNotBlank(errorMessage)) {
                    return errorMessage;
                }
            }
        }
        return null;
    }

    /**
     * 拼接参数中的字符串列表，用指定分隔符进行分割，同时每个字符串对象用单引号括起来。
     *
     * @param dataList  字符串集合。
     * @param separator 分隔符。
     * @return 拼接后的字符串。
     */
    public static String joinString(Collection<String> dataList, final char separator) {
        int index = 0;
        StringBuilder sb = new StringBuilder(128);
        for (String data : dataList) {
            sb.append("'").append(data).append("'");
            if (index++ != dataList.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * 将SQL Like中的通配符替换为字符本身的含义，以便于比较。
     *
     * @param str 待替换的字符串。
     * @return 替换后的字符串。
     */
    public static String replaceSqlWildcard(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        return StrUtil.replaceChars(StrUtil.replaceChars(str, "_", "\\_"), "%", "\\%");
    }

    /**
     * 获取对象中，非空字段的名字列表。
     *
     * @param object 数据对象。
     * @param clazz  数据对象的class类型。
     * @param <T>    数据对象类型。
     * @return 数据对象中，值不为NULL的字段数组。
     */
    public static <T> String[] getNotNullFieldNames(T object, Class<T> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        List<String> fieldNameList = Arrays.stream(fields)
                .filter(f -> ReflectUtil.getFieldValue(object, f) != null)
                .map(Field::getName).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(fieldNameList)) {
            return fieldNameList.toArray(new String[]{});
        }
        return new String[]{};
    }

    /**
     * 获取请求头中的设备信息。
     *
     * @return 设备类型，具体值可参考AppDeviceType常量类。
     */
    public static int getDeviceType() {
        // 缺省都按照Web登录方式设置，如果前端header中的值为不合法值，这里也不会报错，而是使用Web缺省方式。
        int deviceType = AppDeviceType.WEB;
        String deviceTypeString = ContextUtil.getHttpRequest().getHeader("deviceType");
        if (StrUtil.isNotBlank(deviceTypeString)) {
            Integer type = Integer.valueOf(deviceTypeString);
            if (AppDeviceType.isValid(type)) {
                deviceType = type;
            }
        }
        return deviceType;
    }

    /**
     * 按(User-Agent)获取设备类型
     *
     * @return 字符串
     * @author 王立宏
     * @date 2023/11/22 02:34
     */
    public static String getDeviceTypeByUserAgent() {
        String userAgent = ContextUtil.getHttpRequest().getHeader("User-Agent");
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.contains("android")) {
                return "Android";
            } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
                return "IOS";
            } else if (userAgent.contains("windows phone")) {
                return "Windows Phone";
            } else if (userAgent.contains("windows")) {
                return "Windows";
            } else {
                return "Unknown device type";
            }
        } else {
            return "Unknown device type";
        }
    }

    /**
     * 获取客户端 IP 地址
     *
     * @return IP 地址
     * @author 王立宏
     * @date 2023/11/22 02:32
     */
    public static String getClientIpAddress() {
        String ipAddress = ContextUtil.getHttpRequest().getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("HTTP_X_FORWARDED");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("HTTP_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("HTTP_FORWARDED");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("HTTP_VIA");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getHeader("REMOTE_ADDR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = ContextUtil.getHttpRequest().getRemoteAddr();
        }
        // 如果 IP 地址包含多个值，则提取第一个值
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }

    /**
     * 转换为字典格式的数据列表。
     *
     * @param dataList   源数据列表。
     * @param idGetter   获取字典Id字段值的函数方法。
     * @param nameGetter 获取字典名字段值的函数方法。
     * @param <M>        源数据对象类型。
     * @param <R>        字典Id的类型。
     * @return 字典格式的数据列表。
     */
    public static <M, R> List<Map<String, Object>> toDictDataList(
            Collection<M> dataList, Function<M, R> idGetter, Function<M, String> nameGetter) {
        if (CollUtil.isEmpty(dataList)) {
            return new LinkedList<>();
        }
        return dataList.stream().map(item -> {
            Map<String, Object> dataMap = new HashMap<>(2);
            dataMap.put(ApplicationConstant.DICT_ID, idGetter.apply(item));
            dataMap.put(ApplicationConstant.DICT_NAME, nameGetter.apply(item));
            return dataMap;
        }).collect(Collectors.toList());
    }

    /**
     * 转换为树形字典格式的数据列表。
     *
     * @param dataList       源数据列表。
     * @param idGetter       获取字典Id字段值的函数方法。
     * @param nameGetter     获取字典名字段值的函数方法。
     * @param parentIdGetter 获取字典Id父字段值的函数方法。
     * @param <M>            源数据对象类型。
     * @param <R>            字典Id的类型。
     * @return 字典格式的数据列表。
     */
    public static <M, R> List<Map<String, Object>> toDictDataList(
            Collection<M> dataList,
            Function<M, R> idGetter,
            Function<M, String> nameGetter,
            Function<M, R> parentIdGetter) {
        if (CollUtil.isEmpty(dataList)) {
            return new LinkedList<>();
        }
        return dataList.stream().map(item -> {
            Map<String, Object> dataMap = new HashMap<>(2);
            dataMap.put(ApplicationConstant.DICT_ID, idGetter.apply(item));
            dataMap.put(ApplicationConstant.DICT_NAME, nameGetter.apply(item));
            dataMap.put(ApplicationConstant.PARENT_ID, parentIdGetter.apply(item));
            return dataMap;
        }).collect(Collectors.toList());
    }

    /**
     * 转换为字典格式的数据列表，同时支持一个附加字段。
     *
     * @param dataList    源数据列表。
     * @param idGetter    获取字典Id字段值的函数方法。
     * @param nameGetter  获取字典名字段值的函数方法。
     * @param extraName   附加字段名。。
     * @param extraGetter 获取附加字段值的函数方法。
     * @param <M>         源数据对象类型。
     * @param <R>         字典Id的类型。
     * @param <E>         附加字段值的类型。
     * @return 字典格式的数据列表。
     */
    public static <M, R, E> List<Map<String, Object>> toDictDataList(
            Collection<M> dataList,
            Function<M, R> idGetter,
            Function<M, String> nameGetter,
            String extraName,
            Function<M, E> extraGetter) {
        if (CollUtil.isEmpty(dataList)) {
            return new LinkedList<>();
        }
        return dataList.stream().map(item -> {
            Map<String, Object> dataMap = new HashMap<>(2);
            dataMap.put(ApplicationConstant.DICT_ID, idGetter.apply(item));
            dataMap.put(ApplicationConstant.DICT_NAME, nameGetter.apply(item));
            dataMap.put(extraName, extraGetter.apply(item));
            return dataMap;
        }).collect(Collectors.toList());
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private MyCommonUtil() {
    }
}
