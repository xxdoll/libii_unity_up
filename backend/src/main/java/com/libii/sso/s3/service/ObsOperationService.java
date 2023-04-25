package com.libii.sso.s3.service;

import java.io.File;


public interface ObsOperationService {

    void initObsClient();

    /**
     * @description 上传文件到S3服务器,key唯一,否则会变成覆盖相同key的内容
     * @param key  上传key
     * @param jsonStr  上传json字符串
     * @author fengchenxin
     * @date 2023/3/2 11:28
     */
    void uploadJson(String key , String jsonStr);

    /**
     * @description   删除文件
     * @param key   文件key
     * @author fengchenxin
     * @date 2023/3/2 11:29
     */
    void deleteFile(String key);

    /**
     * @description   复制文件
     * @param sourcePath  源文件key
     * @param targetPath  目标key
     * @author fengchenxin
     * @date 2023/3/2 11:29
     */
    void copyFile(String sourcePath, String targetPath);

    /**
     * @description  获取文件内容
     * @param path   文件key
     * @return java.lang.String
     * @author fengchenxin
     * @date 2023/3/2 11:30
     */
    String getFileStr(String path);

    /**
     * @description   获取server路径配置
     * @author fengchenxin
     * @date 2023/3/2 11:31
     */
    String getServer();

    /**
     * 解压zip包并上传到CDN
     * @param srcFile
     * @param basePath
     * @throws RuntimeException
     */
    void unZipToCDN(File srcFile, String basePath) throws RuntimeException;

    /**
     * @description   关闭客户端实例
     * @author fengchenxin
     * @date 2023/3/2 11:31
     */
    void close();
}
