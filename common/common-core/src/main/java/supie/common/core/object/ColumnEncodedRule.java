package supie.common.core.object;

import lombok.Data;

/**
 * 编码字段的编码规则。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class ColumnEncodedRule {

    /**
     * 是否显示是计算并回显。
     */
    private Boolean calculateWhenView;

    /**
     * 前缀。
     */
    private String prefix;

    /**
     * 精确到DAYS/HOURS/MINUTES/SECONDS
     */
    private String precisionTo;

    /**
     * 中缀。
     */
    private String middle;

    /**
     * 流水序号的字符宽度，不足的前面补0。
     */
    private Integer idWidth;
}
