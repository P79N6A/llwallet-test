package com.llwallet.interfaces.dao;

public class TbPayBill {

	private String oid_billno;
	private String trader_src;
	private String stat_bill;
	private String no_order;

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}

	public String getOid_billno() {
		return oid_billno;
	}

	public void setOid_billno(String oid_billno) {
		this.oid_billno = oid_billno;
	}

	public String getTrader_src() {
		return trader_src;
	}

	public void setTrader_src(String trader_src) {
		this.trader_src = trader_src;
	}

	public String getStat_bill() {
		return stat_bill;
	}

	public void setStat_bill(String stat_bill) {
		this.stat_bill = stat_bill;
	}

}