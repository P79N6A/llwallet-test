package com.aggregate.interfaces.bean;

import java.io.Serializable;

public class SdkBaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String imei_request;
	private String imsi_request;
	private String mac_request;
	private String machine_id;
	private String sdk_sign_type;
	private String sdk_sign;
	private String flag_chn;
	private String ver_sdk;
	private String latitude;
	private String longitude;
	private String station_info;
	private String app_id;
	private String app_name;
	private String screen;
	private String manufacturer;
	private String os_model;
	private String os_sdk;
	private String os_release;
	private String net_work;
	private String net_type;
	private String jailbroken;

	public String getImei_request() {
		return imei_request;
	}

	public void setImei_request(String imei_request) {
		this.imei_request = imei_request;
	}

	public String getImsi_request() {
		return imsi_request;
	}

	public void setImsi_request(String imsi_request) {
		this.imsi_request = imsi_request;
	}

	public String getMac_request() {
		return mac_request;
	}

	public void setMac_request(String mac_request) {
		this.mac_request = mac_request;
	}

	public String getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}

	public String getSdk_sign_type() {
		return sdk_sign_type;
	}

	public void setSdk_sign_type(String sdk_sign_type) {
		this.sdk_sign_type = sdk_sign_type;
	}

	public String getSdk_sign() {
		return sdk_sign;
	}

	public void setSdk_sign(String sdk_sign) {
		this.sdk_sign = sdk_sign;
	}

	public String getFlag_chn() {
		return flag_chn;
	}

	public void setFlag_chn(String flag_chn) {
		this.flag_chn = flag_chn;
	}

	public String getVer_sdk() {
		return ver_sdk;
	}

	public void setVer_sdk(String ver_sdk) {
		this.ver_sdk = ver_sdk;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getStation_info() {
		return station_info;
	}

	public void setStation_info(String station_info) {
		this.station_info = station_info;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getOs_model() {
		return os_model;
	}

	public void setOs_model(String os_model) {
		this.os_model = os_model;
	}

	public String getOs_sdk() {
		return os_sdk;
	}

	public void setOs_sdk(String os_sdk) {
		this.os_sdk = os_sdk;
	}

	public String getOs_release() {
		return os_release;
	}

	public void setOs_release(String os_release) {
		this.os_release = os_release;
	}

	public String getNet_work() {
		return net_work;
	}

	public void setNet_work(String net_work) {
		this.net_work = net_work;
	}

	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	public String getJailbroken() {
		return jailbroken;
	}

	public void setJailbroken(String jailbroken) {
		this.jailbroken = jailbroken;
	}
}