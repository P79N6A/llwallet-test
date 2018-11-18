package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.OpenUnitUser;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
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
 * 企业用户直接开户接口测试
 */

public class OpenUnitUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户直接开户接口测试", timeOut = 60000, dataProvider = "openUnitUser")
	public void openUnitUser(Map<String, String> datadriven) throws Exception {

		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String mob_bind = MyConfig.getMobBind(datadriven.get("mob_bind"));
		String eml_bind = user_id + "@yintong.com.cn";
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		String num_license = ApiUtils.getCurrentDateMillisecondStr();
		String num_license1 = 'A' + ApiUtils.getCurrentDateMillisecondStr();

		// 组织请求报文
		OpenUnitUser openUnitUser = new OpenUnitUser();
		openUnitUser.setOid_partner(oid_partner);
		openUnitUser.setSign_type(datadriven.get("sign_type"));
		openUnitUser.setUser_id(user_id);
		openUnitUser.setMob_bind(mob_bind);
		openUnitUser.setName_user(datadriven.get("name_user"));
		openUnitUser.setType_idcard(datadriven.get("type_idcard"));
		openUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUnitUser.setType_expidcard(datadriven.get("type_expidcard"));
		openUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		openUnitUser.setName_agent(datadriven.get("name_agent"));
		openUnitUser.setIdno_agent(datadriven.get("idno_agent"));
		openUnitUser.setExp_idcard_agent(datadriven.get("exp_idcard_agent"));
		openUnitUser.setType_agentexpidcard(datadriven.get("type_agentexpidcard"));
		if (datadriven.get("pwd_login").length() != 0 && datadriven.get("pwd_login").length() < 50) {
			openUnitUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			openUnitUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnitUser.setEml_bind(eml_bind);
		openUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		openUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		openUnitUser.setAddr_city(datadriven.get("addr_city"));
		openUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		openUnitUser.setBusi_user(datadriven.get("busi_user"));
		openUnitUser.setName_unit(datadriven.get("name_unit"));
		if ("get".equals(datadriven.get("num_license"))) {
			openUnitUser.setNum_license(num_license);
		} else if ("get1".equals(datadriven.get("num_license"))) {
			openUnitUser.setNum_license(num_license1);
		} else {
			openUnitUser.setNum_license(datadriven.get("num_license"));
		}
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
		openUnitUser.setAcct_name(datadriven.get("acct_name"));
		openUnitUser.setFlag_check(datadriven.get("flag_check"));
		openUnitUser.setRisk_item(datadriven.get("risk_item"));
		openUnitUser.setRandom_key(datadriven.get("random_key"));
		openUnitUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnitUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnitUser);
		String openUnitUserUrl = Property.get("llwallet.openUnitUser.url");
		String rsp = HttpRequest.httpPostWithJSON(openUnitUserUrl, reqJson);

		Reporter.log("企业用户直接开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", "===============================================");

		// 企业用户直接开户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@Test(description = "企业用户直接开户接口测试", timeOut = 60000, dataProvider = "openUnitUser1")
	public void openUnitUser1(Map<String, String> datadriven) throws Exception {

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
			openUnitUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnitUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
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
		String rsp = HttpRequest.httpPostWithJSON(openUnitUserUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", "===============================================");

		// 企业用户直接开户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));

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
			openUnitUser1.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnitUser1.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnitUser1.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
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
		openUnitUser1
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnitUser1)), datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(openUnitUser1);
		String rsp1 = HttpRequest.httpPostWithJSON(openUnitUserUrl, reqJson1);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//openUnitUser.txt", "===============================================");

		// 企业用户直接开户请求返回检查
		assert rsp1.contains(datadriven.get("expect_return1"));

		Reporter.log("企业用户直接开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "openUnitUser")
	public Iterator<Object[]> data4openUnitUser() throws IOException {
		return new ExcelProvider(this, "openUnitUser", 1,2);
	}

	@DataProvider(name = "openUnitUser1")
	public Iterator<Object[]> data4openUnitUser1() throws IOException {
		return new ExcelProvider(this, "openUnitUser1", 111);
	}

	@AfterClass
	public void afterClass() {

	}

}
