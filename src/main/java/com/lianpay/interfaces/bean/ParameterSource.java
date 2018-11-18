package com.lianpay.interfaces.bean;

public class ParameterSource {

	/**
	 * 3.1 企业用户直接开户接口请求参数
	 * 
	 */
	public String[] openUnitUserParameter() {

		String[] strArray = { "oid_partner", "sign_type", "user_id", "mob_bind", "name_user", "type_idcard",
				"exp_idcard", "type_expidcard", "no_idcard", "name_agent", "idno_agent", "exp_idcard_agent",
				"type_agentexpidcard", "pwd_login", "pwd_pay", "eml_bind", "addr_unit", "addr_pro", "addr_city",
				"addr_dist", "busi_user", "name_unit", "num_license", "exp_license", "org_code", "type_register",
				"type_license", "type_industry", "exp_orgcode", "city_code", "brabank_name", "card_no", "bank_code",
				"prcptcd", "acct_name", "flag_check" };
		return strArray;
	}

	/**
	 * 3.2企业用户上传照片接口请求参数
	 * 
	 */
	public String[] uploadUnitPhotoParameter() {
		String[] strArray = { "oid_partner", "sign_type", "user_id" };
		return strArray;
	}

	/**
	 * Web充值接口请求参数
	 * 
	 */
	public String[] rechargeGatewayParameter() {
		String[] strArray = { "busi_partner", "dt_order", "info_order", "money_order", "name_goods", "no_order",
				"notify_url", "oid_partner", "platform", "sign_type", "timestamp", "url_return", "user_login",
				"userreq_ip", "version" };
		return strArray;
	}

	/**
	 * Web消费接口请求参数
	 * 
	 */
	public String[] paymentGatewayParameter() {
		String[] strArray = { "acct_name", "back_url", "bank_code", "busi_partner", "card_no", "dt_order",
				"flag_modify", "id_no", "id_type", "info_order", "money_order", "name_goods", "no_agree", "no_order",
				"notify_url", "oid_partner", "pay_type", "platform", "risk_item", "shareing_data", "sign_type",
				"timestamp", "url_return", "user_login", "userreq_ip", "valid_order", "version" };
		return strArray;
	}
}
