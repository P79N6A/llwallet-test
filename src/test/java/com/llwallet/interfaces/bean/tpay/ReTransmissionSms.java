package com.llwallet.interfaces.bean.tpay;

import com.llwallet.interfaces.bean.BaseBean;

public class ReTransmissionSms extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String version;
	private String user_id;
	private String token;
	private String verify_code;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}
}