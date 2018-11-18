package com.llwallet.interfaces.bean.custom;

import com.llwallet.interfaces.bean.BaseBean;

public class SecuredPrepayModify extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String secured_partner;
	private String api_version;
	private String no_order;
	private String dt_order;
	private String oid_paybill;
	private String orign_col_custtype;
	private String orign_col_custid;
	private String orign_col_biz;
	private String dest_col_custtype;
	private String dest_col_custid;
	private String dest_col_biz;

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

	public String getOrign_col_custtype() {
		return orign_col_custtype;
	}

	public void setOrign_col_custtype(String orign_col_custtype) {
		this.orign_col_custtype = orign_col_custtype;
	}

	public String getOrign_col_custid() {
		return orign_col_custid;
	}

	public void setOrign_col_custid(String orign_col_custid) {
		this.orign_col_custid = orign_col_custid;
	}

	public String getOrign_col_biz() {
		return orign_col_biz;
	}

	public void setOrign_col_biz(String orign_col_biz) {
		this.orign_col_biz = orign_col_biz;
	}

	public String getDest_col_custtype() {
		return dest_col_custtype;
	}

	public void setDest_col_custtype(String dest_col_custtype) {
		this.dest_col_custtype = dest_col_custtype;
	}

	public String getDest_col_custid() {
		return dest_col_custid;
	}

	public void setDest_col_custid(String dest_col_custid) {
		this.dest_col_custid = dest_col_custid;
	}

	public String getDest_col_biz() {
		return dest_col_biz;
	}

	public void setDest_col_biz(String dest_col_biz) {
		this.dest_col_biz = dest_col_biz;
	}
}