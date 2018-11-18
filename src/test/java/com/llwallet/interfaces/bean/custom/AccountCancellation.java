package com.llwallet.interfaces.bean.custom;

import com.llwallet.interfaces.bean.BaseBean;

public class AccountCancellation extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String pwd_pay;
	private String consent_agreement;
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

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getConsent_agreement() {
		return consent_agreement;
	}

	public void setConsent_agreement(String consent_agreement) {
		this.consent_agreement = consent_agreement;
	}
}