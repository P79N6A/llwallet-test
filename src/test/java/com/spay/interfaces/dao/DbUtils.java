package com.spay.interfaces.dao;

import com.tools.db.Oracle;

public class DbUtils {

	/**
	 * 删除用户开户信息
	 * @param oid_userno
	 * 
	 */
	public void deleteUser(String oid_userno) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Delete("delete from TU_ACCT_USERBASE where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_USER_PWDPRQ where OID_USERNO = '"
					+ oid_userno + "'");			
			db.Delete("delete from TU_INFO_MOBILE where OID_USERNO = '"
							+ oid_userno + "'");			
			db.Delete("delete from TU_INFO_EMAIL where OID_USERNO = '"
									+ oid_userno + "'");
			db.Delete("delete from TU_INFO_SECURITY where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_INFO_LOGIN where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_INFO_CERT where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_UNIT_APPENDINFO where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_REAL_AUTH where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_PWDPRQ_CHECK where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_INFO_BELONG where OID_USERNO = '"
					+ oid_userno + "'");
			db.Delete("delete from TU_INFO_USERBASE where OID_USERNO = '"
					+ oid_userno + "'");			
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
