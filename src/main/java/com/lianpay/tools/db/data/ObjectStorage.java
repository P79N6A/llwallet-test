package com.lianpay.tools.db.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ObjectStorage {
	public static int DEAULT_ADD_STEP = 30;

	private Object[] values;

	private int dataType;

	private String dataTypeName;

	private Object defaultValue;

	/**
	 * @param defaultValue
	 *            要设置的 defaultValue。
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param dataTypeName
	 *            要设置的 dataTypeName。
	 */
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}

	public ObjectStorage() {
		this(DataTypes.DATATABLE_OBJECT);
	}

	public ObjectStorage(int dataType) {
		if (!DataTypes.checkDataType(dataType))
			dataType = DataTypes.DATATABLE_OBJECT;
		values = new Object[DEAULT_ADD_STEP];
		this.dataType = dataType;
		this.dataTypeName = DataTypes.getDataTypeName(dataType);
	}

	private void checkDataFilterData(Object obj) throws DataException {
		// 这里检查类型是否符合转换规则。
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#convertObjectToXml(java.lang.Object)
	 */
	public String getString(int arg0) throws DataException {
		try {
			Object value = this.getObject(arg0);
			if (value == null)
				return null;

			switch (this.dataType) {
			case DataTypes.DATATABLE_DATE: {
				Date tempObj = (Date) value;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				return sdf.format(tempObj);
			}
			case DataTypes.DATATABLE_TIME: {
				Time tempObj = (Time) value;
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

				return sdf.format(tempObj);
			}
			case DataTypes.DATATABLE_TIMESTAMP: {
				Timestamp tempObj = (Timestamp) value;
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				return sdf.format(tempObj);
			}

			case DataTypes.DATATABLE_BYTES: {
				byte[] tempObj = (byte[]) value;
				BASE64Encoder endocer = new BASE64Encoder();
				return endocer.encode(tempObj);
			}
			default:
				return value.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e.getMessage());
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#convertXmlToObject(java.lang.String)
	 */
	public void setString(int arg0, String arg1) throws DataException {
		try {
			Object obj = arg1;
			switch (this.dataType) {
			case DataTypes.DATATABLE_OBJECT:

			case DataTypes.DATATABLE_STRING:
				obj = arg1;
				break;

			case DataTypes.DATATABLE_BOOLEAN:
				obj = Boolean.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_SHORT:
				obj = Short.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_INT:
				obj = Integer.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_BIGDECIMAL:
			case DataTypes.DATATABLE_LONG:
				obj = Long.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_FLOAT:
				obj = Float.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_DOUBLE:
				obj = Double.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_DATE:
				obj = Date.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_TIME:
				obj = Time.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_TIMESTAMP:
				/*
				 * System.out.println("*****************************");
				 * System.out.println(arg1);
				 * System.out.println("*****************************");
				 * SimpleDateFormat df1 = new
				 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); java.util.Date
				 * date11 = df1.parse(arg1); String time = df1.format(date11);
				 * obj= Timestamp.valueOf(time);
				 */
				obj = Timestamp.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_BYTE:
				obj = Byte.valueOf(arg1);
				break;

			case DataTypes.DATATABLE_BYTES: {

				BASE64Decoder decoder = new BASE64Decoder();
				obj = decoder.decodeBuffer(arg1);
				break;
			}

			default:
				obj = arg1;

			}
			this.values[arg0] = obj;
		} catch (Exception e) {
			System.out.println("第" + arg0 + "列转换出错" + this.dataTypeName);
			e.printStackTrace();
			throw new DataException(e.getMessage());
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#get(int)
	 */
	public synchronized Object getObject(int recordNo) throws DataException {
		// TODO 自动生成方法存根
		return values[recordNo];
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#isNull(int)
	 */
	public boolean isNull(int recordNo) {
		// TODO 自动生成方法存根
		return values[recordNo] == null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#set(int, java.lang.Object)
	 */
	public synchronized void setObject(int arg0, Object arg1)
			throws DataException {
		// TODO 自动生成方法存根
		try {
			Object value = null;
			if (arg1 == null) {
				value = null;
			} else if (arg1 instanceof String) {
				this.setString(arg0, (String) arg1);
				return;
			} else {
				switch (this.dataType) {
				case DataTypes.DATATABLE_OBJECT: {
					value = arg1;
					break;
				}

				case DataTypes.DATATABLE_STRING: {
					value = arg1.toString();
					break;
				}

				case DataTypes.DATATABLE_BOOLEAN: {
					value = this.convertBoolean(arg1);
					break;
				}

				case DataTypes.DATATABLE_SHORT: {
					Number num = this.convertNumber(arg1);
					value = new Short(num.shortValue());
					break;
				}

				case DataTypes.DATATABLE_INT: {
					Number num = this.convertNumber(arg1);
					value = new Integer(num.intValue());
					break;
				}

				case DataTypes.DATATABLE_BIGDECIMAL: {
					Number num = this.convertNumber(arg1);
					value = (BigDecimal) num;
					break;
				}
				case DataTypes.DATATABLE_LONG: {
					Number num = this.convertNumber(arg1);
					value = new Long(num.longValue());
					break;
				}

				case DataTypes.DATATABLE_FLOAT: {
					Number num = this.convertNumber(arg1);
					value = new Float(num.floatValue());
					break;
				}

				case DataTypes.DATATABLE_DOUBLE: {
					Number num = this.convertNumber(arg1);
					value = new Float(num.doubleValue());
					break;
				}

				case DataTypes.DATATABLE_DATE: {
					java.util.Date date = this.convertDateTime(arg1);
					value = new Date(date.getTime());
					break;
				}

				case DataTypes.DATATABLE_TIME: {
					java.util.Date date = this.convertDateTime(arg1);
					value = new Time(date.getTime());
					break;
				}

				case DataTypes.DATATABLE_TIMESTAMP: {
					java.util.Date date = this.convertDateTime(arg1);
					value = new Timestamp(date.getTime());
					break;
				}

				case DataTypes.DATATABLE_BYTE: {
					Number num = this.convertNumber(arg1);
					value = new Byte(num.byteValue());
					break;
				}

				case DataTypes.DATATABLE_BYTES: {
					value = this.convertBytes(arg1);
					break;
				}

				default:
					value = arg1;

				}
			}
			this.values[arg0] = value;
		} catch (Exception e) {
			System.out.println("第" + arg0 + "列转换出错" + this.dataTypeName);
			e.printStackTrace();
			throw new DataException(e.getMessage());
		}

	}

	/**
	 * @return 返回 dataType。
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            要设置的 dataType。
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#getDataTypeName()
	 */
	public String getDataTypeName() {
		// TODO 自动生成方法存根
		return this.dataTypeName;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#getDefaultValue()
	 */
	public Object getDefaultValue() {
		// TODO 自动生成方法存根
		return this.defaultValue;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#clearData()
	 */
	public void clearData() {
		// TODO 自动生成方法存根
		this.values = null;
		this.values = new Object[DEAULT_ADD_STEP];

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see data.common.DataStorage#expandArray(int)
	 */
	public void expandArray(int newLength) {
		if (newLength >= this.values.length) {
			newLength = newLength + DEAULT_ADD_STEP;
			Object[] tempArray = new Object[newLength];
			System.arraycopy(this.values, 0, tempArray, 0, this.values.length);
			this.values = null;
			this.values = tempArray;

		}

	}

	private Number convertNumber(Object obj) // i 输入的类型,j
			// 表示当前列类型
			throws DataException {
		try {
			if (obj instanceof Number)
				return (Number) obj;
			else
				return new Double(obj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("类型转换错误:" + obj.toString() + "转换成数字型错误!");
		}
	}

	private java.util.Date convertDateTime(Object obj) // i 输入的类型,j
			// 表示当前列类型
			throws DataException {
		if (obj instanceof java.util.Date) {
			return (java.util.Date) obj;
		} else if (obj instanceof Number) {
			return new java.util.Date(((Number) obj).longValue());
		} else
			throw new DataException("类型转换错误:" + obj.toString() + "转换成日期型错误!");

	}

	private byte[] convertBytes(Object obj) // i 输入的类型,j
			// 表示当前列类型
			throws DataException {
		if (obj instanceof byte[]) {
			return (byte[]) obj;
		} else
			throw new DataException("类型转换错误:" + obj.toString()
					+ "转换成Byte[]型错误!");

	}

	private Boolean convertBoolean(Object obj) // i 输入的类型,j
			// 表示当前列类型
			throws DataException {
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof Number) {
			return new Boolean(((Number) obj).doubleValue() != 0.0D);
		} else
			throw new DataException("类型转换错误:" + obj.toString()
					+ "转换成Boolean型错误!");

	}
}
