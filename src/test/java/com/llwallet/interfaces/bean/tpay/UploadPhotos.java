package com.llwallet.interfaces.bean.tpay;

import com.llwallet.interfaces.bean.BaseBean;

public class UploadPhotos extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String version;
	private String user_id;
	// private String front_card;
	// private String back_card;

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

}