package com.llwallet.interfaces.dao;

import com.tools.db.Oracle;
import com.tools.db.MySql;
import java.io.UnsupportedEncodingException;
import com.lianlian.crypt.exception.AESException;
import com.lianlian.crypt.service.IAESCryptService;

public class DbUtils {

	/**
	 * 查询用户号
	 * 
	 * @param oid_traderno
	 * @param exter_name
	 * @return oid_userno、acctno_dirc_flag
	 */
	public TuInfoBelong queryOidUserNo(String oid_traderno, String exter_name) {
		TuInfoBelong tuInfoBelong = new TuInfoBelong();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String oid_userno = db.query("select OID_USERNO from TU_INFO_BELONG where OID_TRADERNO = '" + oid_traderno
					+ "' and EXTER_NAME = '" + exter_name + "'");
			tuInfoBelong.setOid_userno(oid_userno);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tuInfoBelong;
	}

	/**
	 * 查询用户账户号
	 * 
	 * @param oid_traderno
	 * @param exter_name
	 * @return oid_acctno
	 */
	public TuAcctUserbase queryOidAcctNo(String oid_traderno, String exter_name) {
		TuAcctUserbase tuAcctUserbase = new TuAcctUserbase();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String oid_acctno = db
					.query("select oid_acctno from TU_ACCT_USERBASE where OID_USERNO in (select OID_USERNO from TU_INFO_BELONG where OID_TRADERNO = '"
							+ oid_traderno + "' and EXTER_NAME = '" + exter_name + "')");
			tuAcctUserbase.setOid_acctno(oid_acctno);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tuAcctUserbase;
	}

	/**
	 * 删除用户开户信息
	 * 
	 * @param oid_userno
	 * 
	 */
	public void deleteUser(String oid_userno) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Delete("delete from TU_ACCT_USERBASE where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_USER_PWDPRQ where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_INFO_MOBILE where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_INFO_EMAIL where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_INFO_SECURITY where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_INFO_LOGIN where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_INFO_CERT where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_UNIT_APPENDINFO where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_REAL_AUTH where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_PWDPRQ_CHECK where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_INFO_BELONG where OID_USERNO = '" + oid_userno + "'");
			db.Delete("delete from TU_INFO_USERBASE where OID_USERNO = '" + oid_userno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询短信验证码
	 * 
	 * @param tno_smscd
	 * @return cd_smscd
	 */
	public TuUserCheck queryCdSmscd(String tno_smscd) {
		TuUserCheck tuUserCheck = new TuUserCheck();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String cd_smscd = db.query("select CD_SMSCD from(select * from TU_USER_CHECK t where tno_smscd = '"
					+ tno_smscd + "' and dt_smscdgen>sysdate-5/1440 order by dt_smscdgen desc) where rownum=1");
			tuUserCheck.setTno_smscd(cd_smscd);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tuUserCheck;
	}

	/**
	 * 查询绑定手机号
	 * 
	 * @param user_id
	 * @return mob_bind
	 */
	public TuInfoMobile queryMob(String user_id) {
		TuInfoMobile tuInfoMobile = new TuInfoMobile();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String mob_bind = db
					.query("select mob_bind from TU_INFO_MOBILE where oid_userno in (select oid_userno from TU_INFO_BELONG where exter_name='"
							+ user_id + "')")
					.trim();
			tuInfoMobile.setMob_bind(mob_bind);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tuInfoMobile;
	}

	/**
	 * 查询签约协议号
	 * 
	 * @param user_id
	 * @return agreementno
	 */
	public TcInfoSigned querySigned(String user_id) {
		TcInfoSigned tcInfoSigned = new TcInfoSigned();
		try {
			Oracle db = Dbconn.getDBConnCust();
			// String agreementno = db.query("select agreementno from
			// DB_FINANCE.TC_INFO_SIGNED where oid_cust='" + user_id + "' and
			// agreement_s='2' and rownum=1");
			String agreementno = db
					.query("select agreementno from (select * from DB_FINANCE.TC_INFO_SIGNED where oid_cust='" + user_id
							+ "' and agreement_s='2' order by agreementno desc) where rownum<2");
			tcInfoSigned.setAgreementno(agreementno);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tcInfoSigned;
	}

	/**
	 * 修改用户图片信息
	 * 
	 * @param oid_userno
	 * 
	 */
	public void updatePhoto(String oid_userno) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update(
					"update tu_corperate_kyc_apply t set t.legal_id_front_img='',t.legal_id_back_img='',t.copy_org='',t.copy_license='',t.open_bank_account_permit_img='',t.agent_cert_front_img='',t.agent_cert_back_img='' where t.OID_USERNO = '"
							+ oid_userno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改用户KYC信息审核状态
	 * 
	 * @param oid_userno
	 * 
	 */
	public void updateUnitUserAuthResult(String oid_userno) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update(
					"update tu_corperate_kyc_apply t set t.auth_result='5' where t.auth_result='3' and t.OID_USERNO = '"
							+ oid_userno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改商户业务额度信息
	 * 
	 * @param oid_partner
	 * 
	 */
	public void updateTraderBusiCtl(String oid_partner) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update(
					"update dbbusi.tb_trader_busi_ctl t set t.pay_type_balancepay='0',t.amt_balancepay_sms='1',t.pay_type_unit_balancepay='0',t.amt_unit_balancepay_sms='1',t.amt_single_cashout='99',t.amt_unit_single_cashout='99' where t.oid_partner='"
							+ oid_partner + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改用户业务额度信息
	 * 
	 * @param user_id
	 * 
	 */
	public void updateUserBusiCtl(String user_id) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update(
					"update dbbusi.tb_user_busi_ctl t set t.amt_day_recharge='-1',t.amt_month_recharge='-1',t.amt_day_trans='-1',t.amt_month_trans='-1',t.amt_day_balancepay='-1',t.amt_month_balancepay='-1',t.amt_day_cashout='-1',t.amt_month_cashout='-1',t.cash_times='-1',t.trans_times='-1',t.nopwdbalancepaytimes='-1',t.nopwdbankcardtimes='-1',t.amtdbalance2othercard='-1',t.amtmbalance2othercard='-1' where t.oid_userno in (select trim(oid_userno) from dbcust.tu_info_login where user_login='"
							+ user_id + "')");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询用户账户信息
	 * 
	 * @param oid_userno
	 * @return stat_acct
	 */
	public TaAcctInfo queryAcctStat(String oid_userno) {
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String results = db
					.query("select stat_acct,pay_pwd,oid_acctno from DBACCT.TA_ACCT_INFO where oid_acctno=(select oid_acctno from DBCUST.TU_ACCT_USERBASE where OID_USERNO = '"
							+ oid_userno + "')");
			taAcctInfo.setStat_acct(results.split("\\;")[0]);
			taAcctInfo.setPay_pwd(results.split("\\;")[1]);
			taAcctInfo.setOid_acctno(results.split("\\;")[2]);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taAcctInfo;
	}

	/**
	 * 查询支付单订单号
	 * 
	 * @param stat_bill，trader_src
	 * @return no_order
	 */
	public TbPayBill queryNoOrder(String stat_bill, String trader_src) {
		TbPayBill tbPayBill = new TbPayBill();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String no_order = db.query("select order_id from dbpay.TB_PAY_BILL where trader_src='" + trader_src
					+ "' and stat_bill='" + stat_bill + "' and dt_billstart>sysdate-1/2 and rownum<2");
			tbPayBill.setNo_order(no_order);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbPayBill;
	}

	/**
	 * 查询支付单处理组号
	 * 
	 * @param
	 * @return
	 */
	public String queryPayBillRunGroupId(String order_id) {
		String groupid = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			groupid = db.query("select t.oid_rungroupid from dbpay.tb_pay_bill t where t.order_id='" + order_id + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupid;
	}
	
	/**
	 * 查询支付单状态
	 * 
	 * @param
	 * @return
	 */
	public String queryPayBillStat(String order_id) {
		String stat_bill = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			stat_bill = db.query("select t.stat_bill from dbpay.tb_pay_bill t where t.order_id='" + order_id + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat_bill;
	}

	/**
	 * 查询担保单状态
	 * 
	 * @param
	 * @return
	 */
	public String querySecuredBillStat(String order_id) {
		String stat = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			stat = db.query("select t.stat from dbpay.tb_secured_transactions t where t.order_id='" + order_id + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat;
	}
	
	/**
	 * 查询支付单状态
	 * 
	 * @param
	 * @return
	 */
	public String queryPayBillStat(String order_id,String AMT) {
		String stat_bill = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			stat_bill = db.query("select t.stat_bill from dbpay.tb_pay_bill t where t.order_id='" + order_id + "' and t.amt_paybill='" + AMT + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat_bill;
	}
	
	/**
	 * 查询支付单状态
	 * 
	 * @param
	 * @return
	 */
	public String queryPayBillStat1(String oid_billno,String AMT,String pay_timetype,String col_accttype) {
		String stat_bill = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			stat_bill = db.query("select t.stat_bill from dbpay.tb_pay_bill t where t.oid_billno='" + oid_billno + "' and t.amt_paybill='" + AMT + "' and t.pay_time_type='" + pay_timetype + "' and t.col_accttype='" + col_accttype + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat_bill;
	}
	
	/**
	 * 查询消费通支付单状态
	 * 
	 * @param
	 * @return
	 */
	public String queryTpayStatBill(String order_id) {
		String stat_bill = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			stat_bill = db
					.query("select t.stat_bill from dbpay.tb_pay_bill t where t.order_id=(select t.oid_paybill from db_finance.tb_tpaybusibill t  where t.no_order='"
							+ order_id + "')");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat_bill;
	}

	/**
	 * 查询商户结算账户
	 * 
	 * @param oid_traderno
	 * @return acctno
	 */
	public String queryTraderSettlementAcct(String oid_traderno) {
		String acctno = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			acctno = db.query("select t.acctno_trader from dbcust.TT_SIGH_BIZINFO t where t.oid_traderno='"
					+ oid_traderno + "' and t.type_acctno='1' and t.cur_code='CNY' ");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acctno;
	}

	/**
	 * 查询商户业务账户
	 * 
	 * @param oid_traderno,oid_biz
	 * @return acctno
	 */
	public String queryTraderBusinessAcct(String oid_traderno, String oid_biz) {
		String acctno = "";
		try {
			Oracle db = Dbconn.getDBConnCust();
			acctno = db.query("select t.acctno_trader from dbcust.TT_SIGH_BIZINFO t where t.oid_traderno='"
					+ oid_traderno + "' and t.oid_biz='" + oid_biz + "' and t.cur_code='CNY'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acctno;
	}

	/**
	 * 查询用户账户
	 * 
	 * @param oid_acctno
	 * @return
	 */
	public TaAcctInfo queryUserAcctInfo(String user_id, String oid_traderno) {
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String results = db
					.query("select t.stat_acct,t.pay_pwd,t.type_acctlog,t.type_bal,t.amt_balcur,t.amt_balaval,t.amt_balfrz,t.amt_lastbal,t.amt_lastaval,t.amt_lastfrz from DBACCT.TA_ACCT_INFO t where oid_acctno= (select oid_acctno from dbcust.tu_acct_userbase where oid_userno=(select oid_userno from dbcust.tu_info_login where user_login='"
							+ user_id + "'  and oid_traderno='" + oid_traderno + "'))");
			taAcctInfo.setStat_acct(results.split("\\;")[0]);
			taAcctInfo.setPay_pwd(results.split("\\;")[1]);
			taAcctInfo.setType_acctlog(results.split("\\;")[2]);
			taAcctInfo.setType_bal(results.split("\\;")[3]);
			taAcctInfo.setAmt_balcur(results.split("\\;")[4]);
			taAcctInfo.setAmt_balaval(results.split("\\;")[5]);
			taAcctInfo.setAmt_balfrz(results.split("\\;")[6]);
			taAcctInfo.setAmt_lastcur(results.split("\\;")[7]);
			taAcctInfo.setAmt_lastaval(results.split("\\;")[8]);
			taAcctInfo.setAmt_lastfrz(results.split("\\;")[9]);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taAcctInfo;
	}

	/**
	 * 查询商户账户
	 * 
	 * @param oid_acctno
	 * @return
	 */
	public TaAcctInfo queryTraderAcctInfo(String oid_acctno) {
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String results = db
					.query("select t.stat_acct,t.pay_pwd,t.type_acctlog,t.type_bal,t.amt_balcur,t.amt_balaval,t.amt_balfrz,t.amt_lastbal,t.amt_lastaval,t.amt_lastfrz from DBACCT.TA_ACCT_INFO t where oid_acctno='"
							+ oid_acctno + "'");
			taAcctInfo.setStat_acct(results.split("\\;")[0]);
			taAcctInfo.setPay_pwd(results.split("\\;")[1]);
			taAcctInfo.setType_acctlog(results.split("\\;")[2]);
			taAcctInfo.setType_bal(results.split("\\;")[3]);
			taAcctInfo.setAmt_balcur(results.split("\\;")[4]);
			taAcctInfo.setAmt_balaval(results.split("\\;")[5]);
			taAcctInfo.setAmt_balfrz(results.split("\\;")[6]);
			taAcctInfo.setAmt_lastcur(results.split("\\;")[7]);
			taAcctInfo.setAmt_lastaval(results.split("\\;")[8]);
			taAcctInfo.setAmt_lastfrz(results.split("\\;")[9]);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taAcctInfo;
	}

	/**
	 * 查询消费通账户
	 * 
	 * @param user_id
	 * @return TbTPayAcct
	 */
	public TbTPayAcct queryTPayAcct(String user_id) {
		TbTPayAcct tbTPayAcct = new TbTPayAcct();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String results = db
					.query("select tpay_acctno,flash_acctno,type_acct,stat_acct,bank_code,no_agree,oid_userno from dbbusi.TB_TPAYACCT where oid_userno in (select substr(oid_userno,0,16) from dbcust.TU_INFO_LOGIN t where user_login='"
							+ user_id + "')");
			tbTPayAcct.setTpay_acctno(results.split("\\;")[0]);
			tbTPayAcct.setFlash_acctno(results.split("\\;")[1]);
			tbTPayAcct.setType_acct(results.split("\\;")[2]);
			tbTPayAcct.setStat_acct(results.split("\\;")[3]);
			tbTPayAcct.setBank_code(results.split("\\;")[4]);
			tbTPayAcct.setNo_agree(results.split("\\;")[5]);
			tbTPayAcct.setOid_userno(results.split("\\;")[6]);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbTPayAcct;
	}

	/**
	 * 查询用户消费通业务状态
	 * 
	 * @param user_id
	 * @return TbTUserBusi
	 */
	public TbTUserBusi queryTUserBusi(String user_id) {
		TbTUserBusi tbTUserBusi = new TbTUserBusi();
		try {
			Oracle db = Dbconn.getDBConnCust();
			String results = db
					.query("select stat_busi from dbbusi.TB_TUSERBUSI where oid_userno in (select substr(oid_userno,0,16) from dbcust.TU_INFO_LOGIN t where user_login='"
							+ user_id + "') order by dt_create desc ");
			tbTUserBusi.setStat_busi(results.split("\\;")[0]);
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbTUserBusi;
	}

	/**
	 * 更新渠道状态
	 * 
	 * @param
	 * @return
	 */
	public void updateChnlBankState(String status, String oidChnl, String bankCode) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update db_finance.CHNL_BANKLIST t set t.STATUS='" + status + "' where t.oid_chnl='" + oidChnl
					+ "' and t.BANKCODE='" + bankCode + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新请求IP限制
	 * 
	 * @param
	 * @return
	 */
	public void updateIpRequest(String ipRequest, String oidTraderNo) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbbusi.TB_TRADERPARA t set t.ip_request='" + ipRequest + "' where t.oid_traderno='"
					+ oidTraderNo + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新商户状态
	 * 
	 * @param
	 * @return
	 */
	public void updateTraderStatus(String stat_trader, String oidTraderNo) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbbusi.TB_TRADERPARA t set t.enable_flag='" + stat_trader + "' where t.oid_traderno='"
					+ oidTraderNo + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新用户状态
	 * 
	 * @param user_id
	 * 
	 */
	public void updateUserStat(String stat_user, String user_id) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbcust.TU_INFO_USERBASE t set t.stat_user='" + stat_user
					+ "' where t.oid_userno = (select oid_userno from dbcust.tu_info_login where user_login='" + user_id
					+ "')");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新用户消费通业务状态
	 * 
	 * @param user_id
	 * 
	 */
	public void updateUserBusiStat(String stat_busi, String user_id) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbbusi.TB_TUSERBUSI t set t.stat_busi='" + stat_busi
					+ "' where t.oid_userno in (select trim(oid_userno) from dbcust.tu_info_login where user_login='"
					+ user_id + "')");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新用户消费通账户状态
	 * 
	 * @param user_id
	 * 
	 */
	public void updateUserBusiAcctStat(String stat_acct, String flash_acctno) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbbusi.TB_TPAYACCT t set t.stat_acct='" + stat_acct + "' where t.flash_acctno = '"
					+ flash_acctno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新账户余额
	 * 
	 */
	public void updateAcctBalance(String oid_acctno, String AMT) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbacct.TA_ACCT_INFO t set t.amt_balcur='" + AMT + "'， t.amt_balaval='" + AMT
					+ "' where t.oid_acctno = '" + oid_acctno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新商户授权状态
	 * 
	 * @param response
	 * 
	 */
	public void updateMerchantAuth(String response) {
		try {
			MySql db = Dbconn.getDBConn();
			db.Update("update mock_rules t set t.response='" + response + "' where t.url='/tppay/merchant_auth'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新支付单状态
	 * 
	 * @param oid_billno
	 * 
	 */
	public void updatePayBillStat(String oid_billno, String stat_bill) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbpay.tb_pay_bill t set t.stat_bill='" + stat_bill + "' where t.oid_billno = '"
					+ oid_billno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新担保交易状态
	 * 
	 * @param oid_billno
	 * 
	 */
	public void updateSecuredBillStat(String oid_billno, String stat) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Update("update dbpay.tb_secured_transactions t set t.stat='" + stat + "' where t.oid_billno = '"
					+ oid_billno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除用户虚拟卡账户绑定信息
	 * 
	 * @param oid_userno
	 *            oid_bank
	 * 
	 */
	public void deleteVbAccountBind(String oid_userno, String oid_bank) {
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Delete("delete from DBPAY.TB_VBACCOUNT_BINDING_INFO where BIND_OID_USERNO = '" + oid_userno
					+ "' and  OID_BANK = '" + oid_bank + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除卡签约信息
	 * 
	 * @param aesCryptService
	 *            card_no
	 * @throws AESException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public void deleteCardSigned(IAESCryptService aesCryptService, String card_no)
			throws UnsupportedEncodingException, AESException {
		String cardno = aesCryptService.encrypt(card_no.getBytes("UTF-8"));
		try {
			Oracle db = Dbconn.getDBConnCust();
			db.Delete("delete from DB_FINANCE.TC_INFO_SIGNED where CARDNO = '" + cardno + "'");
			db.Delete("delete from DB_FINANCE.USER_BANK_CARD_INFO where CARD_NUMBER = '" + cardno + "'");
			db.closeDBcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
