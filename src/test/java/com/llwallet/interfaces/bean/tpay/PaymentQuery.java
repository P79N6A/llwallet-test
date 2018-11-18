package com.llwallet.interfaces.bean.tpay;

import com.llwallet.interfaces.bean.BaseBean;

public class PaymentQuery extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String version;
	private String timestamp;
	private String oid_paybill;
	private String no_order;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

}