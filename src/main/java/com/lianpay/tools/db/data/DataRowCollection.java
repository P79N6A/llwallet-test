/*
 * 创建日期 2007-1-15
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.lianpay.tools.db.data;

/**
 * @author 
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DataRowCollection {

	/**
	 *  
	 */
	private DataTable table;

	/**
	 * 
	 * @param table
	 */
	DataRowCollection(DataTable table) {

		this.table = table;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.Collection#size()
	 */
	public int size() {
		// TODO 自动生成方法存根
		return this.table.getEntityRows().size();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		// TODO 自动生成方法存根
		this.table.getEntityRows().clear();

	}

	@SuppressWarnings("unchecked")
	public void add(int arg0, DataRow row) throws Exception {
		if (row.getTable().equals(this.table)) {
			this.table.getEntityRows().add(arg0, row);
		} else
			throw new Exception("该Row不是由table生成的!");
	}

	@SuppressWarnings("unchecked")
	public void add(DataRow row) throws Exception {
		if (row.getTable().equals(this.table)) {
			this.table.getEntityRows().add(row);
		} else
			throw new Exception("该Row不是由table生成的!");
	}

	public void remove(DataRow row) {

		this.table.getEntityRows().remove(row);

	}

	public void remove(int arg0) {

		this.table.getEntityRows().remove(arg0);

	}

	public DataRow get(int rowIndex) {

		return (DataRow) this.table.getEntityRows().get(rowIndex);

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public DataRow addNew() throws Exception {
		DataRow tempRow = this.table.newRow();
		this.add(tempRow);
		return tempRow;
	}

}