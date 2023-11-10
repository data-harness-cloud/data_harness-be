package supie.common.quartz.utils;

import cn.hutool.json.JSONUtil;
import supie.common.quartz.object.JobFieldType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class QuaryzUtilTest {

    public static void main(String[] args) {
        JobFieldType jobFieldType = JobFieldType.JOB_CLASS_NAME;
        switch (jobFieldType) {
            case JOB_NAME:
                System.out.println(jobFieldType.getFieldDescription());
                break;
            case JOB_GROUP:
                System.out.println(jobFieldType.getFieldDescription());
                break;
            case JOB_CLASS_NAME:
                System.out.println(jobFieldType.getFieldDescription());
                break;
            case JOB_DATA_MAP:
                System.out.println(jobFieldType.getFieldDescription());
                break;
            case DESCRIPTION:
                System.out.println(jobFieldType.getFieldDescription());
                break;
            case CRON_EXPRESSION:
                System.out.println(jobFieldType.getFieldDescription());
                break;
        }
    }
  
}