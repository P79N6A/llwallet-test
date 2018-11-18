package com.llwallet.interfaces.bean.enterprise;

import com.llwallet.interfaces.bean.BaseBean;

public class UploadUnitPhoto extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String notify_url;
	// private String front_card;
	// private String back_card;
	// private String copy_license;
	// private String copy_org;
	// private String bank_openlicense;

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}