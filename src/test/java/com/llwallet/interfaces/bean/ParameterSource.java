package com.llwallet.interfaces.bean;

public class ParameterSource {

	/**
	 * Web充值接口请求参数
	 * 
	 */
	public String[] chargeParameter() {
		String[] strArray = { "busi_partner", "dt_order", "info_order", "money_order", "name_goods", "no_order",
				"notify_url", "oid_partner", "platform", "sign_type", "timestamp", "url_return", "user_login",
				"userreq_ip", "version" };
		return strArray;
	}

	/**
	 * Web消费接口请求参数
	 * 
	 */
	public String[] paymentParameter() {
		String[] strArray = { "version", "timestamp", "oid_partner", "user_id", "sign_type", "busi_partner", "no_order",
				"dt_order", "name_goods", "info_order", "money_order", "notify_url", "url_return", "bank_code",
				"pay_type", "userreq_ip", "url_order", "valid_order", "risk_item", "no_agree", "card_no", "back_url",
				"shareing_data", "secured_partner", "buyer_confirm_valid", "seller_send_valid", "col_oidpartner",
				"col_userid" };
		return strArray;
	}

	/**
	 * 聚合支付Web接口请求参数
	 *
	 */
	public String[] aggregateWebParameter() {
		String[] strArray = { "back_url", "bank_code", "busi_partner", "buyer_confirm_valid", "card_no",
				"col_oidpartner", "col_userid", "dt_order", "info_order", "money_order", "name_goods", "no_agree",
				"no_order", "notify_url", "oid_partner", "pay_type", "risk_item", "secured_partner",
				"seller_send_valid", "shareing_data", "sign_type", "timestamp", "url_order", "url_return", "user_id",
				"userreq_ip", "valid_order", "version" };
		return strArray;
	}
}