package com.llwallet.interfaces.bean.custom;

import com.llwallet.interfaces.bean.BaseBean;

public class SecpayRefund extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String no_refund;
	private String dt_refund;
	private Float money_refund;
	private String no_order;
	private String dt_order;
	private String oid_paybill;
	private String notify_url;
	private String col_partner;
	private String col_custid;
	private String col_custtype;

	public String getNo_refund() {
		return no_refund;
	}

	public void setNo_refund(String no_refund) {
		this.no_refund = no_refund;
	}

	public String getDt_refund() {
		return dt_refund;
	}

	public void setDt_refund(String dt_refund) {
		this.dt_refund = dt_refund;
	}

	public Float getMoney_refund() {
		return money_refund;
	}

	public void setMoney_refund(Float money_refund) {
		this.money_refund = money_refund;
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

	public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getCol_partner() {
		return col_partner;
	}

	public void setCol_partner(String col_partner) {
		this.col_partner = col_partner;
	}

	public String getCol_custid() {
		return col_custid;
	}

	public void setCol_custid(String col_custid) {
		this.col_custid = col_custid;
	}

	public String getCol_custtype() {
		return col_custtype;
	}

	public void setCol_custtype(String col_custtype) {
		this.col_custtype = col_custtype;
	}

}