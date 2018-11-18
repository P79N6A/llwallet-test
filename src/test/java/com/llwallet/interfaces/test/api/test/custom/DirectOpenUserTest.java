package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.DirectOpenUser;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
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
 * 个人用户直接开户接口测试
 */

public class DirectOpenUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户直接开户接口测试", timeOut = 60000, dataProvider = "directOpenUser")
	public void directOpenUser(Map<String, String> datadriven) throws Exception {

		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String mob_bind = MyConfig.getMobBind(datadriven.get("mob_bind"));
		String eml_bind = user_id + "@yintong.com.cn";
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		String no_idcard = MyConfig.getNoIdcard(datadriven.get("no_idcard"));
		DbUtils dbUtils = new DbUtils();

		// 组织请求报文
		DirectOpenUser directOpenUser = new DirectOpenUser();
		directOpenUser.setOid_partner(oid_partner);
		directOpenUser.setSign_type(datadriven.get("sign_type"));
		directOpenUser.setUser_id(user_id);
		directOpenUser.setMob_bind(mob_bind);
		directOpenUser.setName_user(datadriven.get("name_user"));
		directOpenUser.setType_idcard(datadriven.get("type_idcard"));
		directOpenUser.setNo_idcard(no_idcard);
		directOpenUser.setExp_idcard(datadriven.get("exp_idcard"));
		directOpenUser.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0 && datadriven.get("pwd_login").length() < 50) {
			directOpenUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			directOpenUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("auto".equals(datadriven.get("eml_bind"))) {
			directOpenUser.setEml_bind(eml_bind);
		} else {
			directOpenUser.setEml_bind(datadriven.get("eml_bind"));
		}
		directOpenUser.setAddr_conn(datadriven.get("addr_conn"));
		directOpenUser.setAddr_pro(datadriven.get("addr_pro"));
		directOpenUser.setAddr_city(datadriven.get("addr_city"));
		directOpenUser.setAddr_dist(datadriven.get("addr_dist"));
		directOpenUser.setNation_user(datadriven.get("nation_user"));
		directOpenUser.setFlg_sex(datadriven.get("flg_sex"));
		directOpenUser.setOid_job(datadriven.get("oid_job"));
		directOpenUser.setFlag_auth(datadriven.get("flag_auth"));
		directOpenUser.setFlag_check(datadriven.get("flag_check"));
		directOpenUser.setRisk_item(datadriven.get("risk_item"));
		directOpenUser.setRandom_key(datadriven.get("random_key"));
		directOpenUser
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(directOpenUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(directOpenUser);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);
		
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			// 个人用户开户成功数据检查
			JSONObject jsobj = JSONObject.parseObject(rsp);
			String oid_userno_new = jsobj.getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			// assert datadriven.get("result_acctstat").equals(stat_acct);
		}

		Reporter.log("个人用户直接开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt",
				"===============================================");

		// 个人用户直接开户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@Test(description = "个人用户直接开户接口测试", timeOut = 60000, dataProvider = "pt")
	public void pt(Map<String, String> datadriven) throws Exception {

		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String mob_bind = MyConfig.getMobBind(datadriven.get("mob_bind"));
		String eml_bind = user_id + "@yintong.com.cn";
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		String no_idcard = MyConfig.getNoIdcard(datadriven.get("no_idcard"));

		// 组织请求报文
		DirectOpenUser directOpenUser = new DirectOpenUser();
		directOpenUser.setOid_partner(oid_partner);
		directOpenUser.setSign_type(datadriven.get("sign_type"));
		directOpenUser.setUser_id(user_id);
		directOpenUser.setMob_bind(mob_bind);
		directOpenUser.setName_user(datadriven.get("name_user"));
		directOpenUser.setType_idcard(datadriven.get("type_idcard"));
		directOpenUser.setNo_idcard(no_idcard);
		directOpenUser.setExp_idcard(datadriven.get("exp_idcard"));
		directOpenUser.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			directOpenUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			directOpenUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			directOpenUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("auto".equals(datadriven.get("eml_bind"))) {
			directOpenUser.setEml_bind(eml_bind);
		} else {
			directOpenUser.setEml_bind(datadriven.get("eml_bind"));
		}
		directOpenUser.setAddr_conn(datadriven.get("addr_conn"));
		directOpenUser.setAddr_pro(datadriven.get("addr_pro"));
		directOpenUser.setAddr_city(datadriven.get("addr_city"));
		directOpenUser.setAddr_dist(datadriven.get("addr_dist"));
		directOpenUser.setNation_user(datadriven.get("nation_user"));
		directOpenUser.setFlg_sex(datadriven.get("flg_sex"));
		directOpenUser.setOid_job(datadriven.get("oid_job"));
		directOpenUser.setFlag_auth(datadriven.get("flag_auth"));
		directOpenUser.setFlag_check(datadriven.get("flag_check"));
		directOpenUser.setRisk_item(datadriven.get("risk_item"));
		directOpenUser.setRandom_key(datadriven.get("random_key"));
		directOpenUser
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(directOpenUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(directOpenUser);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);

		Reporter.log("个人用户直接开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//directOpenUser.txt",
				"===============================================");
		// 个人用户直接开户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "directOpenUser")
	public Iterator<Object[]> data4directOpenUser() throws IOException {
		return new ExcelProvider(this, "directOpenUser", 5,7);
	}

	@DataProvider(name = "pt")
	public Iterator<Object[]> data4pt() throws IOException {
		return new ExcelProvider(this, "pt", 999);
	}

	@AfterClass
	public void afterClass() {

	}
}