package com.llwallet.interfaces.test.api.test.combination;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import com.llwallet.interfaces.bean.combination.PayeeInfo;
import com.llwallet.interfaces.bean.combination.PayerInfo;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 组合支付申请接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class CombinationPayTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@Autowired
	private PaymentResultNotifyService paymentResultNotifyService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"AGGREGATEAPI_RELATION_KEY")
				.append(Property.get("oid_partner")).append("\"])");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();
	}

	@SuppressWarnings("rawtypes")
	// @Test(description = "组合支付申请接口测试", threadPoolSize = 2, invocationCount =
	// 2, timeOut = 60000, dataProvider = "combinationPay")
	@Test(description = "组合支付申请接口测试", timeOut = 60000, dataProvider = "combinationPay")
	public void combinationPay(Map<String, String> datadriven) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, datadriven.get("card_no"));
		}

		// 组合支付前余额
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String oid_traderno = null, oid_acctno = null;
		if (!StringUtils.isBlank(datadriven.get("secured_partner"))) {
			oid_traderno = datadriven.get("secured_partner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, datadriven.get("busi_partner")).trim();
		} else if (!StringUtils.isBlank(datadriven.get("col_oidpartner"))) {
			oid_traderno = datadriven.get("col_oidpartner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, datadriven.get("busi_partner")).trim();
		}
		TaAcctInfo taAcctInfo, taAcctInfoB, taAcctInfoC = new TaAcctInfo();
		String amtBalCur, amtBalAval, amtBalFrz, amtBalCurB = null, amtBalAvalB = null, amtBalFrzB = null,
				amtBalCurC = null, amtBalAvalC = null, amtBalFrzC = null;
		if (StringUtils.isBlank(datadriven.get("col_oidpartner"))
				&& StringUtils.isBlank(datadriven.get("secured_partner"))) {
			taAcctInfo = dbUtils.queryUserAcctInfo(datadriven.get("col_userid"), oid_partner);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("支付前收款用户： " + datadriven.get("col_userid") + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval
					+ "	冻结余额：" + amtBalFrz, true);
		} else {
			taAcctInfo = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log(
					"支付前收款商户： " + oid_traderno + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);
		}

		if (!StringUtils.isBlank(datadriven.get("amt_balance"))) {
			taAcctInfoB = dbUtils.queryUserAcctInfo(user_id, oid_partner);
			amtBalCurB = taAcctInfoB.getAmt_balcur();
			amtBalAvalB = taAcctInfoB.getAmt_balaval();
			amtBalFrzB = taAcctInfoB.getAmt_balfrz();
			Reporter.log(
					"支付前付款用户账户： " + user_id + "	总余额：" + amtBalCurB + "	可用余额：" + amtBalAvalB + "	冻结余额：" + amtBalFrzB,
					true);
		}

		if (!StringUtils.isBlank(datadriven.get("amt_coupon"))) {
			String oid_acctnoC = dbUtils.queryTraderSettlementAcct(datadriven.get("coupon_partner"));
			taAcctInfoC = dbUtils.queryTraderAcctInfo(oid_acctnoC);
			amtBalCurC = taAcctInfoC.getAmt_balcur();
			amtBalAvalC = taAcctInfoC.getAmt_balaval();
			amtBalFrzC = taAcctInfoC.getAmt_balfrz();
			Reporter.log("支付前优惠券商户结算账户： " + datadriven.get("coupon_partner") + "	总余额：" + amtBalCurC + "	可用余额："
					+ amtBalAvalC + "	冻结余额：" + amtBalFrzC, true);
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
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			combinationPay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			combinationPay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		combinationPay.setRisk_item(datadriven.get("risk_item"));
		combinationPay.setRandom_key(datadriven.get("random_key"));
		
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
		String money_order = datadriven.get("money_order");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		String dt_order = JSONObject.parseObject(rsp).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(rsp).getString("oid_paybill");
		System.out.println("no_order：" + no_order);
		System.out.println("dt_order：" + dt_order);
		System.out.println("oid_paybill：" + oid_paybill);
		System.out.println("payload：" + JSONObject.parseObject(rsp).getString("payload"));
		assert rsp.contains(datadriven.get("expect_return"));

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
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp1);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt",
					"===============================================");
			// 支付验证请求返回检查
			assert rsp1.contains(datadriven.get("expect_return1"));

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

			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
						JSONObject.parseObject(rsp1).getString("sign"));

				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
				assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
			}
		} else {
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt",
					"===============================================");
			// 支付请求返回检查
			assert rsp.contains(datadriven.get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));

				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
				assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
			}
		}

		// 组合支付后余额
		TaAcctInfo taAcctInfo1, taAcctInfoB1, taAcctInfoC1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1, amtBalCurB1, amtBalAvalB1, amtBalFrzB1, amtBalCurC1, amtBalAvalC1,
				amtBalFrzC1;
		BigDecimal amt1 = null, amt2 = null, amt3, amtB1 = null, amtB2 = null, amtB3, amtC1 = null, amtC2 = null, amtC3;
		if (!StringUtils.isBlank(datadriven.get("col_oidpartner"))
				|| !StringUtils.isBlank(datadriven.get("secured_partner"))
				|| !StringUtils.isBlank(datadriven.get("col_userid"))) {
			if (StringUtils.isBlank(datadriven.get("col_oidpartner"))
					&& StringUtils.isBlank(datadriven.get("secured_partner"))) {
				taAcctInfo1 = dbUtils.queryUserAcctInfo(datadriven.get("col_userid"), oid_partner);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("支付后收款用户： " + datadriven.get("col_userid") + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval
						+ "	冻结余额：" + amtBalFrz, true);

				amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
				amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
				amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
				Reporter.log("支付后收款用户： " + datadriven.get("col_userid") + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2
						+ "	冻结余额增加：" + amt3, true);
			} else {
				taAcctInfo1 = dbUtils.queryTraderAcctInfo(oid_acctno);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("支付后收款商户： " + oid_traderno + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额："
						+ amtBalFrz1, true);

				amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
				amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
				amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
				Reporter.log("支付后收款商户： " + oid_traderno + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3,
						true);
			}
		}

		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "8866".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			if (!StringUtils.isBlank(datadriven.get("amt_balance"))) {
				taAcctInfoB1 = dbUtils.queryUserAcctInfo(user_id, oid_partner);
				amtBalCurB1 = taAcctInfoB1.getAmt_balcur();
				amtBalAvalB1 = taAcctInfoB1.getAmt_balaval();
				amtBalFrzB1 = taAcctInfoB1.getAmt_balfrz();
				Reporter.log("支付后付款用户账户： " + user_id + "	总余额：" + amtBalCurB1 + "	可用余额：" + amtBalAvalB1 + "	冻结余额："
						+ amtBalFrzB1, true);

				amtB1 = new BigDecimal(amtBalCurB).subtract(new BigDecimal(amtBalCurB1));
				amtB2 = new BigDecimal(amtBalAvalB).subtract(new BigDecimal(amtBalAvalB1));
				amtB3 = new BigDecimal(amtBalFrzB).subtract(new BigDecimal(amtBalFrzB1));
				Reporter.log("支付后付款用户： " + user_id + "	总余额减少：" + amtB1 + "	可用余额减少：" + amtB2 + "	冻结余额减少：" + amtB3,
						true);
			}

			if (!StringUtils.isBlank(datadriven.get("amt_coupon"))) {
				String oid_acctnoC = dbUtils.queryTraderSettlementAcct(datadriven.get("coupon_partner"));
				taAcctInfoC1 = dbUtils.queryTraderAcctInfo(oid_acctnoC);
				amtBalCurC1 = taAcctInfoC1.getAmt_balcur();
				amtBalAvalC1 = taAcctInfoC1.getAmt_balaval();
				amtBalFrzC1 = taAcctInfoC1.getAmt_balfrz();
				Reporter.log("支付后优惠券商户结算账户： " + datadriven.get("coupon_partner") + "	总余额：" + amtBalCurC1 + "	可用余额："
						+ amtBalAvalC1 + "	冻结余额：" + amtBalFrzC1, true);

				amtC1 = new BigDecimal(amtBalCurC).subtract(new BigDecimal(amtBalCurC1));
				amtC2 = new BigDecimal(amtBalAvalC).subtract(new BigDecimal(amtBalAvalC1));
				amtC3 = new BigDecimal(amtBalFrzC).subtract(new BigDecimal(amtBalFrzC1));
				Reporter.log("支付后优惠券商户结算账户：： " + datadriven.get("coupon_partner") + "	总余额减少：" + amtC1 + "	可用余额减少："
						+ amtC2 + "	冻结余额减少：" + amtC3, true);
			}
		}

		Reporter.log("组合支付申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 收款商户余额校验
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "8866".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			assert amt1.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_order"));
			assert amt2.divide(new BigDecimal(1000)).toString().equals(datadriven.get("money_order"));
			if (!StringUtils.isBlank(datadriven.get("amt_balance"))) {
				assert amtB1.divide(new BigDecimal(1000)).toString().equals(datadriven.get("amt_balance"));
				assert amtB2.divide(new BigDecimal(1000)).toString().equals(datadriven.get("amt_balance"));
			}
			if (!StringUtils.isBlank(datadriven.get("amt_coupon"))) {
				assert amtC1.divide(new BigDecimal(1000)).toString().equals(datadriven.get("amt_coupon"));
				assert amtC2.divide(new BigDecimal(1000)).toString().equals(datadriven.get("amt_coupon"));
			}

		} else if (!"1004".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			assert amt1.toString().equals("0");
			assert amt2.toString().equals("0");
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "组合支付申请接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = dt.get("combinationPay1").get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, dt.get("combinationPay1").get("card_no"));
		}

		// 组合支付申请
		CombinationPay combinationPay = new CombinationPay();
		PayerInfo payerInfo = new PayerInfo();
		PayeeInfo payeeInfo = new PayeeInfo();
		Object reqPayerObj = JavaBeanUtil.convertMap(payerInfo.getClass(),
				excel.getParamMap(dt.get("combinationPay1")));
		Object reqPayeeObj = JavaBeanUtil.convertMap(payeeInfo.getClass(),
				excel.getParamMap(dt.get("combinationPay1")));
		BeanUtils.populate(payerInfo, JSON.parseObject(JSON.toJSONString(reqPayerObj), Map.class));
		BeanUtils.populate(payeeInfo, JSON.parseObject(JSON.toJSONString(reqPayeeObj), Map.class));
		combinationPay.setPayerInfo(payerInfo);
		combinationPay.setPayeeInfo(payeeInfo);
		combinationPay.setOid_partner(MyConfig.getPartner(dt.get("combinationPay1").get("oid_partner")));
		combinationPay.setNo_order(MyConfig.getNoOrder(dt.get("combinationPay1").get("no_order")));
		combinationPay.setDt_order(MyConfig.getDtOrder(dt.get("combinationPay1").get("dt_order")));
		combinationPay.setSign_type(dt.get("combinationPay1").get("sign_type"));
		combinationPay.setTimestamp(MyConfig.getTimestamp(dt.get("combinationPay1").get("timestamp")));
		combinationPay.setApi_version(dt.get("combinationPay1").get("api_version"));
		combinationPay.setBusi_partner(dt.get("combinationPay1").get("busi_partner"));
		combinationPay.setMoney_order(dt.get("combinationPay1").get("money_order"));
		combinationPay.setName_goods(dt.get("combinationPay1").get("name_goods"));
		combinationPay.setInfo_order(dt.get("combinationPay1").get("info_order"));
		combinationPay.setValid_order(dt.get("combinationPay1").get("valid_order"));
		combinationPay.setNotify_url(dt.get("combinationPay1").get("notify_url"));
		combinationPay.setFaceauth_url(dt.get("combinationPay1").get("faceauth_url"));
		if (dt.get("combinationPay1").get("pwd_pay").length() != 0 && dt.get("combinationPay1").get("pwd_pay").length() < 50 ) {
			combinationPay
					.setPwd_pay(RSAUtil.encrypt(dt.get("combinationPay1").get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			combinationPay.setPwd_pay(dt.get("combinationPay1").get("pwd_pay"));
		}
		combinationPay.setRisk_item(dt.get("combinationPay1").get("risk_item"));
		combinationPay.setRandom_key(dt.get("combinationPay1").get("random_key"));
		combinationPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPay)),
				dt.get("combinationPay1").get("key")));
		String reqJson = JSON.toJSONString(combinationPay);
		String combinationPayUrl = Property.get("llwallet.combinationPay.url");
		// 组合支付申请请求提交
		String rsp = HttpRequest.httpPostWithJSON(combinationPayUrl, reqJson);
		String money_order = dt.get("combinationPay1").get("money_order");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		System.out.println("no_order：" + JSONObject.parseObject(rsp).getString("no_order"));
		System.out.println("oid_paybill：" + JSONObject.parseObject(rsp).getString("oid_paybill"));
		System.out.println("payload：" + JSONObject.parseObject(rsp).getString("payload"));
		// excel.writeCurrentCell("combinationPay1", "actual_return", rsp);
		assert rsp.contains(dt.get("combinationPay1").get("expect_return"));
		
		// 网银类异步回调处理成功
		String pay_type = dt.get("combinationPay1").get("pay_type");
		if (("0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
				|| ("8866".equals(JSONObject.parseObject(rsp).getString("ret_code")))) {
			String oid_rungroupid = null;
			oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order);
			String amt_paytype = dt.get("combinationPay1").get("amt_paytype");
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
			combinationPayVerify.setOid_partner(MyConfig.getPartner(dt.get("combinationPay1").get("oid_partner")));
			combinationPayVerify.setSign_type(dt.get("combinationPay1").get("sign_type"));
			combinationPayVerify.setUser_id(dt.get("combinationPay1").get("user_id"));
			combinationPayVerify.setApi_version(dt.get("combinationPay1").get("api_version"));
			combinationPayVerify.setTimestamp(MyConfig.getTimestamp(dt.get("combinationPay1").get("timestamp")));
			combinationPayVerify.setToken(JSONObject.parseObject(rsp).getString("token"));
			combinationPayVerify.setNo_order(JSONObject.parseObject(rsp).getString("no_order"));
			combinationPayVerify
					.setVerify_code(dbUtils.queryCdSmscd(dt.get("combinationPay1").get("bind_mob")).getTno_smscd());
			combinationPayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPayVerify)),
					dt.get("combinationPay1").get("key")));
			String reqJson1 = JSON.toJSONString(combinationPayVerify);
			String combinationPayVerifyUrl = Property.get("llwallet.combinationPayVerify.url");
			rsp1 = HttpRequest.httpPostWithJSON(combinationPayVerifyUrl, reqJson1);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp1);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt",
					"===============================================");
			// 支付验证请求返回检查
			assert rsp1.contains(dt.get("combinationPay1").get("expect_return1"));

			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
						JSONObject.parseObject(rsp1).getString("sign"));

				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
				assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
			}
		} else {
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt",
					"===============================================");
			// 支付请求返回检查
			assert rsp.contains(dt.get("combinationPay1").get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));

				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
				assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
			}
		}

		Reporter.log("组合支付申请接口测试： " + dt.get("combinationPay1").get("comment"), true);
		System.out.println("===============================================");

	}

	@SuppressWarnings({ "rawtypes" })
	@Test(description = "组合支付接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		Thread.sleep(10);
		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = dt.get("baseline").get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, dt.get("baseline").get("card_no"));
		}

		// 组合支付前余额
		String oid_partner = MyConfig.getPartner(dt.get("baseline").get("oid_partner"));
		String user_id = MyConfig.getUser(dt.get("baseline").get("user_id"));
		String oid_traderno = null, oid_acctno = null;
		if (!StringUtils.isBlank(dt.get("baseline").get("secured_partner"))) {
			oid_traderno = dt.get("baseline").get("secured_partner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, dt.get("baseline").get("busi_partner")).trim();
		} else if (!StringUtils.isBlank(dt.get("baseline").get("col_oidpartner"))) {
			oid_traderno = dt.get("baseline").get("col_oidpartner");
			oid_acctno = dbUtils.queryTraderBusinessAcct(oid_traderno, dt.get("baseline").get("busi_partner")).trim();
		}
		TaAcctInfo taAcctInfo, taAcctInfoB, taAcctInfoC = new TaAcctInfo();
		String amtBalCur, amtBalAval, amtBalFrz, amtBalCurB = null, amtBalAvalB = null, amtBalFrzB = null,
				amtBalCurC = null, amtBalAvalC = null, amtBalFrzC = null;
		if (StringUtils.isBlank(dt.get("baseline").get("col_oidpartner"))
				&& StringUtils.isBlank(dt.get("baseline").get("secured_partner"))) {
			taAcctInfo = dbUtils.queryUserAcctInfo(dt.get("baseline").get("col_userid"), oid_partner);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("支付前收款用户： " + dt.get("baseline").get("col_userid") + "	总余额：" + amtBalCur + "	可用余额："
					+ amtBalAval + "	冻结余额：" + amtBalFrz, true);
		} else {
			taAcctInfo = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log(
					"支付前收款商户： " + oid_traderno + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);
		}

		if (!StringUtils.isBlank(dt.get("baseline").get("amt_balance"))) {
			taAcctInfoB = dbUtils.queryUserAcctInfo(user_id, oid_partner);
			amtBalCurB = taAcctInfoB.getAmt_balcur();
			amtBalAvalB = taAcctInfoB.getAmt_balaval();
			amtBalFrzB = taAcctInfoB.getAmt_balfrz();
			Reporter.log(
					"支付前付款用户账户： " + user_id + "	总余额：" + amtBalCurB + "	可用余额：" + amtBalAvalB + "	冻结余额：" + amtBalFrzB,
					true);
		}

		if (!StringUtils.isBlank(dt.get("baseline").get("amt_coupon"))) {
			String oid_acctnoC = dbUtils.queryTraderSettlementAcct(dt.get("baseline").get("coupon_partner"));
			taAcctInfoC = dbUtils.queryTraderAcctInfo(oid_acctnoC);
			amtBalCurC = taAcctInfoC.getAmt_balcur();
			amtBalAvalC = taAcctInfoC.getAmt_balaval();
			amtBalFrzC = taAcctInfoC.getAmt_balfrz();
			Reporter.log("支付前优惠券商户结算账户： " + dt.get("baseline").get("coupon_partner") + "	总余额：" + amtBalCurC
					+ "	可用余额：" + amtBalAvalC + "	冻结余额：" + amtBalFrzC, true);
		}

		// 组合支付申请
		CombinationPay combinationPay = new CombinationPay();
		PayerInfo payerInfo = new PayerInfo();
		PayeeInfo payeeInfo = new PayeeInfo();
		combinationPay.setOid_partner(MyConfig.getPartner(dt.get("baseline").get("oid_partner")));
		combinationPay.setNo_order(MyConfig.getNoOrder(dt.get("baseline").get("no_order")));
		combinationPay.setDt_order(MyConfig.getDtOrder(dt.get("baseline").get("dt_order")));
		combinationPay.setSign_type(dt.get("baseline").get("sign_type"));
		combinationPay.setTimestamp(MyConfig.getTimestamp(dt.get("baseline").get("timestamp")));
		combinationPay.setApi_version(dt.get("baseline").get("api_version"));
		combinationPay.setBusi_partner(dt.get("baseline").get("busi_partner"));
		combinationPay.setMoney_order(dt.get("baseline").get("money_order"));
		combinationPay.setName_goods(dt.get("baseline").get("name_goods"));
		combinationPay.setInfo_order(dt.get("baseline").get("info_order"));
		combinationPay.setValid_order(dt.get("baseline").get("valid_order"));
		combinationPay.setNotify_url(dt.get("baseline").get("notify_url"));
		combinationPay.setFaceauth_url(dt.get("baseline").get("faceauth_url"));
		if (dt.get("baseline").get("pwd_pay").length() != 0 && dt.get("baseline").get("pwd_pay").length() < 50 ) {
			combinationPay.setPwd_pay(RSAUtil.encrypt(dt.get("baseline").get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			combinationPay.setPwd_pay(dt.get("baseline").get("pwd_pay"));
		}
		combinationPay.setRisk_item(dt.get("baseline").get("risk_item"));
		combinationPay.setRandom_key(dt.get("baseline").get("random_key"));

		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("baseline").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("")) {
				params.put((String) entry.getKey(), (String) entry.getValue());
			}
			if (entry.getKey().equals("user_id")) {
				params.put((String) entry.getKey(), MyConfig.getUser(dt.get("baseline").get("user_id")));
			}
		}
		BeanUtils.populate(payerInfo, params);
		BeanUtils.populate(payeeInfo, params);
		combinationPay.setPayerInfo(payerInfo);
		combinationPay.setPayeeInfo(payeeInfo);
		combinationPay.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPay)), dt.get("baseline").get("key")));
		String reqJson = JSON.toJSONString(combinationPay);
		String combinationPayUrl = Property.get("llwallet.combinationPay.url");

		String rsp = HttpRequest.httpPostWithJSON(combinationPayUrl, reqJson);
		String money_order = dt.get("baseline").get("money_order");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		String dt_order = JSONObject.parseObject(rsp).getString("dt_order");
		String oid_paybill = JSONObject.parseObject(rsp).getString("oid_paybill");
		System.out.println("no_order：" + no_order);
		System.out.println("dt_order：" + dt_order);
		System.out.println("oid_paybill：" + oid_paybill);
		System.out.println("payload：" + JSONObject.parseObject(rsp).getString("payload"));
		assert rsp.contains(dt.get("baseline").get("expect_return"));

		// 网银异步回调处理成功
		String pay_type = dt.get("baseline").get("pay_type");
		if (("0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
				|| ("8866".equals(JSONObject.parseObject(rsp).getString("ret_code")))) {
			String oid_rungroupid = null;
			oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order);
			String amt_paytype = dt.get("baseline").get("amt_paytype");
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
			combinationPayVerify.setOid_partner(MyConfig.getPartner(dt.get("baseline").get("oid_partner")));
			combinationPayVerify.setSign_type(dt.get("baseline").get("sign_type"));
			combinationPayVerify.setUser_id(dt.get("baseline").get("user_id"));
			combinationPayVerify.setApi_version(dt.get("baseline").get("api_version"));
			combinationPayVerify.setTimestamp(MyConfig.getTimestamp(dt.get("baseline").get("timestamp")));
			combinationPayVerify.setToken(JSONObject.parseObject(rsp).getString("token"));
			combinationPayVerify.setNo_order(no_order);
			combinationPayVerify
					.setVerify_code(dbUtils.queryCdSmscd(dt.get("baseline").get("bind_mob")).getTno_smscd());
			combinationPayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPayVerify)),
					dt.get("baseline").get("key")));
			String reqJson1 = JSON.toJSONString(combinationPayVerify);
			String combinationPayVerifyUrl = Property.get("llwallet.combinationPayVerify.url");
			rsp1 = HttpRequest.httpPostWithJSON(combinationPayVerifyUrl, reqJson1);
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp1);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt",
					"===============================================");
			// 支付验证请求返回检查
			assert rsp1.contains(dt.get("baseline").get("expect_return1"));

			if (("0000".equals(JSONObject.parseObject(rsp1).getString("ret_code"))
					&& ("I".equals(pay_type) || "L".equals(pay_type) || "Y".equals(pay_type) || "W".equals(pay_type)))
					|| ("8866".equals(JSONObject.parseObject(rsp1).getString("ret_code")))) {
				String oid_rungroupid = null;
				oid_rungroupid = dbUtils.queryPayBillRunGroupId(no_order);
				String amt_paytype = dt.get("baseline").get("amt_paytype");
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

			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
						JSONObject.parseObject(rsp1).getString("sign"));

				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
				assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
			}
		} else {
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//combinationPayVerify.txt",
					"===============================================");
			// 支付请求返回检查
			assert rsp.contains(dt.get("baseline").get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));

				// 支付单状态校验
				BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
				assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
			}
		}

		// 组合支付后余额
		TaAcctInfo taAcctInfo1, taAcctInfoB1, taAcctInfoC1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1, amtBalCurB1, amtBalAvalB1, amtBalFrzB1, amtBalCurC1, amtBalAvalC1,
				amtBalFrzC1;
		BigDecimal amt1 = null, amt2 = null, amt3, amtB1 = null, amtB2 = null, amtB3, amtC1 = null, amtC2 = null, amtC3;
		if (!StringUtils.isBlank(dt.get("baseline").get("col_oidpartner"))
				|| !StringUtils.isBlank(dt.get("baseline").get("secured_partner"))
				|| !StringUtils.isBlank(dt.get("baseline").get("col_userid"))) {
			if (StringUtils.isBlank(dt.get("baseline").get("col_oidpartner"))
					&& StringUtils.isBlank(dt.get("baseline").get("secured_partner"))) {
				taAcctInfo1 = dbUtils.queryUserAcctInfo(dt.get("baseline").get("col_userid"), oid_partner);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("支付后收款用户： " + dt.get("baseline").get("col_userid") + "	总余额：" + amtBalCur + "	可用余额："
						+ amtBalAval + "	冻结余额：" + amtBalFrz, true);

				amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
				amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
				amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
				Reporter.log("支付后收款用户： " + dt.get("baseline").get("col_userid") + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2
						+ "	冻结余额增加：" + amt3, true);
			} else {
				taAcctInfo1 = dbUtils.queryTraderAcctInfo(oid_acctno);
				amtBalCur1 = taAcctInfo1.getAmt_balcur();
				amtBalAval1 = taAcctInfo1.getAmt_balaval();
				amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
				Reporter.log("支付后收款商户： " + oid_traderno + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额："
						+ amtBalFrz1, true);

				amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
				amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
				amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
				Reporter.log("支付后收款商户： " + oid_traderno + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3,
						true);
			}
		}

		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "8866".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			if (!StringUtils.isBlank(dt.get("baseline").get("amt_balance"))) {
				taAcctInfoB1 = dbUtils.queryUserAcctInfo(user_id, oid_partner);
				amtBalCurB1 = taAcctInfoB1.getAmt_balcur();
				amtBalAvalB1 = taAcctInfoB1.getAmt_balaval();
				amtBalFrzB1 = taAcctInfoB1.getAmt_balfrz();
				Reporter.log("支付后付款用户账户： " + user_id + "	总余额：" + amtBalCurB1 + "	可用余额：" + amtBalAvalB1 + "	冻结余额："
						+ amtBalFrzB1, true);

				amtB1 = new BigDecimal(amtBalCurB).subtract(new BigDecimal(amtBalCurB1));
				amtB2 = new BigDecimal(amtBalAvalB).subtract(new BigDecimal(amtBalAvalB1));
				amtB3 = new BigDecimal(amtBalFrzB).subtract(new BigDecimal(amtBalFrzB1));
				Reporter.log("支付后付款用户： " + user_id + "	总余额减少：" + amtB1 + "	可用余额减少：" + amtB2 + "	冻结余额减少：" + amtB3,
						true);
			}

			if (!StringUtils.isBlank(dt.get("baseline").get("amt_coupon"))) {
				String oid_acctnoC = dbUtils.queryTraderSettlementAcct(dt.get("baseline").get("coupon_partner"));
				taAcctInfoC1 = dbUtils.queryTraderAcctInfo(oid_acctnoC);
				amtBalCurC1 = taAcctInfoC1.getAmt_balcur();
				amtBalAvalC1 = taAcctInfoC1.getAmt_balaval();
				amtBalFrzC1 = taAcctInfoC1.getAmt_balfrz();
				Reporter.log("支付后优惠券商户结算账户： " + dt.get("baseline").get("coupon_partner") + "	总余额：" + amtBalCurC1
						+ "	可用余额：" + amtBalAvalC1 + "	冻结余额：" + amtBalFrzC1, true);

				amtC1 = new BigDecimal(amtBalCurC).subtract(new BigDecimal(amtBalCurC1));
				amtC2 = new BigDecimal(amtBalAvalC).subtract(new BigDecimal(amtBalAvalC1));
				amtC3 = new BigDecimal(amtBalFrzC).subtract(new BigDecimal(amtBalFrzC1));
				Reporter.log("支付后优惠券商户结算账户：： " + dt.get("baseline").get("coupon_partner") + "	总余额减少：" + amtC1
						+ "	可用余额减少：" + amtC2 + "	冻结余额减少：" + amtC3, true);
			}
		}

		Reporter.log("组合支付申请接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 收款商户余额校验
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "8866".equals(JSONObject.parseObject(rsp).getString("ret_code"))
				|| "0000".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			assert amt1.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order"));
			assert amt2.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order"));
			if (!StringUtils.isBlank(dt.get("baseline").get("amt_balance"))) {
				assert amtB1.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("amt_balance"));
				assert amtB2.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("amt_balance"));
			}
			if (!StringUtils.isBlank(dt.get("baseline").get("amt_coupon"))) {
				assert amtC1.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("amt_coupon"));
				assert amtC2.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("amt_coupon"));
			}

		} else if (!"1004".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			assert amt1.toString().equals("0");
			assert amt2.toString().equals("0");
		}
	}

	@DataProvider(name = "combinationPay")
	public Iterator<Object[]> data4combinationPay() throws IOException {
		return new ExcelProvider(this, "combinationPay", 2);
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 999);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3111);
		// return new ExcelProvider2(this, 3, 31);
	}

	@AfterClass
	public void afterClass() {

	}

}