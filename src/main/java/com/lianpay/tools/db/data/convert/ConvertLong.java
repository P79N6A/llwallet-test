/*
 * 创建日期 2007-1-19
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.lianpay.tools.db.data.convert;

import java.util.Date;

/**
 * @author Administrator
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ConvertLong {
	public static long toLong(String value) {
		return Long.parseLong(value);
	}

	public static long toLong(Date value) {
		return value.getTime();
	}

	public static long toLong(char value) {
		return value;
	}

	public static long toLong(short value) {
		return value;
	}

	public static long toLong(boolean value) {
		if (value)
			return 1;
		else
			return 0;
	}

	public static long toLong(float value) {
		return (long) value;
	}

	public static long toLong(double value) {
		return (long) value;
	}

	public static long toLong(byte value) {
		return value;
	}

	public static long toLong(int value) throws Exception {
		return value;
	}

	public static long toLong(long value) throws Exception {
		return value;
	}
}