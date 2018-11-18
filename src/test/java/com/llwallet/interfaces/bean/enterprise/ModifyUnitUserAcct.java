package com.llwallet.interfaces.bean.enterprise;

import com.llwallet.interfaces.bean.BaseBean;

public class ModifyUnitUserAcct extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String token;
	private String city_code;
	private String brabank_name;
	private String card_no;
	private String bank_code;
	private String prcptcd;
	private String risk_item;
	private String acct_name;

	public String getAcct_name() {
		return acct_name;
	}

	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
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

}