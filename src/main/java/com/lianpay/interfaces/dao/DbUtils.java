package com.lianpay.interfaces.dao;

import com.lianpay.tools.db.Oracle;

public class DbUtils {

	/**
	 * 查询用户号
	 * @param oid_traderno
	 * @param exter_name
	 * @return oid_userno、acctno_dirc_flag
	 */
	public TuInfoBelong queryOidUserNo(String oid_traderno,String exter_name) {
		TuInfoBelong tuInfoBelong = new TuInfoBelong();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String oid_userno = db
					.query("select OID_USERNO from TU_INFO_BELONG where OID_TRADERNO = '"
							+ oid_traderno + "' and EXTER_NAME = '"
							+ exter_name + "'" );
			tuInfoBelong.setOid_userno(oid_userno);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tuInfoBelong;
	}

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
	
	/**
	 * 查询短信验证码
	 * @param tno_smscd
	 * @return cd_smscd
	 */
	public  TuUserCheck queryCdSmscd(String tno_smscd) {
		TuUserCheck tuUserCheck = new TuUserCheck();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String cd_smscd = db
					.query("select CD_SMSCD from(select * from TU_USER_CHECK t where tno_smscd = '"
							+ tno_smscd + "' order by dt_smscdgen desc) where rownum=1");
			tuUserCheck.setTno_smscd(cd_smscd);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tuUserCheck;
	}
}
