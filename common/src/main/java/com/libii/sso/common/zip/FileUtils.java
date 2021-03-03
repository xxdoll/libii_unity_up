package com.libii.sso.common.zip;

import com.libii.sso.common.exception.CustomException;
import com.libii.sso.common.restResult.ResultCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtils {

	public static List<ZipModel> unzip(File file) {
		// 判断文件是否为zip文件
		String filename = file.getName();
		if (!filename.endsWith("zip")) {
			throw new RuntimeException("传入文件格式错误" + filename);
		}
		List<ZipModel> fileModelList = new ArrayList<>();
		String zipFileName = null;
		// 对文件进行解析
		try {

			// 获取每个文件的正确编码
//			ZipInputStream zis = new ZipInputStream(in, Charset.forName("utf-8"));
			ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
//			BufferedInputStream bis = new BufferedInputStream(zis);
			ZipEntry zipEntry = null;
			byte[] bytes = null;
			// 获取zip包中的每一个zip file entry
			while ((zipEntry = zis.getNextEntry()) != null) {
				if(zipEntry.isDirectory()){
					continue;
				}
				System.out.println("Entry size: " + zipEntry.getSize());
				System.out.println("Compressed size: " + zipEntry.getCompressedSize());
				System.out.println("CRC: " + zipEntry.getCrc());
				zipFileName = zipEntry.getName();
				ZipModel fileModel = new ZipModel();
				fileModel.setFileName(zipFileName);
				long size = zipEntry.getSize();
				// 解决 zipEntry 没有 size 的问题
				if (size == -1){
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while (true) {
						int bytes2 = zis.read();
						if (bytes2 == -1) {
							break;
						}
						baos.write(bytes2);
					}
					baos.close();
					bytes = baos.toByteArray();
					size = bytes.length;
				}else{
					bytes = new byte[(int) size];
				}
				zis.read(bytes, 0, (int) size);
				zis.closeEntry();
				InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
				fileModel.setFileInputstream(byteArrayInputStream);
				boolean directory = zipEntry.isDirectory();
				fileModel.setFileType(directory ? "dir" : "file");
				fileModelList.add(fileModel);
			}
			zis.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("读取部署包文件内容失败,请确认部署包格式正确:" + zipFileName);
		}
		return fileModelList;
	}

	/**
	 * 将InputStream写入本地文件
	 * @param dest 写入本地目录
	 * @param in	输入流
	 * @throws IOException
	 */
	public static void writeToLocal(String dest, InputStream in)
			throws IOException {
		int index;
		byte[] bytes = new byte[1024];
		System.out.println("writeToLocal: "+dest);
		File file = new File(dest);
		if (!file.exists()){
			file.getParentFile().mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(dest);
		while ((index = in.read(bytes)) != -1) {
			fos.write(bytes, 0, index);
			fos.flush();
		}
		fos.close();
		in.close();
	}

	public static String convertSize(long size) {
		//定义GB的计算常量
		int GB = 1024 * 1024 * 1024;
		//定义MB的计算常量
		int MB = 1024 * 1024;
		//定义KB的计算常量
		int KB = 1024;
		//格式化小数
		DecimalFormat df = new DecimalFormat("0.00");
		String resultSize = "";
		if (size / GB >= 1) {
			//如果当前Byte的值大于等于1GB
			resultSize = df.format(size / (float) GB) + "GB";
		} else if (size / MB >= 1) {
			//如果当前Byte的值大于等于1MB
			resultSize = df.format(size / (float) MB) + "MB";
		} else if (size / KB >= 1) {
			//如果当前Byte的值大于等于1KB
			resultSize = df.format(size / (float) KB) + "KB";
		} else {
			resultSize = size + "B";
		}
		return resultSize;
	}



	public static void main(String[] args) throws IOException {
		System.out.println(convertSize(55000));
		String path = "C:\\Users\\admin\\Pictures\\壁纸\\a.png";
		FileInputStream fis= new FileInputStream(path);
		byte[] bytes1 = IOUtils.toByteArray(fis);
		String md52 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
		System.out.println("md52: "+md52);
		String md51 = getFileMd5(bytes1);
		System.out.println("md51: "+md51);
	}

	/**
	 * 获取文件的md5码
	 * @param bytes
	 * @return
	 */
	public static String getFileMd5(byte[] bytes) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			throw new CustomException(ResultCode.FILE_MD5_FAIL);
		}
		byte[] digest = md5.digest(bytes);
		return new BigInteger(1, digest).toString(16);
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}
}
