package com.lianpay.interfaces.dao;

import com.lianpay.tools.db.MySql;
import com.lianpay.tools.db.Oracle;
import com.lianpay.tools.utils.Property;

public class Dbconn {
	
	public static Oracle getDBConnCust() throws Exception {
		return new Oracle(Property.get("db.servertaf.connect"),
				Property.get("db.servertaf.username"),
				Property.get("db.servertaf.password"));
	}
	
	public static MySql getDBConn() throws Exception {
		return new MySql(Property.get("db.mysql.connect"),
				Property.get("db.mysql.username"),
				Property.get("db.mysql.password"));
	}
}
