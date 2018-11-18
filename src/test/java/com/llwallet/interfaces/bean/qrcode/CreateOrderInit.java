package com.llwallet.interfaces.bean.qrcode;

import com.llwallet.interfaces.bean.BaseBean;

public class CreateOrderInit extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String no_order;
	private String oid_biz;
	private String money_order;
	private String user_id;
	private String name_goods;
	private String barcode;
	private String dt_order;
	private String notify_url;
	private String info_order;
	private String memo2;
	private String pay_type;
	private String risk_item;
	private String dimension_url;

	public String getDimension_url() {
		return dimension_url;
	}

	public void setDimension_url(String dimension_url) {
		this.dimension_url = dimension_url;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getOid_biz() {
		return oid_biz;
	}

	public void setOid_biz(String oid_biz) {
		this.oid_biz = oid_biz;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

}