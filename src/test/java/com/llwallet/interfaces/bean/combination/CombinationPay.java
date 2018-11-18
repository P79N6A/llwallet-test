package com.llwallet.interfaces.bean.combination;

import com.llwallet.interfaces.bean.BaseBean;

public class CombinationPay extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String api_version;
	private String timestamp;
	private String busi_partner;
	private String no_order;
	private String dt_order;
	private String money_order;
	private String name_goods;
	private String info_order;
	private String valid_order;	
	private String notify_url;
	private String faceauth_url;
	private String pwd_pay;
	private String risk_item;
	private PayerInfo payerInfo;
	private PayeeInfo payeeInfo;
	private String random_key;

	public String getRandom_key() {
		return random_key;
	}

	public void setRandom_key(String random_key) {
		this.random_key = random_key;
	}	

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getBusi_partner() {
		return busi_partner;
	}

	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
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

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getValid_order() {
		return valid_order;
	}

	public void setValid_order(String valid_order) {
		this.valid_order = valid_order;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getFaceauth_url() {
		return faceauth_url;
	}

	public void setFaceauth_url(String faceauth_url) {
		this.faceauth_url = faceauth_url;
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

	public PayerInfo getPayerInfo() {
		return payerInfo;
	}

	public void setPayerInfo(PayerInfo payerInfo) {
		this.payerInfo = payerInfo;
	}

	public PayeeInfo getPayeeInfo() {
		return payeeInfo;
	}

	public void setPayeeInfo(PayeeInfo payeeInfo) {
		this.payeeInfo = payeeInfo;
	}

}