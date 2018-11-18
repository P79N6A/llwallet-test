package com.llwallet.interfaces.bean.personal;

import com.llwallet.interfaces.bean.BaseBean;

public class UploadCardPhoto extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String notify_url;
	// private String front_card;
	// private String back_card;

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