package com.llwallet.interfaces.bean.tpay;

import com.llwallet.interfaces.bean.BaseBean;

public class OpenUser extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String version;
	private String user_id;
	private String timestamp;
	private String name_user;
	private String type_idcard;
	private String no_idcard;
	private String exp_idcard;
	private String type_expidcard;
	private String oid_job;
	private String addr_pro;
	private String addr_city;
	private String addr_dist;
	private String addr_conn;
	private String eml_bind;
	private String risk_item;
	private String card_no;
	private String bind_mob;
	private String open_chnl;
	private String consent_agreement;
	private String pay_type;

	// private String front_card;
	// private String back_card;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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

	public String getEml_bind() {
		return eml_bind;
	}

	public void setEml_bind(String eml_bind) {
		this.eml_bind = eml_bind;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getBind_mob() {
		return bind_mob;
	}

	public void setBind_mob(String bind_mob) {
		this.bind_mob = bind_mob;
	}

	public String getOpen_chnl() {
		return open_chnl;
	}

	public void setOpen_chnl(String open_chnl) {
		this.open_chnl = open_chnl;
	}

	public String getConsent_agreement() {
		return consent_agreement;
	}

	public void setConsent_agreement(String consent_agreement) {
		this.consent_agreement = consent_agreement;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
}