package com.llwallet.interfaces.bean.combination;

import java.io.Serializable;

public class PayerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String coupon_partner;
	private String amt_coupon;
	private String card_no;
	private String cvv2;
	private String vali_date;
	private String bind_mob;
	private String acct_name;
	private String id_no;
	private String id_type;
	private String pay_type;
	private String amt_paytype;
	private String appid;// 微信app支付需要传
	private String openid;// 微信公众号支付需要传
	private String no_agree;
	private String amt_balance;
	private String channel_valid_order;

	public String getChannel_valid_order() {
		return channel_valid_order;
	}

	public void setChannel_valid_order(String channel_valid_order) {
		this.channel_valid_order = channel_valid_order;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCoupon_partner() {
		return coupon_partner;
	}

	public void setCoupon_partner(String coupon_partner) {
		this.coupon_partner = coupon_partner;
	}

	public String getAmt_coupon() {
		return amt_coupon;
	}

	public void setAmt_coupon(String amt_coupon) {
		this.amt_coupon = amt_coupon;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getVali_date() {
		return vali_date;
	}

	public void setVali_date(String vali_date) {
		this.vali_date = vali_date;
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

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getAmt_paytype() {
		return amt_paytype;
	}

	public void setAmt_paytype(String amt_paytype) {
		this.amt_paytype = amt_paytype;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}

	public String getAmt_balance() {
		return amt_balance;
	}

	public void setAmt_balance(String amt_balance) {
		this.amt_balance = amt_balance;
	}

}