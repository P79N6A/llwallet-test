package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
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
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
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
 * 钱包银行卡支付预处理接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class BankCardPrepayTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包银行卡支付预处理接口测试", timeOut = 60000, dataProvider = "bankCardPrepay")
	public void bankCardPrepay(Map<String, String> datadriven) throws Exception {

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

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner"));
			bankCardPay.setSign_type(datadriven.get("sign_type"));
			bankCardPay.setNo_order(no_order);
			bankCardPay.setToken(token);
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
		}

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt",
				"===============================================");

		Reporter.log("钱包银行卡支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡支付短信验证请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"") || rsp.contains("\"ret_code\":\"8888\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

	}

	@Test(description = "钱包银行卡支付-二维码", timeOut = 60000, dataProvider = "bankCardPrepay1")
	public void bankCardPrepay1(Map<String, String> datadriven) throws Exception {

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

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner"));
			bankCardPay.setSign_type(datadriven.get("sign_type"));
			bankCardPay.setNo_order(no_order);
			bankCardPay.setToken(token);
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
		}

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt",
				"===============================================");

		Reporter.log("钱包银行卡支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡支付短信验证请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"") || rsp.contains("\"ret_code\":\"8888\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

	}

	@Test(description = "钱包银行卡支付-分账", timeOut = 60000, dataProvider = "bankCardPrepay2")
	public void bankCardPrepay2(Map<String, String> datadriven) throws Exception {

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

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner"));
			bankCardPay.setSign_type(datadriven.get("sign_type"));
			bankCardPay.setNo_order(no_order);
			bankCardPay.setToken(token);
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
		}

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt",
				"===============================================");

		Reporter.log("钱包银行卡支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡支付短信验证请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"") || rsp.contains("\"ret_code\":\"8888\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

	}

	@Test(description = "钱包银行卡支付-担保交易", timeOut = 60000, dataProvider = "bankCardPrepay3")
	public void bankCardPrepay3(Map<String, String> datadriven) throws Exception {

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

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner"));
			bankCardPay.setSign_type(datadriven.get("sign_type"));
			bankCardPay.setNo_order(no_order);
			bankCardPay.setToken(token);
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
		}

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt",
				"===============================================");

		Reporter.log("钱包银行卡支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡支付短信验证请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"") || rsp.contains("\"ret_code\":\"8888\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

	}

	@Test(description = "钱包银行卡支付-冒烟测试", timeOut = 60000, dataProvider = "smoke")
	public void smoke(Map<String, String> datadriven) throws Exception {

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

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
			bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
			bankCardPay.setOid_partner(datadriven.get("oid_partner"));
			bankCardPay.setSign_type(datadriven.get("sign_type"));
			bankCardPay.setNo_order(no_order);
			bankCardPay.setToken(token);
			if ("get".equals(datadriven.get("verify_code1"))) {
				bankCardPay.setVerify_code(verify_code);
			} else {
				bankCardPay.setVerify_code(datadriven.get("verify_code1"));
			}
			bankCardPay
					.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)), datadriven.get("key")));
			String reqJson1 = JSON.toJSONString(bankCardPay);
			String bankCardPayUrl = Property.get("llwallet.bankCardPay.url");
			HttpRequest.httpPostWithJSON(bankCardPayUrl, reqJson1);
		}

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardPrepay.txt",
				"===============================================");

		Reporter.log("钱包银行卡支付预处理接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡支付短信验证请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"") || rsp.contains("\"ret_code\":\"8888\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

	}

	@DataProvider(name = "bankCardPrepay")
	public Iterator<Object[]> data4bankCardPrepay() throws IOException {
		return new ExcelProvider(this, "bankCardPrepay", 416);
	}

	@DataProvider(name = "bankCardPrepay1")
	public Iterator<Object[]> data4bankCardPrepay1() throws IOException {
		return new ExcelProvider(this, "bankCardPrepay1", 34);
	}

	@DataProvider(name = "bankCardPrepay2")
	public Iterator<Object[]> data4bankCardPrepay2() throws IOException {
		return new ExcelProvider(this, "bankCardPrepay2", 999);
	}

	@DataProvider(name = "bankCardPrepay3")
	public Iterator<Object[]> data4bankCardPrepay3() throws IOException {
		return new ExcelProvider(this, "bankCardPrepay3", 999);
	}

	@DataProvider(name = "smoke")
	public Iterator<Object[]> data4smoke() throws IOException {
		return new ExcelProvider(this, "smoke", 999);
	}

	@AfterClass
	public void afterClass() {

	}

}
