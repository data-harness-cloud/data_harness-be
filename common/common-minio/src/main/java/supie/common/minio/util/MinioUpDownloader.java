package supie.common.minio.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.BooleanUtil;
import org.apache.commons.io.FilenameUtils;
import supie.common.core.upload.UpDownloaderFactory;
import supie.common.core.upload.UploadResponseInfo;
import supie.common.core.upload.BaseUpDownloader;
import supie.common.core.upload.UploadStoreTypeEnum;
import supie.common.core.util.MyCommonUtil;
import supie.common.minio.wrapper.MinioTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 基于Minio上传和下载文件操作的工具类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "minio", name = "enabled")
public class MinioUpDownloader extends BaseUpDownloader {

    @Autowired
    private MinioTemplate minioTemplate;
    @Autowired
    private UpDownloaderFactory factory;

    @PostConstruct
    public void doRegister() {
        factory.registerUpDownloader(UploadStoreTypeEnum.MINIO_SYSTEM, this);
    }

    @Override
    public UploadResponseInfo doUpload(
            String serviceContextPath,
            String rootBaseDir,
            String modelName,
            String fieldName,
            Boolean asImage,
            MultipartFile uploadFile) throws IOException {
        UploadResponseInfo responseInfo = super.verifyUploadArgument(asImage, uploadFile);
        if (BooleanUtil.isTrue(responseInfo.getUploadFailed())) {
            return responseInfo;
        }
        String uploadPath = super.makeFullPath(null, modelName, fieldName, asImage);
        responseInfo.setUploadPath(uploadPath);
        super.fillUploadResponseInfo(responseInfo, serviceContextPath, uploadFile.getOriginalFilename());
        minioTemplate.putObject(uploadPath + "/" + responseInfo.getFilename(), uploadFile.getInputStream());
        return responseInfo;
    }
    @Override
    public UploadResponseInfo doUpload(
            String serviceContextPath,
            String rootBaseDir,
            String modelName,
            String fieldName,
            Boolean asImage,
            FileInputStream uploadFileInputStream,
            String originalFilename) throws IOException {
        UploadResponseInfo responseInfo = super.verifyUploadArgument(asImage, uploadFileInputStream);
        if (BooleanUtil.isTrue(responseInfo.getUploadFailed())) {
            return responseInfo;
        }
        String uploadPath = super.makeFullPath(null, modelName, fieldName, asImage);
        responseInfo.setUploadPath(uploadPath);
        StringBuilder filenameBuilder = new StringBuilder(64);
        filenameBuilder.append(MyCommonUtil.generateUuid())
                .append(".").append(FilenameUtils.getExtension(originalFilename));
        responseInfo.setFilename(filenameBuilder.toString());
        minioTemplate.putObject(uploadPath + "/" + responseInfo.getFilename(), uploadFileInputStream);
        uploadFileInputStream.close();
        return responseInfo;
    }



    @Override
    public void doDownload(
            String rootBaseDir,
            String modelName,
            String fieldName,
            String fileName,
            Boolean asImage,
            HttpServletResponse response) throws IOException {
        String uploadPath = this.makeFullPath(null, modelName, fieldName, asImage);
        String fullFileanme = uploadPath + "/" + fileName;
        this.downloadInternal(fullFileanme, fileName, response);
    }

    @Override
    public void doDownload(
            String rootBaseDir,
            String uriPath,
            String fileName,
            HttpServletResponse response) throws IOException {
        StringBuilder pathBuilder = new StringBuilder(128);
        String fullFileanme = pathBuilder.append(uriPath).append("/").append(fileName).toString();
        this.downloadInternal(fullFileanme, fileName, response);
    }

    private void downloadInternal(String fullFileanme, String fileName, HttpServletResponse response) throws IOException {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        InputStream in = minioTemplate.getStream(fullFileanme);
        IoUtil.copy(in, response.getOutputStream());
        in.close();
    }


    @Override
    public String obtainTheConfigurationFileContent(String rootBaseDir, String modelName, String fieldName, String fileName, Boolean asImage) throws IOException {
        String uploadPath = this.makeFullPath(null, modelName, fieldName, asImage);
        String fullFileanme = uploadPath + "/" + fileName;
        return fullFileanme;
    }
}
