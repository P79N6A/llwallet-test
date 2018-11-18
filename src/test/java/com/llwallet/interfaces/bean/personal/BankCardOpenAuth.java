package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class BankCardOpenAuth extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String pay_type;
	private String api_version;
	private String risk_item;
	private String card_no;
	private String bind_mob;
	private String vali_date;
	private String cvv2;
	private String flag_auth;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getBind_mob() {
		return bind_mob;
	}

	public void setBind_mob(String bind_mob) {
		this.bind_mob = bind_mob;
	}

	public String getVali_date() {
		return vali_date;
	}

	public void setVali_date(String vali_date) {
		this.vali_date = vali_date;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getFlag_auth() {
		return flag_auth;
	}

	public void setFlag_auth(String flag_auth) {
		this.flag_auth = flag_auth;
	}
}