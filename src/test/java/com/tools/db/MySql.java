package com.tools.db;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author 
 */
public class MySql extends DbBase {

	public MySql(String connect, String username, String pass) {
		connect(connect, username, pass);
	}

	private Boolean connect(String connect, String username, String pass) {
		try {
			System.out.println("mysqlConnect 连接到mysql数据库：" + connect);
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		String url = "jdbc:mysql://" + connect;
		try {
			con = DriverManager.getConnection(url, username, pass);
			return con.isClosed() == false;
		} catch (SQLException e2) {
			e2.printStackTrace();
			return false;
		}
	}

}
