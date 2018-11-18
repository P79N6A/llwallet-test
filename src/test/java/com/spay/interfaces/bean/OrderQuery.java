package com.spay.interfaces.bean;

import java.io.Serializable;

public class OrderQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	private String oid_partner;
	private String sign_type;
	private String sign;
	private String no_order;
	private String dt_order;
	private String oid_paybill;
	private String query_version;

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

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}

	public String getQuery_version() {
		return query_version;
	}

	public void setQuery_version(String query_version) {
		this.query_version = query_version;
	}
}