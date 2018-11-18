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
public class ConvertChar {

	public static char toChar(String value) {
		return value.toCharArray()[0];
	}

	public static char toChar(Date value) throws Exception {
		throw new Exception("");
	}

	public static char toChar(char value) {
		return value;
	}

	public static char toChar(short value) throws Exception {
		if (value < 0)
			throw new Exception("");
		else
			return (char) value;
	}

	public static char toChar(boolean value) {
		if (value)
			return '1';
		else
			return '0';
	}

	public static char toChar(float value) {
		return (char) value;
	}

	public static char toChar(double value) {
		return (char) value;
	}

	public static char toChar(byte value) {
		return (char) value;
	}

	public static char toChar(int value) {
		return (char) value;
	}

	public static char toChar(long value) {
		return (char) value;
	}

}
