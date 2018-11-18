package com.lianpay.tools.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 
 */
public class Oracle extends DbBase {

	public Oracle(String connect, String username, String pass) {
		connect(connect, username, pass);
	}

	private Boolean connect(String connect, String username, String pass) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e1) {
			Logger.getLogger(Oracle.class.getName())
					.log(Level.SEVERE, null, e1);
		}
		String url = "jdbc:oracle:thin:@" + connect;
		try {
			con = DriverManager.getConnection(url, username, pass);
			return con.isClosed() == false;
		} catch (SQLException e2) {
			Logger.getLogger(Oracle.class.getName())
					.log(Level.SEVERE, null, e2);
		}
		return false;
	}

}
