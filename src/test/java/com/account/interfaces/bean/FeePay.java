package com.account.interfaces.bean;

import com.llwallet.interfaces.bean.BaseBean;

public class FeePay extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String no_order;
	private String dt_order;
	private Float money_order;
	private String name_goods;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public Float getMoney_order() {
		return money_order;
	}

	public void setMoney_order(Float money_order) {
		this.money_order = money_order;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}
}