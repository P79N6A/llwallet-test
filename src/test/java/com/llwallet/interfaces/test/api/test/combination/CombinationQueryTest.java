package com.llwallet.interfaces.test.api.test.combination;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
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
import com.llwallet.interfaces.bean.combination.CombinationQuery;
import com.llwallet.interfaces.bean.combination.PayeeInfo;
import com.llwallet.interfaces.bean.combination.PayerInfo;
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
 * 组合支付结果查询接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class CombinationQueryTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@Autowired
	private PaymentResultNotifyService paymentResultNotifyService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "组合支付结果查询接口测试", timeOut = 60000, dataProvider = "combinationQuery")
	public void combinationQuery(Map<String, String> datadriven) throws Exception {

		CombinationQuery combinationQuery = new CombinationQuery();
		combinationQuery.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		combinationQuery.setSign_type(datadriven.get("sign_type"));
		combinationQuery.setApi_version(datadriven.get("api_version"));
		combinationQuery.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
		combinationQuery.setNo_order(datadriven.get("no_order"));
		combinationQuery.setDt_order(datadriven.get("dt_order"));
		combinationQuery.setOid_paybill(datadriven.get("oid_paybill"));
		combinationQuery
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationQuery)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationQuery);
		String combinationQueryUrl = Property.get("llwallet.combinationQuery.url");
		String rsp = HttpRequest.httpPostWithJSON(combinationQueryUrl, reqJson);
		// 组合支付结果查询请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("组合支付结果查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings("rawtypes")
	@Test(description = "组合支付查询测试", timeOut = 60000, dataProvider = "combinationQuery1")
	public void combinationQuery1(Map<String, String> datadriven) throws Exception {

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

		Map<String, String> params = new HashMap<String, String>();
		Iterator it = datadriven.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("")) {
				params.put((String) entry.getKey(), (String) entry.getValue());
			}
			if (entry.getKey().equals("user_id")) {
				params.put((String) entry.getKey(), MyConfig.getUser(datadriven.get("user_id")));
			}
		}
		BeanUtils.populate(payerInfo, params);
		BeanUtils.populate(payeeInfo, params);
		combinationPay.setPayerInfo(payerInfo);
		combinationPay.setPayeeInfo(payeeInfo);
		combinationPay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationPay);
		String combinationPayUrl = Property.get("llwallet.combinationPay.url");

		String rsp = HttpRequest.httpPostWithJSON(combinationPayUrl, reqJson);
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		String dt_order = JSONObject.parseObject(rsp).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(rsp).getString("oid_paybill");
		System.out.println("no_order：" + no_order);
		System.out.println("dt_order：" + dt_order);
		System.out.println("oid_paybill：" + oid_paybill);
		System.out.println("payload：" + JSONObject.parseObject(rsp).getString("payload"));

		// 网银异步回调处理成功
		String pay_type = datadriven.get("pay_type");
		if (("0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
				|| ("8866".equals(JSONObject.parseObject(rsp).getString("ret_code")))) {
			String oid_rungroupid = null;
			oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order);
			String amt_paytype = datadriven.get("amt_paytype");
			String amount = new BigDecimal(amt_paytype).multiply(new BigDecimal(100)).toBigInteger().toString();
			System.out.println("amount：" + amount);
			PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
			paymentprocessreq.setStatus("00");
			paymentprocessreq.setPayRungroupid(oid_rungroupid);
			paymentprocessreq.setSettleDate("20180503");
			paymentprocessreq.setBankSerial(no_order);
			paymentprocessreq.setPayAmount(amount);
			System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
			String rsp2 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
			System.out.println("异步回调返回：" + rsp2);
		}

		// 组合支付验证
		String rsp1 = null;
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			CombinationPayVerify combinationPayVerify = new CombinationPayVerify();
			combinationPayVerify.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
			combinationPayVerify.setSign_type(datadriven.get("sign_type"));
			combinationPayVerify.setUser_id(datadriven.get("user_id"));
			combinationPayVerify.setApi_version(datadriven.get("api_version"));
			combinationPayVerify.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
			combinationPayVerify.setToken(JSONObject.parseObject(rsp).getString("token"));
			combinationPayVerify.setNo_order(no_order);
			combinationPayVerify.setVerify_code(dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd());
			combinationPayVerify.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPayVerify)), datadriven.get("key")));
			String reqJson1 = JSON.toJSONString(combinationPayVerify);
			String combinationPayVerifyUrl = Property.get("llwallet.combinationPayVerify.url");
			rsp1 = HttpRequest.httpPostWithJSON(combinationPayVerifyUrl, reqJson1);

			if (("0000".equals(JSONObject.parseObject(rsp1).getString("ret_code"))
					&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
					|| ("8866".equals(JSONObject.parseObject(rsp1).getString("ret_code")))) {
				String oid_rungroupid = null;
				oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order);
				String amt_paytype = datadriven.get("amt_paytype");
				String amount = new BigDecimal(amt_paytype).multiply(new BigDecimal(100)).toBigInteger().toString();
				System.out.println("amount：" + amount);
				PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
				paymentprocessreq.setStatus("00");
				paymentprocessreq.setPayRungroupid(oid_rungroupid);
				paymentprocessreq.setSettleDate("20180503");
				paymentprocessreq.setBankSerial(no_order);
				paymentprocessreq.setPayAmount(amount);
				System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
				String rsp2 = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
				System.out.println("异步回调返回：" + rsp2);
			}
		}

		Thread.sleep(100);
		CombinationQuery combinationQuery = new CombinationQuery();
		combinationQuery.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner1")));
		combinationQuery.setSign_type(datadriven.get("sign_type1"));
		combinationQuery.setApi_version(datadriven.get("api_version1"));
		combinationQuery.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp1")));
		if ("get".equals(datadriven.get("no_order1"))) {
			combinationQuery.setNo_order(no_order);
		} else {
			combinationQuery.setNo_order(datadriven.get("no_order1"));
		}
		if ("get".equals(datadriven.get("dt_order1"))) {
			combinationQuery.setDt_order(dt_order);
		} else {
			combinationQuery.setDt_order(datadriven.get("dt_order1"));
		}
		if ("get".equals(datadriven.get("oid_paybill1"))) {
			combinationQuery.setOid_paybill(oid_paybill);
		} else {
			combinationQuery.setOid_paybill(datadriven.get("oid_paybill1"));
		}
		combinationQuery.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationQuery)), datadriven.get("key1")));
		String reqJson3 = JSON.toJSONString(combinationQuery);
		String combinationQueryUrl = Property.get("llwallet.combinationQuery.url");
		String rsp3 = HttpRequest.httpPostWithJSON(combinationQueryUrl, reqJson3);
		// 组合支付结果查询返回检查
		assert rsp3.contains(datadriven.get("expect_return"));
		assert rsp3.contains(datadriven.get("expect_return1"));

		Reporter.log("组合支付结果查询测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "combinationQuery")
	public Iterator<Object[]> data4combinationQuery() throws IOException {
		return new ExcelProvider(this, "combinationQuery",999);
	}

	@DataProvider(name = "combinationQuery1")
	public Iterator<Object[]> data4combinationQuery1() throws IOException {
		return new ExcelProvider(this, "combinationQuery1");
	}

	@AfterClass
	public void afterClass() {

	}

}