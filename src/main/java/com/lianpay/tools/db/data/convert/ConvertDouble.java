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
public class ConvertDouble {
	public static double toDouble(String value) {
		return Double.parseDouble(value);
	}

	public static double toDouble(Date value) {
		return value.getTime();
	}

	public static double toDouble(char value) {
		return value;
	}

	public static double toDouble(short value) {
		return value;
	}

	public static double toDouble(boolean value) {
		if (value)
			return 1;
		else
			return 0;
	}

	public static double toDouble(float value) {
		return value;
	}

	public static double toDouble(double value) {
		return value;
	}

	public static double toDouble(byte value) {
		return value;
	}

	public static double toDouble(int value) {
		return value;
	}

	public static double toDouble(long value) {
		return value;
	}

}
