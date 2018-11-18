package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.SecpayRefund;
import com.llwallet.interfaces.bean.personal.BalancePwdPay;
import com.llwallet.interfaces.bean.personal.BankCardPrepay;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 担保交易退款测试
 */

public class SecpayRefundTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保交易退款测试", timeOut = 60000, dataProvider = "secpayRefund")
	public void secpayRefund(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String dt_refund = ApiUtils.getCurrentDateSecondStr();
		String no_refund = ApiUtils.getCurrentDateMillisecondStr();

		SecpayRefund secpayRefund = new SecpayRefund();
		secpayRefund.setOid_partner(datadriven.get("oid_partner"));
		secpayRefund.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("no_refund"))) {
			secpayRefund.setNo_refund(no_refund);
		} else {
			secpayRefund.setNo_refund(datadriven.get("no_refund"));
		}
		if ("auto".equals(datadriven.get("dt_refund"))) {
			secpayRefund.setDt_refund(dt_refund);
		} else {
			secpayRefund.setDt_refund(datadriven.get("dt_refund"));
		}

		if (datadriven.get("money_refund").length() < 1) {
			secpayRefund.setMoney_refund(null);
		} else {
			secpayRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund")));
		}
		secpayRefund.setNo_order(datadriven.get("no_order"));
		secpayRefund.setDt_order(datadriven.get("dt_order"));
		secpayRefund.setOid_paybill(datadriven.get("oid_paybill"));
		secpayRefund.setNotify_url(datadriven.get("notify_url"));
		secpayRefund.setCol_partner(datadriven.get("col_partner"));
		secpayRefund.setCol_custid(datadriven.get("col_custid"));
		secpayRefund.setCol_custtype(datadriven.get("col_custtype"));		
		secpayRefund.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(secpayRefund)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(secpayRefund);
		String secpayRefundUrl = Property.get("llwallet.secpayRefund.url");

		// 担保交易退款请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(secpayRefundUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 担保交易退款请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("担保交易退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "担保交易退款测试", timeOut = 60000, dataProvider = "secpayRefund1")
	public void secpayRefund1(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();

		BankCardPrepay bankCardPrepay = new BankCardPrepay();
		bankCardPrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardPrepay.setSign_type(datadriven.get("sign_type"));
		bankCardPrepay.setUser_id(datadriven.get("user_id"));
		bankCardPrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardPrepay.setApi_version(datadriven.get("api_version"));
		if ("auto".equals(datadriven.get("no_order"))) {
			bankCardPrepay.setNo_order(no_order);
		} else {
			bankCardPrepay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))) {
			bankCardPrepay.setDt_order(dt_order);
		} else {
			bankCardPrepay.setDt_order(datadriven.get("dt_order"));
		}

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
		bankCardPrepay.setType_user(datadriven.get("type_user"));
		bankCardPrepay.setAcct_name(datadriven.get("acct_name"));
		bankCardPrepay.setId_type(datadriven.get("id_type"));
		bankCardPrepay.setId_no(datadriven.get("id_no"));
		bankCardPrepay.setCol_userid(datadriven.get("col_userid"));
		bankCardPrepay.setSecured_partner(datadriven.get("secured_partner"));
		if (datadriven.get("buyer_confirm_valid").length() > 0) {
			bankCardPrepay.setBuyer_confirm_valid(datadriven.get("buyer_confirm_valid"));
		}
		if (datadriven.get("seller_send_valid").length() > 0) {
			bankCardPrepay.setSeller_send_valid(datadriven.get("seller_send_valid"));
		}
		bankCardPrepay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPrepay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardPrepay);
		String bankCardPrepayUrl = Property.get("llwallet.bankCardPrepay.url");

		// 钱包签约支付预处理请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(bankCardPrepayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包签约支付预处理请求返回检查
		// assert hf.getResponseBody().contains(datadriven.get("result_code"));

		String no_order1 = JSONObject.parseObject(hf.getResponseBody()).getString("no_order");
		String dt_order1 = JSONObject.parseObject(hf.getResponseBody()).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(hf.getResponseBody()).getString("oid_paybill");
		System.out.println("no_order------" + no_order1);
		System.out.println("dt_order------" + dt_order1);
		System.out.println("oid_paybill------" + oid_paybill);

		Thread.sleep(100);
		String dt_refund1 = ApiUtils.getCurrentDateSecondStr();
		String no_refund1 = ApiUtils.getCurrentDateMillisecondStr();

		SecpayRefund secpayRefund = new SecpayRefund();
		secpayRefund.setOid_partner(datadriven.get("oid_partner1"));
		secpayRefund.setSign_type(datadriven.get("sign_type1"));
		if ("auto".equals(datadriven.get("no_refund1"))) {
			secpayRefund.setNo_refund(no_refund1);
		} else {
			secpayRefund.setNo_refund(datadriven.get("no_refund1"));
		}
		if ("auto".equals(datadriven.get("dt_refund1"))) {
			secpayRefund.setDt_refund(dt_refund1);
		} else {
			secpayRefund.setDt_refund(datadriven.get("dt_refund1"));
		}

		if (datadriven.get("money_refund1").length() < 1) {
			secpayRefund.setMoney_refund(null);
		} else {
			secpayRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund1")));
		}
		if ("auto".equals(datadriven.get("no_order1"))) {
			secpayRefund.setNo_order(no_order1);
		} else {
			secpayRefund.setNo_order(datadriven.get("no_order1"));
		}
		if ("auto".equals(datadriven.get("dt_order1"))) {
			secpayRefund.setDt_order(dt_order1);
		} else {
			secpayRefund.setDt_order(datadriven.get("dt_order1"));
		}
		if ("auto".equals(datadriven.get("oid_paybill1"))) {
			secpayRefund.setOid_paybill(oid_paybill);
		} else {
			secpayRefund.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		secpayRefund.setNotify_url(datadriven.get("notify_url1"));
		secpayRefund.setCol_partner(datadriven.get("col_partner1"));
		secpayRefund.setCol_custid(datadriven.get("col_custid1"));
		secpayRefund.setCol_custtype(datadriven.get("col_custtype1"));
		secpayRefund
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(secpayRefund)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(secpayRefund);
		String secpayRefundUrl = Property.get("llwallet.secpayRefund.url");

		// 担保交易退款请求提交
		hf.nextRequest();
		hf.setUrl(secpayRefundUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 担保交易退款请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("担保交易退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "担保交易退款测试", timeOut = 60000, dataProvider = "secpayRefund2")
	public void secpayRefund2(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();

		BalancePwdPay balancePwdPay = new BalancePwdPay();
		balancePwdPay.setOid_partner(datadriven.get("oid_partner"));
		balancePwdPay.setSign_type(datadriven.get("sign_type"));
		balancePwdPay.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))) {
			balancePwdPay.setNo_order(no_order);
		} else {
			balancePwdPay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))) {
			balancePwdPay.setDt_order(dt_order);
		} else {
			balancePwdPay.setDt_order(datadriven.get("dt_order"));
		}

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

		// 余额消费请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(balancePwdPayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		String no_order1 = JSONObject.parseObject(hf.getResponseBody()).getString("no_order");
		String dt_order1 = JSONObject.parseObject(hf.getResponseBody()).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(hf.getResponseBody()).getString("oid_paybill");
		System.out.println("no_order------" + no_order1);
		System.out.println("dt_order------" + dt_order1);
		System.out.println("oid_paybill------" + oid_paybill);

		Thread.sleep(100);
		String dt_refund1 = ApiUtils.getCurrentDateSecondStr();
		String no_refund1 = ApiUtils.getCurrentDateMillisecondStr();

		SecpayRefund secpayRefund = new SecpayRefund();
		secpayRefund.setOid_partner(datadriven.get("oid_partner1"));
		secpayRefund.setSign_type(datadriven.get("sign_type1"));
		if ("auto".equals(datadriven.get("no_refund1"))) {
			secpayRefund.setNo_refund(no_refund1);
		} else {
			secpayRefund.setNo_refund(datadriven.get("no_refund1"));
		}
		if ("auto".equals(datadriven.get("dt_refund1"))) {
			secpayRefund.setDt_refund(dt_refund1);
		} else {
			secpayRefund.setDt_refund(datadriven.get("dt_refund1"));
		}

		if (datadriven.get("money_refund1").length() < 1) {
			secpayRefund.setMoney_refund(null);
		} else {
			secpayRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund1")));
		}
		if ("auto".equals(datadriven.get("no_order1"))) {
			secpayRefund.setNo_order(no_order1);
		} else {
			secpayRefund.setNo_order(datadriven.get("no_order1"));
		}
		if ("auto".equals(datadriven.get("dt_order1"))) {
			secpayRefund.setDt_order(dt_order1);
		} else {
			secpayRefund.setDt_order(datadriven.get("dt_order1"));
		}
		if ("auto".equals(datadriven.get("oid_paybill1"))) {
			secpayRefund.setOid_paybill(oid_paybill);
		} else {
			secpayRefund.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		secpayRefund.setNotify_url(datadriven.get("notify_url1"));
		secpayRefund.setCol_partner(datadriven.get("col_partner1"));
		secpayRefund.setCol_custid(datadriven.get("col_custid1"));
		secpayRefund.setCol_custtype(datadriven.get("col_custtype1"));
		secpayRefund
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(secpayRefund)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(secpayRefund);
		String secpayRefundUrl = Property.get("llwallet.secpayRefund.url");
									
		// 担保交易退款请求提交
		hf.nextRequest();
		hf.setUrl(secpayRefundUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 担保交易退款请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("担保交易退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "secpayRefund")
	public Iterator<Object[]> data4secpayRefund() throws IOException {
		return new ExcelProvider(this, "secpayRefund",1);
	}

	@DataProvider(name = "secpayRefund1")
	public Iterator<Object[]> data4secpayRefund1() throws IOException {
		return new ExcelProvider(this, "secpayRefund1",919);
	}

	@DataProvider(name = "secpayRefund2")
	public Iterator<Object[]> data4secpayRefund2() throws IOException {
		return new ExcelProvider(this, "secpayRefund2",999);
	}

	@AfterClass
	public void afterClass() {

	}

}
