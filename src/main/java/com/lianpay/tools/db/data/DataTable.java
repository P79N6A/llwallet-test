/*
 * 创建日期 2007-1-15
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.lianpay.tools.db.data;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/*
 import org.dom4j.Document;
 import org.dom4j.DocumentHelper;
 import org.dom4j.Element;
 */
/**
 * @author 
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public final class DataTable {

	public static boolean DEFAULT_READONLY = false;

	public static String DEFAULT_GETSTRING_NULL = "";

	public static int DEFAULT_GETINT_NULL = 0;

	public static Date DEFAULT_GETDATETIME_NULL = null;

	private DataColumnCollection columns;

	private DataRowCollection rows;

	private String tableName;

	private boolean readOnly = false;

	private List entityRows;

	private List deleteRows;

	private int maxIndex = -1;

	public DataTable() {
		columns = new DataColumnCollection(this);
		rows = new DataRowCollection(this);
		entityRows = new Vector();
		this.readOnly = DEFAULT_READONLY;
	}

	/**
	 * @return 返回 entityRows。
	 */
	List getEntityRows() {
		return entityRows;
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
	 * @param rows
	 *            要设置的 rows。
	 */
	public void setEntityRows(List rows) {
		entityRows = rows;
	}

	/**
	 * @return 返回 tableName。
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            要设置的 tableName。
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return 返回 rows。
	 */
	public DataRowCollection getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            要设置的 rows。
	 */
	public void setRows(DataRowCollection rows) {
		this.rows = rows;
	}

	/**
	 * @return 返回 columns。
	 */
	public DataColumnCollection getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            要设置的 columns。
	 */
	public void setColumns(DataColumnCollection columns) {

		this.columns = columns;
	}

	/**
	 * 
	 * @return
	 */
	int getNewIndex() {
		maxIndex++;
		this.columns.expandArray(maxIndex);
		return maxIndex;
	}

	/**
	 * 
	 * @return
	 */
	public DataRow newRow() {
		DataRow tempRow = new DataRow(this, getNewIndex());
		return tempRow;
	}
}