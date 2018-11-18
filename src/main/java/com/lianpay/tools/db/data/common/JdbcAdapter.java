/*
 * 
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.lianpay.tools.db.data.common;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import com.lianpay.tools.db.data.DataColumn;
import com.lianpay.tools.db.data.DataRow;
import com.lianpay.tools.db.data.DataTable;
import com.lianpay.tools.db.data.DataTypes;

/**
 * @author 
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class JdbcAdapter implements DataAdapter {

	private Map<Integer, Integer> typeMap;

	public JdbcAdapter() {
		typeMap = new HashMap<Integer, Integer>();
		typeMap.put(new Integer(Types.BIGINT), new Integer(
				DataTypes.DATATABLE_LONG));

		typeMap.put(new Integer(Types.BIT), new Integer(
				DataTypes.DATATABLE_BOOLEAN));
		typeMap.put(new Integer(Types.BOOLEAN), new Integer(
				DataTypes.DATATABLE_BOOLEAN));

		typeMap.put(new Integer(Types.BINARY), new Integer(
				DataTypes.DATATABLE_BYTES));
		typeMap.put(new Integer(Types.VARBINARY), new Integer(
				DataTypes.DATATABLE_BYTES));
		typeMap.put(new Integer(Types.LONGVARBINARY), new Integer(
				DataTypes.DATATABLE_BYTES));
		typeMap.put(new Integer(Types.BLOB), new Integer(
				DataTypes.DATATABLE_BYTES));

		typeMap.put(new Integer(Types.DOUBLE), new Integer(
				DataTypes.DATATABLE_DOUBLE));

		typeMap.put(new Integer(Types.INTEGER), new Integer(
				DataTypes.DATATABLE_INT));

		typeMap.put(new Integer(Types.CHAR), new Integer(
				DataTypes.DATATABLE_STRING));
		typeMap.put(new Integer(Types.VARCHAR), new Integer(
				DataTypes.DATATABLE_STRING));
		typeMap.put(new Integer(Types.LONGVARCHAR), new Integer(
				DataTypes.DATATABLE_STRING));
		typeMap.put(new Integer(Types.CLOB), new Integer(
				DataTypes.DATATABLE_STRING));

		typeMap.put(new Integer(Types.NUMERIC), new Integer(
				DataTypes.DATATABLE_BIGDECIMAL));

		typeMap.put(new Integer(Types.REAL), new Integer(
				DataTypes.DATATABLE_FLOAT));
		typeMap.put(new Integer(Types.FLOAT), new Integer(
				DataTypes.DATATABLE_FLOAT));

		typeMap.put(new Integer(Types.SMALLINT), new Integer(
				DataTypes.DATATABLE_SHORT));

		typeMap.put(new Integer(Types.NULL), new Integer(
				DataTypes.DATATABLE_OBJECT));
		typeMap.put(new Integer(Types.ARRAY), new Integer(
				DataTypes.DATATABLE_OBJECT));
		typeMap.put(new Integer(Types.OTHER), new Integer(
				DataTypes.DATATABLE_OBJECT));
		typeMap.put(new Integer(Types.JAVA_OBJECT), new Integer(
				DataTypes.DATATABLE_OBJECT));
		typeMap.put(new Integer(Types.REF), new Integer(
				DataTypes.DATATABLE_OBJECT));
		typeMap.put(new Integer(Types.DATALINK), new Integer(
				DataTypes.DATATABLE_OBJECT));
		typeMap.put(new Integer(Types.DISTINCT), new Integer(
				DataTypes.DATATABLE_OBJECT));
		typeMap.put(new Integer(Types.STRUCT), new Integer(
				DataTypes.DATATABLE_OBJECT));

		typeMap.put(new Integer(Types.DATE), new Integer(
				DataTypes.DATATABLE_DATE));

		typeMap.put(new Integer(Types.TIME), new Integer(
				DataTypes.DATATABLE_TIME));

		typeMap.put(new Integer(Types.TIMESTAMP), new Integer(
				DataTypes.DATATABLE_TIMESTAMP));

		typeMap.put(new Integer(Types.TINYINT), new Integer(
				DataTypes.DATATABLE_BYTE));

	}

	@SuppressWarnings("rawtypes")
	public Map getTypeMap() throws SQLException {
		// TODO 自动生成方法存根
		return this.typeMap;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataAdapter#fillBean()
	 */
	public void fillBean() {
		// TODO 自动生成方法存根

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataAdapter#fillDataTable(data.DataTable,
	 * java.sql.ResultSet)
	 */
	public void fillDataTable(DataTable arg0, ResultSet arg1) throws Exception {
		this.fillDataTable(arg0, arg1, -1, 1000000000);

	}

	/**
     */
	public void fillDataTable(DataTable arg0, ResultSet arg1, int count)
			throws Exception {
		this.fillDataTable(arg0, arg1, -1, count);

	}

	/**
     *  
     */
	public void fillDataTable(DataTable myTable, ResultSet rs, int startIndex,
			int count) throws Exception {
		if (rs == null)
			return;
		long t1 = System.currentTimeMillis();
		ResultSetMetaData md = rs.getMetaData();

		int columnCount = md.getColumnCount();
		int[] DataBaseTypes = new int[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			/*
			 * System.out.println("*****************************");
			 * System.out.println(md.getColumnType(i));
			 * System.out.println(md.getColumnName(i));
			 * System.out.println("*****************************");
			 */
			int jdbctype = md.getColumnType(i);
			DataColumn column = new DataColumn(
					JdbcAdapter.getDataTypeFromJdbcType(jdbctype));
			column.setColumnName(md.getColumnName(i).trim());
			column.setDataTypeName(md.getColumnTypeName(i));
			DataBaseTypes[i - 1] = jdbctype;
			myTable.getColumns().add(column);

		}
		int temp = 0;

		if (startIndex > 1) // 表示非第一页的情况
		{
			if (!rs.absolute(startIndex)) // 先移动到第N页的第0行。
				return;
			if (!rs.next()) // 再移动到第N页的第0行。
				return;
		} else if (startIndex < 0) // 表示不需要分页。
		{
			if (!rs.next())
				return;
		} else if (!rs.first()) // 表示0页从第一行开始的
			return;

		while (temp < count) {
			temp++;
			DataRow row = myTable.getRows().addNew();
			for (int i = 0; i < columnCount; i++) {
				Object obj1 = null;
				switch (DataBaseTypes[i]) {
				// case Types.BIGINT:
				// case Types.BIT: //-7
				// case Types.BOOLEAN:
				// case Types.BINARY: //-2
				// case Types.VARBINARY: //-3
				// case Types.LONGVARBINARY: //-4
				// {
				// obj1 = rs.getObject(i + 1);
				// break;
				// }

				case Types.BLOB: // 2004
				{
					Blob tempBlob = (Blob) rs.getBlob(i + 1);
					if (tempBlob != null) {
						long length = tempBlob.length();
						obj1 = tempBlob.getBytes(1, (int) length);
					}
					break;
				}

				// case Types.DECIMAL: //3
				// case Types.DOUBLE: //8
				// case Types.INTEGER: //4
				// case Types.CHAR: //1
				// case Types.VARCHAR: //12
				// case Types.LONGVARCHAR: //-1
				// {
				// obj1 = rs.getObject(i + 1);
				// break;
				// }

				case Types.CLOB: // 2005
				{
					Clob tempClob = rs.getClob(i + 1);
					if (tempClob != null) {
						long length = tempClob.length();
						obj1 = tempClob.getSubString(1, (int) length);
					}
					break;
				}

				// case Types.NUMERIC: //2
				// case Types.REAL: //7
				// case Types.FLOAT: //6
				// case Types.SMALLINT: //5
				// case Types.NULL: //0
				// case Types.ARRAY: //2003
				// case Types.OTHER: //1111
				// case Types.JAVA_OBJECT: //2000
				// case Types.REF: //2006
				// case Types.DATALINK: //
				// case Types.DISTINCT: //2001
				// case Types.STRUCT: //2002
				// {
				// obj1 = rs.getObject(i + 1);
				// break;
				// }

				case Types.DATE: // 91
				{
					obj1 = rs.getDate(i + 1);
					break;
				}

				case Types.TIME: // 92
				{
					obj1 = rs.getTime(i + 1);
					break;
				}

				case Types.TIMESTAMP: // 93
				{
					obj1 = rs.getTimestamp(i + 1);
					break;
				}

				// case Types.TINYINT: //-6
				default:
					obj1 = rs.getObject(i + 1);

				}
				row.setObject(i, obj1);
			}
			if (!rs.next())
				break;
		}
		long t2 = System.currentTimeMillis() - t1;

		System.out.println("单独插入花费时间:" + t2);

	}

	public static int getDataTypeFromJdbcType(int JDBC_TPYE) {

		switch (JDBC_TPYE) {
		case Types.BIGINT: // -5
			return DataTypes.DATATABLE_LONG;

		case Types.BIT: // -7
		case Types.BOOLEAN:
			return DataTypes.DATATABLE_BOOLEAN;// 返回BOOLEAN型

		case Types.BINARY: // -2
		case Types.VARBINARY: // -3
		case Types.LONGVARBINARY: // -4
		case Types.BLOB: // 2004
			return DataTypes.DATATABLE_BYTES;

		case Types.DECIMAL: // 3
			return DataTypes.DATATABLE_BIGDECIMAL;

		case Types.DOUBLE: // 8
			return DataTypes.DATATABLE_DOUBLE;

		case Types.INTEGER: // 4
			return DataTypes.DATATABLE_INT;

		case Types.CHAR: // 1
		case Types.VARCHAR: // 12
		case Types.LONGVARCHAR: // -1
		case Types.CLOB: // 2005
			return DataTypes.DATATABLE_STRING;

		case Types.NUMERIC: // 2
			return DataTypes.DATATABLE_BIGDECIMAL;

		case Types.REAL: // 7
		case Types.FLOAT: // 6
			return DataTypes.DATATABLE_FLOAT;

		case Types.SMALLINT: // 5
			return DataTypes.DATATABLE_SHORT;

		case Types.NULL: // 0
		case Types.ARRAY: // 2003
		case Types.OTHER: // 1111
		case Types.JAVA_OBJECT: // 2000
		case Types.REF: // 2006
		case Types.DATALINK: //
		case Types.DISTINCT: // 2001
		case Types.STRUCT: // 2002
			return DataTypes.DATATABLE_OBJECT;

		case Types.DATE: // 91
			return DataTypes.DATATABLE_DATE;

		case Types.TIME: // 92
			return DataTypes.DATATABLE_TIME;

		case Types.TIMESTAMP: // 93
			return DataTypes.DATATABLE_TIMESTAMP;

		case Types.TINYINT: // -6
			return DataTypes.DATATABLE_BYTE;

		default:
			return DataTypes.DATATABLE_OBJECT;

		}

	}

}