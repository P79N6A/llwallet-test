package com.lianpay.tools.utils;

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
			// Properties p = new Properties();
			Properties p = new Properties(System.getProperties());
			p.load(new FileInputStream(
					"src/main/resources/properties/env.properties"));
			p.load(new FileInputStream(
					"src/main/resources/properties/datasource.properties"));
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