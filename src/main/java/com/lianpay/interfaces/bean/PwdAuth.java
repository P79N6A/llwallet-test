package com.lianpay.interfaces.bean;

public class PwdAuth extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String pwd_pay;
	private String no_idcard;
	private String flag_check;
	private String mob_bind;
	
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
}
