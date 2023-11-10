package supie.common.core.annotation;

import java.lang.annotation.*;

/**
 * 业务表中记录流程最后审批状态标记的字段。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowLatestApprovalStatusColumn {

}
