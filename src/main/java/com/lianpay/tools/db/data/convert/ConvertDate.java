/*
 * 创建日期 2007-1-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.lianpay.tools.db.data.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ConvertDate {
	public static Date toDateTime(String value) throws Exception {
		return toDateTime(value, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date toDateTime(String value, String formatString)
			throws Exception {
		Date dt = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try {
			dt = sdf.parse(value);
		} catch (Exception e) {
			throw e;
		}
		return dt;
	}

	public static Date toDateTime(Date value) throws Exception {
		return value;
	}

	public static Date toDateTime(char value) throws Exception {
		throw new Exception("从 char 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(short value) throws Exception {
		return new Date(value);
	}

	public static Date toDateTime(boolean value) throws Exception {
		throw new Exception("从 boolean 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(float value) throws Exception {
		return new Date((long) value);
	}

	public static Date toDateTime(double value) throws Exception {
		return new Date((long) value);
	}

	public static Date toDateTime(byte value) throws Exception {
		throw new Exception("从 byte 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(int value) throws Exception {
		return new Date((long) value);
	}

}