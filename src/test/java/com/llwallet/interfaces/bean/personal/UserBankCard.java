package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class UserBankCard extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String pay_type;
	private String no_agree;
	private String card_no;
	private String cashcard_flag;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCashcard_flag() {
		return cashcard_flag;
	}

	public void setCashcard_flag(String cashcard_flag) {
		this.cashcard_flag = cashcard_flag;
	}
}