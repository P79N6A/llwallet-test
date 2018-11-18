package com.account.interfaces.bean;

import com.llwallet.interfaces.bean.BaseBean;

public class BalanceQuery extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}