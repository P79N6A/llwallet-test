package com.llwallet.interfaces.dao;

public class TaAcctInfo {

	private String stat_acct;
	private String pay_pwd;
	private String type_acctlog;
	private String type_bal;
	private String amt_balcur;
	private String amt_balaval;
	private String amt_balfrz;
	private String amt_lastcur;
	private String amt_lastaval;
	private String amt_lastfrz;
	private String oid_acctno;

	public String getOid_acctno() {
		return oid_acctno;
	}

	public void setOid_acctno(String oid_acctno) {
		this.oid_acctno = oid_acctno;
	}

	public String getStat_acct() {
		return stat_acct;
	}

	public void setStat_acct(String stat_acct) {
		this.stat_acct = stat_acct;
	}

	public String getPay_pwd() {
		return pay_pwd;
	}

	public void setPay_pwd(String pay_pwd) {
		this.pay_pwd = pay_pwd;
	}

	public String getType_acctlog() {
		return type_acctlog;
	}

	public void setType_acctlog(String type_acctlog) {
		this.type_acctlog = type_acctlog;
	}

	public String getType_bal() {
		return type_bal;
	}

	public void setType_bal(String type_bal) {
		this.type_bal = type_bal;
	}

	public String getAmt_balcur() {
		return amt_balcur;
	}

	public void setAmt_balcur(String amt_balcur) {
		this.amt_balcur = amt_balcur;
	}

	public String getAmt_balaval() {
		return amt_balaval;
	}

	public void setAmt_balaval(String amt_balaval) {
		this.amt_balaval = amt_balaval;
	}

	public String getAmt_balfrz() {
		return amt_balfrz;
	}

	public void setAmt_balfrz(String amt_balfrz) {
		this.amt_balfrz = amt_balfrz;
	}

	public String getAmt_lastcur() {
		return amt_lastcur;
	}

	public void setAmt_lastcur(String amt_lastcur) {
		this.amt_lastcur = amt_lastcur;
	}

	public String getAmt_lastaval() {
		return amt_lastaval;
	}

	public void setAmt_lastaval(String amt_lastaval) {
		this.amt_lastaval = amt_lastaval;
	}

	public String getAmt_lastfrz() {
		return amt_lastfrz;
	}

	public void setAmt_lastfrz(String amt_lastfrz) {
		this.amt_lastfrz = amt_lastfrz;
	}

}