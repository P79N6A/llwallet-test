package com.llwallet.interfaces.bean.tpay;

public class RiskInfo {
	private String device_type;
	private String source_ip;
	private String device_location;
	private String fulldevice_number;
	private String device_id;

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getSource_ip() {
		return source_ip;
	}

	public void setSource_ip(String source_ip) {
		this.source_ip = source_ip;
	}

	public String getDevice_location() {
		return device_location;
	}

	public void setDevice_location(String device_location) {
		this.device_location = device_location;
	}

	public String getFulldevice_number() {
		return fulldevice_number;
	}

	public void setFulldevice_number(String fulldevice_number) {
		this.fulldevice_number = fulldevice_number;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
}