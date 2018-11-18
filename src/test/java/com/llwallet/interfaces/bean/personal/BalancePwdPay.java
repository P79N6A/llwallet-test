package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class BalancePwdPay extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String no_order;
	private String dt_order;
	private String valid_order;
	private String busi_partner;
	private String name_goods;
	private String money_order;
	private String info_order;
	private String pwd_pay;
	private String notify_url;
	private String risk_item;
	private String shareing_data;
	private String col_oidpartner;
	private String col_userid;
	private String secured_partner;
	private int buyer_confirm_valid;
	private int seller_send_valid;
	private String random_key;

	public String getRandom_key() {
		return random_key;
	}

	public void setRandom_key(String random_key) {
		this.random_key = random_key;
	}
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getValid_order() {
		return valid_order;
	}

	public void setValid_order(String valid_order) {
		this.valid_order = valid_order;
	}

	public String getBusi_partner() {
		return busi_partner;
	}

	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getShareing_data() {
		return shareing_data;
	}

	public void setShareing_data(String shareing_data) {
		this.shareing_data = shareing_data;
	}

	public String getCol_oidpartner() {
		return col_oidpartner;
	}

	public void setCol_oidpartner(String col_oidpartner) {
		this.col_oidpartner = col_oidpartner;
	}

	public String getCol_userid() {
		return col_userid;
	}

	public void setCol_userid(String col_userid) {
		this.col_userid = col_userid;
	}

	public String getSecured_partner() {
		return secured_partner;
	}

	public void setSecured_partner(String secured_partner) {
		this.secured_partner = secured_partner;
	}

	public int getBuyer_confirm_valid() {
		return buyer_confirm_valid;
	}

	public void setBuyer_confirm_valid(int buyer_confirm_valid) {
		this.buyer_confirm_valid = buyer_confirm_valid;
	}

	public int getSeller_send_valid() {
		return seller_send_valid;
	}

	public void setSeller_send_valid(int seller_send_valid) {
		this.seller_send_valid = seller_send_valid;
	}

}