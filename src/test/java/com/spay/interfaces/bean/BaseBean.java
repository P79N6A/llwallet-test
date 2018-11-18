package com.spay.interfaces.bean;

import java.io.Serializable;

public class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String oid_partner;
	private String partner_sign_type;
	private String partner_sign;

	public String getOid_partner() {
		return oid_partner;
	}

	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}

	public String getPartner_sign_type() {
		return partner_sign_type;
	}

	public void setPartner_sign_type(String partner_sign_type) {
		this.partner_sign_type = partner_sign_type;
	}

	public String getPartner_sign() {
		return partner_sign;
	}

	public void setPartner_sign(String partner_sign) {
		this.partner_sign = partner_sign;
	}
}