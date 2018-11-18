package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.BalancePayVerify;
import com.llwallet.interfaces.bean.personal.BalancePwdPay;
import com.llwallet.interfaces.bean.personal.BankCardPay;
import com.llwallet.interfaces.bean.personal.BankCardPrepay;
import com.llwallet.interfaces.bean.personal.UserRefund;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户退款测试
 */

public class UserRefundTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户退款测试", timeOut = 60000, dataProvider = "userRefund")
	public void userRefund(Map<String, String> datadriven) throws Exception {

		UserRefund userRefund = new UserRefund();
		userRefund.setOid_partner(datadriven.get("oid_partner"));
		userRefund.setSign_type(datadriven.get("sign_type"));
		userRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund")));
		userRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund")));
		userRefund.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("money_refund").length() < 1) {
			userRefund.setMoney_refund(null);
		} else {
			userRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund")));
		}
		userRefund.setNo_order(datadriven.get("no_order"));
		userRefund.setDt_order(datadriven.get("dt_order"));
		userRefund.setOid_paybill(datadriven.get("oid_paybill"));
		userRefund.setNotify_url(datadriven.get("notify_url"));
		userRefund.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userRefund)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(userRefund);
		String userRefundUrl = Property.get("llwallet.userRefund.url");
		String rsp = HttpRequest.httpPostWithJSON(userRefundUrl, reqJson);

		Reporter.log("用户退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 用户退款请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@Test(description = "用户退款测试", timeOut = 60000, dataProvider = "userRefund1")
	public void userRefund1(Map<String, String> datadriven) throws Exception {

		// 银行卡支付
		BankCardPrepay bankCardPrepay = new BankCardPrepay();
		bankCardPrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardPrepay.setSign_type(datadriven.get("sign_type"));
		bankCardPrepay.setUser_id(datadriven.get("user_id"));
		bankCardPrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardPrepay.setApi_version(datadriven.get("api_version"));
		bankCardPrepay.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
		bankCardPrepay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		bankCardPrepay.setName_goods(datadriven.get("name_goods"));
		bankCardPrepay.setMoney_order(datadriven.get("money_order"));
		bankCardPrepay.setInfo_order(datadriven.get("info_order"));
		bankCardPrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardPrepay.setValid_order(datadriven.get("valid_order"));
		bankCardPrepay.setRisk_item(datadriven.get("risk_item"));
		bankCardPrepay.setPay_type(datadriven.get("pay_type"));
		bankCardPrepay.setCard_no(datadriven.get("card_no"));
		bankCardPrepay.setBank_code(datadriven.get("bank_code"));
		bankCardPrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardPrepay.setVali_date(datadriven.get("vali_date"));
		bankCardPrepay.setCvv2(datadriven.get("cvv2"));
		bankCardPrepay.setNo_agree(datadriven.get("no_agree"));
		if (datadriven.get("pwd_pay").length() != 0) {
			bankCardPrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardPrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}

		bankCardPrepay.setCol_oidpartner(datadriven.get("col_oidpartner"));
		bankCardPrepay.setShareing_data(datadriven.get("shareing_data"));
		bankCardPrepay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPrepay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardPrepay);
		String bankCardPrepayUrl = Property.get("llwallet.bankCardPrepay.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardPrepayUrl, reqJson);
		String no_order1 = JSONObject.parseObject(rsp).getString("no_order");
		String dt_order1 = JSONObject.parseObject(rsp).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(rsp).getString("oid_paybill");
		String token = JSONObject.parseObject(rsp).getString("token");
		System.out.println("no_order------" + no_order1);
		System.out.println("dt_order------" + dt_order1);
		System.out.println("oid_paybill------" + oid_paybill);
		Thread.sleep(10);

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			DbUtils dbUtils = new DbUtils();
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner"));
			bankCardPay.setSign_type(datadriven.get("sign_type"));
			bankCardPay.setNo_order(no_order1);
			bankCardPay.setToken(token);
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key")));
			String reqJson2 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson2);
		}

		// 用户退款
		UserRefund userRefund = new UserRefund();
		userRefund.setOid_partner(datadriven.get("oid_partner1"));
		userRefund.setSign_type(datadriven.get("sign_type1"));
		userRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
		userRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
		userRefund.setUser_id(datadriven.get("user_id1"));
		if (datadriven.get("money_refund1").length() < 1) {
			userRefund.setMoney_refund(null);
		} else {
			userRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund1")));
		}
		if ("get".equals(datadriven.get("no_order1"))) {
			userRefund.setNo_order(no_order1);
		} else {
			userRefund.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			userRefund.setDt_order(dt_order1);
		} else {
			userRefund.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			userRefund.setOid_paybill(oid_paybill);
		} else {
			userRefund.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		userRefund.setNotify_url(datadriven.get("notify_url1"));
		userRefund.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userRefund)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(userRefund);
		String userRefundUrl = Property.get("llwallet.userRefund.url");
		String rsp1 = HttpRequest.httpPostWithJSON(userRefundUrl, reqJson1);

		Reporter.log("用户退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 用户退款请求返回检查
		assert rsp1.contains(datadriven.get("expect_return"));
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
					JSONObject.parseObject(rsp1).getString("sign"));
		}
	}

	@Test(description = "用户退款测试", timeOut = 60000, dataProvider = "userRefund2")
	public void userRefund2(Map<String, String> datadriven) throws Exception {

		// 余额支付
		BalancePwdPay balancePwdPay = new BalancePwdPay();
		balancePwdPay.setOid_partner(datadriven.get("oid_partner"));
		balancePwdPay.setSign_type(datadriven.get("sign_type"));
		balancePwdPay.setUser_id(datadriven.get("user_id"));
		balancePwdPay.setNo_order(MyConfig.getNoOrder(datadriven.get("no_order")));
		balancePwdPay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		balancePwdPay.setBusi_partner(datadriven.get("busi_partner"));
		balancePwdPay.setName_goods(datadriven.get("name_goods"));
		balancePwdPay.setMoney_order(datadriven.get("money_order"));
		balancePwdPay.setInfo_order(datadriven.get("info_order"));
		if (datadriven.get("pwd_pay").length() != 0) {
			balancePwdPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			balancePwdPay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		balancePwdPay.setNotify_url(datadriven.get("notify_url"));
		balancePwdPay.setRisk_item(datadriven.get("risk_item"));
		balancePwdPay.setShareing_data(datadriven.get("shareing_data"));
		balancePwdPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
		balancePwdPay.setCol_userid(datadriven.get("col_userid"));
		balancePwdPay.setSecured_partner(datadriven.get("secured_partner"));
		if (datadriven.get("buyer_confirm_valid").length() > 0) {
			balancePwdPay.setBuyer_confirm_valid(Integer.parseInt(datadriven.get("buyer_confirm_valid")));
		}
		if (datadriven.get("seller_send_valid").length() > 0) {
			balancePwdPay.setSeller_send_valid(Integer.parseInt(datadriven.get("seller_send_valid")));
		}
		balancePwdPay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePwdPay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(balancePwdPay);
		String balancePwdPayUrl = Property.get("llwallet.balancePwdPay.url");
		String rsp = HttpRequest.httpPostWithJSON(balancePwdPayUrl, reqJson);

		String no_order1 = JSONObject.parseObject(rsp).getString("no_order");
		String dt_order1 = JSONObject.parseObject(rsp).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(rsp).getString("oid_paybill");
		String token = JSONObject.parseObject(rsp).getString("token");
		System.out.println("no_order------" + no_order1);
		System.out.println("dt_order------" + dt_order1);
		System.out.println("oid_paybill------" + oid_paybill);
		Thread.sleep(10);

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			DbUtils dbUtils = new DbUtils();
			String mob_bind = dbUtils.queryMob(datadriven.get("user_id")).getMob_bind();
			String verify_code = dbUtils.queryCdSmscd(mob_bind).getTno_smscd();
			BalancePayVerify balancePayVerify = new BalancePayVerify();
			balancePayVerify.setOid_partner(datadriven.get("oid_partner1"));
			balancePayVerify.setSign_type(datadriven.get("sign_type1"));
			balancePayVerify.setUser_id(datadriven.get("user_id1"));
			balancePayVerify.setNo_order(no_order1);
			balancePayVerify.setToken(token);
			balancePayVerify.setMoney_order(datadriven.get("money_order1"));
			if ("auto".equals(datadriven.get("verify_code1"))) {
				balancePayVerify.setVerify_code(verify_code);
			} else {
				balancePayVerify.setVerify_code(datadriven.get("verify_code1"));
			}
			balancePayVerify.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePayVerify)), datadriven.get("key1")));
			String reqJson2 = JSON.toJSONString(balancePayVerify);
			String balancePayVerifyUrl = Property.get("llwallet.balancePayVerify.url");
			HttpRequest.httpPostWithJSON(balancePayVerifyUrl, reqJson2);
		}

		// 用户退款
		UserRefund userRefund = new UserRefund();
		userRefund.setOid_partner(datadriven.get("oid_partner1"));
		userRefund.setSign_type(datadriven.get("sign_type1"));
		userRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund1")));
		userRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund1")));
		userRefund.setUser_id(datadriven.get("user_id1"));
		if (datadriven.get("money_refund1").length() < 1) {
			userRefund.setMoney_refund(null);
		} else {
			userRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund1")));
		}
		if ("get".equals(datadriven.get("no_order1"))) {
			userRefund.setNo_order(no_order1);
		} else {
			userRefund.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			userRefund.setDt_order(dt_order1);
		} else {
			userRefund.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			userRefund.setOid_paybill(oid_paybill);
		} else {
			userRefund.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		userRefund.setNotify_url(datadriven.get("notify_url1"));
		userRefund.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userRefund)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(userRefund);
		String userRefundUrl = Property.get("llwallet.userRefund.url");
		String rsp1 = HttpRequest.httpPostWithJSON(userRefundUrl, reqJson1);

		Reporter.log("用户退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 用户退款请求返回检查
		assert rsp1.contains(datadriven.get("expect_return"));
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
					JSONObject.parseObject(rsp1).getString("sign"));
		}
	}

	@DataProvider(name = "userRefund")
	public Iterator<Object[]> data4userRefund() throws IOException {
		return new ExcelProvider(this, "userRefund");
	}

	@DataProvider(name = "userRefund1")
	public Iterator<Object[]> data4userRefund1() throws IOException {
		return new ExcelProvider(this, "userRefund1", 999);
	}

	@DataProvider(name = "userRefund2")
	public Iterator<Object[]> data4userRefund2() throws IOException {
		return new ExcelProvider(this, "userRefund2", 999);
	}

	@AfterClass
	public void afterClass() {

	}

}
