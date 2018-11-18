package com.llwallet.interfaces.dao;

public class TbTPayAcct {

	private String tpay_acctno;
	private String flash_acctno;
	private String type_acct;
	private String stat_acct;
	private String bank_code;
	private String no_agree;
	private String oid_userno;

	public String getOid_userno() {
		return oid_userno;
	}

	public void setOid_userno(String oid_userno) {
		this.oid_userno = oid_userno;
	}

	public String getTpay_acctno() {
		return tpay_acctno;
	}

	public void setTpay_acctno(String tpay_acctno) {
		this.tpay_acctno = tpay_acctno;
	}

	public String getFlash_acctno() {
		return flash_acctno;
	}

	public void setFlash_acctno(String flash_acctno) {
		this.flash_acctno = flash_acctno;
	}

	public String getType_acct() {
		return type_acct;
	}

	public void setType_acct(String type_acct) {
		this.type_acct = type_acct;
	}

	public String getStat_acct() {
		return stat_acct;
	}

	public void setStat_acct(String stat_acct) {
		this.stat_acct = stat_acct;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getNo_agree() {
		return no_agree;
	}

	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}
}