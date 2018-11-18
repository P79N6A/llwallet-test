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
public class ConvertBoolean {

	public static boolean toBoolean(String value) {
		if (value.trim().equals("1"))
			return true;
		else if (value.trim().equals("0"))
			return false;
		else if (value.trim().toLowerCase().equals("t"))
			return true;
		else if (value.trim().toLowerCase().equals("f"))
			return false;
		else
			return Boolean.getBoolean(value.trim());

	}

	public static boolean toBoolean(Date value) throws Exception {
		throw new Exception("从Date类型转换到Boolean类型出错");
	}

	public static boolean toBoolean(char value) {
		if (value == 1)
			return true;
		else
			return false;

	}

	public static boolean toBoolean(short value) {
		if (value == 1)
			return true;
		else
			return false;

	}

	public static boolean toBoolean(boolean value) {
		return value;
	}

	public static boolean toBoolean(float value) throws Exception {
		if (value == 1)
			return true;
		else if (value == 0)
			return false;
		else
			throw new Exception("从float类型转换到Boolean类型出错");

	}

	public static boolean toBoolean(double value) throws Exception {
		if (value == 1)
			return true;
		else if (value == 0)
			return false;
		else
			throw new Exception("从double类型转换到Boolean类型出错");

	}

	public static boolean toBoolean(byte value) throws Exception {
		if (value == 1)
			return true;
		else if (value == 0)
			return false;
		else
			throw new Exception("从byte类型转换到Boolean类型出错");

	}

	public static boolean toBoolean(int value) throws Exception {
		if (value == 1)
			return true;
		else if (value == 0)
			return false;
		else
			throw new Exception("从int类型转换到Boolean类型出错");

	}

	public static boolean toBoolean(long value) throws Exception {
		if (value == 1)
			return true;
		else if (value == 0)
			return false;
		else
			throw new Exception("从long类型转换到Boolean类型出错");

	}

}