package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class SecuredPayConfirm extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String secured_partner;
	private String api_version;
	private String no_confirm;
	private String dt_confirm;
	private String money_order;
	private String col_custid;
	private String col_custtype;
	private String fill_sellerInfo;
	private String busi_partner;
	private String col_memo;
	private String no_order;
	private String dt_order;
	private String oid_paybill;

	public String getSecured_partner() {
		return secured_partner;
	}

	public void setSecured_partner(String secured_partner) {
		this.secured_partner = secured_partner;
	}

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getNo_confirm() {
		return no_confirm;
	}

	public void setNo_confirm(String no_confirm) {
		this.no_confirm = no_confirm;
	}

	public String getDt_confirm() {
		return dt_confirm;
	}

	public void setDt_confirm(String dt_confirm) {
		this.dt_confirm = dt_confirm;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
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

	public String getFill_sellerInfo() {
		return fill_sellerInfo;
	}

	public void setFill_sellerInfo(String fill_sellerInfo) {
		this.fill_sellerInfo = fill_sellerInfo;
	}

	public String getBusi_partner() {
		return busi_partner;
	}

	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}

	public String getCol_memo() {
		return col_memo;
	}

	public void setCol_memo(String col_memo) {
		this.col_memo = col_memo;
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

}