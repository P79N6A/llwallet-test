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
public class ConvertByte {
	public static byte toByte(String value) {
		return 0;
	}

	public static byte toByte(Date value) throws Exception {
		throw new Exception("");
	}

	public static byte toByte(char value) {
		return (byte) value;
	}

	public static byte toByte(short value) throws Exception {
		if (value < 0 || value > 127)
			throw new Exception("");
		else
			return (byte) value;
	}

	public static byte toByte(boolean value) {
		if (value)
			return 1;
		else
			return 0;
	}

	public static byte toByte(float value) throws Exception {
		if (value < 0 || value > 127)
			throw new Exception("");
		else
			return (byte) value;
	}

	public static byte toByte(double value) throws Exception {
		if (value < 0 || value > 127)
			throw new Exception("");
		else
			return (byte) value;
	}

	public static byte toByte(byte value) {
		return value;
	}

	public static byte toByte(int value) throws Exception {
		if (value < 0 || value > 127)
			throw new Exception("");
		else
			return (byte) value;
	}

	public static byte toByte(long value) throws Exception {
		if (value < 0 || value > 127)
			throw new Exception("");
		else
			return (byte) value;
	}
}
