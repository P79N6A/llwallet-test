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
public class ConvertShort {
	public static short toShort(String value) {
		return Short.parseShort(value);
	}

	public static short toShort(Date value) {
		return (short) value.getTime();
	}

	public static short toShort(char value) {
		return (short) value;
	}

	public static short toShort(short value) {
		return value;
	}

	public static short toShort(boolean value) {
		if (value)
			return 1;
		else
			return 0;
	}

	public static short toShort(float value) {
		return (short) value;
	}

	public static short toShort(double value) {
		return (short) value;
	}

	public static short toShort(byte value) {
		return value;
	}

	public static short toShort(int value) {
		return (short) value;
	}

	public static short toShort(long value) {
		return (short) value;
	}

}
