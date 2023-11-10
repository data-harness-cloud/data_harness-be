package supie.common.core.upload;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FilenameUtils;
import supie.common.core.constant.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import supie.common.core.util.MyCommonUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 存储本地文件的上传下载实现类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Component
public class LocalUpDownloader extends BaseUpDownloader {

    @Autowired
    private UpDownloaderFactory factory;

    @PostConstruct
    public void doRegister() {
        factory.registerUpDownloader(UploadStoreTypeEnum.LOCAL_SYSTEM, this);
    }

    /**
     * 执行下载操作，从本地文件系统读取数据，并将读取的数据直接写入到HttpServletResponse应答对象。
     *
     * @param rootBaseDir 文件下载的根目录。
     * @param modelName   所在数据表的实体对象名。
     * @param fieldName   关联字段的实体对象属性名。
     * @param fileName    文件名。
     * @param asImage     是否为图片对象。图片是无需权限验证的，因此和附件存放在不同的子目录。
     * @param response    Http 应答对象。
     */
    @Override
    public void doDownload(
            String rootBaseDir,
            String modelName,
            String fieldName,
            String fileName,
            Boolean asImage,
            HttpServletResponse response) {
        String uploadPath = makeFullPath(rootBaseDir, modelName, fieldName, asImage);
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
        if (StringUtils.isNotBlank(rootBaseDir)) {
            pathBuilder.append(rootBaseDir);
        }
        if (!uriPath.startsWith("/")) {
            pathBuilder.append("/");
        }
        String fullFileanme = pathBuilder.append(uriPath).append("/").append(fileName).toString();
        this.downloadInternal(fullFileanme, fileName, response);
    }

    private void downloadInternal(String fullFileanme, String fileName, HttpServletResponse response) {
        File file = new File(fullFileanme);
        if (!file.exists()) {
            log.warn("Download file [" + fullFileanme + "] failed, no file found!");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[2048];
        try (OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, i);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            log.error("Failed to call LocalUpDownloader.doDownload", e);
        }
    }

    /**
     * 执行文件上传操作，并存入本地文件系统，再将与该文件下载对应的Url直接写入到HttpServletResponse应答对象，返回给前端。
     *
     * @param serviceContextPath 微服务的上下文路径，如: /admin/upms。
     * @param rootBaseDir        存放上传文件的根目录。
     * @param modelName          所在数据表的实体对象名。
     * @param fieldName          关联字段的实体对象属性名。
     * @param uploadFile         Http请求中上传的文件对象。
     * @param asImage            是否为图片对象。图片是无需权限验证的，因此和附件存放在不同的子目录。
     * @return 存储在本地上传文件名。
     * @throws IOException 文件操作错误。
     */
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
        String uploadPath = makeFullPath(rootBaseDir, modelName, fieldName, asImage);
        responseInfo.setUploadPath(uploadPath);
        fillUploadResponseInfo(responseInfo, serviceContextPath, uploadFile.getOriginalFilename());
        try {
            byte[] bytes = uploadFile.getBytes();
            StringBuilder sb = new StringBuilder(256);
            sb.append(uploadPath).append("/").append(responseInfo.getFilename());
            Path path = Paths.get(sb.toString());
            // 如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(uploadPath));
            }
            // 文件写入指定路径
            Files.write(path, bytes);
        } catch (IOException e) {
            log.error("Failed to write uploaded file [" + uploadFile.getOriginalFilename() + " ].", e);
            responseInfo.setUploadFailed(true);
            responseInfo.setErrorMessage(ErrorCodeEnum.INVALID_UPLOAD_FILE_IOERROR.getErrorMessage());
            return responseInfo;
        }
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
        String uploadPath = makeFullPath(rootBaseDir, modelName, fieldName, asImage);
        responseInfo.setUploadPath(uploadPath);
//        fillUploadResponseInfo(responseInfo, serviceContextPath, originalFilename);
        StringBuilder filenameBuilder = new StringBuilder(64);
        filenameBuilder.append(MyCommonUtil.generateUuid())
                .append(".").append(FilenameUtils.getExtension(originalFilename));
//        responseInfo.setDownloadUri(downloadUri);
        responseInfo.setFilename(filenameBuilder.toString());
        try {
            byte[] bytes = fileInputStreamToByteArray(uploadFileInputStream);
            StringBuilder sb = new StringBuilder(256);
            sb.append(uploadPath).append("/").append(responseInfo.getFilename());
            Path path = Paths.get(sb.toString());
            // 如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(uploadPath));
            }
            // 文件写入指定路径
            Files.write(path, bytes);
        } catch (IOException e) {
            log.error("Failed to write uploaded file [" + originalFilename + " ].", e);
            responseInfo.setUploadFailed(true);
            responseInfo.setErrorMessage(ErrorCodeEnum.INVALID_UPLOAD_FILE_IOERROR.getErrorMessage());
            return responseInfo;
        }
        return responseInfo;
    }
    public static byte[] fileInputStreamToByteArray(FileInputStream fileInputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] buffer = new byte[1024];
        int bytesRead;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
    public static byte[] fileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断filename参数指定的文件名，是否被包含在fileInfoJson参数中。
     *
     * @param fileInfoJson 内部类UploadFileInfo的JSONArray数组。
     * @param filename     被包含的文件名。
     * @return 存在返回true，否则false。
     */
    public static boolean containFile(String fileInfoJson, String filename) {
        if (StringUtils.isAnyBlank(fileInfoJson, filename)) {
            return false;
        }
        List<UploadResponseInfo> fileInfoList = JSON.parseArray(fileInfoJson, UploadResponseInfo.class);
        if (CollectionUtils.isNotEmpty(fileInfoList)) {
            for (UploadResponseInfo fileInfo : fileInfoList) {
                if (StringUtils.equals(filename, fileInfo.getFilename())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String obtainTheConfigurationFileContent(String rootBaseDir, String modelName, String fieldName, String fileName, Boolean asImage) throws IOException {
        String uploadPath = makeFullPath(rootBaseDir, modelName, fieldName, asImage);
        String fullFileanme = uploadPath + "/" + fileName;
        return  fullFileanme;
    }
}
