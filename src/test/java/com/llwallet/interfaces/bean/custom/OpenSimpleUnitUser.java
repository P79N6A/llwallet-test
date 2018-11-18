package com.llwallet.interfaces.bean.custom;

import com.llwallet.interfaces.bean.BaseBean;

public class OpenSimpleUnitUser extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String mob_bind;
	private String token;
	private String name_user;
	private String type_idcard;
	private String exp_idcard;
	private String type_expidcard;
	private String no_idcard;
	private String name_agent;
	private String idno_agent;
	private String exp_idcard_agent;
	private String type_agentexpidcard;
	private String pwd_login;
	private String pwd_pay;
	private String risk_item;
	private String random_key;

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

	public String getNo_idcard() {
		return no_idcard;
	}

	public void setNo_idcard(String no_idcard) {
		this.no_idcard = no_idcard;
	}

	public String getName_agent() {
		return name_agent;
	}

	public void setName_agent(String name_agent) {
		this.name_agent = name_agent;
	}

	public String getIdno_agent() {
		return idno_agent;
	}

	public void setIdno_agent(String idno_agent) {
		this.idno_agent = idno_agent;
	}

	public String getExp_idcard_agent() {
		return exp_idcard_agent;
	}

	public void setExp_idcard_agent(String exp_idcard_agent) {
		this.exp_idcard_agent = exp_idcard_agent;
	}

	public String getType_agentexpidcard() {
		return type_agentexpidcard;
	}

	public void setType_agentexpidcard(String type_agentexpidcard) {
		this.type_agentexpidcard = type_agentexpidcard;
	}

	public String getPwd_login() {
		return pwd_login;
	}

	public void setPwd_login(String pwd_login) {
		this.pwd_login = pwd_login;
	}

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getRandom_key() {
		return random_key;
	}

	public void setRandom_key(String random_key) {
		this.random_key = random_key;
	}

}