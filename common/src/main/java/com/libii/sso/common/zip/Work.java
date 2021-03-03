package com.libii.sso.common.zip;

import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.restResult.ResultCode;
import com.libii.sso.common.util.LocalCdnUtil;
import com.obs.services.ObsClient;
import lombok.Data;

import java.io.InputStream;

/**
 * @author lirong
 * @ClassName: Work
 * @Description: 上传文件
 * @date 2020-03-17 14:19
 */
public class Work implements Runnable {

    private String fileName;
    private String bucket;
    private InputStream in;
    private ObsClient obsClient;
    public Work(ObsClient obsClient, String bucket, String fileName, InputStream in){
        this.obsClient = obsClient;
        this.bucket = bucket;
        this.fileName = fileName;
        this.in = in;
    }

    @Override
    public void run() {
        try {
            obsClient.putObject(bucket, fileName, in);
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }
}
