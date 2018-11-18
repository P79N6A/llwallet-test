package com.llwallet.interfaces.bean.enterprise;

import com.llwallet.interfaces.bean.BaseBean;

public class Active extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String timestamp;
	private String user_id;
	private String url_return;
	private String risk_item;

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

	public String getUrl_return() {
		return url_return;
	}

	public void setUrl_return(String url_return) {
		this.url_return = url_return;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}	
}