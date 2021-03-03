package com.libii.sso.common.util;

import com.obs.services.ObsClient;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
@Component
public class LocalCdnUtil {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("时间"+new Date(start));
		ObsClient obs = new ObsClient("EV6YS2YONZHD3I8EB8NL", "oJ2R07XKzqyOvF75ArURCf35MatchBi6miXF1p5D", "https://obs.cn-east-2.myhuaweicloud.com");
		long middle = System.currentTimeMillis();
		System.out.println("时间"+new Date(middle));
		System.out.println(middle - start);
		File file = new File("C:\\Users\\admin\\OneDrive\\Pictures\\头像\\头像.jpg");
		String path = "test/test4.jpg";
		obs.putObject("lr-test", path, file);
		long end = System.currentTimeMillis();
		System.out.println(end - middle);
		System.out.println("时间"+new Date(end));
	}

	@Value("${cdn.accessKey}")
	private String accessKey;

	@Value("${cdn.secretKey}")
	private String secretKey;

	@Value("${cdn.endPoint}")
	private String endPoint;

	@Value("${cdn.bucket}")
	private String bucket;

	private ObsClient obsClient;

	public LocalCdnUtil() {
	}

	/**
	 * 获取ObsClient对象
	 * @return
	 */
	public ObsClient getObsClient(){
		return new ObsClient(accessKey, secretKey, endPoint);
	}

	/**
	 * 获取bucket
	 * @return
	 */
	public String getBucket(){
		return bucket;
	}
	/**
	 * 新增文件上传
	 */
	public void putFile(String path, File file) throws IOException {
		obsClient = new ObsClient(accessKey, secretKey, endPoint);
		PutObjectResult putObjectResult = obsClient.putObject(bucket, path, file);
		log.debug("新增上传：" + putObjectResult.getObjectUrl());
		obsClient.close();
	}

	/**
	 * 新增字节数组上传
	 * @param path
	 * @param in
	 * @throws IOException
	 */
	public void putFile(String path, InputStream in) throws IOException {
		obsClient = new ObsClient(accessKey, secretKey, endPoint);
		PutObjectResult putObjectResult = obsClient.putObject(bucket, path, in);
		log.debug("新增上传：" + putObjectResult.getObjectUrl());
		obsClient.close();
	}

	/**
	 * 删除文件
	 * @param path
	 * @throws IOException
	 */
	public void deleteFile(String path) throws IOException {
		obsClient = new ObsClient(accessKey, secretKey, endPoint);
		DeleteObjectResult result = obsClient.deleteObject(bucket, path);
		log.debug("删除成功：" + result.getObjectKey());
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (null != obsClient) {
			obsClient.close();
		}
	}
}
