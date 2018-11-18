package com.account.interfaces.bean;

import com.llwallet.interfaces.bean.BaseBean;

public class AccountBillQuery extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String no_order;
	
	public String getNo_order() {
		return no_order;
	}
	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}
}