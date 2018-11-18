package com.llwallet.interfaces.bean.combination;

import java.io.Serializable;

public class PayeeInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String col_oidpartner;
	private String col_userid;
	private String shareing_data;
	private String secured_partner;

	public String getCol_oidpartner() {
		return col_oidpartner;
	}

	public void setCol_oidpartner(String col_oidpartner) {
		this.col_oidpartner = col_oidpartner;
	}

	public String getCol_userid() {
		return col_userid;
	}

	public void setCol_userid(String col_userid) {
		this.col_userid = col_userid;
	}

	public String getShareing_data() {
		return shareing_data;
	}

	public void setShareing_data(String shareing_data) {
		this.shareing_data = shareing_data;
	}

	public String getSecured_partner() {
		return secured_partner;
	}

	public void setSecured_partner(String secured_partner) {
		this.secured_partner = secured_partner;
	}

}