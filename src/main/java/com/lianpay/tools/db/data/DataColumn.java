package com.lianpay.tools.db.data;

public class DataColumn {
	private String label;
	private boolean readOnly;
	private DataTable table;
	private String columnName;
	private ObjectStorage dataStorage;

	/**
	 * @return 返回 typeName。
	 */
	public String getDataTypeName() {
		return this.dataStorage.getDataTypeName();
	}

	/**
	 * @param typeName
	 *            要设置的 typeName。
	 */
	public void setDataTypeName(String arg0) {
		this.getDataStorage().setDataTypeName(arg0);
	}

	/**
	 * @return 返回 type。
	 */
	public int getDataType() {
		return this.getDataStorage().getDataType();
	}

	public void setDataType(int arg0) {
		this.getDataStorage().setDataType(arg0);
	}

	public DataColumn() {
		this("default1");
	}

	public DataColumn(int dataType) {
		this("default1", dataType);
	}

	public DataColumn(String columnName) {
		this(columnName, DataTypes.DATATABLE_OBJECT);
	}

	public DataColumn(String columnName, int dataType) {
		dataStorage = new ObjectStorage(dataType);
		this.columnName = columnName;
	}

	/**
	 * @return 返回 caption。
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param caption
	 *            要设置的 caption。
	 */
	public void setLabel(String caption) {
		this.label = caption;
	}

	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            要设置的 columnName。
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return 返回 defaultValue。
	 */
	public Object getDefaultValue() {
		return this.dataStorage.getDefaultValue();
	}

	/**
	 * @param defaultValue
	 *            要设置的 defaultValue。
	 */
	public void setDefaultValue(Object arg0) {
		this.dataStorage.setDefaultValue(arg0);
	}

	/**
	 * @return 返回 readOnly。
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly
	 *            要设置的 readOnly。
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return 返回 table。
	 */
	public DataTable getTable() {
		return table;
	}

	/**
	 * @param table
	 *            要设置的 table。
	 */
	public void setTable(DataTable table) {
		this.table = table;

	}

	/**
	 * 
	 * @param newLength
	 */
	void expandArray(int newLength) {
		this.dataStorage.expandArray(newLength);

	}

	/**
     * 
     *  
     */
	void clearData() {
		this.clearData();
	}

	/**
	 * @return 返回 data。
	 */
	ObjectStorage getDataStorage() {
		return dataStorage;
	}

	void setObject(int arg0, Object arg1) throws DataException {
		this.dataStorage.setObject(arg0, arg1);
	}

	Object getObject(int arg0) throws DataException {
		return this.dataStorage.getObject(arg0);
	}

	void setString(int arg0, String arg1) throws DataException {
		this.dataStorage.setString(arg0, arg1);
	}

	String getString(int arg0) throws DataException {
		return this.dataStorage.getString(arg0);
	}

	boolean isNull(int arg0) {
		return this.dataStorage.isNull(arg0);
	}
}
