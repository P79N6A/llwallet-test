package com.llwallet.interfaces.bean.enterprise;

import com.llwallet.interfaces.bean.BaseBean;

public class PwdAuth extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String pwd_pay;
	private String num_license;
	private String no_idcard;
	private String flag_check;
	private String mob_bind;
	private String risk_item;
	private String flag_cashcard;
	private String random_key;

	public String getRandom_key() {
		return random_key;
	}

	public void setRandom_key(String random_key) {
		this.random_key = random_key;
	}

	public String getFlag_cashcard() {
		return flag_cashcard;
	}

	public void setFlag_cashcard(String flag_cashcard) {
		this.flag_cashcard = flag_cashcard;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getNum_license() {
		return num_license;
	}

	public void setNum_license(String num_license) {
		this.num_license = num_license;
	}

	public String getNo_idcard() {
		return no_idcard;
	}

	public void setNo_idcard(String no_idcard) {
		this.no_idcard = no_idcard;
	}

	public String getFlag_check() {
		return flag_check;
	}

	public void setFlag_check(String flag_check) {
		this.flag_check = flag_check;
	}

	public String getMob_bind() {
		return mob_bind;
	}

	public void setMob_bind(String mob_bind) {
		this.mob_bind = mob_bind;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}
}