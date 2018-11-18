package com.spay.interfaces.bean;

public class ConfirmPay extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String no_order;
	private String money_final;
	private String user_id;
	private String return_url;

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getMoney_final() {
		return money_final;
	}

	public void setMoney_final(String money_final) {
		this.money_final = money_final;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
}