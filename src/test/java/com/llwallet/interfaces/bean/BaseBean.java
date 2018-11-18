package com.llwallet.interfaces.bean;

import java.io.Serializable;

public class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String oid_partner;
	private String sign_type;
	private String sign;

	public String getOid_partner() {
		return oid_partner;
	}

	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
