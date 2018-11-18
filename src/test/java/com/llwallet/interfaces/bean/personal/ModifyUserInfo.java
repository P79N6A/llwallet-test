package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class ModifyUserInfo extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String token;
	private String name_user;
	private String type_idcard;
	private String no_idcard;
	private String addr_conn;
	private String exp_idcard;
	private String flg_sex;
	private String oid_job;
	private String risk_item;
	private String addr_pro;
	private String addr_city;
	private String addr_dist;

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

	public String getName_user() {
		return name_user;
	}

	public void setName_user(String name_user) {
		this.name_user = name_user;
	}

	public String getType_idcard() {
		return type_idcard;
	}

	public void setType_idcard(String type_idcard) {
		this.type_idcard = type_idcard;
	}

	public String getNo_idcard() {
		return no_idcard;
	}

	public void setNo_idcard(String no_idcard) {
		this.no_idcard = no_idcard;
	}

	public String getAddr_conn() {
		return addr_conn;
	}

	public void setAddr_conn(String addr_conn) {
		this.addr_conn = addr_conn;
	}

	public String getExp_idcard() {
		return exp_idcard;
	}

	public void setExp_idcard(String exp_idcard) {
		this.exp_idcard = exp_idcard;
	}

	public String getFlg_sex() {
		return flg_sex;
	}

	public void setFlg_sex(String flg_sex) {
		this.flg_sex = flg_sex;
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
}