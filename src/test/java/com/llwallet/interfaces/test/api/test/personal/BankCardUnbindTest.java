package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
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
import com.llwallet.interfaces.bean.personal.BankCardUnbind;
import com.llwallet.interfaces.dao.DbUtils;
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
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 银行卡解绑接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class BankCardUnbindTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡解绑接口测试", timeOut = 60000, dataProvider = "bankCardUnbind")
	public void bankCardUnbind(Map<String, String> datadriven) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, datadriven.get("card_no"));
		}

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (datadriven.get("expect_return").contains("7701")) {
			String oid_userno = dbUtils.queryOidUserNo(datadriven.get("oid_partner"), datadriven.get("user_id"))
					.getOid_userno().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

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
		bankCardPrepay.setRandom_key(datadriven.get("random_key"));
		bankCardPrepay
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPrepay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardPrepay);
		String bankCardPrepayUrl = Property.get("llwallet.bankCardPrepay.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardPrepayUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		String user_id = JSONObject.parseObject(rsp).getString("user_id");

		// 支付验证
		String no_agree = null;
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order1"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner1"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner1"));
			bankCardPay.setSign_type(datadriven.get("sign_type1"));
			bankCardPay.setNo_order(no_order);
			bankCardPay.setToken(token);
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
			no_agree = JSONObject.parseObject(rsp1).getString("no_agree");
		}

		// 银行卡解绑
		BankCardUnbind bankCardUnbind = new BankCardUnbind();
		bankCardUnbind.setOid_partner(datadriven.get("oid_partner2"));
		bankCardUnbind.setSign_type(datadriven.get("sign_type2"));
		if ("get".equals(datadriven.get("user_id"))) {
			bankCardUnbind.setUser_id(user_id);
		} else {
			bankCardUnbind.setUser_id(datadriven.get("user_id2"));
		}
		if ("get".equals(datadriven.get("no_agree2"))) {
			bankCardUnbind.setNo_agree(no_agree);
		} else {
			bankCardUnbind.setNo_agree(datadriven.get("no_agree2"));
		}
		bankCardUnbind.setPay_type(datadriven.get("pay_type2"));
		if (datadriven.get("pwd_pay2").length() != 0 && datadriven.get("pwd_pay2").length() < 50) {
			bankCardUnbind.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay2"), Property.get("rsa_pub_key")));
		} else {
			bankCardUnbind.setPwd_pay(datadriven.get("pwd_pay2"));
		}
		bankCardUnbind.setType_user(datadriven.get("type_user2"));
		bankCardUnbind.setRandom_key(datadriven.get("random_key2"));
		bankCardUnbind
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardUnbind)), datadriven.get("key2")));
		String reqJson2 = JSON.toJSONString(bankCardUnbind);
		String bankCardUnbindUrl = Property.get("llwallet.bankCardUnbind.url");
		String rsp2 = HttpRequest.httpPostWithJSON(bankCardUnbindUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt",
				"===============================================");

		Reporter.log("银行卡解绑接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡解绑请求返回检查
		assert rsp2.contains(datadriven.get("expect_return"));
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp2)),
					JSONObject.parseObject(rsp2).getString("sign"));
		}
	}

	@Test(description = "银行卡解绑接口测试", timeOut = 60000, dataProvider = "bankCardUnbind1")
	public void bankCardUnbind1(Map<String, String> datadriven) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = datadriven.get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, datadriven.get("card_no"));
		}

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (datadriven.get("expect_return").contains("7701")) {
			String oid_userno = dbUtils.queryOidUserNo(datadriven.get("oid_partner"), datadriven.get("user_id"))
					.getOid_userno().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

		BankCardUnbind bankCardUnbind = new BankCardUnbind();
		bankCardUnbind.setOid_partner(datadriven.get("oid_partner"));
		bankCardUnbind.setSign_type(datadriven.get("sign_type"));
		bankCardUnbind.setUser_id(datadriven.get("user_id"));
		bankCardUnbind.setNo_agree(datadriven.get("no_agree"));
		bankCardUnbind.setPay_type(datadriven.get("pay_type"));
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			bankCardUnbind.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardUnbind.setPwd_pay(datadriven.get("pwd_pay"));
		}
		bankCardUnbind.setType_user(datadriven.get("type_user"));
		bankCardUnbind.setRandom_key(datadriven.get("random_key"));
		bankCardUnbind
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardUnbind)), datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(bankCardUnbind);
		String bankCardUnbindUrl = Property.get("llwallet.bankCardUnbind.url");
		String rsp2 = HttpRequest.httpPostWithJSON(bankCardUnbindUrl, reqJson2);

		Reporter.log("银行卡解绑接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡解绑请求返回检查
		assert rsp2.contains(datadriven.get("expect_return"));
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp2)),
					JSONObject.parseObject(rsp2).getString("sign"));
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "银行卡解绑接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		String cardSignUpdate = dt.get("baseline").get("cardSignUpdate");
		if (!StringUtils.isBlank(cardSignUpdate)) {
			dbUtils.deleteCardSigned(aesCryptService, dt.get("baseline").get("card_no"));
		}

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (dt.get("baseline").get("expect_return").contains("7701")) {
			String oid_userno = dbUtils
					.queryOidUserNo(dt.get("baseline").get("oid_partner"), dt.get("baseline").get("user_id"))
					.getOid_userno().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

		// 签约支付申请
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
		String no_order = JSONObject.parseObject(rsp).getString("no_order");
		String verify_code = dbUtils.queryCdSmscd(dt.get("baseline").get("bind_mob")).getTno_smscd();
		System.out.println("verify_code-----------" + verify_code);

		// 签约支付验证
		BankCardPay bankCardPay = new BankCardPay();
		if ("get".equals(dt.get("baseline").get("bankcardpay.token"))) {
			bankCardPay.setToken(token);
		}
		if ("get".equals(dt.get("baseline").get("bankcardpay.verify_code"))) {
			bankCardPay.setVerify_code(verify_code);
		}
		bankCardPay.setCol_oidpartner(dt.get("baseline").get("bankcardpay.col_oidpartner"));
		bankCardPay.setMoney_order(dt.get("baseline").get("bankcardpay.money_order"));
		bankCardPay.setNo_order(no_order);
		bankCardPay.setOid_partner(dt.get("baseline").get("bankcardpay.oid_partner"));
		bankCardPay.setSign_type(dt.get("baseline").get("bankcardpay.sign_type"));
		bankCardPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)),
				dt.get("baseline").get("bankcardpay.key")));
		String reqJson1 = JSON.toJSONString(bankCardPay);
		String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
		String rsp1 = HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
		String no_agree = JSONObject.parseObject(rsp1).getString("no_agree");

		// 解绑
		BankCardUnbind bankCardUnbind = new BankCardUnbind();
		if ("get".equals(dt.get("baseline").get("bankcardunbind.no_agree"))) {
			bankCardUnbind.setNo_agree(no_agree);
		}
		bankCardUnbind.setOid_partner(dt.get("baseline").get("bankcardunbind.oid_partner"));
		bankCardUnbind.setPay_type(dt.get("baseline").get("bankcardunbind.pay_type"));
		bankCardUnbind.setSign_type(dt.get("baseline").get("bankcardunbind.sign_type"));
		bankCardUnbind.setType_user(dt.get("baseline").get("bankcardunbind.type_user"));
		bankCardUnbind.setUser_id(dt.get("baseline").get("bankcardunbind.user_id"));
		if (dt.get("baseline").get("bankcardunbind.pwd_pay").length() != 0
				&& dt.get("baseline").get("bankcardunbind.pwd_pay").length() < 50) {
			bankCardUnbind.setPwd_pay(
					RSAUtil.encrypt(dt.get("baseline").get("bankcardunbind.pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardUnbind.setPwd_pay(dt.get("baseline").get("pwd_pay"));
		}
		bankCardUnbind.setRandom_key(dt.get("baseline").get("bankcardunbind.random_key"));
		bankCardUnbind.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardUnbind)),
				dt.get("baseline").get("bankcardunbind.key")));
		String reqJson2 = JSON.toJSONString(bankCardUnbind);
		String bankCardUnbindUrl = Property.get("llwallet.bankCardUnbind.url");
		String rsp2 = HttpRequest.httpPostWithJSON(bankCardUnbindUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//bankCardUnbind.txt",
				"===============================================");

		Reporter.log("银行卡绑卡验证接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 银行卡解绑请求返回检查
		assert rsp2.contains(dt.get("baseline").get("expect_return"));
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp2)),
					JSONObject.parseObject(rsp2).getString("sign"));
		}

	}

	@DataProvider(name = "bankCardUnbind")
	public Iterator<Object[]> data4bankCardUnbind() throws IOException {
		return new ExcelProvider(this, "bankCardUnbind", 5, 7);
	}

	@DataProvider(name = "bankCardUnbind1")
	public Iterator<Object[]> data4bankCardUnbind1() throws IOException {
		return new ExcelProvider(this, "bankCardUnbind1", 111);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 316);
	}

	@AfterClass
	public void afterClass() {

	}

}