package com.libii.sso.s3.service.impl;

import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.s3.service.ObsOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author: fengchenxin
 * @ClassName: AmazonS3OperationServiceImpl
 * @date: 2023-03-02  10:17
 * @Description: TODO
 * @version: 1.0
 */
@Configuration
@ConditionalOnProperty(name = "local-server.s3server-type", havingValue="Amazon")
@Slf4j
public class AmazonS3OperationServiceImpl implements ObsOperationService {

    @Value("${amazon.aws.access-key-id}")
    private String accessKeyId;
    @Value("${amazon.aws.secret-access-key}")
    private String secretAccessKey;
    @Value("${amazon.s3.default-bucket}")
    private String bucketName;
    @Value("${amazon.s3.server}")
    private String server;

    private S3Client s3;

    @PostConstruct
    public void initObsClient(){
        //初始化AWS密钥
        System.setProperty("aws.accessKeyId",accessKeyId);
        System.setProperty("aws.secretAccessKey",secretAccessKey);
        //从默认AWS区域加载验证密钥
        SystemPropertyCredentialsProvider credentialsProvider = SystemPropertyCredentialsProvider.create();

//        AwsCredentialsProviderChain credentialsProvider = AwsCredentialsProviderChain
//                .builder()
//                .addCredentialsProvider(new AwsCredentialsProvider() {
//                    @Override
//                    public AwsCredentials resolveCredentials() {
//                        return AwsBasicCredentials.create(accessKeyId, secretAccessKey);
//                    }
//                }).build();
        //默认地区
        Region region = Region.US_EAST_2;
        //初始化s3client
        s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public S3Presigner getS3PreSigner() {
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .endpointOverride(URI.create(server))
                .serviceConfiguration(S3Configuration.builder()
                        .checksumValidationEnabled(false)
                        .pathStyleAccessEnabled(true)
                        .build())
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }

    @Override
    public void uploadJson(String key , String jsonStr){
        uploadFileByByte(key,jsonStr.getBytes());
    }

    /**
     * 上传文件
     */
    private void uploadFileByByte(String path, byte[] file) {
        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();
            log.warn("S3上传请求初始化");
            PutObjectResponse putObjectResponse = s3.putObject(putOb, RequestBody.fromBytes(file));
            log.debug("新增上传：" + path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultCode.AWS_PUT_ERROR);
        }
    }

    /**
     * 删除文件
     */
    @Override
    public void deleteFile(String path) {
        try {
            DeleteObjectRequest delOb = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();
            log.warn("S3删除请求初始化");
            DeleteObjectResponse deleteObjectResponse = s3.deleteObject(delOb);
            log.debug("删除成功：" + path);
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultCode.AWS_DELETE_ERROR);
        }
    }

    /**
     * 复制文件
     */
    @Override
    public void copyFile(String sourcePath,String targetPath) {
        try {
            CopyObjectRequest copyObj = CopyObjectRequest.builder()
                    .sourceBucket(bucketName)
                    .sourceKey(sourcePath)
                    .destinationBucket(bucketName)
                    .destinationKey(targetPath)
                    .build();
            log.warn("S3复制请求初始化");
            CopyObjectResponse copyResponse = s3.copyObject(copyObj);
            log.debug("复制完成");
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultCode.AWS_COPY_ERROR);
        }
    }

    @Override
    public String getFileStr(String key) {
        try {
            ResponseBytes<GetObjectResponse> responseResponseBytes = s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build(),
                    ResponseTransformer.toBytes());
            return this.decode(responseResponseBytes.asByteBuffer());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void unZipToCDN(File srcFile, String basePath) throws RuntimeException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(srcFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryFileName = entry.getName();
                    // 拼接 S3 对象键（key）
                    String s3Key = basePath + entryFileName;
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    // 上传到 S3
                    byte[] content = bos.toByteArray();
                    ByteArrayInputStream bis = new ByteArrayInputStream(content);
                    PutObjectRequest request = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .build();
                    s3.putObject(request, RequestBody.fromInputStream(bis, content.length));
                }
                zis.closeEntry();
            }
        } catch (IOException | S3Exception e) {
            // 处理异常
            throw new CustomException("上传 ZIP 文件到 S3 失败: " + e.getMessage(), e);
        }
    }
//    public void unZipToCDN(File srcFile, String basePath) throws RuntimeException {
//        try (
//            InputStream zipInputStream = new FileInputStream(srcFile);
//            ZipInputStream zis = new ZipInputStream(zipInputStream)
//        ) {
//            ZipEntry entry;
//            while ((entry = zis.getNextEntry()) != null) {
//                if (!entry.isDirectory()) {
//                    String entryFileName = entry.getName();
//                    // 拼接 S3 对象键（key）
//                    String s3Key = basePath + entryFileName;
//                    Path tempFile = Files.createTempFile("temp", ".tmp");
//                    try (OutputStream os = new FileOutputStream(tempFile.toFile())) {
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//                        while ((bytesRead = zis.read(buffer)) != -1) {
//                            os.write(buffer, 0, bytesRead);
//                        }
//                        // 上传到 S3
//                        PutObjectRequest request = PutObjectRequest.builder()
//                                .bucket(bucketName)
//                                .key(s3Key)
//                                .build();
//                        s3.putObject(request, RequestBody.fromFile(tempFile));
//                    } finally {
//                        Files.delete(tempFile);
//                    }
//                }
//                zis.closeEntry();
//            }
//        } catch (IOException | S3Exception e) {
//            // 处理异常
//            throw new CustomException("上传 ZIP 文件到 S3 失败: " + e.getMessage(), e);
//        }
//    }

    private String decode(ByteBuffer byteBuffer) {
        Charset charset = StandardCharsets.UTF_8;
        return charset.decode(byteBuffer).toString();
    }

    @Override
    public String getServer() {
        return server;
    }

    @Override
    public void close(){
        try {
            if(null!=s3){
                s3.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
