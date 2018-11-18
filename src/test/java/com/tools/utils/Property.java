package com.tools.utils;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author 
 */

public class Property {

	/**
	 * 
	 * 
	 */
	public static void set() {
		try {
//			Properties p = new Properties();
			Properties p = new Properties(System.getProperties());
			p.load(new FileInputStream(
					"src\\test\\resources\\properties\\combination.properties"));
			p.load(new FileInputStream(
					"src\\test\\resources\\properties\\llwallet.properties"));
			p.load(new FileInputStream(
					"src/test/resources/properties/llwallet_personal.properties"));
			p.load(new FileInputStream(
					"src/test/resources/properties/llwallet_enterprise.properties"));
			p.load(new FileInputStream(
					"src/test/resources/properties/llwallet_custom.properties"));
			p.load(new FileInputStream(
					"src/test/resources/properties/spay.properties"));
			p.load(new FileInputStream(
					"src/test/resources/properties/aggregate_sdk_server.properties"));
			p.load(new FileInputStream(
					"src/test/resources/properties/datasource.properties"));
			p.load(new FileInputStream(
					"src/test/resources/properties/llwalletactive.properties"));
			System.setProperties(p);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	/**
	 * 
	 * @param key
	 * 
	 * @return 获取的名字为key的环境变量的值
	 */
	public static String get(String key) {
		String result = "";
		try {
			String tmp1 = System.getProperty(key);
			if (tmp1 == null || tmp1.equals("")) {
				return null;
			}
			byte[] temp2 = tmp1.getBytes("ISO-8859-1");
			result = new String(temp2, "utf-8");
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
		}
		return result;
	}
}