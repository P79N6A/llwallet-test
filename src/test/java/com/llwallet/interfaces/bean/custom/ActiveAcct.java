package com.llwallet.interfaces.bean.custom;

import com.llwallet.interfaces.bean.BaseBean;

public class ActiveAcct extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String eml_bind;
	private String mob_bind;
	private String oid_job;
	private String addr_conn;
	private String addr_pro;
	private String addr_city;
	private String addr_dist;
	private String consent_agreement;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getEml_bind() {
		return eml_bind;
	}

	public void setEml_bind(String eml_bind) {
		this.eml_bind = eml_bind;
	}

	public String getMob_bind() {
		return mob_bind;
	}

	public void setMob_bind(String mob_bind) {
		this.mob_bind = mob_bind;
	}

	public String getOid_job() {
		return oid_job;
	}

	public void setOid_job(String oid_job) {
		this.oid_job = oid_job;
	}

	public String getAddr_conn() {
		return addr_conn;
	}

	public void setAddr_conn(String addr_conn) {
		this.addr_conn = addr_conn;
	}

	public String getAddr_pro() {
		return addr_pro;
	}

	public void setAddr_pro(String addr_pro) {
		this.addr_pro = addr_pro;
	}

	public String getAddr_city() {
		return addr_city;
	}

	public void setAddr_city(String addr_city) {
		this.addr_city = addr_city;
	}

	public String getAddr_dist() {
		return addr_dist;
	}

	public void setAddr_dist(String addr_dist) {
		this.addr_dist = addr_dist;
	}

	public String getConsent_agreement() {
		return consent_agreement;
	}

	public void setConsent_agreement(String consent_agreement) {
		this.consent_agreement = consent_agreement;
	}

}