package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class BankPayPwdSet extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String card_no;
	private String bind_mob;
	private String acct_name;
	private String id_type;
	private String id_no;
	private String vali_date;
	private String cvv2;
	private String pay_type;
	private String pwd_pay;
	private String risk_item;
	private String flag_cashcard;	// 提现卡标识 0 通过提现卡去判断签约信息	默认是绑卡找回支付密码
	private String random_key;

	public String getRandom_key() {
		return random_key;
	}

	public void setRandom_key(String random_key) {
		this.random_key = random_key;
	}
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getBind_mob() {
		return bind_mob;
	}

	public void setBind_mob(String bind_mob) {
		this.bind_mob = bind_mob;
	}

	public String getAcct_name() {
		return acct_name;
	}

	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getVali_date() {
		return vali_date;
	}

	public void setVali_date(String vali_date) {
		this.vali_date = vali_date;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getPwd_pay() {
		return pwd_pay;
	}

	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getFlag_cashcard() {
		return flag_cashcard;
	}

	public void setFlag_cashcard(String flag_cashcard) {
		this.flag_cashcard = flag_cashcard;
	}
	
}