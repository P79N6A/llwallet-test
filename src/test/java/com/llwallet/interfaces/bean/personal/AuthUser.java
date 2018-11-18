package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class AuthUser extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String check_auth;
	private String risk_item;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCheck_auth() {
		return check_auth;
	}

	public void setCheck_auth(String check_auth) {
		this.check_auth = check_auth;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}
}