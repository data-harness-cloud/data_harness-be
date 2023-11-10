package supie.common.core.constant;

/**
 * 字段脱敏类型枚举。。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
public enum MaskFieldTypeEnum {

    /**
     * 自定义实现。
     */
    CUSTOM,
    /**
     * 姓名。
     */
    NAME,
    /**
     * 移动电话。
     */
    MOBILE_PHONE,
    /**
     * 座机电话。
     */
    FIXED_PHONE,
    /**
     * 身份证。
     */
    ID_CARD,
    /**
     * 银行卡号。
     */
    BANK_CARD,
    /**
     * 汽车牌照号。
     */
    CAR_LICENSE,
    /**
     * 邮件。
     */
    EMAIL,
    /**
     * 固定长度的前缀和后缀不被掩码。
     */
    NO_MASK_PREFIX_SUFFIX,
}
