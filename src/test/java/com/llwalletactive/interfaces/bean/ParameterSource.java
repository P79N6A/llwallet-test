package com.llwalletactive.interfaces.bean;

public class ParameterSource {

	/**
	 * 完善账户信息接口
	 * 
	 */
	public String[] activeDetailInfoParameter() {
		String[] strArray = { "activeAccountIndex.pwd_pay_new", "activeAccountIndex.user_id",
				"activeAccountIndex.oid_job", "activeAccountIndex.addr_pro", "activeAccountIndex.addr_city",
				"activeAccountIndex.addr_dist", "activeAccountIndex.addr_conn" };
		return strArray;
	}
}
