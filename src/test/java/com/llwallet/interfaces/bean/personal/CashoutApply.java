package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class CashoutApply extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String dt_order;
	private String no_order;
	private String money_order;
	private String card_no;
	private String pwd_pay;
	private String no_agree;
	private String city_code;
	private String brabank_name;
	private String info_order;
	private String notify_url;
	private String api_version;
	private String prcptcd;
	private String risk_item;
	private String fee_data;
	private String paytime_type;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getBrabank_name() {
		return brabank_name;
	}

	public void setBrabank_name(String brabank_name) {
		this.brabank_name = brabank_name;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getPrcptcd() {
		return prcptcd;
	}

	public void setPrcptcd(String prcptcd) {
		this.prcptcd = prcptcd;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getFee_data() {
		return fee_data;
	}

	public void setFee_data(String fee_data) {
		this.fee_data = fee_data;
	}

	public String getPaytime_type() {
		return paytime_type;
	}

	public void setPaytime_type(String paytime_type) {
		this.paytime_type = paytime_type;
	}
}