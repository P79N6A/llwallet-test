package com.lianpay.tools.db.data;

public class DataTypes {
	public final static int DATATABLE_OBJECT = 0;

	public final static int DATATABLE_STRING = 1;

	public final static int DATATABLE_BOOLEAN = 2;

	public final static int DATATABLE_SHORT = 3;

	public final static int DATATABLE_INT = 4;

	public final static int DATATABLE_LONG = 5;

	public final static int DATATABLE_FLOAT = 6;

	public final static int DATATABLE_DOUBLE = 7;

	public final static int DATATABLE_DATE = 8;

	public final static int DATATABLE_TIME = 9;

	public final static int DATATABLE_TIMESTAMP = 10;

	public final static int DATATABLE_BYTE = 11;

	public final static int DATATABLE_BYTES = 12;

	public final static int DATATABLE_BIGDECIMAL = 13;

	public final static String[] DATATABLE_TYPENAMES = new String[] { "OBJECT",
			"STRING", "BOOLEAN", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE",
			"DATE", "TIME", "TIMESTAMPLE", "BYTE", "BYTES", "BIGDECIMAL" };

	public static boolean checkDataType(int dataType) {
		return (dataType > -1 && dataType < 14);

	}

	public static String getDataTypeName(int dataType) {
		if (checkDataType(dataType))
			return DATATABLE_TYPENAMES[dataType];
		else
			return null;
	}
}
