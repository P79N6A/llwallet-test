package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class UserBusiQuery extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String busi_user;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBusi_user() {
		return busi_user;
	}

	public void setBusi_user(String busi_user) {
		this.busi_user = busi_user;
	}
}