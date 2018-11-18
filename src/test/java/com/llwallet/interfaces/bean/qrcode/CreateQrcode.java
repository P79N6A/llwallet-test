package com.llwallet.interfaces.bean.qrcode;

import com.llwallet.interfaces.bean.BaseBean;

public class CreateQrcode extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String type_user;
	private String qrcode_data;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getType_user() {
		return type_user;
	}

	public void setType_user(String type_user) {
		this.type_user = type_user;
	}
	
	public String getQrcode_data() {
		return qrcode_data;
	}

	public void setQrcode_data(String qrcode_data) {
		this.qrcode_data = qrcode_data;
	}
}