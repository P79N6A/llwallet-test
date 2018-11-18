package com.lianpay.interfaces.bean;

public class BalancePwdPay extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String no_order;
	private String dt_order;
	private String busi_partner;
	private String name_goods;
	private Float money_order;
	private String info_order;
	private String pwd_pay;
	private String notify_url;
	private String risk_item;
	private String shareing_data;
	private String col_oidpartner;
	
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
	public String getBusi_partner() {
		return busi_partner;
	}
	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}
	public String getName_goods() {
		return name_goods;
	}
	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}
	public Float getMoney_order() {
		return money_order;
	}
	public void setMoney_order(Float money_order) {
		this.money_order = money_order;
	}
	public String getInfo_order() {
		return info_order;
	}
	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}
	public String getPwd_pay() {
		return pwd_pay;
	}
	public void setPwd_pay(String pwd_pay) {
		this.pwd_pay = pwd_pay;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getRisk_item() {
		return risk_item;
	}
	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}
	public String getShareing_data() {
		return shareing_data;
	}
	public void setShareing_data(String shareing_data) {
		this.shareing_data = shareing_data;
	}
	public String getCol_oidpartner() {
		return col_oidpartner;
	}
	public void setCol_oidpartner(String col_oidpartner) {
		this.col_oidpartner = col_oidpartner;
	}
}
