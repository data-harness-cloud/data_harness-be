package supie.common.minio.wrapper;

import supie.common.core.exception.MyRuntimeException;
import supie.common.minio.config.MinioProperties;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 封装的minio客户端模板类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class MinioTemplate {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    private final MinioProperties properties;
    private final MinioClient client;

    public MinioTemplate(MinioProperties properties, MinioClient client) {
        super();
        this.properties = properties;
        this.client = client;
    }

    /**
     * 判断bucket是否存在。
     *
     * @param bucketName 桶名称。
     * @return 存在返回true，否则false。
     */
    public boolean bucketExists(String bucketName) {
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 创建桶。
     *
     * @param bucketName 桶名称。
     */
    public void makeBucket(String bucketName) {
        try {
            if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 存放对象。
     *
     * @param bucketName 桶名称。
     * @param objectName 对象名称。
     * @param filename   本地上传的文件名称。
     */
    public void putObject(String bucketName, String objectName, String filename) {
        try {
            this.putObject(bucketName, objectName, new FileInputStream(filename));
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 存放对象。桶名称为配置中的桶名称。
     *
     * @param objectName 对象名称。
     * @param filename   本地上传的文件名称。
     */
    public void putObject(String objectName, String filename) {
        try {
            this.putObject(properties.getBucketName(), objectName, filename);
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 读取输入流并存放。
     *
     * @param bucketName 桶名称。
     * @param objectName 对象名称。
     * @param stream     读取后上传的文件流。
     */
    public void putObject(String bucketName, String objectName, InputStream stream) {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).build());
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 读取输入流并存放。
     *
     * @param objectName 对象名称。
     * @param stream     读取后上传的文件流。
     */
    public void putObject(String objectName, InputStream stream) {
        this.putObject(properties.getBucketName(), objectName, stream);
    }

    /**
     * 移除对象。
     *
     * @param bucketName 桶名称。
     * @param objectName 对象名称。
     */
    public void removeObject(String bucketName, String objectName) {
        try {
            client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 移除对象。桶名称为配置中的桶名称。
     *
     * @param objectName 对象名称。
     */
    public void removeObject(String objectName) {
        this.removeObject(properties.getBucketName(), objectName);
    }

    /**
     * 获取文件输入流。
     *
     * @param bucket     桶名称。
     * @param objectName 对象名称。
     * @return 文件的输入流。
     */
    public InputStream getStream(String bucket, String objectName) {
        try {
            return client.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 获取文件输入流。
     *
     * @param objectName 对象名称。
     * @return 文件的输入流。
     */
    public InputStream getStream(String objectName) {
        return this.getStream(properties.getBucketName(), objectName);
    }

    /**
     * 获取存储的文件对象。
     *
     * @param bucket     桶名称。
     * @param objectName 对象名称。
     * @return 读取后存储到文件的文件对象。
     */
    public File getFile(String bucket, String objectName) throws IOException {
        InputStream in = getStream(bucket, objectName);
        File dir = new File(TMP_DIR);
        if (!dir.exists() || dir.isFile()) {
            dir.mkdirs();
        }
        File file = new File(TMP_DIR + objectName);
        FileUtils.copyInputStreamToFile(in, file);
        in.close();
        return file;
    }

    /**
     * 获取存储的文件对象。桶名称为配置中的桶名称。
     *
     * @param objectName 对象名称。
     * @return 读取后存储到文件的文件对象。
     */
    public File getFile(String objectName) throws IOException {
        return this.getFile(properties.getBucketName(), objectName);
    }
}
