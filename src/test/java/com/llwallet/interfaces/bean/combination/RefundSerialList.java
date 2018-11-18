package com.llwallet.interfaces.bean.combination;

import java.io.Serializable;

public class RefundSerialList implements Serializable {
	private static final long serialVersionUID = 1L;
	private String refund_custId;
	private String cust_type;
	private String orign_paytype;
	private String refund_amt;

	public String getRefund_custId() {
		return refund_custId;
	}

	public void setRefund_custId(String refund_custId) {
		this.refund_custId = refund_custId;
	}

	public String getCust_type() {
		return cust_type;
	}

	public void setCust_type(String cust_type) {
		this.cust_type = cust_type;
	}

	public String getOrign_paytype() {
		return orign_paytype;
	}

	public void setOrign_paytype(String orign_paytype) {
		this.orign_paytype = orign_paytype;
	}

	public String getRefund_amt() {
		return refund_amt;
	}

	public void setRefund_amt(String refund_amt) {
		this.refund_amt = refund_amt;
	}
}