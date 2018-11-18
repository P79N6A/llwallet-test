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
public class ConvertFloat {
	public static float toFloat(String value) {
		return Float.parseFloat(value);
	}

	public static float toFloat(Date value) {
		return value.getTime();
	}

	public static float toFloat(char value) {
		return value;
	}

	public static float toFloat(short value) {
		return value;
	}

	public static float toFloat(boolean value) {
		if (value)
			return 1;
		else
			return 0;
	}

	public static float toFloat(float value) {
		return value;
	}

	public static float toFloat(double value) {
		return (float) value;
	}

	public static float toFloat(byte value) {
		return value;
	}

	public static float toFloat(int value) {
		return value;
	}

	public static float toFloat(long value) {
		return value;
	}

}
