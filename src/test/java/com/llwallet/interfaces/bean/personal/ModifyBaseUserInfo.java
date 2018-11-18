package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class ModifyBaseUserInfo extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String addr_conn;
	private String addr_pro;
	private String addr_city;
	private String addr_dist;
	private String oid_job;
	private String risk_item;
	private String exp_idcard;
	private String type_expidcard;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getOid_job() {
		return oid_job;
	}

	public void setOid_job(String oid_job) {
		this.oid_job = oid_job;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}	
	
	public String getExp_idcard() {
		return exp_idcard;
	}

	public void setExp_idcard(String exp_idcard) {
		this.exp_idcard = exp_idcard;
	}

	public String getType_expidcard() {
		return type_expidcard;
	}

	public void setType_expidcard(String type_expidcard) {
		this.type_expidcard = type_expidcard;
	}
}