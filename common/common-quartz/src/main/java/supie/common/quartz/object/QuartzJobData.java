package supie.common.quartz.object;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询Quartz Job时返回的数据对象。
 *
 * @author zw
 * @date 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuartzJobData extends QuartzJobParam {

    /**
     * trigger 名称。
     */
    private String triggerName;
    /**
     * trigger 分组。
     */
    private String triggerGroup;
    /**
     * trigger 状态。
     */
    private String state;
}
