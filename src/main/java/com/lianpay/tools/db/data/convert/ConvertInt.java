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
public class ConvertInt {

	public static int toInt(String value) {
		return Integer.parseInt(value);
	}

	public static int toInt(Date value) {
		return (int) value.getTime();
	}

	public static int toInt(char value) {
		return value;
	}

	public static int toInt(short value) {
		return value;
	}

	public static int toInt(boolean value) {
		if (value)
			return 1;
		else
			return 0;
	}

	public static int toInt(float value) {
		return (int) value;
	}

	public static int toInt(double value) {
		return (int) value;
	}

	public static int toInt(byte value) {
		return value;
	}

	public static int toInt(int value) {
		return value;
	}

	public static int toInt(long value) {
		return (int) value;
	}

}
