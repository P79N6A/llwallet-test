package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.SmsCheck;
import com.lianpay.interfaces.bean.SmsSend;
import com.lianpay.interfaces.dao.DbUtils;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.tools.utils.TelnetUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包短信验证接口测试
 */

public class SmsCheckTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包短信验证接口测试", timeOut = 60000, dataProvider = "smsCheck")
	public void smsCheck(Map<String, String> datadriven) throws Exception {

		SmsCheck smsCheck = new SmsCheck();
		smsCheck.setOid_partner(datadriven.get("oid_partner"));
		smsCheck.setSign_type(datadriven.get("sign_type"));
		smsCheck.setUser_id(datadriven.get("user_id"));
		smsCheck.setToken(datadriven.get("token"));
		smsCheck.setVerify_code(datadriven.get("verify_code"));
		smsCheck.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(smsCheck);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url");

		// 钱包短信验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + smsCheckUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包短信验证接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@Test(description = "钱包短信验证接口测试", timeOut = 60000, dataProvider = "smsCheck1")
	public void smsCheck1(Map<String, String> datadriven) throws Exception {

		// 清除缓存 用户短信发送5次
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("flush_all");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();

		SmsSend smsSend = new SmsSend();
		smsSend.setOid_partner(datadriven.get("oid_partner"));
		smsSend.setSign_type(datadriven.get("sign_type"));
		smsSend.setUser_id(datadriven.get("user_id"));
		smsSend.setMob_bind(datadriven.get("mob_bind"));
		smsSend.setFlag_send(datadriven.get("flag_send"));
		smsSend.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsSend)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(smsSend);
		String smsSendUrl = Property.get("llwallet.smsSend.url");

		// 钱包下发短信请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(smsSendUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包下发短信请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		String token1 = StringUtils.substringBetween(hf.getResponseBody(), "token\":\"", "\"");
		DbUtils dbUtils = new DbUtils();
		String verify_code1 = dbUtils.queryCdSmscd(datadriven.get("mob_bind")).getTno_smscd();

		SmsCheck smsCheck1 = new SmsCheck();
		smsCheck1.setOid_partner(datadriven.get("oid_partner1"));
		smsCheck1.setSign_type(datadriven.get("sign_type1"));
		smsCheck1.setUser_id(datadriven.get("user_id1"));
		// smsCheck1.setToken(datadriven.get("token1"));
		// smsCheck1.setVerify_code(datadriven.get("verify_code1"));

		smsCheck1.setToken(token1);
		smsCheck1.setVerify_code(verify_code1);
		smsCheck1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck1)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(smsCheck1);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url");

		// 钱包短信验证请求提交
		hf.nextRequest();
		hf.setUrl(smsCheckUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("钱包短信验证接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "smsCheck")
	public Iterator<Object[]> data4smsCheck() throws IOException {
		return new ExcelProvider(this, "smsCheck",111);
	}

	@DataProvider(name = "smsCheck1")
	public Iterator<Object[]> data4smsCheck1() throws IOException {
		return new ExcelProvider(this, "smsCheck1",5);
	}

	@AfterClass
	public void afterClass() {

	}

}
