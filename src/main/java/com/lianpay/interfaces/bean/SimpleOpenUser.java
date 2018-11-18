package com.lianpay.interfaces.bean;

public class SimpleOpenUser extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String mob_bind;
	private String eml_bind;
	private String name_user;
	private String no_idcard;
	private String risk_item;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMob_bind() {
		return mob_bind;
	}

	public void setMob_bind(String mob_bind) {
		this.mob_bind = mob_bind;
	}

	public String getEml_bind() {
		return eml_bind;
	}

	public void setEml_bind(String eml_bind) {
		this.eml_bind = eml_bind;
	}

	public String getName_user() {
		return name_user;
	}

	public void setName_user(String name_user) {
		this.name_user = name_user;
	}

	public String getNo_idcard() {
		return no_idcard;
	}

	public void setNo_idcard(String no_idcard) {
		this.no_idcard = no_idcard;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}
}