package com.llwallet.interfaces.bean.other;

import com.llwallet.interfaces.bean.BaseBean;

public class ThreeFactorsSignApply extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String api_version;
	private String timestamp;
	private String user_id;
	private String faceauth_url;
	private String card_no;
	private String risk_item;	

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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFaceauth_url() {
		return faceauth_url;
	}

	public void setFaceauth_url(String faceauth_url) {
		this.faceauth_url = faceauth_url;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

}