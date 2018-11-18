package com.llwallet.interfaces.bean.other;

import com.llwallet.interfaces.bean.BaseBean;

public class SpayApply extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String name_user;
	private String busi_partner;
	private String no_order;
	private String dt_order;
	private String name_goods;
	private String info_order;
	private String money_order;
	private String notify_url;
	private String return_url;
	private String risk_item;
	private String pay_type;
	private String id_type;
	private String id_no;
	private String col_oidpartner;
	private String col_userid;
	private String shareing_data;
	private String secured_partner;
	private String buyer_confirm_valid;
	private String seller_send_valid;
	private Long valid_order;

	public Long getValid_order() {
		return valid_order;
	}

	public void setValid_order(Long valid_order) {
		this.valid_order = valid_order;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName_user() {
		return name_user;
	}

	public void setName_user(String name_user) {
		this.name_user = name_user;
	}

	public String getBusi_partner() {
		return busi_partner;
	}

	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

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

	public String getBuyer_confirm_valid() {
		return buyer_confirm_valid;
	}

	public void setBuyer_confirm_valid(String buyer_confirm_valid) {
		this.buyer_confirm_valid = buyer_confirm_valid;
	}

	public String getSeller_send_valid() {
		return seller_send_valid;
	}

	public void setSeller_send_valid(String seller_send_valid) {
		this.seller_send_valid = seller_send_valid;
	}
}