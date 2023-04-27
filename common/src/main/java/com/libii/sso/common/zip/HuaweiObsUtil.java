package com.libii.sso.common.zip;

import com.obs.services.ObsClient;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author: fengchenxin
 * @ClassName: HuaweiObsServiceImpl
 * @date: 2023-03-02  10:16
 * @Description: TODO
 * @version: 1.0
 */

@Configuration
@Slf4j
public class HuaweiObsUtil {

    @Value("${huawei.obs.access-key}")
    private String accessKey;

    @Value("${huawei.obs.secret-key}")
    private String secretKey;

    @Value("${huawei.obs.endpoint}")
    private String endPoint;

    @Value("${huawei.obs.bucket}")
    private String bucket;

    @Value("${huawei.obs.server}")
    private String server;

    private ObsClient obsClient;

    /**
     * 获取ObsClient对象
     * @return
     */
    @PostConstruct
    public void initObsClient(){
        obsClient = new ObsClient(accessKey, secretKey, endPoint);
    }

    public void uploadJson(String key , String jsonStr){
        uploadFileByFlow(key,new ByteArrayInputStream(jsonStr.getBytes()));
    }

    /**
     * 新增文件上传
     */
    private void uploadFile(String path, File file){
        PutObjectResult putObjectResult = obsClient.putObject(bucket, path, file);
        log.debug("新增上传：" + putObjectResult.getObjectUrl());
    }

    /**
     * 新增字节数组上传
     * @param path
     * @param in
     * @throws IOException
     */
    private void uploadFileByFlow(String path, InputStream in){
        PutObjectResult putObjectResult = obsClient.putObject(bucket, path, in);
        log.debug("新增上传：" + putObjectResult.getObjectUrl());
    }

    /**
     * 删除文件
     * @param key 删除key
     * @throws IOException
     */
    public void deleteFile(String key){
        DeleteObjectResult result = obsClient.deleteObject(bucket, key);
        log.debug("删除成功：" + result.getObjectKey());
    }

    /**
     * 删除文件
     * @param sourceKey 源key
     * @param targetKey 目标key
     * @throws IOException
     */
    public void copyFile(String sourceKey, String targetKey) {
        obsClient.copyObject(bucket,sourceKey,bucket,targetKey);
    }


    /**
     * 获取文件
     * @param path
     * @throws IOException
     */
    public String getFileStr(String path){
        ObsObject object = obsClient.getObject(bucket, path);
        InputStream objectContent = object.getObjectContent();
        return getStream(objectContent);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (null != obsClient) {
            obsClient.close();
        }
    }

    public String getServer() {
        return server;
    }

    private String getStream(InputStream in){
        InputStreamReader isr = null;
        try {
            //将字节流转化成字符流，并指定字符集
            isr = new InputStreamReader(in,"UTF-8");
            //将字符流以缓存的形式一行一行输出
            BufferedReader bf = new BufferedReader(isr);
            String results = "";
            String newLine = "";
            while((newLine = bf.readLine()) != null){
                results += newLine+"\n";
            }
            return results;

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            // 流关闭
            if( isr != null){
                try {
                    isr .close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }

    /**
     * 解压zip包并上传到CDN
     * @param srcFile
     * @param basePath
     * @throws RuntimeException
     */
    public void unZipToCDN(File srcFile, String basePath) throws RuntimeException {
        long start = System.currentTimeMillis();
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile, Charset.forName("gbk"));
            Enumeration<?> entries = zipFile.entries();
            ExecutorService es = Executors.newFixedThreadPool(4);
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                } else {
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    String name = basePath + entry.getName();
                    es.execute(new Work(obsClient, bucket, name, is));
                }
            }
            es.shutdown();
            // awaitTermination返回false即超时会继续循环，返回true即线程池中的线程执行完成主线程跳出循环往下执行，每隔5秒循环一次
            while (!es.awaitTermination(5, TimeUnit.SECONDS)){
                // 超时等待后，可以手动结束所有正常执行的线程。不执行下面的语句将循环等待，直到所有子线程结束。
                // es.shutdownNow();
            }
            log.info("所有子线程执行完毕了。。。");
            long end = System.currentTimeMillis();
            log.info("解压并上传CDN完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if (obsClient != null){
                try {
                    obsClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(zipFile != null){
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close(){
        try {
            obsClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
