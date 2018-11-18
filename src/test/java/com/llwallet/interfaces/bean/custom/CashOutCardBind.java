package com.llwallet.interfaces.bean.custom;

import com.llwallet.interfaces.bean.BaseBean;

public class CashOutCardBind extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String card_no;
	private String bind_mob;
	private String acct_name;
	private String id_no;
	private String pwd_pay;
	private String risk_item;
	private String random_key;

	public String getRandom_key() {
		return random_key;
	}

	public void setRandom_key(String random_key) {
		this.random_key = random_key;
	}	
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getAcct_name() {
		return acct_name;
	}

	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
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
}