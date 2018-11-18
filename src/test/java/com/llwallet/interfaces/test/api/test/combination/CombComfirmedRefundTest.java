package com.llwallet.interfaces.test.api.test.combination;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianlian.crypt.service.IAESCryptService;
import com.lianpay.chnl.domain.notify.PaymentProcessReq;
import com.lianpay.chnl.service.notify.PaymentResultNotifyService;
import com.llwallet.interfaces.bean.combination.CombinationPay;
import com.llwallet.interfaces.bean.combination.CombinationPayVerify;
import com.llwallet.interfaces.bean.combination.CombComfirmedRefund;
import com.llwallet.interfaces.bean.combination.PayeeInfo;
import com.llwallet.interfaces.bean.combination.PayerInfo;
import com.llwallet.interfaces.bean.combination.RefundSerialList;
import com.llwallet.interfaces.bean.personal.SecuredPayConfirm;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 组合支付确认后退款测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class CombComfirmedRefundTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@Autowired
	private PaymentResultNotifyService paymentResultNotifyService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	// @Test(description = "组合支付确认后退款测试", threadPoolSize = 2, invocationCount =
	// 2, timeOut = 60000, dataProvider = "combComfirmedRefund")
	@Test(description = "组合支付确认后退款测试", timeOut = 60000, dataProvider = "combComfirmedRefund")
	public void combComfirmedRefund(Map<String, String> datadriven) throws Exception {

		Thread.sleep(10);
		String refundSerialList1 = datadriven.get("refundSerialList1");
		String refundSerialList2 = datadriven.get("refundSerialList2");
		String refundSerialList3 = datadriven.get("refundSerialList3");
		RefundSerialList refundList1 = new RefundSerialList();
		RefundSerialList refundList2 = new RefundSerialList();
		RefundSerialList refundList3 = new RefundSerialList();
		List<RefundSerialList> refundList = new ArrayList<RefundSerialList>();
		CombComfirmedRefund combComfirmedRefund = new CombComfirmedRefund();
		combComfirmedRefund.setSign_type(datadriven.get("sign_type"));
		combComfirmedRefund.setApi_version(datadriven.get("api_version"));
		combComfirmedRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
		combComfirmedRefund.setCol_oidpartner(datadriven.get("col_oidpartner"));
		combComfirmedRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund")));
		combComfirmedRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund")));
		combComfirmedRefund.setMoney_refund(datadriven.get("money_refund"));
		combComfirmedRefund.setNo_order(datadriven.get("no_order"));
		combComfirmedRefund.setDt_order(datadriven.get("dt_order"));
		combComfirmedRefund.setOid_paybill(datadriven.get("oid_paybill"));
		combComfirmedRefund.setNotify_url(datadriven.get("notify_url"));
		combComfirmedRefund.setCol_custid(datadriven.get("col_custid"));
		combComfirmedRefund.setCol_custtype(datadriven.get("col_custtype"));
		if (!StringUtils.isBlank(refundSerialList1)) {
			String result1[] = refundSerialList1.split(",");
			refundList1.setRefund_custId(result1[0]);
			refundList1.setCust_type(result1[1]);
			refundList1.setOrign_paytype(result1[2]);
			refundList1.setRefund_amt(result1[3]);
			refundList.add(refundList1);
		}
		if (!StringUtils.isBlank(refundSerialList2)) {
			String result2[] = refundSerialList2.split(",");
			refundList2.setRefund_custId(result2[0]);
			refundList2.setCust_type(result2[1]);
			refundList2.setOrign_paytype(result2[2]);
			refundList2.setRefund_amt(result2[3]);
			refundList.add(refundList2);
		}
		if (!StringUtils.isBlank(refundSerialList3)) {
			String result3[] = refundSerialList3.split(",");
			refundList3.setRefund_custId(result3[0]);
			refundList3.setCust_type(result3[1]);
			refundList3.setOrign_paytype(result3[2]);
			refundList3.setRefund_amt(result3[3]);
			refundList.add(refundList3);
		}
		combComfirmedRefund.setRefundSerialList(refundList);
		combComfirmedRefund.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combComfirmedRefund)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combComfirmedRefund);
		String combComfirmedRefundUrl = Property.get("llwallet.combComfirmedRefund.url");

		String rsp = HttpRequest.httpPostWithJSON(combComfirmedRefundUrl, reqJson);
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt",
				"===============================================");

		Reporter.log("组合支付确认后退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 组合支付确认后退款请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
	}

	@SuppressWarnings({ "rawtypes" })
	@Test(description = "组合支付确认后退款测试", timeOut = 60000, dataProvider = "combComfirmedRefund1")
	public void combComfirmedRefund1(Map<String, String> datadriven) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, datadriven.get("card_no"));
		}

		String balanceUpdate = datadriven.get("balanceUpdate");
		String balanceUpdate1 = datadriven.get("balanceUpdate1");

		try {
			// 组合支付申请
			CombinationPay combinationPay = new CombinationPay();
			PayerInfo payerInfo = new PayerInfo();
			PayeeInfo payeeInfo = new PayeeInfo();
			combinationPay.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
			combinationPay.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
			combinationPay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
			combinationPay.setSign_type(datadriven.get("sign_type"));
			combinationPay.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			combinationPay.setApi_version(datadriven.get("api_version"));
			combinationPay.setBusi_partner(datadriven.get("busi_partner"));
			combinationPay.setMoney_order(datadriven.get("money_order"));
			combinationPay.setName_goods(datadriven.get("name_goods"));
			combinationPay.setInfo_order(datadriven.get("info_order"));
			combinationPay.setValid_order(datadriven.get("valid_order"));
			combinationPay.setNotify_url(datadriven.get("notify_url"));
			combinationPay.setFaceauth_url(datadriven.get("faceauth_url"));
			if (datadriven.get("pwd_pay").length() != 0) {
				combinationPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
			} else {

			}
			combinationPay.setRisk_item(datadriven.get("risk_item"));
			payerInfo.setAmt_balance(datadriven.get("amt_balance"));
			payerInfo.setAmt_coupon(datadriven.get("amt_coupon"));
			payerInfo.setAmt_paytype(datadriven.get("amt_paytype"));
			payerInfo.setAppid(datadriven.get("appid"));
			payerInfo.setBind_mob(datadriven.get("bind_mob"));
			payerInfo.setCard_no(datadriven.get("card_no"));
			payerInfo.setCoupon_partner(datadriven.get("coupon_partner"));
			payerInfo.setCvv2(datadriven.get("cvv2"));
			payerInfo.setNo_agree(datadriven.get("no_agree"));
			payerInfo.setOpenid(datadriven.get("openid"));
			payerInfo.setPay_type(datadriven.get("pay_type"));
			payerInfo.setUser_id(datadriven.get("user_id"));
			payerInfo.setVali_date(datadriven.get("vali_date"));
			combinationPay.setPayerInfo(payerInfo);

			payeeInfo.setCol_oidpartner(datadriven.get("col_oidpartner"));
			payeeInfo.setCol_userid(datadriven.get("col_userid"));
			payeeInfo.setSecured_partner(datadriven.get("secured_partner"));
			payeeInfo.setShareing_data(datadriven.get("shareing_data"));
			combinationPay.setPayeeInfo(payeeInfo);

			combinationPay.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPay)), datadriven.get("key")));
			String reqJson = JSON.toJSONString(combinationPay);
			String combinationPayUrl = Property.get("llwallet.combinationPay.url");

			String rsp = HttpRequest.httpPostWithJSON(combinationPayUrl, reqJson);
			String no_order1 = JSONObject.parseObject(rsp).getString("no_order");
			String dt_order1 = JSONObject.parseObject(rsp).getString("dt_order");
			String oid_paybill1 = JSONObject.parseObject(rsp).getString("oid_paybill");
			System.out.println("no_order------" + no_order1);
			System.out.println("dt_order------" + dt_order1);
			System.out.println("oid_paybill------" + oid_paybill1);
			Thread.sleep(10);

			// 网银异步回调处理成功
			String pay_type = datadriven.get("pay_type");
			if (("0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))
					&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
					|| ("8866".equals(JSONObject.parseObject(rsp).getString("ret_code")))) {
				String oid_rungroupid = null;
				oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order1);
				String amt_paytype = datadriven.get("amt_paytype");
				String amount = new BigDecimal(amt_paytype).multiply(new BigDecimal(100)).toBigInteger().toString();
				System.out.println("amount：" + amount);
				PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
				paymentprocessreq.setStatus("00");
				paymentprocessreq.setPayRungroupid(oid_rungroupid);
				paymentprocessreq.setSettleDate("20180503");
				paymentprocessreq.setBankSerial(no_order1);
				paymentprocessreq.setPayAmount(amount);
				System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
				String rsp4 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
				System.out.println("异步回调返回：" + rsp4);
			}

			// 组合支付验证
			if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
				CombinationPayVerify combinationPayVerify = new CombinationPayVerify();
				combinationPayVerify.setOid_partner(datadriven.get("oid_partner"));
				combinationPayVerify.setSign_type(datadriven.get("sign_type"));
				combinationPayVerify.setUser_id(datadriven.get("user_id"));
				combinationPayVerify.setApi_version(datadriven.get("api_version"));
				combinationPayVerify.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
				combinationPayVerify.setToken(JSONObject.parseObject(rsp).getString("token"));
				combinationPayVerify.setNo_order(no_order1);
				combinationPayVerify.setVerify_code(dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd());
				combinationPayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPayVerify)),
						datadriven.get("key")));
				String reqJsonV = JSON.toJSONString(combinationPayVerify);
				String combinationPayVerifyUrl = Property.get("llwallet.combinationPayVerify.url");
				String rspV = HttpRequest.httpPostWithJSON(combinationPayVerifyUrl, reqJsonV);

				if (("0000".equals(JSONObject.parseObject(rspV).getString("ret_code")) && ("I".equals(pay_type)
						|| "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
						|| ("8866".equals(JSONObject.parseObject(rspV).getString("ret_code")))) {
					String oid_rungroupid = null;
					oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order1);
					String amt_paytype = datadriven.get("amt_paytype");
					String amount = new BigDecimal(amt_paytype).multiply(new BigDecimal(100)).toBigInteger().toString();
					System.out.println("amount：" + amount);
					PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
					paymentprocessreq.setStatus("00");
					paymentprocessreq.setPayRungroupid(oid_rungroupid);
					paymentprocessreq.setSettleDate("20180503");
					paymentprocessreq.setBankSerial(no_order1);
					paymentprocessreq.setPayAmount(amount);
					System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
					String rsp4 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
					System.out.println("异步回调返回：" + rsp4);
				}
			}
			Thread.sleep(10);

			// 担保交易确认
			SecuredPayConfirm securedPayConfirm = new SecuredPayConfirm();
			Map<String, String> params = new HashMap<String, String>();
			Iterator it = datadriven.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if (!entry.getValue().equals("")
						&& StringUtils.startsWith((String) entry.getKey(), "securedPayConfirm.")) {
					params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
				}
			}
			BeanUtils.populate(securedPayConfirm, params);
			if ("get_no_confirm".equals(datadriven.get("securedPayConfirm.no_confirm"))) {
				securedPayConfirm.setNo_confirm(MyConfig.getNoConfirm(datadriven.get("securedPayConfirm.no_confirm")));
			}
			if ("get_dt_confirm".equals(datadriven.get("securedPayConfirm.dt_confirm"))) {
				securedPayConfirm.setDt_confirm(MyConfig.getDtConfirm(datadriven.get("securedPayConfirm.dt_confirm")));
			}
			if ("get".equals(datadriven.get("securedPayConfirm.oid_paybill"))) {
				securedPayConfirm.setOid_paybill(oid_paybill1);
			} else {
				securedPayConfirm.setOid_paybill(datadriven.get("securedPayConfirm.oid_paybill"));
			}
			if ("get".equals(datadriven.get("securedPayConfirm.no_order"))) {
				securedPayConfirm.setNo_order(no_order1);
			} else {
				securedPayConfirm.setNo_order(datadriven.get("securedPayConfirm.no_order"));
			}
			if ("get".equals(datadriven.get("securedPayConfirm.dt_order"))) {
				securedPayConfirm.setDt_order(dt_order1);
			} else {
				securedPayConfirm.setDt_order(datadriven.get("securedPayConfirm.dt_order"));
			}
			securedPayConfirm.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(securedPayConfirm)),
					datadriven.get("securedPayConfirm.key")));
			String reqJson3 = JSON.toJSONString(securedPayConfirm);
			String securedPayConfirmUrl = Property.get("llwallet.securedPayConfirm.url");
			String rsp3 = HttpRequest.httpPostWithJSON(securedPayConfirmUrl, reqJson3);

			if (!StringUtils.isBlank(datadriven.get("payBillStatUpdate"))) {
				dbUtils.updatePayBillStat(oid_paybill1, datadriven.get("payBillStatUpdate"));
			}
			if (!StringUtils.isBlank(datadriven.get("securedBillStatUpdate"))) {
				dbUtils.updateSecuredBillStat(oid_paybill1, datadriven.get("securedBillStatUpdate"));
			}
			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], result[1]);
			}
			if (!StringUtils.isBlank(balanceUpdate1)) {
				String result1[] = balanceUpdate1.split(",");
				dbUtils.updateAcctBalance(result1[0], result1[1]);
			}

			// 退款前余额
			String secured_partner = datadriven.get("secured_partner");
			String oid_partner = datadriven.get("oid_partner");
			String col_partner, col_userid, col_acctno;
			String secured_acctno = dbUtils.queryTraderBusinessAcct(secured_partner, datadriven.get("busi_partner"))
					.trim();

			TaAcctInfo taAcctInfo, taAcctInfoC = new TaAcctInfo();
			String amtBalCur = null, amtBalAval = null, amtBalFrz = null, amtBalCurC = null, amtBalAvalC = null,
					amtBalFrzC = null;
			if (rsp3.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				taAcctInfo = dbUtils.queryTraderAcctInfo(secured_acctno);
				amtBalCur = taAcctInfo.getAmt_balcur();
				amtBalAval = taAcctInfo.getAmt_balaval();
				amtBalFrz = taAcctInfo.getAmt_balfrz();
				Reporter.log("退款前担保商户： " + secured_partner + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额："
						+ amtBalFrz, true);

				if ("1".equals(datadriven.get("col_custtype1"))) {
					col_partner = datadriven.get("col_custid1");
					col_acctno = dbUtils.queryTraderBusinessAcct(col_partner, datadriven.get("busi_partner")).trim();
					taAcctInfoC = dbUtils.queryTraderAcctInfo(col_acctno);
					amtBalCurC = taAcctInfoC.getAmt_balcur();
					amtBalAvalC = taAcctInfoC.getAmt_balaval();
					amtBalFrzC = taAcctInfoC.getAmt_balfrz();
					Reporter.log("退款前收款商户： " + col_partner + "	总余额：" + amtBalCurC + "	可用余额：" + amtBalAvalC
							+ "	冻结余额：" + amtBalFrzC, true);

				} else if ("0".equals(datadriven.get("col_custtype1"))) {
					col_userid = datadriven.get("col_custid1");
					taAcctInfoC = dbUtils.queryUserAcctInfo(col_userid, oid_partner);
					amtBalCurC = taAcctInfoC.getAmt_balcur();
					amtBalAvalC = taAcctInfoC.getAmt_balaval();
					amtBalFrzC = taAcctInfoC.getAmt_balfrz();
					Reporter.log("退款前收款用户： " + col_userid + "	总余额：" + amtBalCurC + "	可用余额：" + amtBalAvalC
							+ "	冻结余额：" + amtBalFrzC, true);
				}

			}
			Thread.sleep(10);

			// 组合支付确认后退款
			String refundSerialList1 = datadriven.get("refundSerialList1");
			String refundSerialList2 = datadriven.get("refundSerialList2");
			String refundSerialList3 = datadriven.get("refundSerialList3");
			RefundSerialList refundList1 = new RefundSerialList();
			RefundSerialList refundList2 = new RefundSerialList();
			RefundSerialList refundList3 = new RefundSerialList();
			List<RefundSerialList> refundList = new ArrayList<RefundSerialList>();
			CombComfirmedRefund combComfirmedRefund = new CombComfirmedRefund();
			combComfirmedRefund.setSign_type(datadriven.get("sign_type1"));
			combComfirmedRefund.setApi_version(datadriven.get("api_version1"));
			combComfirmedRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp1")));
			combComfirmedRefund.setCol_oidpartner(datadriven.get("col_oidpartner1"));
			combComfirmedRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
			combComfirmedRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
			combComfirmedRefund.setMoney_refund(datadriven.get("money_refund1"));
			if ("get".equals(datadriven.get("no_order1"))) {
				combComfirmedRefund.setNo_order(no_order1);
			} else {
				combComfirmedRefund.setNo_order(datadriven.get("no_order1"));
			}
			if ("get".equals(datadriven.get("dt_order1"))) {
				combComfirmedRefund.setDt_order(dt_order1);
			} else {
				combComfirmedRefund.setDt_order(datadriven.get("dt_order1"));
			}
			if ("get".equals(datadriven.get("oid_paybill1"))) {
				combComfirmedRefund.setOid_paybill(oid_paybill1);
			} else {
				combComfirmedRefund.setOid_paybill(datadriven.get("oid_paybill1"));
			}
			combComfirmedRefund.setNotify_url(datadriven.get("notify_url1"));
			combComfirmedRefund.setCol_custid(datadriven.get("col_custid1"));
			combComfirmedRefund.setCol_custtype(datadriven.get("col_custtype1"));
			if (!StringUtils.isBlank(refundSerialList1)) {
				String result1[] = refundSerialList1.split(",");
				refundList1.setRefund_custId(result1[0]);
				refundList1.setCust_type(result1[1]);
				refundList1.setOrign_paytype(result1[2]);
				refundList1.setRefund_amt(result1[3]);
				refundList.add(refundList1);
			}
			if (!StringUtils.isBlank(refundSerialList2)) {
				String result2[] = refundSerialList2.split(",");
				refundList2.setRefund_custId(result2[0]);
				refundList2.setCust_type(result2[1]);
				refundList2.setOrign_paytype(result2[2]);
				refundList2.setRefund_amt(result2[3]);
				refundList.add(refundList2);
			}
			if (!StringUtils.isBlank(refundSerialList3)) {
				String result3[] = refundSerialList3.split(",");
				refundList3.setRefund_custId(result3[0]);
				refundList3.setCust_type(result3[1]);
				refundList3.setOrign_paytype(result3[2]);
				refundList3.setRefund_amt(result3[3]);
				refundList.add(refundList3);
			}
			combComfirmedRefund.setRefundSerialList(refundList);
			combComfirmedRefund.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(combComfirmedRefund)), datadriven.get("key1")));
			String reqJson1 = JSON.toJSONString(combComfirmedRefund);
			String combComfirmedRefundUrl = Property.get("llwallet.combComfirmedRefund.url");
			String rsp1 = HttpRequest.httpPostWithJSON(combComfirmedRefundUrl, reqJson1);
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson3);
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp3);
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp1);
			SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt",
					"===============================================");

			// 支付单状态校验
			if (!StringUtils.isBlank(datadriven.get("expect_PayBillStat"))) {
				String stat_bill = dbUtils.queryPayBillStat(no_order1);
				assert datadriven.get("expect_PayBillStat").equals(stat_bill);
			}
			if (!StringUtils.isBlank(datadriven.get("expect_SecuredBillStat"))) {
				String stat = dbUtils.querySecuredBillStat(no_order1);
				assert datadriven.get("expect_SecuredBillStat").equals(stat);
			}
			// 组合支付确认后退款请求返回检查
			assert rsp1.contains(datadriven.get("expect_return"));
			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
						JSONObject.parseObject(rsp1).getString("sign"));
			}

			// 退款后余额
			TaAcctInfo taAcctInfo1, taAcctInfoC1 = new TaAcctInfo();
			String amtBalCur1, amtBalAval1, amtBalFrz1, amtBalCurC1, amtBalAvalC1, amtBalFrzC1;
			BigDecimal amt1 = null, amt2 = null, amt3, amtC1 = null, amtC2 = null, amtC3;
			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				taAcctInfo1 = dbUtils.queryTraderAcctInfo(secured_acctno);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("退款后担保商户： " + secured_partner + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1
						+ "	冻结余额：" + amtBalFrz1, true);

				amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
				amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
				amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
				Reporter.log("退款后担保商户： " + secured_partner + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3,
						true);
				assert "0".equals(amt1.toString());
				assert "0".equals(amt2.toString());

				if ("1".equals(datadriven.get("col_custtype1"))) {
					col_partner = datadriven.get("col_custid1");
					col_acctno = dbUtils.queryTraderBusinessAcct(col_partner, datadriven.get("busi_partner")).trim();
					taAcctInfoC1 = dbUtils.queryTraderAcctInfo(col_acctno);
					amtBalCurC1 = taAcctInfoC1.getAmt_balcur();
					amtBalAvalC1 = taAcctInfoC1.getAmt_balaval();
					amtBalFrzC1 = taAcctInfoC1.getAmt_balfrz();
					Reporter.log("退款后收款商户： " + col_partner + "	总余额：" + amtBalCurC1 + "	可用余额：" + amtBalAvalC1
							+ "	冻结余额：" + amtBalFrzC1, true);

					amtC1 = new BigDecimal(amtBalCurC).subtract(new BigDecimal(amtBalCurC1));
					amtC2 = new BigDecimal(amtBalAvalC).subtract(new BigDecimal(amtBalAvalC1));
					amtC3 = new BigDecimal(amtBalFrzC).subtract(new BigDecimal(amtBalFrzC1));
					Reporter.log("退款后收款商户： " + col_partner + "	总余额减少：" + amtC1 + "	可用余额减少：" + amtC2 + "	冻结余额减少："
							+ amtC3, true);

					assert amtC1.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_refund1"));
					assert amtC2.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_refund1"));

				} else if ("0".equals(datadriven.get("col_custtype1"))) {
					col_userid = datadriven.get("col_custid1");
					taAcctInfoC1 = dbUtils.queryUserAcctInfo(col_userid, oid_partner);
					amtBalCurC1 = taAcctInfoC1.getAmt_balcur();
					amtBalAvalC1 = taAcctInfoC1.getAmt_balaval();
					amtBalFrzC1 = taAcctInfoC1.getAmt_balfrz();
					Reporter.log("退款后收款用户： " + col_userid + "	总余额：" + amtBalCurC1 + "	可用余额：" + amtBalAvalC1
							+ "	冻结余额：" + amtBalFrzC1, true);

					amtC1 = new BigDecimal(amtBalCurC).subtract(new BigDecimal(amtBalCurC1));
					amtC2 = new BigDecimal(amtBalAvalC).subtract(new BigDecimal(amtBalAvalC1));
					amtC3 = new BigDecimal(amtBalFrzC).subtract(new BigDecimal(amtBalFrzC1));
					Reporter.log("退款后收款用户： " + col_userid + "	总余额减少：" + amtC1 + "	可用余额减少：" + amtC2 + "	冻结余额减少："
							+ amtC3, true);

					assert amtC1.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_refund1"));
					assert amtC2.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_refund1"));
				}
			}

			Reporter.log("组合支付确认后退款测试： " + datadriven.get("comment"), true);
			System.out.println("===============================================");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], "100000000");
			}
			if (!StringUtils.isBlank(balanceUpdate1)) {
				String result1[] = balanceUpdate1.split(",");
				dbUtils.updateAcctBalance(result1[0], "100000000");
			}

		}

	}

	@SuppressWarnings("rawtypes")
	@Test(description = "组合支付确认后退款测试", timeOut = 60000, dataProvider = "combComfirmedRefund2")
	public void combComfirmedRefund2(Map<String, String> datadriven) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, datadriven.get("card_no"));
		}

		// 组合支付申请
		CombinationPay combinationPay = new CombinationPay();
		PayerInfo payerInfo = new PayerInfo();
		PayeeInfo payeeInfo = new PayeeInfo();
		combinationPay.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		combinationPay.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
		combinationPay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		combinationPay.setSign_type(datadriven.get("sign_type"));
		combinationPay.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
		combinationPay.setApi_version(datadriven.get("api_version"));
		combinationPay.setBusi_partner(datadriven.get("busi_partner"));
		combinationPay.setMoney_order(datadriven.get("money_order"));
		combinationPay.setName_goods(datadriven.get("name_goods"));
		combinationPay.setInfo_order(datadriven.get("info_order"));
		combinationPay.setValid_order(datadriven.get("valid_order"));
		combinationPay.setNotify_url(datadriven.get("notify_url"));
		combinationPay.setFaceauth_url(datadriven.get("faceauth_url"));
		if (datadriven.get("pwd_pay").length() != 0) {
			combinationPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {

		}
		combinationPay.setRisk_item(datadriven.get("risk_item"));
		payerInfo.setAmt_balance(datadriven.get("amt_balance"));
		payerInfo.setAmt_coupon(datadriven.get("amt_coupon"));
		payerInfo.setAmt_paytype(datadriven.get("amt_paytype"));
		payerInfo.setAppid(datadriven.get("appid"));
		payerInfo.setBind_mob(datadriven.get("bind_mob"));
		payerInfo.setCard_no(datadriven.get("card_no"));
		payerInfo.setCoupon_partner(datadriven.get("coupon_partner"));
		payerInfo.setCvv2(datadriven.get("cvv2"));
		payerInfo.setNo_agree(datadriven.get("no_agree"));
		payerInfo.setOpenid(datadriven.get("openid"));
		payerInfo.setPay_type(datadriven.get("pay_type"));
		payerInfo.setUser_id(datadriven.get("user_id"));
		payerInfo.setVali_date(datadriven.get("vali_date"));
		combinationPay.setPayerInfo(payerInfo);

		payeeInfo.setCol_oidpartner(datadriven.get("col_oidpartner"));
		payeeInfo.setCol_userid(datadriven.get("col_userid"));
		payeeInfo.setSecured_partner(datadriven.get("secured_partner"));
		payeeInfo.setShareing_data(datadriven.get("shareing_data"));
		combinationPay.setPayeeInfo(payeeInfo);

		combinationPay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationPay);
		String combinationPayUrl = Property.get("llwallet.combinationPay.url");

		String rsp = HttpRequest.httpPostWithJSON(combinationPayUrl, reqJson);
		String no_order1 = JSONObject.parseObject(rsp).getString("no_order");
		String dt_order1 = JSONObject.parseObject(rsp).getString("dt_order");
		String oid_paybill1 = JSONObject.parseObject(rsp).getString("oid_paybill");
		System.out.println("no_order------" + no_order1);
		System.out.println("dt_order------" + dt_order1);
		System.out.println("oid_paybill------" + oid_paybill1);
		Thread.sleep(10);

		// 网银异步回调处理成功
		String pay_type = datadriven.get("pay_type");
		if (("0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
				|| ("8866".equals(JSONObject.parseObject(rsp).getString("ret_code")))) {
			String oid_rungroupid = null;
			oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order1);
			String amt_paytype = datadriven.get("amt_paytype");
			String amount = new BigDecimal(amt_paytype).multiply(new BigDecimal(100)).toBigInteger().toString();
			System.out.println("amount：" + amount);
			PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
			paymentprocessreq.setStatus("00");
			paymentprocessreq.setPayRungroupid(oid_rungroupid);
			paymentprocessreq.setSettleDate("20180503");
			paymentprocessreq.setBankSerial(no_order1);
			paymentprocessreq.setPayAmount(amount);
			System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
			String rsp4 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
			System.out.println("异步回调返回：" + rsp4);
		}

		// 组合支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			CombinationPayVerify combinationPayVerify = new CombinationPayVerify();
			combinationPayVerify.setOid_partner(datadriven.get("oid_partner"));
			combinationPayVerify.setSign_type(datadriven.get("sign_type"));
			combinationPayVerify.setUser_id(datadriven.get("user_id"));
			combinationPayVerify.setApi_version(datadriven.get("api_version"));
			combinationPayVerify.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			combinationPayVerify.setToken(JSONObject.parseObject(rsp).getString("token"));
			combinationPayVerify.setNo_order(no_order1);
			combinationPayVerify.setVerify_code(dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd());
			combinationPayVerify.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPayVerify)), datadriven.get("key")));
			String reqJsonV = JSON.toJSONString(combinationPayVerify);
			String combinationPayVerifyUrl = Property.get("llwallet.combinationPayVerify.url");
			String rspV = HttpRequest.httpPostWithJSON(combinationPayVerifyUrl, reqJsonV);

			if (("0000".equals(JSONObject.parseObject(rspV).getString("ret_code"))
					&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
					|| ("8866".equals(JSONObject.parseObject(rspV).getString("ret_code")))) {
				String oid_rungroupid = null;
				oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order1);
				String amt_paytype = datadriven.get("amt_paytype");
				String amount = new BigDecimal(amt_paytype).multiply(new BigDecimal(100)).toBigInteger().toString();
				System.out.println("amount：" + amount);
				PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
				paymentprocessreq.setStatus("00");
				paymentprocessreq.setPayRungroupid(oid_rungroupid);
				paymentprocessreq.setSettleDate("20180503");
				paymentprocessreq.setBankSerial(no_order1);
				paymentprocessreq.setPayAmount(amount);
				System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
				String rsp4 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
				System.out.println("异步回调返回：" + rsp4);
			}
		}
		Thread.sleep(10);

		// 担保交易确认
		SecuredPayConfirm securedPayConfirm = new SecuredPayConfirm();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = datadriven.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "securedPayConfirm.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(securedPayConfirm, params);
		if ("get_no_confirm".equals(datadriven.get("securedPayConfirm.no_confirm"))) {
			securedPayConfirm.setNo_confirm(MyConfig.getNoConfirm(datadriven.get("securedPayConfirm.no_confirm")));
		}
		if ("get_dt_confirm".equals(datadriven.get("securedPayConfirm.dt_confirm"))) {
			securedPayConfirm.setDt_confirm(MyConfig.getDtConfirm(datadriven.get("securedPayConfirm.dt_confirm")));
		}
		if ("get".equals(datadriven.get("securedPayConfirm.oid_paybill"))) {
			securedPayConfirm.setOid_paybill(oid_paybill1);
		} else {
			securedPayConfirm.setOid_paybill(datadriven.get("securedPayConfirm.oid_paybill"));
		}
		if ("get".equals(datadriven.get("securedPayConfirm.no_order"))) {
			securedPayConfirm.setNo_order(no_order1);
		} else {
			securedPayConfirm.setNo_order(datadriven.get("securedPayConfirm.no_order"));
		}
		if ("get".equals(datadriven.get("securedPayConfirm.dt_order"))) {
			securedPayConfirm.setDt_order(dt_order1);
		} else {
			securedPayConfirm.setDt_order(datadriven.get("securedPayConfirm.dt_order"));
		}
		securedPayConfirm.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(securedPayConfirm)),
				datadriven.get("securedPayConfirm.key")));
		String reqJson3 = JSON.toJSONString(securedPayConfirm);
		String securedPayConfirmUrl = Property.get("llwallet.securedPayConfirm.url");
		String rsp3 = HttpRequest.httpPostWithJSON(securedPayConfirmUrl, reqJson3);

		// 退款前余额
		String secured_partner = datadriven.get("secured_partner");
		String oid_partner = datadriven.get("oid_partner");
		String col_partner, col_userid, col_acctno;
		String secured_acctno = dbUtils.queryTraderBusinessAcct(secured_partner, datadriven.get("busi_partner")).trim();

		TaAcctInfo taAcctInfo, taAcctInfoC = new TaAcctInfo();
		String amtBalCur = null, amtBalAval = null, amtBalFrz = null, amtBalCurC = null, amtBalAvalC = null,
				amtBalFrzC = null;
		if (rsp3.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			taAcctInfo = dbUtils.queryTraderAcctInfo(secured_acctno);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("退款前担保商户： " + secured_partner + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额："
					+ amtBalFrz, true);

			if ("1".equals(datadriven.get("col_custtype1"))) {
				col_partner = datadriven.get("col_custid1");
				col_acctno = dbUtils.queryTraderBusinessAcct(col_partner, datadriven.get("busi_partner")).trim();
				taAcctInfoC = dbUtils.queryTraderAcctInfo(col_acctno);
				amtBalCurC = taAcctInfoC.getAmt_balcur();
				amtBalAvalC = taAcctInfoC.getAmt_balaval();
				amtBalFrzC = taAcctInfoC.getAmt_balfrz();
				Reporter.log("退款前收款商户： " + col_partner + "	总余额：" + amtBalCurC + "	可用余额：" + amtBalAvalC + "	冻结余额："
						+ amtBalFrzC, true);

			} else if ("0".equals(datadriven.get("col_custtype1"))) {
				col_userid = datadriven.get("col_custid1");
				taAcctInfoC = dbUtils.queryUserAcctInfo(col_userid, oid_partner);
				amtBalCurC = taAcctInfoC.getAmt_balcur();
				amtBalAvalC = taAcctInfoC.getAmt_balaval();
				amtBalFrzC = taAcctInfoC.getAmt_balfrz();
				Reporter.log("退款前收款用户： " + col_userid + "	总余额：" + amtBalCurC + "	可用余额：" + amtBalAvalC + "	冻结余额："
						+ amtBalFrzC, true);
			}

		}
		Thread.sleep(10);

		if (!StringUtils.isBlank(datadriven.get("payBillStatUpdate"))) {
			dbUtils.updatePayBillStat(oid_paybill1, datadriven.get("payBillStatUpdate"));
		}
		if (!StringUtils.isBlank(datadriven.get("securedBillStatUpdate"))) {
			dbUtils.updateSecuredBillStat(oid_paybill1, datadriven.get("securedBillStatUpdate"));
		}

		// 第一次退款
		String refundSerialList1 = datadriven.get("refundSerialList1");
		String refundSerialList2 = datadriven.get("refundSerialList2");
		String refundSerialList3 = datadriven.get("refundSerialList3");
		RefundSerialList refundList1 = new RefundSerialList();
		RefundSerialList refundList2 = new RefundSerialList();
		RefundSerialList refundList3 = new RefundSerialList();
		List<RefundSerialList> refundList = new ArrayList<RefundSerialList>();
		CombComfirmedRefund combComfirmedRefund = new CombComfirmedRefund();
		combComfirmedRefund.setSign_type(datadriven.get("sign_type1"));
		combComfirmedRefund.setApi_version(datadriven.get("api_version1"));
		combComfirmedRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp1")));
		combComfirmedRefund.setCol_oidpartner(datadriven.get("col_oidpartner1"));
		combComfirmedRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
		combComfirmedRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
		combComfirmedRefund.setMoney_refund(datadriven.get("money_refund1"));
		if ("get".equals(datadriven.get("no_order1"))) {
			combComfirmedRefund.setNo_order(no_order1);
		} else {
			combComfirmedRefund.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			combComfirmedRefund.setDt_order(dt_order1);
		} else {
			combComfirmedRefund.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			combComfirmedRefund.setOid_paybill(oid_paybill1);
		} else {
			combComfirmedRefund.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		combComfirmedRefund.setNotify_url(datadriven.get("notify_url1"));
		combComfirmedRefund.setCol_custid(datadriven.get("col_custid1"));
		combComfirmedRefund.setCol_custtype(datadriven.get("col_custtype1"));
		if (!StringUtils.isBlank(refundSerialList1)) {
			String result1[] = refundSerialList1.split(",");
			refundList1.setRefund_custId(result1[0]);
			refundList1.setCust_type(result1[1]);
			refundList1.setOrign_paytype(result1[2]);
			refundList1.setRefund_amt(result1[3]);
			refundList.add(refundList1);
		}
		if (!StringUtils.isBlank(refundSerialList2)) {
			String result2[] = refundSerialList2.split(",");
			refundList2.setRefund_custId(result2[0]);
			refundList2.setCust_type(result2[1]);
			refundList2.setOrign_paytype(result2[2]);
			refundList2.setRefund_amt(result2[3]);
			refundList.add(refundList2);
		}
		if (!StringUtils.isBlank(refundSerialList3)) {
			String result3[] = refundSerialList3.split(",");
			refundList3.setRefund_custId(result3[0]);
			refundList3.setCust_type(result3[1]);
			refundList3.setOrign_paytype(result3[2]);
			refundList3.setRefund_amt(result3[3]);
			refundList.add(refundList3);
		}
		combComfirmedRefund.setRefundSerialList(refundList);
		combComfirmedRefund.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combComfirmedRefund)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(combComfirmedRefund);
		String combComfirmedRefundUrl = Property.get("llwallet.combComfirmedRefund.url");
		String rsp1 = HttpRequest.httpPostWithJSON(combComfirmedRefundUrl, reqJson1);
		Thread.sleep(10);

		// 第二次退款
		String refundSerialList2_1 = datadriven.get("refundSerialList2_1");
		String refundSerialList2_2 = datadriven.get("refundSerialList2_2");
		String refundSerialList2_3 = datadriven.get("refundSerialList2_3");
		RefundSerialList refundList2_1 = new RefundSerialList();
		RefundSerialList refundList2_2 = new RefundSerialList();
		RefundSerialList refundList2_3 = new RefundSerialList();
		List<RefundSerialList> refundList_ = new ArrayList<RefundSerialList>();

		CombComfirmedRefund combComfirmedRefund1 = new CombComfirmedRefund();
		combComfirmedRefund1.setSign_type(datadriven.get("sign_type1"));
		combComfirmedRefund1.setApi_version(datadriven.get("api_version1"));
		combComfirmedRefund1.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp1")));
		combComfirmedRefund1.setCol_oidpartner(datadriven.get("col_oidpartner1"));
		combComfirmedRefund1.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
		combComfirmedRefund1.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
		combComfirmedRefund1.setMoney_refund(datadriven.get("money_refund2"));
		if ("get".equals(datadriven.get("no_order1"))) {
			combComfirmedRefund1.setNo_order(no_order1);
		} else {
			combComfirmedRefund1.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			combComfirmedRefund1.setDt_order(dt_order1);
		} else {
			combComfirmedRefund1.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			combComfirmedRefund1.setOid_paybill(oid_paybill1);
		} else {
			combComfirmedRefund1.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		combComfirmedRefund1.setNotify_url(datadriven.get("notify_url1"));
		combComfirmedRefund1.setCol_custid(datadriven.get("col_custid1"));
		combComfirmedRefund1.setCol_custtype(datadriven.get("col_custtype1"));
		if (!StringUtils.isBlank(refundSerialList2_1)) {
			String result2_1[] = refundSerialList2_1.split(",");
			refundList2_1.setRefund_custId(result2_1[0]);
			refundList2_1.setCust_type(result2_1[1]);
			refundList2_1.setOrign_paytype(result2_1[2]);
			refundList2_1.setRefund_amt(result2_1[3]);
			refundList_.add(refundList2_1);
		}
		if (!StringUtils.isBlank(refundSerialList2_2)) {
			String result2_2[] = refundSerialList2_2.split(",");
			refundList2_2.setRefund_custId(result2_2[0]);
			refundList2_2.setCust_type(result2_2[1]);
			refundList2_2.setOrign_paytype(result2_2[2]);
			refundList2_2.setRefund_amt(result2_2[3]);
			refundList_.add(refundList2_2);
		}
		if (!StringUtils.isBlank(refundSerialList2_3)) {
			String result2_3[] = refundSerialList2_3.split(",");
			refundList2_3.setRefund_custId(result2_3[0]);
			refundList2_3.setCust_type(result2_3[1]);
			refundList2_3.setOrign_paytype(result2_3[2]);
			refundList2_3.setRefund_amt(result2_3[3]);
			refundList_.add(refundList2_3);
		}
		combComfirmedRefund1.setRefundSerialList(refundList_);
		combComfirmedRefund1.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combComfirmedRefund1)), datadriven.get("key1")));
		String reqJson2 = JSON.toJSONString(combComfirmedRefund1);
		String rsp2 = HttpRequest.httpPostWithJSON(combComfirmedRefundUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson3);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp3);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//combComfirmedRefund.txt",
				"===============================================");

		// 支付单状态校验
		if (!StringUtils.isBlank(datadriven.get("expect_PayBillStat"))) {
			String stat_bill = dbUtils.queryPayBillStat(no_order1);
			assert datadriven.get("expect_PayBillStat").equals(stat_bill);
		}
		if (!StringUtils.isBlank(datadriven.get("expect_SecuredBillStat"))) {
			String stat = dbUtils.querySecuredBillStat(no_order1);
			assert datadriven.get("expect_SecuredBillStat").equals(stat);
		}
		// 组合支付确认后退款请求返回检查
		assert rsp2.contains(datadriven.get("expect_return"));
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp2)),
					JSONObject.parseObject(rsp2).getString("sign"));
		}

		// 退款后余额
		TaAcctInfo taAcctInfo1, taAcctInfoC1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1, amtBalCurC1, amtBalAvalC1, amtBalFrzC1;
		BigDecimal amt1 = null, amt2 = null, amt3, amtC1 = null, amtC2 = null, amtC3;
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			taAcctInfo1 = dbUtils.queryTraderAcctInfo(secured_acctno);
			amtBalCur1 = taAcctInfo1.getAmt_balcur();
			amtBalAval1 = taAcctInfo1.getAmt_balaval();
			amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
			Reporter.log("退款后担保商户： " + secured_partner + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额："
					+ amtBalFrz1, true);

			amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
			amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
			amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
			Reporter.log("退款后担保商户： " + secured_partner + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3,
					true);
			assert "0".equals(amt1.toString());
			assert "0".equals(amt2.toString());

			if ("1".equals(datadriven.get("col_custtype1"))) {
				col_partner = datadriven.get("col_custid1");
				col_acctno = dbUtils.queryTraderBusinessAcct(col_partner, datadriven.get("busi_partner")).trim();
				taAcctInfoC1 = dbUtils.queryTraderAcctInfo(col_acctno);
				amtBalCurC1 = taAcctInfoC1.getAmt_balcur();
				amtBalAvalC1 = taAcctInfoC1.getAmt_balaval();
				amtBalFrzC1 = taAcctInfoC1.getAmt_balfrz();
				Reporter.log("退款后收款商户： " + col_partner + "	总余额：" + amtBalCurC1 + "	可用余额：" + amtBalAvalC1 + "	冻结余额："
						+ amtBalFrzC1, true);

				amtC1 = new BigDecimal(amtBalCurC).subtract(new BigDecimal(amtBalCurC1));
				amtC2 = new BigDecimal(amtBalAvalC).subtract(new BigDecimal(amtBalAvalC1));
				amtC3 = new BigDecimal(amtBalFrzC).subtract(new BigDecimal(amtBalFrzC1));
				Reporter.log(
						"退款后收款商户： " + col_partner + "	总余额减少：" + amtC1 + "	可用余额减少：" + amtC2 + "	冻结余额减少：" + amtC3,
						true);

				assert amtC1.divide(new BigDecimal(1000)).compareTo(new BigDecimal(datadriven.get("money_refund1"))
						.add(new BigDecimal(datadriven.get("money_refund2")))) == 0;
				assert amtC2.divide(new BigDecimal(1000)).compareTo(new BigDecimal(datadriven.get("money_refund1"))
						.add(new BigDecimal(datadriven.get("money_refund2")))) == 0;

			} else if ("0".equals(datadriven.get("col_custtype1"))) {
				col_userid = datadriven.get("col_custid1");
				taAcctInfoC1 = dbUtils.queryUserAcctInfo(col_userid, oid_partner);
				amtBalCurC1 = taAcctInfoC1.getAmt_balcur();
				amtBalAvalC1 = taAcctInfoC1.getAmt_balaval();
				amtBalFrzC1 = taAcctInfoC1.getAmt_balfrz();
				Reporter.log("退款后收款用户： " + col_userid + "	总余额：" + amtBalCurC1 + "	可用余额：" + amtBalAvalC1 + "	冻结余额："
						+ amtBalFrzC1, true);

				amtC1 = new BigDecimal(amtBalCurC).subtract(new BigDecimal(amtBalCurC1));
				amtC2 = new BigDecimal(amtBalAvalC).subtract(new BigDecimal(amtBalAvalC1));
				amtC3 = new BigDecimal(amtBalFrzC).subtract(new BigDecimal(amtBalFrzC1));
				Reporter.log(
						"退款后收款用户： " + col_userid + "	总余额减少：" + amtC1 + "	可用余额减少：" + amtC2 + "	冻结余额减少：" + amtC3,
						true);

				assert amtC1.divide(new BigDecimal(1000)).compareTo(new BigDecimal(datadriven.get("money_refund1"))
						.add(new BigDecimal(datadriven.get("money_refund2")))) == 0;
				assert amtC2.divide(new BigDecimal(1000)).compareTo(new BigDecimal(datadriven.get("money_refund1"))
						.add(new BigDecimal(datadriven.get("money_refund2")))) == 0;

			}
		}

		Reporter.log("组合支付确认后退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "combComfirmedRefund")
	public Iterator<Object[]> data4combComfirmedRefund() throws IOException {
		return new ExcelProvider(this, "combComfirmedRefund",999);
	}

	@DataProvider(name = "combComfirmedRefund1")
	public Iterator<Object[]> data4combComfirmedRefund1() throws IOException {
		return new ExcelProvider(this, "combComfirmedRefund1",41);
	}

	@DataProvider(name = "combComfirmedRefund2")
	public Iterator<Object[]> data4combComfirmedRefund2() throws IOException {
		// return new ExcelProvider(this, "combComfirmedRefund2", 1, 16);
		return new ExcelProvider(this, "combComfirmedRefund2",999);
	}

	@AfterClass
	public void afterClass() {

	}

}