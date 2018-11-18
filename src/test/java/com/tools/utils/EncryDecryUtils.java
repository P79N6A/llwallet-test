package com.tools.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.CipherOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class EncryDecryUtils {

	/**
	 * 加密文件密钥，生产时提供
	 */
	static final String threeDesKey = "132e8a57b4f6139b3a5de9g4";

	/**
	 * 文件夹下文件加密
	 * 
	 * @param resourceDirPath
	 *            需要加密的文件夹目录
	 * @param zipDirPath
	 *            加密后的文件路径
	 * @param fileZipName
	 *            加密后的文件名
	 * @throws IOException
	 * @author FENG_ZH
	 * @author CAO_Z
	 * @sinece 2017-2-10 下午2:00:26
	 */
	public static void makeZip(String resourceDirPath, String zipDirPath, String fileZipName) throws IOException {
		File resourceFile = new File(resourceDirPath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		FileInputStream inputStream = null;
		CipherOutputStream cipherOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			for (File file : resourceFile.listFiles()) {
				ZipEntry zipEntry = new ZipEntry(file.getName());
				zipOutputStream.putNextEntry(zipEntry);
				inputStream = new FileInputStream(file);
				IOUtils.copy(inputStream, zipOutputStream);
				inputStream.close();
			}
			zipOutputStream.finish();
			zipOutputStream.close();

			byteArrayOutputStream = new ByteArrayOutputStream();
			cipherOutputStream = TripleDesTool.encryptMode(threeDesKey, byteArrayOutputStream);
			byte[] b = outputStream.toByteArray();
			cipherOutputStream.write(Base64.encodeBase64(b));
			cipherOutputStream.flush();
			cipherOutputStream.close();
			byte[] bs = byteArrayOutputStream.toByteArray();
			IOUtils.write(Base64.encodeBase64(bs), new FileOutputStream(new File(zipDirPath, fileZipName)));
			outputStream.close();
		} finally {
			if (null != cipherOutputStream) {
				cipherOutputStream.close();
			}
			if (null != byteArrayOutputStream) {
				byteArrayOutputStream.close();
			}
			if (null != inputStream) {
				inputStream.close();
			}
			if (null != zipOutputStream) {
				zipOutputStream.close();
			}
			if (null != outputStream) {
				outputStream.close();
			}
		}
	}

	/**
	 * 加密文件解密
	 * 
	 * @param zipAllFilePath
	 *            加密压缩文件解密
	 * @param unZipFilePath
	 *            解压的文件路径
	 * @throws IOException
	 * @author FENG_ZH
	 * @author CAO_Z
	 * @sinece 2017-2-10 下午1:52:07
	 */
	public static void unZip(String zipAllFilePath, String unZipFilePath) throws IOException {

		FileInputStream fileInputStream = null;
		BufferedInputStream bis = null;
		InputStream decryptInputStream = null;
		ZipInputStream zipInputStream = null;
		FileOutputStream fileOutputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			File file = new File(zipAllFilePath);
			fileInputStream = new FileInputStream(file);
			byte[] bs = IOUtils.toByteArray(fileInputStream);
			bis = new BufferedInputStream(new ByteArrayInputStream(Base64.decodeBase64(bs)));
			decryptInputStream = TripleDesTool.decryptMode(threeDesKey, bis);
			bs = IOUtils.toByteArray(decryptInputStream);
			bs = Base64.decodeBase64(bs);
			byteArrayInputStream = new ByteArrayInputStream(bs);
			zipInputStream = new ZipInputStream(byteArrayInputStream);
			for (ZipEntry archiveEntry = zipInputStream
					.getNextEntry(); archiveEntry != null; archiveEntry = zipInputStream.getNextEntry()) {
				String entryFileName = archiveEntry.getName();
				file = new File(unZipFilePath, entryFileName);
				fileOutputStream = new FileOutputStream(file);
				IOUtils.copy(zipInputStream, fileOutputStream);
				fileOutputStream.close();
			}
			zipInputStream.close();
		} finally {
			if (null != fileOutputStream) {
				fileOutputStream.close();
			}
			if (null != zipInputStream) {
				zipInputStream.close();
			}
			if (null != decryptInputStream) {
				decryptInputStream.close();
			}
			if (byteArrayInputStream != null) {
				byteArrayInputStream.close();
			}
			if (null != bis) {
				bis.close();
			}
			if (null != fileInputStream) {
				fileInputStream.close();
			}
		}
	}
}