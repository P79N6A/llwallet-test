package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class BankCardPay extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String token;
	private String no_order;
	private String money_order;
	private String col_oidpartner;
	private String verify_code;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getCol_oidpartner() {
		return col_oidpartner;
	}

	public void setCol_oidpartner(String col_oidpartner) {
		this.col_oidpartner = col_oidpartner;
	}

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}
}