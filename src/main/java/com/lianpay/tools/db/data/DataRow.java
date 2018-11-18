package com.lianpay.tools.db.data;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import com.lianpay.tools.db.data.convert.Convert;

//import shelper.data.convert.Convert;

/**
 * @author 
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DataRow {

	private int rowIndex = -1;

	/**
     *  
     */
	private DataTable table;

	/**
     *  
     */
	private int state;

	/**
	 * 
	 * @param table
	 * @param rowIndex
	 */
	DataRow(DataTable table, int rowIndex) {
		this.table = table;
		this.rowIndex = rowIndex;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 */
	private int getColumnIndex(String arg0) {
		return this.getTable().getColumns().getColumnIndex(arg0);
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 */
	private DataColumn getColumn(String arg0) {
		return this.getTable().getColumns().get(arg0);
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 */
	private DataColumn getColumn(int arg0) {
		return this.getTable().getColumns().get(arg0);
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	private synchronized Number getNumber(int i) throws DataException {
		Object obj = this.getObject(i);
		if (obj == null || (obj instanceof BigDecimal)
				|| (obj instanceof Number))
			return (Number) obj;
		if (obj instanceof Boolean)
			return new Integer(((Boolean) obj).booleanValue() ? 1 : 0);
		if (obj instanceof String)
			return new BigDecimal((String) obj);
		else
			return Double.valueOf(obj.toString());

	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public synchronized Object getObject(String arg0) throws DataException {
		return this.getColumn(arg0).getObject(this.rowIndex);
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public synchronized Object getObject(int arg0) throws DataException {
		return this.getColumn(arg0).getObject(this.rowIndex);
	}

	/**
	 * 
	 * @param columnName
	 * @throws DataException
	 */
	public String getString(String arg0) throws DataException {
		return this.getColumn(arg0).getString(this.rowIndex);
	}

	/**
	 * 
	 * @param columnName
	 * @throws DataException
	 */
	public String getString(int i) throws DataException {
		return this.getColumn(i).getString(this.rowIndex);

	}

	/**
	 * 
	 * @param columnName
	 * @return
	 * @throws DataException
	 */
	public boolean isNull(String arg0) throws DataException {
		return this.getColumn(arg0).isNull(this.rowIndex);
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	public boolean isNull(int arg0) throws DataException {
		return this.getColumn(arg0).isNull(this.rowIndex);
	}

	/**
	 * 
	 * @return
	 */
	public DataTable getTable() {
		return this.table;
	}

	/**
	 * @param table
	 *            要设置的 table。
	 */
	void setTable(DataTable table) {

		this.table = table;

	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setObject(String arg0, Object arg1) throws DataException {
		this.getColumn(arg0).setObject(this.rowIndex, arg1);
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setObject(int arg0, Object arg1) throws DataException {
		this.getColumn(arg0).setObject(this.rowIndex, arg1);
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 */
	public int getInt(int i) throws DataException {
		Number number = getNumber(i);
		return number != null ? number.intValue() : 0;
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 */
	public int getInt(String arg0) throws DataException {
		return this.getInt(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setInt(String arg0, int arg1) throws DataException {
		this.setObject(arg0, new Integer(arg1));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setInt(int arg0, int arg1) throws DataException {
		this.setObject(arg0, new Integer(arg1));
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 * @throws DataException
	 */
	public short getShort(int i) throws DataException {
		Number number = getNumber(i);
		return number != null ? number.shortValue() : 0;
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 * @throws DataException
	 */
	public short getShort(String arg0) throws DataException {
		return this.getShort(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setShort(String columnName, short objectData)
			throws DataException {

	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws Exception
	 */
	public void setShort(int arg0, short arg1) throws Exception {
		this.setObject(arg0, new Short(arg1));
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 */
	public long getLong(int i) throws DataException {
		Number number = getNumber(i);
		return number != null ? number.longValue() : 0L;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public long getLong(String arg0) throws DataException {
		return this.getLong(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setLong(String arg0, long arg1) throws DataException {
		this.setObject(arg0, new Long(arg1));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setLong(int arg0, long arg1) throws DataException {
		this.setObject(arg0, new Long(arg1));
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 */
	public float getFloat(int i) throws DataException {
		Number number = getNumber(i);
		return number != null ? number.floatValue() : 0.0F;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public float getFloat(String arg0) throws DataException {
		return this.getFloat(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setFloat(String arg0, float arg1) throws DataException {
		this.setObject(arg0, new Float(arg1));
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setFloat(int arg0, float arg1) throws DataException {
		this.setObject(arg0, new Float(arg1));
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 */
	public double getDouble(int i) throws DataException {
		Number number = getNumber(i);
		return number != null ? number.doubleValue() : 0.0D;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public double getDouble(String arg0) throws DataException {
		return this.getDouble(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	public BigDecimal getBigDecimal(int i) throws DataException {
		Number number = getNumber(i);
		if (number == null || (number instanceof BigDecimal))
			return (BigDecimal) number;
		if (number instanceof Number)
			return new BigDecimal(((Number) number).doubleValue());
		else
			return null;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public BigDecimal getBigDecimal(String arg0) throws DataException {
		return this.getBigDecimal(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setDouble(String arg0, double arg1) throws DataException {
		this.setObject(arg0, new Double(arg1));
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setDouble(int arg0, double arg1) throws DataException {
		this.setObject(arg0, new Double(arg1));
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setBytes(int arg0, byte[] arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setBytes(String arg0, byte[] arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setByte(int arg0, byte[] arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setByte(String arg0, byte[] arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 */
	public java.sql.Date getDate(int i) throws DataException {
		Date tempDate = this.getDateTime(i);
		if (tempDate != null) {
			return new java.sql.Date(tempDate.getTime());
		} else
			return (java.sql.Date) null;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public java.sql.Date getDate(String arg0) throws DataException {
		return this.getDate(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	public Date getDateTime(int i) throws DataException {
		Object obj = getObject(i);
		if (obj == null)
			return (Date) obj;
		else if (obj instanceof Number)
			return new java.util.Date(((Number) obj).longValue());
		else if (obj instanceof java.util.Date)
			return (java.util.Date) obj;
		else if (obj instanceof String) {
			try {
				return Convert.toDateTime(obj.toString());

			} catch (Exception e) {
				return null;
			}

		} else
			throw new DataException("类型转换错误:转换到java.util.date格式错误!");

	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public Date getDateTime(String arg0) throws DataException {
		return this.getDate(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	public Time getTime(int i) throws DataException {
		Date tempDate = this.getDateTime(i);
		if (tempDate != null) {
			return new java.sql.Time(tempDate.getTime());
		} else
			return (java.sql.Time) null;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public Time getTime(String arg0) throws DataException {
		return this.getTime(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	public Timestamp getTimestamp(int i) throws DataException {
		Date tempDate = this.getDateTime(i);
		if (tempDate != null) {
			return new Timestamp(tempDate.getTime());
		} else
			return (java.sql.Timestamp) null;
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public Timestamp getTimestamp(String arg0) throws DataException {
		return this.getTimestamp(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public Object[] getArrayList() throws DataException {
		int columnLen = this.table.getColumns().size();
		Object[] array = new Object[columnLen];
		for (int i = 0; i < columnLen; i++) {
			array[i] = this.getObject(i);
		}
		return array;
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	public byte getByte(int i) throws DataException {
		Object obj = getObject(i);
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).byteValue();
		if (obj instanceof String)
			return ((String) obj).getBytes()[0];
		if (obj instanceof Byte[]) {
			return ((byte[]) obj)[0];
		} else {
			throw new DataException("非法的转换到byte格式");
		}
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public byte getByte(String arg0) throws DataException {
		return this.getByte(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws DataException
	 */
	public byte[] getBytes(int i) throws DataException {
		Object obj = getObject(i);
		if (obj == null)
			return (byte[]) obj;
		else if (obj instanceof byte[])
			return (byte[]) obj;
		else if (obj instanceof String)
			return (byte[]) ((String) obj).getBytes();
		else if (obj instanceof Number)
			return (byte[]) ((Number) obj).toString().getBytes();
		else if (obj instanceof BigDecimal)
			return (byte[]) ((BigDecimal) obj).toString().getBytes();
		else {
			throw new DataException("非法的转换到byte[]格式!");
		}
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public byte[] getBytes(String arg0) throws DataException {
		return this.getBytes(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setDate(String arg0, java.sql.Date arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setDate(int arg0, java.sql.Date arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setTime(String arg0, Time arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setTime(int arg0, Time arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setDateTime(String arg0, java.util.Date arg1)
			throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setDateTime(int arg0, java.util.Date arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setTimestamp(String arg0, Timestamp arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setTimestamp(int arg0, Timestamp arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param i
	 * @throws DataException
	 */
	public boolean getBoolean(int i) throws DataException {
		Object obj = getObject(i);
		if (obj == null)
			return false;
		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue();
		if (obj instanceof Number)
			return ((Number) obj).doubleValue() != 0.0D;
		else
			throw new DataException("格式转换错误:不能转换成boolean类型");
	}

	/**
	 * 
	 * @param arg0
	 * @return
	 * @throws DataException
	 */
	public boolean getBoolean(String arg0) throws DataException {
		return this.getBoolean(this.getColumnIndex(arg0));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setBoolean(String arg0, boolean arg1) throws DataException {
		this.setObject(arg0, new Boolean(arg1));
	}

	/**
	 * 
	 * @param columnName
	 * @param object
	 * @throws DataException
	 */
	public void setBoolean(int arg0, boolean arg1) throws DataException {
		this.setObject(arg0, new Boolean(arg1));
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setBigDecimal(String arg0, BigDecimal arg1)
			throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setBigDecimal(int arg0, BigDecimal arg1) throws DataException {
		this.setObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setString(int arg0, String arg1) throws DataException {
		this.getColumn(arg0).setString(this.rowIndex, arg1);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws DataException
	 */
	public void setString(String arg0, String arg1) throws DataException {
		this.getColumn(arg0).setString(this.rowIndex, arg1);
	}

}