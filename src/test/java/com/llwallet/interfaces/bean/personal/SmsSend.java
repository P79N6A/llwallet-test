package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class SmsSend extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String mob_bind;
	private String flag_send;
	private String num_license;

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

	public String getFlag_send() {
		return flag_send;
	}

	public void setFlag_send(String flag_send) {
		this.flag_send = flag_send;
	}

	public String getNum_license() {
		return num_license;
	}

	public void setNum_license(String num_license) {
		this.num_license = num_license;
	}

}