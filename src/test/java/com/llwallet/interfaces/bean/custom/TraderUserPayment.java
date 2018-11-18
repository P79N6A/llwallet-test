package com.llwallet.interfaces.bean.custom;

import com.llwallet.interfaces.bean.BaseBean;

public class TraderUserPayment extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String pay_oidpartner;
	private String col_userid;
	private String busi_partner;
	private String money_order;
	private String risk_item;
	private String info_order;
	private String proc_src;
	private String name_goods;
	private String dt_order;
	private String no_order;

	public String getPay_oidpartner() {
		return pay_oidpartner;
	}

	public void setPay_oidpartner(String pay_oidpartner) {
		this.pay_oidpartner = pay_oidpartner;
	}

	public String getCol_userid() {
		return col_userid;
	}

	public void setCol_userid(String col_userid) {
		this.col_userid = col_userid;
	}

	public String getBusi_partner() {
		return busi_partner;
	}

	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getProc_src() {
		return proc_src;
	}

	public void setProc_src(String proc_src) {
		this.proc_src = proc_src;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}
}