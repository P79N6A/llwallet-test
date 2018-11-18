package com.llwallet.interfaces.bean.tpay;

import com.llwallet.interfaces.bean.BaseBean;

public class CreateQr extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String version;
	private String user_id;
	private String timestamp;
	private String auth_flag;
	private String auth_amt;
	private String pay_partner;
	private String no_order;
	private String dt_order;
	private String qr_validtime;
	private RiskInfo riskInfo;
	
	public RiskInfo getRiskInfo() {
		return riskInfo;
	}

	public void setRiskInfo(RiskInfo riskInfo) {
		this.riskInfo = riskInfo;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getAuth_flag() {
		return auth_flag;
	}

	public void setAuth_flag(String auth_flag) {
		this.auth_flag = auth_flag;
	}

	public String getAuth_amt() {
		return auth_amt;
	}

	public void setAuth_amt(String auth_amt) {
		this.auth_amt = auth_amt;
	}

	public String getPay_partner() {
		return pay_partner;
	}

	public void setPay_partner(String pay_partner) {
		this.pay_partner = pay_partner;
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

	public String getQr_validtime() {
		return qr_validtime;
	}

	public void setQr_validtime(String qr_validtime) {
		this.qr_validtime = qr_validtime;
	}

//	public String getRiskInfo() {
//		return riskInfo;
//	}
//
//	public void setRiskInfo(String riskInfo) {
//		this.riskInfo = riskInfo;
//	}
}