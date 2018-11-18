package com.llwallet.interfaces.bean.combination;

import com.llwallet.interfaces.bean.BaseBean;

public class CombinationQuery extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String api_version;
	private String timestamp;
	private String no_order;
	private String dt_order;
	private String oid_paybill;

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
