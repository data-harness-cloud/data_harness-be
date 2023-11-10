package supie.common.core.annotation;

import supie.common.core.upload.UploadStoreTypeEnum;

import java.lang.annotation.*;

/**
 * 用于标记支持数据上传和下载的字段。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadFlagColumn {

    /**
     * 上传数据存储类型。
     *
     * @return 上传数据存储类型。
     */
    UploadStoreTypeEnum storeType();
}
