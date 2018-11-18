package com.llwallet.interfaces.test.api.test;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.tools.utils.EncryDecryUtils;

public class UnzipTest {

	/**
	 * 方法描述
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		String zipAllFilePath="E:\\Work\\2018Q2\\消费通照片传输定时任务\\test3\\IMGDOC0001_LLE_20180411_00000001.zip";
		String unZipFilePath="E:\\Work\\2018Q2\\消费通照片传输定时任务\\test3\\1\\";
		try {
			EncryDecryUtils.unZip(zipAllFilePath, unZipFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}