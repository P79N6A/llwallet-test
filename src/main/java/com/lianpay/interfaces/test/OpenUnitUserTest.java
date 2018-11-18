package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.OpenUnitUser;
import com.lianpay.interfaces.bean.SimpleOpenUser;
import com.lianpay.interfaces.dao.DbUtils;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.tools.utils.SampleFileUtils;
import com.lianpay.tools.utils.TelnetUtil;
import com.lianpay.util.RSAUtil2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户直接开户接口测试
 */

public class OpenUnitUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户直接开户接口测试", timeOut = 60000, dataProvider = "openUnitUser")
	public void openUnitUser(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		// 删除用户测试数据
		DbUtils dbUtils = new DbUtils();
		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno();
		dbUtils.deleteUser(oid_userno);

		// 清除用户缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("delete ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();

		// 组织请求报文
		OpenUnitUser openUnitUser = new OpenUnitUser();
		openUnitUser.setOid_partner(oid_partner);
		openUnitUser.setSign_type(datadriven.get("sign_type"));
		openUnitUser.setUser_id(user_id);
		openUnitUser.setMob_bind(datadriven.get("mob_bind"));
		openUnitUser.setName_user(datadriven.get("name_user"));
		openUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openUnitUser.setName_agent(datadriven.get("name_agent"));
		openUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnitUser.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnitUser.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnitUser.setEml_bind(datadriven.get("eml_bind"));
		openUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openUnitUser.setAddr_city(datadriven.get("addr_city"));
		openUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openUnitUser.setBusi_user(datadriven.get("busi_user"));
		openUnitUser.setName_unit(datadriven.get("name_unit"));
		openUnitUser.setNum_license(datadriven.get("num_license"));
		openUnitUser.setExp_license(datadriven.get("exp_license"));
		openUnitUser.setOrg_code(datadriven.get("org_code"));
		openUnitUser.setType_register(datadriven.get("type_register"));
		openUnitUser.setType_license(datadriven.get("type_license"));
		openUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openUnitUser.setType_industry(datadriven.get("type_industry"));
		openUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnitUser.setCity_code(datadriven.get("city_code"));
		openUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openUnitUser.setCard_no(datadriven.get("card_no"));
		openUnitUser.setBank_code(datadriven.get("bank_code"));
		openUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		// openUnitUser.setAcct_name(datadriven.get("acct_name"));
		openUnitUser.setFlag_check(datadriven.get("flag_check"));
		openUnitUser.setRisk_item(datadriven.get("risk_item"));
		openUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnitUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnitUser);
		String openUnitUserUrl = Property.get("llwallet.openUnitUser.url");

		// 企业用户直接开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(openUnitUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		// response写入文件
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), new Date().toString());  
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), reqJson);
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), hf.getResponseBody());
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"),
				"===============================================");

		Reporter.log("企业用户直接开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@Test(description = "企业用户直接开户接口测试", timeOut = 60000, dataProvider = "openUnitUser1")
	public void openUnitUser1(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		// 删除用户测试数据
		DbUtils dbUtils = new DbUtils();
		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno();
		dbUtils.deleteUser(oid_userno);

		// 清除用户缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("delete ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
		telnetUtil.execute(command.toString());
//		telnetUtil.disconnect();

		// 组织请求报文
		OpenUnitUser openUnitUser = new OpenUnitUser();
		openUnitUser.setOid_partner(oid_partner);
		openUnitUser.setSign_type(datadriven.get("sign_type"));
		openUnitUser.setUser_id(user_id);
		openUnitUser.setMob_bind(datadriven.get("mob_bind"));
		openUnitUser.setName_user(datadriven.get("name_user"));
		openUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openUnitUser.setName_agent(datadriven.get("name_agent"));
		openUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnitUser.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnitUser.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnitUser.setEml_bind(datadriven.get("eml_bind"));
		openUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openUnitUser.setAddr_city(datadriven.get("addr_city"));
		openUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openUnitUser.setBusi_user(datadriven.get("busi_user"));
		openUnitUser.setName_unit(datadriven.get("name_unit"));
		openUnitUser.setNum_license(datadriven.get("num_license"));
		openUnitUser.setExp_license(datadriven.get("exp_license"));
		openUnitUser.setOrg_code(datadriven.get("org_code"));
		openUnitUser.setType_register(datadriven.get("type_register"));
		openUnitUser.setType_license(datadriven.get("type_license"));
		openUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openUnitUser.setType_industry(datadriven.get("type_industry"));
		openUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnitUser.setCity_code(datadriven.get("city_code"));
		openUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openUnitUser.setCard_no(datadriven.get("card_no"));
		openUnitUser.setBank_code(datadriven.get("bank_code"));
		openUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		// openUnitUser.setAcct_name(datadriven.get("acct_name"));
		openUnitUser.setFlag_check(datadriven.get("flag_check"));
		openUnitUser.setRisk_item(datadriven.get("risk_item"));
		openUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnitUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnitUser);
		String openUnitUserUrl = Property.get("llwallet.openUnitUser.url");

		// 企业用户直接开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(openUnitUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		// response写入文件
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), new Date().toString());  
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), reqJson);
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), hf.getResponseBody());
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"),
				"===============================================");
		
		// 重复开户
		String user_id1 = datadriven.get("user_id1");
		String oid_partner1 = datadriven.get("oid_partner1");
		
		if (oid_partner.equals(oid_partner1)){

		} else {
			//删除用户测试数据
//			DbUtils dbUtils = new DbUtils();		
			String oid_userno1 = dbUtils.queryOidUserNo(oid_partner1, user_id1).getOid_userno();
			dbUtils.deleteUser(oid_userno1);		
			
			// 清除用户缓存
//			TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
			StringBuffer command1 = new StringBuffer();
			command1.append("delete ").append("TRADER_").append(user_id1).append(oid_partner1).append("llwalltet_userbase");
			telnetUtil.execute(command1.toString());
		}

		telnetUtil.disconnect();		
		
		OpenUnitUser openUnitUser1 = new OpenUnitUser();
		openUnitUser1.setOid_partner(oid_partner1);
		openUnitUser1.setSign_type(datadriven.get("sign_type"));
		openUnitUser1.setUser_id(user_id1);
		openUnitUser1.setMob_bind(datadriven.get("mob_bind1"));
		openUnitUser1.setName_user(datadriven.get("name_user"));
		openUnitUser1.setType_idcard(datadriven.get("type_idcard"));
		openUnitUser1.setExp_idcard(datadriven.get("exp_idcard"));
		openUnitUser1.setType_expidcard(datadriven.get("type_expidcard"));
		openUnitUser1.setNo_idcard(datadriven.get("no_idcard"));
		openUnitUser1.setName_agent(datadriven.get("name_agent"));
		openUnitUser1.setIdno_agent(datadriven.get("idno_agent"));
		openUnitUser1.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnitUser1.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnitUser1.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser1.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnitUser1.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser1.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnitUser1.setEml_bind(datadriven.get("eml_bind"));
		openUnitUser1.setAddr_unit(datadriven.get("addr_unit"));
		openUnitUser1.setAddr_pro(datadriven.get("addr_pro"));
		openUnitUser1.setAddr_city(datadriven.get("addr_city"));
		openUnitUser1.setAddr_dist(datadriven.get("addr_dist"));
		openUnitUser1.setBusi_user(datadriven.get("busi_user"));
		openUnitUser1.setName_unit(datadriven.get("name_unit"));
		openUnitUser1.setNum_license(datadriven.get("num_license"));
		openUnitUser1.setExp_license(datadriven.get("exp_license"));
		openUnitUser1.setOrg_code(datadriven.get("org_code"));
		openUnitUser1.setType_register(datadriven.get("type_register"));
		openUnitUser1.setType_license(datadriven.get("type_license"));
		openUnitUser1.setType_explicense(datadriven.get("type_explicense"));
		openUnitUser1.setType_industry(datadriven.get("type_industry"));
		openUnitUser1.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnitUser1.setCity_code(datadriven.get("city_code"));
		openUnitUser1.setBrabank_name(datadriven.get("brabank_name"));
		openUnitUser1.setCard_no(datadriven.get("card_no"));
		openUnitUser1.setBank_code(datadriven.get("bank_code"));
		openUnitUser1.setPrcptcd(datadriven.get("prcptcd"));
		// openUnitUser.setAcct_name(datadriven.get("acct_name"));
		openUnitUser1.setFlag_check(datadriven.get("flag_check"));
		openUnitUser1.setRisk_item(datadriven.get("risk_item"));
		openUnitUser1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnitUser1)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(openUnitUser1);
		
		// 企业用户直接开户请求提交
		hf.nextRequest();		
		hf.setUrl(openUnitUserUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 企业用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));
		
		// response写入文件
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), new Date().toString());  
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), reqJson1);
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), hf.getResponseBody());
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"),
				"===============================================");

		Reporter.log("企业用户直接开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
	
	@Test(description = "企业用户直接开户接口测试", timeOut = 60000, dataProvider = "openUnitUser2")
	public void openUnitUser2(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		// 删除用户测试数据
		DbUtils dbUtils = new DbUtils();
		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno();
		dbUtils.deleteUser(oid_userno);

		// 清除用户缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("delete ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
		telnetUtil.execute(command.toString());
//		telnetUtil.disconnect();

		// 组织请求报文
		SimpleOpenUser simpleOpenUser = new SimpleOpenUser();
		simpleOpenUser.setOid_partner(oid_partner);
		simpleOpenUser.setSign_type(datadriven.get("sign_type"));
		simpleOpenUser.setUser_id(user_id);
		simpleOpenUser.setMob_bind(datadriven.get("mob_bind"));
		simpleOpenUser.setEml_bind(datadriven.get("eml_bind"));
		simpleOpenUser.setName_user(datadriven.get("name_user"));		
		simpleOpenUser.setNo_idcard(datadriven.get("no_idcard"));
		simpleOpenUser.setRisk_item(datadriven.get("risk_item"));
		simpleOpenUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(simpleOpenUser)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(simpleOpenUser);
		String simpleOpenUserUrl = Property.get("llwallet.simpleOpenUser.url");
		String openUnitUserUrl = Property.get("llwallet.openUnitUser.url");

		// 钱包简易开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(simpleOpenUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包简易开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		// response写入文件
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), new Date().toString());  
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), reqJson);
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), hf.getResponseBody());
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"),
				"===============================================");
		
		// 重复开户
		String user_id1 = datadriven.get("user_id1");
		String oid_partner1 = datadriven.get("oid_partner1");
		
		if (oid_partner.equals(oid_partner1)){

		} else {
			//删除用户测试数据
//			DbUtils dbUtils = new DbUtils();		
			String oid_userno1 = dbUtils.queryOidUserNo(oid_partner1, user_id1).getOid_userno();
			dbUtils.deleteUser(oid_userno1);		
			
			// 清除用户缓存
//			TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
			StringBuffer command1 = new StringBuffer();
			command1.append("delete ").append("TRADER_").append(user_id1).append(oid_partner1).append("llwalltet_userbase");
			telnetUtil.execute(command1.toString());
		}

		telnetUtil.disconnect();		
		
		OpenUnitUser openUnitUser1 = new OpenUnitUser();
		openUnitUser1.setOid_partner(oid_partner1);
		openUnitUser1.setSign_type(datadriven.get("sign_type"));
		openUnitUser1.setUser_id(user_id1);
		openUnitUser1.setMob_bind(datadriven.get("mob_bind1"));
		openUnitUser1.setName_user(datadriven.get("name_user"));
		openUnitUser1.setType_idcard(datadriven.get("type_idcard"));
		openUnitUser1.setExp_idcard(datadriven.get("exp_idcard"));
		openUnitUser1.setType_expidcard(datadriven.get("type_expidcard"));
		openUnitUser1.setNo_idcard(datadriven.get("no_idcard"));
		openUnitUser1.setName_agent(datadriven.get("name_agent"));
		openUnitUser1.setIdno_agent(datadriven.get("idno_agent"));
		openUnitUser1.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnitUser1.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnitUser1.setPwd_login(RSAUtil2.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser1.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnitUser1.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser1.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnitUser1.setEml_bind(datadriven.get("eml_bind"));
		openUnitUser1.setAddr_unit(datadriven.get("addr_unit"));
		openUnitUser1.setAddr_pro(datadriven.get("addr_pro"));
		openUnitUser1.setAddr_city(datadriven.get("addr_city"));
		openUnitUser1.setAddr_dist(datadriven.get("addr_dist"));
		openUnitUser1.setBusi_user(datadriven.get("busi_user"));
		openUnitUser1.setName_unit(datadriven.get("name_unit"));
		openUnitUser1.setNum_license(datadriven.get("num_license"));
		openUnitUser1.setExp_license(datadriven.get("exp_license"));
		openUnitUser1.setOrg_code(datadriven.get("org_code"));
		openUnitUser1.setType_register(datadriven.get("type_register"));
		openUnitUser1.setType_license(datadriven.get("type_license"));
		openUnitUser1.setType_explicense(datadriven.get("type_explicense"));
		openUnitUser1.setType_industry(datadriven.get("type_industry"));
		openUnitUser1.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnitUser1.setCity_code(datadriven.get("city_code"));
		openUnitUser1.setBrabank_name(datadriven.get("brabank_name"));
		openUnitUser1.setCard_no(datadriven.get("card_no"));
		openUnitUser1.setBank_code(datadriven.get("bank_code"));
		openUnitUser1.setPrcptcd(datadriven.get("prcptcd"));
		// openUnitUser.setAcct_name(datadriven.get("acct_name"));
		openUnitUser1.setFlag_check(datadriven.get("flag_check"));
		openUnitUser1.setRisk_item(datadriven.get("risk_item"));
		openUnitUser1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnitUser1)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(openUnitUser1);
		
		// 企业用户直接开户请求提交
		hf.nextRequest();		
		hf.setUrl(openUnitUserUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 企业用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));
		
		// response写入文件
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), new Date().toString());  
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), reqJson1);
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"), hf.getResponseBody());
		SampleFileUtils.appendLine(Property.get("openUnitUserFilepath"),
				"===============================================");

		Reporter.log("企业用户直接开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
	
	@DataProvider(name = "openUnitUser")
	public Iterator<Object[]> data4openUnitUser() throws IOException {
		 return new ExcelProvider(this, "openUnitUser",128);
	}

	@DataProvider(name = "openUnitUser1")
	public Iterator<Object[]> data4openUnitUser1() throws IOException {
		 return new ExcelProvider(this, "openUnitUser1",2222);
	}
	
	@DataProvider(name = "openUnitUser2")
	public Iterator<Object[]> data4openUnitUser2() throws IOException {
		 return new ExcelProvider(this, "openUnitUser2",2222);
	}
	
	@AfterClass
	public void afterClass() {

	}

}
