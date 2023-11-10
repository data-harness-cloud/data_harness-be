package supie.common.quartz.object;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/24 10:55
 * @path SDT-supie.common.quartz.object-JobFieldType
 */
public enum JobFieldType {

    JOB_NAME("任务名称"),
    JOB_GROUP("任务分组"),
    DESCRIPTION("任务描述"),
    JOB_CLASS_NAME("作业执行类"),
    CRON_EXPRESSION("Cron表达式"),
    START_TIME("起始时间"),
    END_TIME("结束时间"),
    JOB_DATA_MAP("Job的参数数据"),
    STATE("状态");

    /**
     * 枚举常量描述
     */
    private String enumerateConstantDescriptions;

    JobFieldType(String enumerateConstantDescriptions) {
        this.enumerateConstantDescriptions = enumerateConstantDescriptions;
    }

    public String getFieldDescription() {
        return enumerateConstantDescriptions;
    }
}
