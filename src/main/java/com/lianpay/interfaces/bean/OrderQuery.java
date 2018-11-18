package com.lianpay.interfaces.bean;

public class OrderQuery extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String no_order;
	private String dt_order;
	private String oid_paybill;
	private String type_dc;
	private String query_version;
	
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
	public String getOid_paybill() {
		return oid_paybill;
	}
	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}
	public String getType_dc() {
		return type_dc;
	}
	public void setType_dc(String type_dc) {
		this.type_dc = type_dc;
	}
	public String getQuery_version() {
		return query_version;
	}
	public void setQuery_version(String query_version) {
		this.query_version = query_version;
	}
}
