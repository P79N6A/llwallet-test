package com.lianpay.tools.db.data.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import com.lianpay.tools.db.data.DataTable;

public interface DataAdapter {

	@SuppressWarnings("rawtypes")
	public Map getTypeMap() throws SQLException;

	public void fillDataTable(DataTable arg0, ResultSet arg1) throws Exception;

	public void fillDataTable(DataTable arg0, ResultSet arg1, int count)
			throws Exception;

	public void fillDataTable(DataTable arg0, ResultSet arg1, int startIndex,
			int count) throws Exception;

	public void fillBean();
}
