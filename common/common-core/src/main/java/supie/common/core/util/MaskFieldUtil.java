package supie.common.core.util;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 脱敏的工具类。具体实现的源码基本来自hutool的DesensitizedUtil，
 * 只是因为我们需要支持自定义脱敏字符，因此需要重写hutool中的工具类方法。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public class MaskFieldUtil {

    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**。
     *
     * @param fullName 姓名。
     * @param maskChar 遮掩字符。
     * @return 脱敏后的姓名。
     */
    public static String chineseName(String fullName, char maskChar) {
        if (StrUtil.isBlank(fullName)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.replace(fullName, 1, fullName.length(), maskChar);
    }

    /**
     * 【身份证号】前1位 和后2位。
     *
     * @param idCardNum 身份证。
     * @param front     保留：前面的front位数；从1开始。
     * @param end       保留：后面的end位数；从1开始。
     * @param maskChar  遮掩字符。
     * @return 脱敏后的身份证。
     */
    public static String idCardNum(String idCardNum, int front, int end, char maskChar) {
        return noMaskPrefixAndSuffix(idCardNum, front, end, maskChar);
    }

    /**
     * 字符串的前front位和后end位的字符，不会被脱敏。
     *
     * @param str      原字符串。
     * @param front    保留：前面的front位数；从1开始。
     * @param end      保留：后面的end位数；从1开始。
     * @param maskChar 遮掩字符。
     * @return 脱敏后的结果字符串。
     */
    public static String noMaskPrefixAndSuffix(String str, int front, int end, char maskChar) {
        //身份证不能为空
        if (StrUtil.isBlank(str)) {
            return StrUtil.EMPTY;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > str.length()) {
            return StrUtil.EMPTY;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return StrUtil.EMPTY;
        }
        return StrUtil.replace(str, front, str.length() - end, maskChar);
    }

    /**
     * 【固定电话 前四位，后两位。
     *
     * @param num      固定电话。
     * @param maskChar 遮掩字符。
     * @return 脱敏后的固定电话。
     */
    public static String fixedPhone(String num, char maskChar) {
        if (StrUtil.isBlank(num)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.replace(num, 4, num.length() - 2, maskChar);
    }

    /**
     * 【手机号码】前三位，后4位，其他隐藏，比如135****2210。
     *
     * @param num      移动电话。
     * @param maskChar 遮掩字符。
     * @return 脱敏后的移动电话。
     */
    public static String mobilePhone(String num, char maskChar) {
        if (StrUtil.isBlank(num)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.replace(num, 3, num.length() - 4, maskChar);
    }

    /**
     * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****。
     *
     * @param address       家庭住址。
     * @param sensitiveSize 敏感信息长度。
     * @param maskChar      遮掩字符。
     * @return 脱敏后的家庭地址。
     */
    public static String address(String address, int sensitiveSize, char maskChar) {
        if (StrUtil.isBlank(address)) {
            return StrUtil.EMPTY;
        }
        int length = address.length();
        return StrUtil.replace(address, length - sensitiveSize, length, maskChar);
    }

    /**
     * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com。
     *
     * @param email    邮箱。
     * @param maskChar 遮掩字符。
     * @return 脱敏后的邮箱。
     */
    public static String email(String email, char maskChar) {
        if (StrUtil.isBlank(email)) {
            return StrUtil.EMPTY;
        }
        int index = StrUtil.indexOf(email, '@');
        if (index <= 1) {
            return email;
        }
        return StrUtil.replace(email, 1, index, maskChar);
    }

    /**
     * 【密码】密码的全部字符都用*代替，比如：******。
     *
     * @param password 密码。
     * @return 脱敏后的密码。
     */
    public static String password(String password) {
        if (StrUtil.isBlank(password)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.repeat('*', password.length());
    }

    /**
     * 【中国车牌】车牌中间用*代替。
     * eg1：null       -》 ""
     * eg1：""         -》 ""
     * eg3：苏D40000   -》 苏D4***0
     * eg4：陕A12345D  -》 陕A1****D
     * eg5：京A123     -》 京A123     如果是错误的车牌，不处理。
     *
     * @param carLicense 完整的车牌号。
     * @param maskChar   遮掩字符。
     * @return 脱敏后的车牌。
     */
    public static String carLicense(String carLicense, char maskChar) {
        if (StrUtil.isBlank(carLicense)) {
            return StrUtil.EMPTY;
        }
        // 普通车牌
        if (carLicense.length() == 7) {
            carLicense = StrUtil.replace(carLicense, 3, 6, maskChar);
        } else if (carLicense.length() == 8) {
            // 新能源车牌
            carLicense = StrUtil.replace(carLicense, 3, 7, maskChar);
        }
        return carLicense;
    }

    /**
     * 银行卡号脱敏。
     * eg: 1101 **** **** **** 3256。
     *
     * @param bankCardNo 银行卡号。
     * @param maskChar   遮掩字符。
     * @return 脱敏之后的银行卡号。
     */
    public static String bankCard(String bankCardNo, char maskChar) {
        if (StrUtil.isBlank(bankCardNo)) {
            return bankCardNo;
        }
        bankCardNo = StrUtil.trim(bankCardNo);
        if (bankCardNo.length() < 9) {
            return bankCardNo;
        }
        final int length = bankCardNo.length();
        final int midLength = length - 8;
        final StringBuilder buf = new StringBuilder();
        buf.append(bankCardNo, 0, 4);
        for (int i = 0; i < midLength; ++i) {
            if (i % 4 == 0) {
                buf.append(CharUtil.SPACE);
            }
            buf.append(maskChar);
        }
        buf.append(CharUtil.SPACE).append(bankCardNo, length - 4, length);
        return buf.toString();
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private MaskFieldUtil() {
    }
}
