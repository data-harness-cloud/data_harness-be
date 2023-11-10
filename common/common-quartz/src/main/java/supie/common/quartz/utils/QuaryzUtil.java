package supie.common.quartz.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import supie.common.quartz.object.JobField;
import supie.common.quartz.object.JobFieldType;
import supie.common.quartz.object.QuartzJobData;
import supie.common.quartz.object.QuartzJobParam;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

/**
 * 描述：
 *
 * @author 王立宏
 * @date 2023/10/24 10:53
 * @path SDT-supie.common.quartz.utils-QuaryzUtil
 */
public class QuaryzUtil {

    public static <MDto> QuartzJobParam modelDtoToJobParam(MDto model) {
        QuartzJobParam quartzJobParam = new QuartzJobParam();
        Class<?> modelClass = model.getClass();
        Field[] fields = ReflectUtil.getFields(modelClass);
        for (Field field : fields) {
            Object fieldValue = ReflectUtil.getFieldValue(model, field);
            if (fieldValue == null) continue;
            if (field.isAnnotationPresent(JobField.class)) {
                JobField jobField = field.getAnnotation(JobField.class);
                JobFieldType jobFieldType = jobField.value();
                switch (jobFieldType) {
                    case JOB_NAME:
                        quartzJobParam.setJobName(fieldValue.toString());
                        break;
                    case JOB_GROUP:
                        quartzJobParam.setJobGroup(fieldValue.toString());
                        break;
                    case JOB_CLASS_NAME:
                        quartzJobParam.setJobClassName(fieldValue.toString());
                        break;
                    case JOB_DATA_MAP:
                        quartzJobParam.setJobDataMap(JSONUtil.toBean(fieldValue.toString(), Map.class));
                        break;
                    case DESCRIPTION:
                        quartzJobParam.setDescription(fieldValue.toString());
                        break;
                    case CRON_EXPRESSION:
                        quartzJobParam.setCronExpression(fieldValue.toString());
                        break;
                    case START_TIME:
                        quartzJobParam.setStartTime((Date) fieldValue);
                        break;
                    case END_TIME:
                        quartzJobParam.setEndTime((Date) fieldValue);
                        break;
                }
            }
        }
        return quartzJobParam;
    }

    public static <M> M getModelByJobData(QuartzJobData quartzJobData, Class<M> modelClass) {
        M model = null;
        try {
            model = (M) modelClass.newInstance();
            Field[] fields = modelClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(JobField.class)) {
                    JobField jobField = field.getAnnotation(JobField.class);
                    JobFieldType jobFieldType = jobField.value();
                    switch (jobFieldType) {
                        case JOB_NAME:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getJobName());
//                            field.setAccessible(true);
//                            field.set(model, quartzJobData.getJobName());
                            break;
                        case JOB_GROUP:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getJobGroup());
                            break;
                        case JOB_CLASS_NAME:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getJobClassName());
                            break;
                        case JOB_DATA_MAP:
                            String jsonDataMap = JSONUtil.toJsonStr(quartzJobData.getJobDataMap());
                            ReflectUtil.setFieldValue(model, field, jsonDataMap);
                            break;
                        case DESCRIPTION:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getDescription());
                            break;
                        case CRON_EXPRESSION:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getCronExpression());
                            break;
                        case START_TIME:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getStartTime());
                            break;
                        case END_TIME:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getEndTime());
                            break;
                        case STATE:
                            ReflectUtil.setFieldValue(model, field, quartzJobData.getState());
                            break;
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

}
