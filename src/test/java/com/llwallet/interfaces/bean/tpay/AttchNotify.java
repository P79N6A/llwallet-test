package com.llwallet.interfaces.bean.tpay;

import com.llwallet.interfaces.bean.BaseBean;

public class AttchNotify extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String version;
	private String no_order;
	private String user_id;
	private String timestamp;
	private String qr_no;
	private String result_check;
	private RiskInfo riskInfo;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getQr_no() {
		return qr_no;
	}

	public void setQr_no(String qr_no) {
		this.qr_no = qr_no;
	}

	public String getResult_check() {
		return result_check;
	}

	public void setResult_check(String result_check) {
		this.result_check = result_check;
	}

	public RiskInfo getRiskInfo() {
		return riskInfo;
	}

	public void setRiskInfo(RiskInfo riskInfo) {
		this.riskInfo = riskInfo;
	}

}