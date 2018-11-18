package com.llwallet.interfaces.bean.qrcode;

import com.llwallet.interfaces.bean.BaseBean;

public class RecogniseQrcode extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String qrcode_data;

	public String getQrcode_data() {
		return qrcode_data;
	}

	public void setQrcode_data(String qrcode_data) {
		this.qrcode_data = qrcode_data;
	}
}