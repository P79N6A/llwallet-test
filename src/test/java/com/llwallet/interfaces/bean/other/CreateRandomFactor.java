package com.llwallet.interfaces.bean.other;

import com.llwallet.interfaces.bean.BaseBean;

public class CreateRandomFactor extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String api_version;
	private String source;
	private String pkg_name;
	private String app_name;

	public String getApi_version() {
		return api_version;
	}

	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPkg_name() {
		return pkg_name;
	}

	public void setPkg_name(String pkg_name) {
		this.pkg_name = pkg_name;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

}