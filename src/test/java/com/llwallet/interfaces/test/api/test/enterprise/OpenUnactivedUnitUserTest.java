package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.enterprise.OpenUnactivedUnitUser;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户待激活开户接口测试
 */

public class OpenUnactivedUnitUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户待激活开户接口测试", timeOut = 60000, dataProvider = "openUnactivedUnitUser")
	public void openUnactivedUnitUser(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String user_id = null;
		if ("auto".equals(datadriven.get("user_id"))) {
			user_id = ApiUtils.getCurrentDateMillisecondStr();
		} else {
			user_id = datadriven.get("user_id");
		}
		String mob_bind = ApiUtils.getMobStr();
		String eml_bind = user_id + "@yintong.com.cn";
		String oid_partner = datadriven.get("oid_partner");
		String num_license = ApiUtils.getCurrentDateMillisecondStr();
		String num_license1 = 'A' + ApiUtils.getCurrentDateMillisecondStr();

		// 清除用户缓存
//		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
//		StringBuffer command = new StringBuffer();
//		command.append("delete ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
//		telnetUtil.execute(command.toString());
//		telnetUtil.disconnect();

		// 组织请求报文
		OpenUnactivedUnitUser openUnactivedUnitUser = new OpenUnactivedUnitUser();
		openUnactivedUnitUser.setOid_partner(oid_partner);
		openUnactivedUnitUser.setSign_type(datadriven.get("sign_type"));
		openUnactivedUnitUser.setUser_id(user_id);
		if ("auto".equals(datadriven.get("mob_bind"))) {
			openUnactivedUnitUser.setMob_bind(mob_bind);
		} else {
			openUnactivedUnitUser.setMob_bind(datadriven.get("mob_bind"));
		}
		openUnactivedUnitUser.setName_user(datadriven.get("name_user"));
		openUnactivedUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openUnactivedUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUnactivedUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openUnactivedUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openUnactivedUnitUser.setName_agent(datadriven.get("name_agent"));
		openUnactivedUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openUnactivedUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnactivedUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnactivedUnitUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnactivedUnitUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("auto".equals(datadriven.get("eml_bind"))) {
			openUnactivedUnitUser.setEml_bind(eml_bind);
		} else {
			openUnactivedUnitUser.setEml_bind(datadriven.get("eml_bind"));
		}
		openUnactivedUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openUnactivedUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openUnactivedUnitUser.setAddr_city(datadriven.get("addr_city"));
		openUnactivedUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openUnactivedUnitUser.setBusi_user(datadriven.get("busi_user"));
		openUnactivedUnitUser.setName_unit(datadriven.get("name_unit"));
		if ("auto".equals(datadriven.get("num_license"))) {
			openUnactivedUnitUser.setNum_license(num_license);
		} else if ("auto1".equals(datadriven.get("num_license"))) {
			openUnactivedUnitUser.setNum_license(num_license1);
		} else {
			openUnactivedUnitUser.setNum_license(datadriven.get("num_license"));
		}
		openUnactivedUnitUser.setExp_license(datadriven.get("exp_license"));
		openUnactivedUnitUser.setOrg_code(datadriven.get("org_code"));
		openUnactivedUnitUser.setType_register(datadriven.get("type_register"));
		openUnactivedUnitUser.setType_license(datadriven.get("type_license"));
		openUnactivedUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openUnactivedUnitUser.setType_industry(datadriven.get("type_industry"));
		openUnactivedUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnactivedUnitUser.setCity_code(datadriven.get("city_code"));
		openUnactivedUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openUnactivedUnitUser.setCard_no(datadriven.get("card_no"));
		openUnactivedUnitUser.setBank_code(datadriven.get("bank_code"));
		openUnactivedUnitUser.setPrcptcd(datadriven.get("prcptcd"));
//		openUnactivedUnitUser.setAcct_name(datadriven.get("acct_name"));
		openUnactivedUnitUser.setFlag_check(datadriven.get("flag_check"));
		openUnactivedUnitUser.setRisk_item(datadriven.get("risk_item"));
		openUnactivedUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnactivedUnitUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnactivedUnitUser);
		String openUnactivedUnitUserUrl = Property.get("llwallet.openUnactivedUnitUser.url");

		// 企业用户待激活开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(openUnactivedUnitUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户待激活开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("企业用户待激活开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "企业用户待激活开户接口测试", timeOut = 60000, dataProvider = "openUnactivedUnitUser1")
	public void openUnactivedUnitUser1(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String user_id1 = datadriven.get("user_id1");
		String oid_partner = datadriven.get("oid_partner");
		String oid_partner1 = datadriven.get("oid_partner1");
		// 删除用户测试数据
		DbUtils dbUtils = new DbUtils();
		String oid_userno = dbUtils.queryOidUserNo(oid_partner, user_id).getOid_userno();
		String oid_userno1 = dbUtils.queryOidUserNo(oid_partner1, user_id1).getOid_userno();
		dbUtils.deleteUser(oid_userno);
		dbUtils.deleteUser(oid_userno1);

		// 清除用户缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();

		command.append("delete ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
		telnetUtil.execute(command.toString());
		command1.append("delete ").append("TRADER_").append(user_id1).append(oid_partner1).append("llwalltet_userbase");
		telnetUtil.execute(command1.toString());
		telnetUtil.disconnect();

		// 组织请求报文
		OpenUnactivedUnitUser openUnactivedUnitUser = new OpenUnactivedUnitUser();
		openUnactivedUnitUser.setOid_partner(oid_partner);
		openUnactivedUnitUser.setSign_type(datadriven.get("sign_type"));
		openUnactivedUnitUser.setUser_id(user_id);
		openUnactivedUnitUser.setMob_bind(datadriven.get("mob_bind"));
		openUnactivedUnitUser.setName_user(datadriven.get("name_user"));
		openUnactivedUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openUnactivedUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUnactivedUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openUnactivedUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openUnactivedUnitUser.setName_agent(datadriven.get("name_agent"));
		openUnactivedUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openUnactivedUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnactivedUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnactivedUnitUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnactivedUnitUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnactivedUnitUser.setEml_bind(datadriven.get("eml_bind"));
		openUnactivedUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openUnactivedUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openUnactivedUnitUser.setAddr_city(datadriven.get("addr_city"));
		openUnactivedUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openUnactivedUnitUser.setBusi_user(datadriven.get("busi_user"));
		openUnactivedUnitUser.setName_unit(datadriven.get("name_unit"));
		openUnactivedUnitUser.setNum_license(datadriven.get("num_license"));
		openUnactivedUnitUser.setExp_license(datadriven.get("exp_license"));
		openUnactivedUnitUser.setOrg_code(datadriven.get("org_code"));
		openUnactivedUnitUser.setType_register(datadriven.get("type_register"));
		openUnactivedUnitUser.setType_license(datadriven.get("type_license"));
		openUnactivedUnitUser.setType_explicense(datadriven.get("type_explicense"));
		openUnactivedUnitUser.setType_industry(datadriven.get("type_industry"));
		openUnactivedUnitUser.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnactivedUnitUser.setCity_code(datadriven.get("city_code"));
		openUnactivedUnitUser.setBrabank_name(datadriven.get("brabank_name"));
		openUnactivedUnitUser.setCard_no(datadriven.get("card_no"));
		openUnactivedUnitUser.setBank_code(datadriven.get("bank_code"));
		openUnactivedUnitUser.setPrcptcd(datadriven.get("prcptcd"));
		openUnactivedUnitUser.setFlag_check(datadriven.get("flag_check"));
		openUnactivedUnitUser.setRisk_item(datadriven.get("risk_item"));
		openUnactivedUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnactivedUnitUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnactivedUnitUser);
		String openUnactivedUnitUserUrl = Property.get("llwallet.openUnactivedUnitUser.url");

		// 企业用户待激活开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(openUnactivedUnitUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户待激活开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		Thread.sleep(1000);
		
		OpenUnactivedUnitUser openUnactivedUnitUser1 = new OpenUnactivedUnitUser();
		openUnactivedUnitUser1.setOid_partner(oid_partner1);
		openUnactivedUnitUser1.setSign_type(datadriven.get("sign_type"));
		openUnactivedUnitUser1.setUser_id(user_id1);
		openUnactivedUnitUser1.setMob_bind(datadriven.get("mob_bind1"));
		openUnactivedUnitUser1.setName_user(datadriven.get("name_user"));
		openUnactivedUnitUser1.setType_idcard(datadriven.get("type_idcard"));
		openUnactivedUnitUser1.setExp_idcard(datadriven.get("exp_idcard"));
		openUnactivedUnitUser1.setType_expidcard(datadriven.get("type_expidcard"));
		openUnactivedUnitUser1.setNo_idcard(datadriven.get("no_idcard"));
		openUnactivedUnitUser1.setName_agent(datadriven.get("name_agent"));
		openUnactivedUnitUser1.setIdno_agent(datadriven.get("idno_agent"));
		openUnactivedUnitUser1.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnactivedUnitUser1.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnactivedUnitUser1.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedUnitUser1.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnactivedUnitUser1.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedUnitUser1.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnactivedUnitUser1.setEml_bind(datadriven.get("eml_bind"));
		openUnactivedUnitUser1.setAddr_unit(datadriven.get("addr_unit"));
		openUnactivedUnitUser1.setAddr_pro(datadriven.get("addr_pro"));
		openUnactivedUnitUser1.setAddr_city(datadriven.get("addr_city"));
		openUnactivedUnitUser1.setAddr_dist(datadriven.get("addr_dist"));
		openUnactivedUnitUser1.setBusi_user(datadriven.get("busi_user"));
		openUnactivedUnitUser1.setName_unit(datadriven.get("name_unit"));
		openUnactivedUnitUser1.setNum_license(datadriven.get("num_license"));
		openUnactivedUnitUser1.setExp_license(datadriven.get("exp_license"));
		openUnactivedUnitUser1.setOrg_code(datadriven.get("org_code"));
		openUnactivedUnitUser1.setType_register(datadriven.get("type_register"));
		openUnactivedUnitUser1.setType_license(datadriven.get("type_license"));
		openUnactivedUnitUser1.setType_explicense(datadriven.get("type_explicense"));
		openUnactivedUnitUser1.setType_industry(datadriven.get("type_industry"));
		openUnactivedUnitUser1.setExp_orgcode(datadriven.get("exp_orgcode"));
		openUnactivedUnitUser1.setCity_code(datadriven.get("city_code"));
		openUnactivedUnitUser1.setBrabank_name(datadriven.get("brabank_name"));
		openUnactivedUnitUser1.setCard_no(datadriven.get("card_no"));
		openUnactivedUnitUser1.setBank_code(datadriven.get("bank_code"));
		openUnactivedUnitUser1.setPrcptcd(datadriven.get("prcptcd"));
		openUnactivedUnitUser1.setFlag_check(datadriven.get("flag_check"));
		openUnactivedUnitUser1.setRisk_item(datadriven.get("risk_item"));
		openUnactivedUnitUser1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnactivedUnitUser1)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(openUnactivedUnitUser1);
		
		// 企业用户待激活开户请求提交
		hf.nextRequest();		
		hf.setUrl(openUnactivedUnitUserUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 企业用户待激活开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户待激活开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "openUnactivedUnitUser")
	public Iterator<Object[]> data4openUnactivedUnitUser() throws IOException {
		 return new ExcelProvider(this, "openUnactivedUnitUser",1);
	}

	@DataProvider(name = "openUnactivedUnitUser1")
	public Iterator<Object[]> data4openUnactivedUnitUser1() throws IOException {
		 return new ExcelProvider(this, "openUnactivedUnitUser1",333);
	}
	
	@AfterClass
	public void afterClass() {

	}

}
