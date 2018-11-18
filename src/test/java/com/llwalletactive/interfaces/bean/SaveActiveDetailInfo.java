package com.llwalletactive.interfaces.bean;

import com.llwalletactive.interfaces.bean.BaseBean;

public class SaveActiveDetailInfo extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String pwd_pay_new;
	private String user_id;
	private String oid_job;
	private String addr_pro;
	private String addr_city;
	private String addr_dist;
	private String addr_conn;

	public String getPwd_pay_new() {
		return pwd_pay_new;
	}

	public void setPwd_pay_new(String pwd_pay_new) {
		this.pwd_pay_new = pwd_pay_new;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getOid_job() {
		return oid_job;
	}

	public void setOid_job(String oid_job) {
		this.oid_job = oid_job;
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

	public String getAddr_conn() {
		return addr_conn;
	}

	public void setAddr_conn(String addr_conn) {
		this.addr_conn = addr_conn;
	}
}