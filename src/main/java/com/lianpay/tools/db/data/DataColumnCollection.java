package com.lianpay.tools.db.data;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * @author 
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class DataColumnCollection {

	/**
     *  
     */
	private HashMap nameMap;

	private List columns;

	private DataTable table;

	/**
	 * 
	 * @param table
	 */
	DataColumnCollection(DataTable table) {
		this.nameMap = new HashMap();
		this.columns = new Vector();
		this.table = table;

	}

	@SuppressWarnings("unchecked")
	private void doChangenameMap() {
		this.nameMap.clear();
		int j = this.columns.size();
		for (int i = 0; i < j; i++) {
			DataColumn tempColumn = (DataColumn) this.columns.get(i);
			this.nameMap.put(tempColumn.getColumnName().toLowerCase().trim(),
					new Integer(i));
			// System.out.println("aaaaaaaaa:"+i+tempColumn.getColumnName());
		}
	}

	public int getColumnIndex(String arg0) {
		if (this.nameMap.containsKey(arg0.toLowerCase().trim())) {
			return Integer.parseInt(this.nameMap.get(arg0.toLowerCase().trim())
					.toString());
		} else
			return -1;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.Collection#size()
	 */
	public int size() {
		// TODO 自动生成方法存根
		return this.columns.size();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		this.columns.clear();
		this.nameMap.clear();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(DataColumn arg0) {
		DataColumn temp = this.get(arg0.getColumnName());
		if (temp == null) {
			@SuppressWarnings("unchecked")
			boolean res = this.columns.add(arg0);
			this.doChangenameMap();
			return res;
		} else if (arg0.equals(arg0)) {
			return true;
		} else
			return false;
	}

	@SuppressWarnings("unchecked")
	public void add(int index, DataColumn arg0) {
		DataColumn temp = this.get(arg0.getColumnName());
		if (temp == null) {
			this.columns.add(index, arg0);
			this.doChangenameMap();
		} else if (!temp.equals(arg0)) {
			// 这里应该抛出异常!
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(DataColumn arg0) {

		boolean res = this.columns.remove(arg0);
		this.doChangenameMap();
		return res;
	}

	/*
	 * （非 Javadoc）
	 */
	public DataColumn remove(int arg0) {

		DataColumn res = (DataColumn) this.columns.remove(arg0);
		this.doChangenameMap();
		return res;
	}

	public DataColumn remove(String arg0) {
		int tempIndex = this.getColumnIndex(arg0);
		if (tempIndex > -1)
			return this.remove(tempIndex);
		else
			return null;
	}

	/**
	 * 
	 * @param ColumnIndex
	 * @return
	 */
	public DataColumn get(int arg0) {

		return (DataColumn) this.columns.get(arg0);

	}

	/**
	 * 
	 * @param ColumnIndex
	 * @return
	 */
	public DataColumn get(String arg0) {
		int index = getColumnIndex(arg0);
		if (index > -1) {
			return get(index);
		} else
			return null;
	}

	void expandArray(int newLength) {
		int j = this.columns.size();
		for (int i = 0; i < j; i++) {
			this.get(i).expandArray(newLength);
		}
	}

}