package com.llwallet.interfaces.test.api.test.combination;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianlian.crypt.service.IAESCryptService;
import com.lianpay.chnl.domain.notify.PaymentProcessReq;
import com.lianpay.chnl.service.notify.PaymentResultNotifyService;
import com.llwallet.interfaces.bean.combination.CombinationPay;
import com.llwallet.interfaces.bean.combination.CombinationPayVerify;
import com.llwallet.interfaces.bean.combination.CombinationRefund;
import com.llwallet.interfaces.bean.combination.PayeeInfo;
import com.llwallet.interfaces.bean.combination.PayerInfo;
import com.llwallet.interfaces.bean.combination.RefundSerialList;
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
 * 组合支付退款测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class CombinationRefundTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@Autowired
	private PaymentResultNotifyService paymentResultNotifyService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "组合支付退款测试", timeOut = 60000, dataProvider = "combinationRefund")
	public void combinationRefund(Map<String, String> datadriven) throws Exception {

		Thread.sleep(10);
		String refundSerialList1 = datadriven.get("refundSerialList1");
		String refundSerialList2 = datadriven.get("refundSerialList2");
		String refundSerialList3 = datadriven.get("refundSerialList3");
		RefundSerialList refundList1 = new RefundSerialList();
		RefundSerialList refundList2 = new RefundSerialList();
		RefundSerialList refundList3 = new RefundSerialList();
		List<RefundSerialList> refundList = new ArrayList<RefundSerialList>();
		CombinationRefund combinationRefund = new CombinationRefund();
		combinationRefund.setOid_partner(datadriven.get("oid_partner"));
		combinationRefund.setSign_type(datadriven.get("sign_type"));
		combinationRefund.setApi_version(datadriven.get("api_version"));
		combinationRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
		combinationRefund.setUser_id(datadriven.get("user_id"));
		combinationRefund.setCol_oidpartner(datadriven.get("col_oidpartner"));
		combinationRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund")));
		combinationRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund")));
		combinationRefund.setMoney_refund(datadriven.get("money_refund"));
		combinationRefund.setNo_order(datadriven.get("no_order"));
		combinationRefund.setDt_order(datadriven.get("dt_order"));
		combinationRefund.setOid_paybill(datadriven.get("oid_paybill"));
		combinationRefund.setNotify_url(datadriven.get("notify_url"));
		combinationRefund.setCol_custid(datadriven.get("col_custid"));
		combinationRefund.setCol_custtype(datadriven.get("col_custtype"));
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
		combinationRefund.setRefundSerialList(refundList);
		combinationRefund.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationRefund)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationRefund);
		String combinationRefundUrl = Property.get("llwallet.combinationRefund.url");

		String rsp = HttpRequest.httpPostWithJSON(combinationRefundUrl, reqJson);
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt",
				"===============================================");

		Reporter.log("组合支付退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 组合支付退款请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
	}

	@Test(description = "组合支付退款测试", timeOut = 60000, dataProvider = "combinationRefund1")
	public void combinationRefund1(Map<String, String> datadriven) throws Exception {

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

		if (!StringUtils.isBlank(datadriven.get("payBillStatUpdate"))) {
			dbUtils.updatePayBillStat(oid_paybill1, datadriven.get("payBillStatUpdate"));
		}
		if (!StringUtils.isBlank(datadriven.get("securedBillStatUpdate"))) {
			dbUtils.updateSecuredBillStat(oid_paybill1, datadriven.get("securedBillStatUpdate"));
		}

		// 退款前余额
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		String oid_traderno = null, oid_acctno = null;
		if (!StringUtils.isBlank(datadriven.get("secured_partner"))) {
			oid_traderno = datadriven.get("secured_partner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, datadriven.get("busi_partner")).trim();
		} else if (!StringUtils.isBlank(datadriven.get("col_oidpartner"))) {
			oid_traderno = datadriven.get("col_oidpartner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, datadriven.get("busi_partner")).trim();
		}
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		String amtBalCur, amtBalAval, amtBalFrz = null;
		if (StringUtils.isBlank(datadriven.get("col_oidpartner"))
				&& StringUtils.isBlank(datadriven.get("secured_partner"))) {
			taAcctInfo = dbUtils.queryUserAcctInfo(datadriven.get("col_userid"), oid_partner);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("退款前收款用户： " + datadriven.get("col_userid") + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval
					+ "	冻结余额：" + amtBalFrz, true);
		} else {
			taAcctInfo = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log(
					"退款前收款商户： " + oid_traderno + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);
		}

		// 组合支付退款
		String refundSerialList1 = datadriven.get("refundSerialList1");
		String refundSerialList2 = datadriven.get("refundSerialList2");
		String refundSerialList3 = datadriven.get("refundSerialList3");
		RefundSerialList refundList1 = new RefundSerialList();
		RefundSerialList refundList2 = new RefundSerialList();
		RefundSerialList refundList3 = new RefundSerialList();
		List<RefundSerialList> refundList = new ArrayList<RefundSerialList>();

		CombinationRefund combinationRefund = new CombinationRefund();
		combinationRefund.setOid_partner(datadriven.get("oid_partner1"));
		combinationRefund.setSign_type(datadriven.get("sign_type1"));
		combinationRefund.setApi_version(datadriven.get("api_version1"));
		combinationRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp1")));
		combinationRefund.setUser_id(datadriven.get("user_id1"));
		combinationRefund.setCol_oidpartner(datadriven.get("col_oidpartner1"));
		combinationRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
		combinationRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
		combinationRefund.setMoney_refund(datadriven.get("money_refund1"));
		if ("get".equals(datadriven.get("no_order1"))) {
			combinationRefund.setNo_order(no_order1);
		} else {
			combinationRefund.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			combinationRefund.setDt_order(dt_order1);
		} else {
			combinationRefund.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			combinationRefund.setOid_paybill(oid_paybill1);
		} else {
			combinationRefund.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		combinationRefund.setNotify_url(datadriven.get("notify_url1"));
		combinationRefund.setCol_custid(datadriven.get("col_custid1"));
		combinationRefund.setCol_custtype(datadriven.get("col_custtype1"));
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
		combinationRefund.setRefundSerialList(refundList);
		combinationRefund.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationRefund)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(combinationRefund);
		String combinationRefundUrl = Property.get("llwallet.combinationRefund.url");

		// 组合支付退款请求提交
		String rsp1 = HttpRequest.httpPostWithJSON(combinationRefundUrl, reqJson1);
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt",
				"===============================================");

		// 退款后余额
		TaAcctInfo taAcctInfo1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1;
		BigDecimal amt1 = null, amt2 = null, amt3;
		if (!StringUtils.isBlank(datadriven.get("col_oidpartner"))
				|| !StringUtils.isBlank(datadriven.get("secured_partner"))
				|| !StringUtils.isBlank(datadriven.get("col_userid"))) {
			if (StringUtils.isBlank(datadriven.get("col_oidpartner"))
					&& StringUtils.isBlank(datadriven.get("secured_partner"))) {
				taAcctInfo1 = dbUtils.queryUserAcctInfo(datadriven.get("col_userid"), oid_partner);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("退款后收款用户： " + datadriven.get("col_userid") + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval
						+ "	冻结余额：" + amtBalFrz, true);

				amt1 = new BigDecimal(amtBalCur).subtract(new BigDecimal(amtBalCur1));
				amt2 = new BigDecimal(amtBalAval).subtract(new BigDecimal(amtBalAval1));
				amt3 = new BigDecimal(amtBalFrz).subtract(new BigDecimal(amtBalFrz1));
				Reporter.log("退款后收款用户： " + datadriven.get("col_userid") + "	总余额减少：" + amt1 + "	可用余额减少：" + amt2
						+ "	冻结余额减少：" + amt3, true);
			} else {
				taAcctInfo1 = dbUtils.queryTraderAcctInfo(oid_acctno);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("退款后收款商户： " + oid_traderno + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额："
						+ amtBalFrz1, true);

				amt1 = new BigDecimal(amtBalCur).subtract(new BigDecimal(amtBalCur1));
				amt2 = new BigDecimal(amtBalAval).subtract(new BigDecimal(amtBalAval1));
				amt3 = new BigDecimal(amtBalFrz).subtract(new BigDecimal(amtBalFrz1));
				Reporter.log("退款后收款商户： " + oid_traderno + "	总余额减少：" + amt1 + "	可用余额减少：" + amt2 + "	冻结余额减少：" + amt3,
						true);
			}
		}

		Reporter.log("组合支付退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 支付单状态校验
		if (!StringUtils.isBlank(datadriven.get("expect_PayBillStat"))) {
			String stat_bill = dbUtils.queryPayBillStat(no_order1);
			assert datadriven.get("expect_PayBillStat").equals(stat_bill);
		}
		if (!StringUtils.isBlank(datadriven.get("expect_SecuredBillStat"))) {
			String stat = dbUtils.querySecuredBillStat(no_order1);
			assert datadriven.get("expect_SecuredBillStat").equals(stat);
		}

		// 组合支付退款请求返回检查
		assert rsp1.contains(datadriven.get("expect_return"));
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
					JSONObject.parseObject(rsp1).getString("sign"));
		}

		// 余额校验
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert amt1.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_refund1"));
			assert amt2.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_refund1"));
		}
	}

	@Test(description = "组合支付退款测试", timeOut = 60000, dataProvider = "combinationRefund2")
	public void combinationRefund2(Map<String, String> datadriven) throws Exception {

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

		// 退款前余额
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		String oid_traderno = null, oid_acctno = null;
		if (!StringUtils.isBlank(datadriven.get("secured_partner"))) {
			oid_traderno = datadriven.get("secured_partner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, datadriven.get("busi_partner")).trim();
		} else if (!StringUtils.isBlank(datadriven.get("col_oidpartner"))) {
			oid_traderno = datadriven.get("col_oidpartner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, datadriven.get("busi_partner")).trim();
		}
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		String amtBalCur, amtBalAval, amtBalFrz = null;
		if (StringUtils.isBlank(datadriven.get("col_oidpartner"))
				&& StringUtils.isBlank(datadriven.get("secured_partner"))) {
			taAcctInfo = dbUtils.queryUserAcctInfo(datadriven.get("col_userid"), oid_partner);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("退款前收款用户： " + datadriven.get("col_userid") + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval
					+ "	冻结余额：" + amtBalFrz, true);
		} else {
			taAcctInfo = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log(
					"退款前收款商户： " + oid_traderno + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);
		}

		// 第一次退款
		String refundSerialList1 = datadriven.get("refundSerialList1");
		String refundSerialList2 = datadriven.get("refundSerialList2");
		String refundSerialList3 = datadriven.get("refundSerialList3");
		RefundSerialList refundList1 = new RefundSerialList();
		RefundSerialList refundList2 = new RefundSerialList();
		RefundSerialList refundList3 = new RefundSerialList();
		List<RefundSerialList> refundList = new ArrayList<RefundSerialList>();

		CombinationRefund combinationRefund = new CombinationRefund();
		combinationRefund.setOid_partner(datadriven.get("oid_partner1"));
		combinationRefund.setSign_type(datadriven.get("sign_type1"));
		combinationRefund.setApi_version(datadriven.get("api_version1"));
		combinationRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp1")));
		combinationRefund.setUser_id(datadriven.get("user_id1"));
		combinationRefund.setCol_oidpartner(datadriven.get("col_oidpartner1"));
		combinationRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
		combinationRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
		combinationRefund.setMoney_refund(datadriven.get("money_refund1"));
		if ("get".equals(datadriven.get("no_order1"))) {
			combinationRefund.setNo_order(no_order1);
		} else {
			combinationRefund.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			combinationRefund.setDt_order(dt_order1);
		} else {
			combinationRefund.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			combinationRefund.setOid_paybill(oid_paybill1);
		} else {
			combinationRefund.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		combinationRefund.setNotify_url(datadriven.get("notify_url1"));
		combinationRefund.setCol_custid(datadriven.get("col_custid1"));
		combinationRefund.setCol_custtype(datadriven.get("col_custtype1"));
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
		combinationRefund.setRefundSerialList(refundList);
		combinationRefund.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationRefund)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(combinationRefund);
		String combinationRefundUrl = Property.get("llwallet.combinationRefund.url");
		String rsp1 = HttpRequest.httpPostWithJSON(combinationRefundUrl, reqJson1);
		Thread.sleep(10);

		// 第二次退款
		String refundSerialList2_1 = datadriven.get("refundSerialList2_1");
		String refundSerialList2_2 = datadriven.get("refundSerialList2_2");
		String refundSerialList2_3 = datadriven.get("refundSerialList2_3");
		RefundSerialList refundList2_1 = new RefundSerialList();
		RefundSerialList refundList2_2 = new RefundSerialList();
		RefundSerialList refundList2_3 = new RefundSerialList();
		List<RefundSerialList> refundList_ = new ArrayList<RefundSerialList>();

		CombinationRefund combinationRefund1 = new CombinationRefund();
		combinationRefund1.setOid_partner(datadriven.get("oid_partner1"));
		combinationRefund1.setSign_type(datadriven.get("sign_type1"));
		combinationRefund1.setApi_version(datadriven.get("api_version1"));
		combinationRefund1.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp1")));
		combinationRefund1.setUser_id(datadriven.get("user_id1"));
		combinationRefund1.setCol_oidpartner(datadriven.get("col_oidpartner1"));
		combinationRefund1.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
		combinationRefund1.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
		combinationRefund1.setMoney_refund(datadriven.get("money_refund2"));
		if ("get".equals(datadriven.get("no_order1"))) {
			combinationRefund1.setNo_order(no_order1);
		} else {
			combinationRefund1.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			combinationRefund1.setDt_order(dt_order1);
		} else {
			combinationRefund1.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			combinationRefund1.setOid_paybill(oid_paybill1);
		} else {
			combinationRefund1.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		combinationRefund1.setNotify_url(datadriven.get("notify_url1"));
		combinationRefund1.setCol_custid(datadriven.get("col_custid1"));
		combinationRefund1.setCol_custtype(datadriven.get("col_custtype1"));
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
		combinationRefund1.setRefundSerialList(refundList_);
		combinationRefund1.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationRefund1)), datadriven.get("key1")));
		String reqJson2 = JSON.toJSONString(combinationRefund1);
		String rsp2 = HttpRequest.httpPostWithJSON(combinationRefundUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//combinationRefund.txt",
				"===============================================");

		// 退款后余额
		TaAcctInfo taAcctInfo1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1;
		BigDecimal amt1 = null, amt2 = null, amt3;
		if (!StringUtils.isBlank(datadriven.get("col_oidpartner"))
				|| !StringUtils.isBlank(datadriven.get("secured_partner"))
				|| !StringUtils.isBlank(datadriven.get("col_userid"))) {
			if (StringUtils.isBlank(datadriven.get("col_oidpartner"))
					&& StringUtils.isBlank(datadriven.get("secured_partner"))) {
				taAcctInfo1 = dbUtils.queryUserAcctInfo(datadriven.get("col_userid"), oid_partner);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("退款后收款用户： " + datadriven.get("col_userid") + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval
						+ "	冻结余额：" + amtBalFrz, true);

				amt1 = new BigDecimal(amtBalCur).subtract(new BigDecimal(amtBalCur1));
				amt2 = new BigDecimal(amtBalAval).subtract(new BigDecimal(amtBalAval1));
				amt3 = new BigDecimal(amtBalFrz).subtract(new BigDecimal(amtBalFrz1));
				Reporter.log("退款后收款用户： " + datadriven.get("col_userid") + "	总余额减少：" + amt1 + "	可用余额减少：" + amt2
						+ "	冻结余额减少：" + amt3, true);
			} else {
				taAcctInfo1 = dbUtils.queryTraderAcctInfo(oid_acctno);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("退款后收款商户： " + oid_traderno + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额："
						+ amtBalFrz1, true);

				amt1 = new BigDecimal(amtBalCur).subtract(new BigDecimal(amtBalCur1));
				amt2 = new BigDecimal(amtBalAval).subtract(new BigDecimal(amtBalAval1));
				amt3 = new BigDecimal(amtBalFrz).subtract(new BigDecimal(amtBalFrz1));
				Reporter.log("退款后收款商户： " + oid_traderno + "	总余额减少：" + amt1 + "	可用余额减少：" + amt2 + "	冻结余额减少：" + amt3,
						true);
			}
		}

		Reporter.log("组合支付退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 支付单状态校验
		if (!StringUtils.isBlank(datadriven.get("expect_PayBillStat"))) {
			String stat_bill = dbUtils.queryPayBillStat(no_order1);
			assert datadriven.get("expect_PayBillStat").equals(stat_bill);
		}
		if (!StringUtils.isBlank(datadriven.get("expect_SecuredBillStat"))) {
			String stat = dbUtils.querySecuredBillStat(no_order1);
			assert datadriven.get("expect_SecuredBillStat").equals(stat);
		}

		// 组合支付退款请求返回检查
		assert rsp2.contains(datadriven.get("expect_return"));
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
					JSONObject.parseObject(rsp1).getString("sign"));
		}

		// 余额校验
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert amt1.divide(new BigDecimal(1000)).compareTo(new BigDecimal(datadriven.get("money_refund1"))
					.add(new BigDecimal(datadriven.get("money_refund2")))) == 0;
			assert amt2.divide(new BigDecimal(1000)).compareTo(new BigDecimal(datadriven.get("money_refund1"))
					.add(new BigDecimal(datadriven.get("money_refund2")))) == 0;
		}
	}

	@DataProvider(name = "combinationRefund")
	public Iterator<Object[]> data4combinationRefund() throws IOException {
		return new ExcelProvider(this, "combinationRefund");
	}

	@DataProvider(name = "combinationRefund1")
	public Iterator<Object[]> data4combinationRefund1() throws IOException {
		return new ExcelProvider(this, "combinationRefund1");
	}

	@DataProvider(name = "combinationRefund2")
	public Iterator<Object[]> data4combinationRefund2() throws IOException {
		return new ExcelProvider(this, "combinationRefund2");
	}

	@AfterClass
	public void afterClass() {

	}

}