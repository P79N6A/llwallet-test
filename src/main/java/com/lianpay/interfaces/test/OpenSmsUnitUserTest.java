package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.OpenSmsUnitUser;
import com.lianpay.interfaces.bean.SmsCheck;
import com.lianpay.interfaces.bean.SmsSend;
import com.lianpay.interfaces.dao.DbUtils;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.tools.utils.TelnetUtil;
import com.lianpay.util.RSAUtil2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户短信开户接口测试
 */

public class OpenSmsUnitUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户短信开户接口测试", timeOut = 60000, dataProvider = "openSmsUnitUser")
	public void openSmsUnitUser(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id1");
		String oid_partner = datadriven.get("oid_partner");
		// 删除用户测试数据
		DbUtils dbUtils = new DbUtils();
		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno();
		dbUtils.deleteUser(oid_userno);
		
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
//		DbUtils dbUtils = new DbUtils();
		String verify_code1 = dbUtils.queryCdSmscd(datadriven.get("mob_bind")).getTno_smscd();

		SmsCheck smsCheck1 = new SmsCheck();
		smsCheck1.setOid_partner(datadriven.get("oid_partner"));
		smsCheck1.setSign_type(datadriven.get("sign_type"));
		smsCheck1.setUser_id(datadriven.get("user_id"));
		// smsCheck1.setToken(datadriven.get("token1"));
		// smsCheck1.setVerify_code(datadriven.get("verify_code1"));

		smsCheck1.setToken(token1);
		smsCheck1.setVerify_code(verify_code1);
		smsCheck1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck1)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(smsCheck1);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url");

		// 钱包短信验证请求提交
		hf.nextRequest();
		hf.setUrl(smsCheckUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String token2 = StringUtils.substringBetween(hf.getResponseBody(), "token\":\"", "\"");
		
		OpenSmsUnitUser openSmsUnitUser = new OpenSmsUnitUser();
		openSmsUnitUser.setOid_partner(datadriven.get("oid_partner"));
		openSmsUnitUser.setSign_type(datadriven.get("sign_type"));
		openSmsUnitUser.setUser_id(datadriven.get("user_id1"));
		openSmsUnitUser.setMob_bind(datadriven.get("mob_bind1"));
		openSmsUnitUser.setToken(token2);
		openSmsUnitUser.setName_user(datadriven.get("name_user"));
		openSmsUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openSmsUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openSmsUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openSmsUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openSmsUnitUser.setName_agent(datadriven.get("name_agent"));
		openSmsUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openSmsUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openSmsUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openSmsUnitUser.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openSmsUnitUser.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openSmsUnitUser.setEml_bind(datadriven.get("eml_bind"));
		openSmsUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openSmsUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openSmsUnitUser.setAddr_city(datadriven.get("addr_city"));
		openSmsUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openSmsUnitUser.setBusi_user(datadriven.get("busi_user"));
		openSmsUnitUser.setName_unit(datadriven.get("name_unit"));
		openSmsUnitUser.setNum_license(datadriven.get("num_license"));
		openSmsUnitUser.setExp_license(datadriven.get("exp_license"));
		openSmsUnitUser.setOrg_code(datadriven.get("org_code"));
		openSmsUnitUser.setType_register(datadriven.get("type_register"));
		openSmsUnitUser.setType_license(datadriven.get("type_license"));
		openSmsUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openSmsUnitUser.setType_industry(datadriven.get("type_industry"));
		openSmsUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openSmsUnitUser.setCity_code(datadriven.get("city_code"));
		openSmsUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openSmsUnitUser.setCard_no(datadriven.get("card_no"));
		openSmsUnitUser.setBank_code(datadriven.get("bank_code"));
		openSmsUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		openSmsUnitUser.setFlag_check(datadriven.get("flag_check"));
		openSmsUnitUser.setRisk_item(datadriven.get("risk_item"));
		openSmsUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openSmsUnitUser)),datadriven.get("key")));
		
		String reqJson2 = JSON.toJSONString(openSmsUnitUser);
		String openSmsUnitUserUrl = Property.get("llwallet.openSmsUnitUser.url");

		// 企业用户短信开户请求提交
		hf.nextRequest();
		hf.setUrl(openSmsUnitUserUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 企业用户短信开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户短信开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
   
	@Test(description = "企业用户短信开户接口测试", timeOut = 60000, dataProvider = "openSmsUnitUser1")
	public void openSmsUnitUser1(Map<String, String> datadriven) throws Exception {

		OpenSmsUnitUser openSmsUnitUser = new OpenSmsUnitUser();
		openSmsUnitUser.setOid_partner(datadriven.get("oid_partner"));
		openSmsUnitUser.setSign_type(datadriven.get("sign_type"));
		openSmsUnitUser.setUser_id(datadriven.get("user_id"));
		openSmsUnitUser.setMob_bind(datadriven.get("mob_bind"));
		openSmsUnitUser.setToken(datadriven.get("token"));
		openSmsUnitUser.setName_user(datadriven.get("name_user"));
		openSmsUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openSmsUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openSmsUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openSmsUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openSmsUnitUser.setName_agent(datadriven.get("name_agent"));
		openSmsUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openSmsUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openSmsUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openSmsUnitUser.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openSmsUnitUser.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openSmsUnitUser.setEml_bind(datadriven.get("eml_bind"));
		openSmsUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openSmsUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openSmsUnitUser.setAddr_city(datadriven.get("addr_city"));
		openSmsUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openSmsUnitUser.setBusi_user(datadriven.get("busi_user"));
		openSmsUnitUser.setName_unit(datadriven.get("name_unit"));
		openSmsUnitUser.setNum_license(datadriven.get("num_license"));
		openSmsUnitUser.setExp_license(datadriven.get("exp_license"));
		openSmsUnitUser.setOrg_code(datadriven.get("org_code"));
		openSmsUnitUser.setType_register(datadriven.get("type_register"));
		openSmsUnitUser.setType_license(datadriven.get("type_license"));
		openSmsUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openSmsUnitUser.setType_industry(datadriven.get("type_industry"));
		openSmsUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openSmsUnitUser.setCity_code(datadriven.get("city_code"));
		openSmsUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openSmsUnitUser.setCard_no(datadriven.get("card_no"));
		openSmsUnitUser.setBank_code(datadriven.get("bank_code"));
		openSmsUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		openSmsUnitUser.setFlag_check(datadriven.get("flag_check"));
		openSmsUnitUser.setRisk_item(datadriven.get("risk_item"));
		openSmsUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openSmsUnitUser)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(openSmsUnitUser);
		String openSmsUnitUserUrl = Property.get("llwallet.openSmsUnitUser.url");

		// 企业用户短信开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(openSmsUnitUserUrl);
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户短信开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("企业用户短信开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
	
	@Test(description = "企业用户短信开户接口测试", timeOut = 60000, dataProvider = "openSmsUnitUser2")
	public void openSmsUnitUser2(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		// 删除用户测试数据
		DbUtils dbUtils = new DbUtils();
		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno();
		dbUtils.deleteUser(oid_userno);
		
		// 清除缓存 用户短信发送5次
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("flush_all");
		telnetUtil.execute(command.toString());
//		telnetUtil.disconnect();

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
//		DbUtils dbUtils = new DbUtils();
		String verify_code1 = dbUtils.queryCdSmscd(datadriven.get("mob_bind")).getTno_smscd();

		SmsCheck smsCheck1 = new SmsCheck();
		smsCheck1.setOid_partner(datadriven.get("oid_partner"));
		smsCheck1.setSign_type(datadriven.get("sign_type"));
		smsCheck1.setUser_id(datadriven.get("user_id"));
		// smsCheck1.setToken(datadriven.get("token1"));
		// smsCheck1.setVerify_code(datadriven.get("verify_code1"));

		smsCheck1.setToken(token1);
		smsCheck1.setVerify_code(verify_code1);
		smsCheck1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck1)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(smsCheck1);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url");

		// 钱包短信验证请求提交
		hf.nextRequest();
		hf.setUrl(smsCheckUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String token2 = StringUtils.substringBetween(hf.getResponseBody(), "token\":\"", "\"");
		
		// 清除用户缓存
//		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command1 = new StringBuffer();
		command1.append("delete ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
		telnetUtil.execute(command1.toString());
//		telnetUtil.disconnect();

		// 组织请求报文
		OpenSmsUnitUser openSmsUnitUser = new OpenSmsUnitUser();
		openSmsUnitUser.setOid_partner(oid_partner);
		openSmsUnitUser.setSign_type(datadriven.get("sign_type"));
		openSmsUnitUser.setUser_id(user_id);
		openSmsUnitUser.setMob_bind(datadriven.get("mob_bind"));
		openSmsUnitUser.setToken(token2);
		openSmsUnitUser.setName_user(datadriven.get("name_user"));
		openSmsUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openSmsUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openSmsUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openSmsUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openSmsUnitUser.setName_agent(datadriven.get("name_agent"));
		openSmsUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openSmsUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openSmsUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openSmsUnitUser.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openSmsUnitUser.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openSmsUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openSmsUnitUser.setEml_bind(datadriven.get("eml_bind"));
		openSmsUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openSmsUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openSmsUnitUser.setAddr_city(datadriven.get("addr_city"));
		openSmsUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openSmsUnitUser.setBusi_user(datadriven.get("busi_user"));
		openSmsUnitUser.setName_unit(datadriven.get("name_unit"));
		openSmsUnitUser.setNum_license(datadriven.get("num_license"));
		openSmsUnitUser.setExp_license(datadriven.get("exp_license"));
		openSmsUnitUser.setOrg_code(datadriven.get("org_code"));
		openSmsUnitUser.setType_register(datadriven.get("type_register"));
		openSmsUnitUser.setType_license(datadriven.get("type_license"));
		openSmsUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openSmsUnitUser.setType_industry(datadriven.get("type_industry"));
		openSmsUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openSmsUnitUser.setCity_code(datadriven.get("city_code"));
		openSmsUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openSmsUnitUser.setCard_no(datadriven.get("card_no"));
		openSmsUnitUser.setBank_code(datadriven.get("bank_code"));
		openSmsUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		openSmsUnitUser.setFlag_check(datadriven.get("flag_check"));
		openSmsUnitUser.setRisk_item(datadriven.get("risk_item"));
		openSmsUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openSmsUnitUser)), datadriven.get("key")));
		String reqJson2= JSON.toJSONString(openSmsUnitUser);
		String openSmsUnitUserUrl = Property.get("llwallet.openSmsUnitUser.url");

		// 企业用户直接开户请求提交
        hf.nextRequest();
		hf.setUrl(openSmsUnitUserUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 企业用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户直接开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
	
	@DataProvider(name = "openSmsUnitUser")
	public Iterator<Object[]> data4openSmsUnitUser() throws IOException {
		return new ExcelProvider(this, "openSmsUnitUser",1);
	}
	
	@DataProvider(name = "openSmsUnitUser1")
	public Iterator<Object[]> data4openSmsUnitUser1() throws IOException {
		return new ExcelProvider(this, "openSmsUnitUser1",1211);
	}

	@DataProvider(name = "openSmsUnitUser2")
	public Iterator<Object[]> data4openSmsUnitUser2() throws IOException {
		return new ExcelProvider(this, "openSmsUnitUser2",3222);
	}
	
	@AfterClass
	public void afterClass() {

	}

}
