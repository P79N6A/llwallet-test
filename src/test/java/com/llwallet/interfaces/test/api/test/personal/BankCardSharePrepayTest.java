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
import com.llwallet.interfaces.bean.personal.BankCardSharePrepay;
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
public class BankCardSharePrepayTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包银行卡支付预处理接口测试", timeOut = 60000, dataProvider = "bankCardSharePrepay")
	public void bankCardSharePrepay(Map<String, String> datadriven) throws Exception {

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
		String no_order = MyConfig.getNoOrder(datadriven.get("no_order"));
		BankCardSharePrepay bankCardSharePrepay = new BankCardSharePrepay();
		bankCardSharePrepay.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		bankCardSharePrepay.setSign_type(datadriven.get("sign_type"));
		bankCardSharePrepay.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		bankCardSharePrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardSharePrepay.setApi_version(datadriven.get("api_version"));
		bankCardSharePrepay.setNo_order(no_order);
		bankCardSharePrepay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		bankCardSharePrepay.setName_goods(datadriven.get("name_goods"));
		bankCardSharePrepay.setMoney_order(datadriven.get("money_order"));
		bankCardSharePrepay.setInfo_order(datadriven.get("info_order"));
		bankCardSharePrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardSharePrepay.setValid_order(datadriven.get("valid_order"));
		bankCardSharePrepay.setRisk_item(datadriven.get("risk_item"));
		bankCardSharePrepay.setPay_type(datadriven.get("pay_type"));
		bankCardSharePrepay.setCard_no(datadriven.get("card_no"));
		bankCardSharePrepay.setBank_code(datadriven.get("bank_code"));
		bankCardSharePrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardSharePrepay.setVali_date(datadriven.get("vali_date"));
		bankCardSharePrepay.setCvv2(datadriven.get("cvv2"));
		if ("get".equals(datadriven.get("no_agree"))) {
			String no_agree = dbUtils.querySigned(MyConfig.getUser(datadriven.get("user_id"))).getAgreementno();
			bankCardSharePrepay.setNo_agree(no_agree);
		} else {
			bankCardSharePrepay.setNo_agree(datadriven.get("no_agree"));
		}

		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			bankCardSharePrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardSharePrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		bankCardSharePrepay.setShareing_data(no_order + "1" + datadriven.get("shareing_data"));
		bankCardSharePrepay.setRandom_key(datadriven.get("random_key"));
		bankCardSharePrepay.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardSharePrepay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardSharePrepay);
		String bankCardSharePrepayUrl = Property.get("llwallet.bankCardSharePrepay.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardSharePrepayUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
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
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt",
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

	@Test(description = "钱包银行卡支付-二维码", timeOut = 60000, dataProvider = "bankCardSharePrepay1")
	public void bankCardSharePrepay1(Map<String, String> datadriven) throws Exception {

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
		String no_order = MyConfig.getNoOrder(datadriven.get("no_order"));
		BankCardSharePrepay bankCardSharePrepay = new BankCardSharePrepay();
		bankCardSharePrepay.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		bankCardSharePrepay.setSign_type(datadriven.get("sign_type"));
		bankCardSharePrepay.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		bankCardSharePrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardSharePrepay.setApi_version(datadriven.get("api_version"));
		bankCardSharePrepay.setNo_order(no_order);
		bankCardSharePrepay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		bankCardSharePrepay.setName_goods(datadriven.get("name_goods"));
		bankCardSharePrepay.setMoney_order(datadriven.get("money_order"));
		bankCardSharePrepay.setInfo_order(datadriven.get("info_order"));
		bankCardSharePrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardSharePrepay.setValid_order(datadriven.get("valid_order"));
		bankCardSharePrepay.setRisk_item(datadriven.get("risk_item"));
		bankCardSharePrepay.setPay_type(datadriven.get("pay_type"));
		bankCardSharePrepay.setCard_no(datadriven.get("card_no"));
		bankCardSharePrepay.setBank_code(datadriven.get("bank_code"));
		bankCardSharePrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardSharePrepay.setVali_date(datadriven.get("vali_date"));
		bankCardSharePrepay.setCvv2(datadriven.get("cvv2"));
		if ("get".equals(datadriven.get("no_agree"))) {
			String no_agree = dbUtils.querySigned(MyConfig.getUser(datadriven.get("user_id"))).getAgreementno();
			bankCardSharePrepay.setNo_agree(no_agree);
		} else {
			bankCardSharePrepay.setNo_agree(datadriven.get("no_agree"));
		}

		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			bankCardSharePrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardSharePrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		bankCardSharePrepay.setShareing_data(no_order + "1" + datadriven.get("shareing_data"));
		bankCardSharePrepay.setRandom_key(datadriven.get("random_key"));
		bankCardSharePrepay.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardSharePrepay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardSharePrepay);
		String bankCardSharePrepayUrl = Property.get("llwallet.bankCardSharePrepay.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardSharePrepayUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
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
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt",
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

	@Test(description = "钱包银行卡支付-分账", timeOut = 60000, dataProvider = "bankCardSharePrepay2")
	public void bankCardSharePrepay2(Map<String, String> datadriven) throws Exception {

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
		String no_order = MyConfig.getNoOrder(datadriven.get("no_order"));
		BankCardSharePrepay bankCardSharePrepay = new BankCardSharePrepay();
		bankCardSharePrepay.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		bankCardSharePrepay.setSign_type(datadriven.get("sign_type"));
		bankCardSharePrepay.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		bankCardSharePrepay.setBusi_partner(datadriven.get("busi_partner"));
		bankCardSharePrepay.setApi_version(datadriven.get("api_version"));
		bankCardSharePrepay.setNo_order(no_order);
		bankCardSharePrepay.setDt_order(MyConfig.getDtOrder(datadriven.get("dt_order")));
		bankCardSharePrepay.setName_goods(datadriven.get("name_goods"));
		bankCardSharePrepay.setMoney_order(datadriven.get("money_order"));
		bankCardSharePrepay.setInfo_order(datadriven.get("info_order"));
		bankCardSharePrepay.setNotify_url(datadriven.get("notify_url"));
		bankCardSharePrepay.setValid_order(datadriven.get("valid_order"));
		bankCardSharePrepay.setRisk_item(datadriven.get("risk_item"));
		bankCardSharePrepay.setPay_type(datadriven.get("pay_type"));
		bankCardSharePrepay.setCard_no(datadriven.get("card_no"));
		bankCardSharePrepay.setBank_code(datadriven.get("bank_code"));
		bankCardSharePrepay.setBind_mob(datadriven.get("bind_mob"));
		bankCardSharePrepay.setVali_date(datadriven.get("vali_date"));
		bankCardSharePrepay.setCvv2(datadriven.get("cvv2"));
		if ("get".equals(datadriven.get("no_agree"))) {
			String no_agree = dbUtils.querySigned(MyConfig.getUser(datadriven.get("user_id"))).getAgreementno();
			bankCardSharePrepay.setNo_agree(no_agree);
		} else {
			bankCardSharePrepay.setNo_agree(datadriven.get("no_agree"));
		}

		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			bankCardSharePrepay.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankCardSharePrepay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		bankCardSharePrepay.setShareing_data(no_order + "1" + datadriven.get("shareing_data"));
		bankCardSharePrepay.setRandom_key(datadriven.get("random_key"));
		bankCardSharePrepay.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardSharePrepay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardSharePrepay);
		String bankCardSharePrepayUrl = Property.get("llwallet.bankCardSharePrepay.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardSharePrepayUrl, reqJson);
		String token = JSONObject.parseObject(rsp).getString("token");

		// 支付验证
		if ("8888".equals(JSONObject.parseObject(rsp).getString("ret_code"))) {
			Thread.sleep(10);
			String verify_code = dbUtils.queryCdSmscd(datadriven.get("bind_mob")).getTno_smscd();
			BankCardPay bankCardPay = new BankCardPay();
			bankCardPay.setMoney_order(datadriven.get("money_order"));
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
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardSharePrepay.txt",
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

	@DataProvider(name = "bankCardSharePrepay")
	public Iterator<Object[]> data4bankCardSharePrepay() throws IOException {
		return new ExcelProvider(this, "bankCardSharePrepay", 5, 7);
	}

	@DataProvider(name = "bankCardSharePrepay1")
	public Iterator<Object[]> data4bankCardSharePrepay1() throws IOException {
		return new ExcelProvider(this, "bankCardSharePrepay1", 711);
	}

	@DataProvider(name = "bankCardSharePrepay2")
	public Iterator<Object[]> data4bankCardSharePrepay2() throws IOException {
		return new ExcelProvider(this, "bankCardSharePrepay2", 9911);
	}

	@AfterClass
	public void afterClass() {

	}

}
