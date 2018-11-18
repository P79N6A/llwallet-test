package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class PayPwdModify extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String pwd_pay;
	private String pwd_pay_new;
	private String risk_item;
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

	public String getPwd_pay_new() {
		return pwd_pay_new;
	}

	public void setPwd_pay_new(String pwd_pay_new) {
		this.pwd_pay_new = pwd_pay_new;
	}

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}
}