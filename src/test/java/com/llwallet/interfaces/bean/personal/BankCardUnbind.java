package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class BankCardUnbind extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String no_agree;
	private String pay_type;
	private String pwd_pay;
	private String type_user;
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

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getType_user() {
		return type_user;
	}

	public void setType_user(String type_user) {
		this.type_user = type_user;
	}
}