package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import com.llwallet.interfaces.bean.personal.BankCardPay;
import com.llwallet.interfaces.bean.personal.BankCardPrepay;
import com.llwallet.interfaces.dao.DbUtils;
import com.llwallet.interfaces.dao.TaAcctInfo;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
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
 * 银行卡支付验证接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class BankCardPayTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡支付验证接口测试", timeOut = 60000, dataProvider = "bankCardPay")
	public void bankCardPay(Map<String, String> datadriven) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, datadriven.get("card_no"));
		}

		// 支付申请
		BankCardPrepay bankCardPrepay = new BankCardPrepay();
		bankCardPrepay.setOid_partner(datadriven.get("oid_partner"));
		bankCardPrepay.setSign_type(datadriven.get("sign_type"));
		bankCardPrepay.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
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
		bankCardPrepay.setBind_mob(MyConfig.getMobBind(datadriven.get("bind_mob")));
		bankCardPrepay.setVali_date(datadriven.get("vali_date"));
		bankCardPrepay.setCvv2(datadriven.get("cvv2"));
		if ("get".equals(datadriven.get("no_agree"))) {
			String no_agree = dbUtils.querySigned(datadriven.get("user_id")).getAgreementno();
			bankCardPrepay.setNo_agree(no_agree);
		} else {
			bankCardPrepay.setNo_agree(datadriven.get("no_agree"));
		}
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
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
		String rsp = HttpRequest.httpPostWithJSON(bankCardPrepayUrl, reqJson);
		Thread.sleep(10);
		String token = JSONObject.parseObject(rsp).getString("token");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(100);
			String verify_code = dbUtils.queryCdSmscd(MyConfig.getMobBind(datadriven.get("bind_mob"))).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setOid_partner(datadriven.get("oid_partner1"));
			if ("get".equals(datadriven.get("token1"))) {
				bankCardPay.setToken(token);
			} else {
				bankCardPay.setToken(datadriven.get("token1"));
			}
			bankCardPay.setSign_type(datadriven.get("sign_type1"));
			if ("get".equals(datadriven.get("no_order1"))) {
				bankCardPay.setNo_order(no_order);
			} else {
				bankCardPay.setNo_order(datadriven.get("no_order1"));
			}
			bankCardPay.setMoney_order(datadriven.get("money_order1"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner1"));
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key1")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			String rsp1 = HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", rsp1);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt",
					"===============================================");
			// 银行卡支付短信验证请求返回检查
			assert rsp1.contains(datadriven.get("expect_return"));
			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
						JSONObject.parseObject(rsp1).getString("sign"));
			}
		} else {
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt",
					"===============================================");
			// 银行卡支付短信验证请求返回检查
			assert rsp.contains(datadriven.get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));
			}
		}

		Reporter.log("银行卡支付验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings("unchecked")
	@Test(description = "银行卡支付验证接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = dt.get("baseline").get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, dt.get("baseline").get("card_no"));
		}

		// 银行卡支付前余额
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
		TaAcctInfo taAcctInfo = new TaAcctInfo();
		String amtBalCur, amtBalAval, amtBalFrz;
		if ("110001".equals(dt.get("baseline").get("busi_partner"))
				&& StringUtils.isBlank(dt.get("baseline").get("secured_partner"))
				&& StringUtils.isBlank(dt.get("baseline").get("col_userid"))) {
			taAcctInfo = dbUtils.queryUserAcctInfo(user_id, oid_partner);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("支付前用户： " + user_id + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);
		} else if (StringUtils.isBlank(dt.get("baseline").get("col_oidpartner"))
				&& StringUtils.isBlank(dt.get("baseline").get("secured_partner"))) {
			taAcctInfo = dbUtils.queryUserAcctInfo(dt.get("baseline").get("col_userid"), oid_partner);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log("支付前用户： " + dt.get("baseline").get("col_userid") + "	总余额：" + amtBalCur + "	可用余额："
					+ amtBalAval + "	冻结余额：" + amtBalFrz, true);
		} else {
			taAcctInfo = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur = taAcctInfo.getAmt_balcur();
			amtBalAval = taAcctInfo.getAmt_balaval();
			amtBalFrz = taAcctInfo.getAmt_balfrz();
			Reporter.log(
					"支付前商户： " + oid_traderno + "	总余额：" + amtBalCur + "	可用余额：" + amtBalAval + "	冻结余额：" + amtBalFrz,
					true);
		}

		// 银行卡支付申请
		BankCardPrepay bankCardPrepay = new BankCardPrepay();
		Object reqObj = JavaBeanUtil.convertMap(bankCardPrepay.getClass(), dt.get("baseline"));
		bankCardPrepay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", bankCardPrepay.getSign());
		String reqJson = JSON.toJSONString(map);
		String bankCardPrepayUrl = Property.get("llwallet.bankCardPrepay.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardPrepayUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");
		String money_order = JSONObject.parseObject(rsp).getString("money_order");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");

		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(100);
			String verify_code = dbUtils.queryCdSmscd(dt.get("baseline").get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(dt.get("baseline").get("money_order1"));
			bankCardPay.setCol_oidpartner(dt.get("baseline").get("col_oidpartner1"));
			bankCardPay.setOid_partner(dt.get("baseline").get("oid_partner1"));
			bankCardPay.setSign_type(dt.get("baseline").get("sign_type1"));
			if ("get".equals(dt.get("baseline").get("no_order1"))) {
				bankCardPay.setNo_order(no_order);
			} else {
				bankCardPay.setNo_order(dt.get("baseline").get("no_order1"));
			}
			if ("get".equals(dt.get("baseline").get("token1"))) {
				bankCardPay.setToken(token);
			} else {
				bankCardPay.setToken(dt.get("baseline").get("token1"));
			}
			if ("get".equals(dt.get("baseline").get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(dt.get("baseline").get("verify_code1"));
			}
			bankCardPay.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), dt.get("baseline").get("key1")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			String rsp1 = HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);

			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", reqJson1);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", rsp1);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt",
					"===============================================");
			// 银行卡支付短信验证请求返回检查
			assert rsp1.contains(dt.get("baseline").get("expect_return1"));
			assert rsp.contains(dt.get("baseline").get("expect_return"));
			if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
						JSONObject.parseObject(rsp1).getString("sign"));
			}
		} else {
			// 请求&响应写入文件
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", MyDate.getStringDate());
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", reqJson);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt", rsp);
			SampleFileUtils.appendLine("D://TA//log//bankCardPay.txt",
					"===============================================");
			// 银行卡支付短信验证请求返回检查
			assert rsp.contains(dt.get("baseline").get("expect_return"));
			if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
				assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
						JSONObject.parseObject(rsp).getString("sign"));
			}
		}

		// 银行卡支付后余额
		TaAcctInfo taAcctInfo1 = new TaAcctInfo();
		String amtBalCur1, amtBalAval1, amtBalFrz1;
		BigDecimal amt1, amt2, amt3;
		if ("110001".equals(dt.get("baseline").get("busi_partner"))
				&& StringUtils.isBlank(dt.get("baseline").get("secured_partner"))
				&& StringUtils.isBlank(dt.get("baseline").get("col_userid"))) {
			taAcctInfo1 = dbUtils.queryUserAcctInfo(user_id, oid_partner);
			amtBalCur1 = taAcctInfo1.getAmt_balcur();
			amtBalAval1 = taAcctInfo1.getAmt_balaval();
			amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
			Reporter.log(
					"支付后用户： " + user_id + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额：" + amtBalFrz1,
					true);

			amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
			amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
			amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
			Reporter.log("支付后用户： " + user_id + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3, true);
		} else if (StringUtils.isBlank(dt.get("baseline").get("col_oidpartner"))
				&& StringUtils.isBlank(dt.get("baseline").get("secured_partner"))) {
			taAcctInfo1 = dbUtils.queryUserAcctInfo(dt.get("baseline").get("col_userid"), oid_partner);
			amtBalCur1 = taAcctInfo1.getAmt_balcur();
			amtBalAval1 = taAcctInfo1.getAmt_balaval();
			amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
			Reporter.log("支付后用户： " + dt.get("baseline").get("col_userid") + "	总余额：" + amtBalCur + "	可用余额："
					+ amtBalAval + "	冻结余额：" + amtBalFrz, true);

			amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
			amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
			amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
			Reporter.log("支付后用户： " + dt.get("baseline").get("col_userid") + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2
					+ "	冻结余额增加：" + amt3, true);
		} else {
			taAcctInfo1 = dbUtils.queryTraderAcctInfo(oid_acctno);
			amtBalCur1 = taAcctInfo1.getAmt_balcur();
			amtBalAval1 = taAcctInfo1.getAmt_balaval();
			amtBalFrz1 = taAcctInfo1.getAmt_balfrz();
			Reporter.log("支付后商户： " + oid_traderno + "	总余额：" + amtBalCur1 + "	可用余额：" + amtBalAval1 + "	冻结余额："
					+ amtBalFrz1, true);

			amt1 = new BigDecimal(amtBalCur1).subtract(new BigDecimal(amtBalCur));
			amt2 = new BigDecimal(amtBalAval1).subtract(new BigDecimal(amtBalAval));
			amt3 = new BigDecimal(amtBalFrz1).subtract(new BigDecimal(amtBalFrz));
			Reporter.log("支付后商户： " + oid_traderno + "	总余额增加：" + amt1 + "	可用余额增加：" + amt2 + "	冻结余额增加：" + amt3, true);
		}

		Reporter.log("银行卡支付验证接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 支付单状态校验
		BigDecimal AMT = new BigDecimal(String.valueOf(money_order));
		assert "0".equals(dbUtils.queryPayBillStat(no_order, AMT.multiply(new BigDecimal(1000)).toString()));
		// 收款商户余额校验
		assert amt1.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order2"));
		assert amt2.divide(new BigDecimal(1000)).toString().equals(dt.get("baseline").get("money_order2"));
	}

	@DataProvider(name = "bankCardPay")
	public Iterator<Object[]> data4bankCardPay() throws IOException {
		return new ExcelProvider(this, "bankCardPay", 54);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3137);
	}

	@AfterClass
	public void afterClass() {

	}

}
