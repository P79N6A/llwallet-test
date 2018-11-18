package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.DirectOpenUser;
import com.llwallet.interfaces.bean.enterprise.PwdAuth;
import com.llwallet.interfaces.bean.personal.ModifyUserInfo;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpFixture;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户基本信息修改接口测试
 */

public class ModifyUserInfoTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUserInfo")
	public void modifyUserInfo(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String user_id = ApiUtils.getCurrentDateMillisecondStr();
		String mob_bind = ApiUtils.getMobStr();
		String eml_bind = user_id + "@yintong.com.cn";
		String oid_partner = datadriven.get("oid_partner");

		// 组织开户请求报文
		DirectOpenUser directOpenUser = new DirectOpenUser();
		directOpenUser.setOid_partner(oid_partner);
		directOpenUser.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))) {
			directOpenUser.setUser_id(user_id);
		} else {
			directOpenUser.setUser_id(datadriven.get("user_id"));
		}
		if ("auto".equals(datadriven.get("mob_bind"))) {
			directOpenUser.setMob_bind(mob_bind);
		} else {
			directOpenUser.setMob_bind(datadriven.get("mob_bind"));
		}
		directOpenUser.setName_user(datadriven.get("name_user"));
		directOpenUser.setType_idcard(datadriven.get("type_idcard"));
		directOpenUser.setNo_idcard(datadriven.get("no_idcard"));
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
		directOpenUser
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(directOpenUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(directOpenUser);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");

		// 个人用户直接开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(directOpenUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 个人用户直接开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		// 钱包支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(oid_partner);
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))) {
			pwdAuth.setUser_id(user_id);
		} else {
			pwdAuth.setUser_id(datadriven.get("user_id"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		// pwdAuth.setNum_license(datadriven.get("num_license"));
		pwdAuth.setNo_idcard(datadriven.get("no_idcard"));
		pwdAuth.setFlag_check(datadriven.get("flag_check1"));
		if ("auto".equals(datadriven.get("mob_bind"))) {
			pwdAuth.setMob_bind(mob_bind);
		} else {
			pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		}
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");

		// 钱包支付密码验证授权请求提交
		hf.nextRequest();
		hf.setUrl(pwdAuthUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String token = StringUtils.substringBetween(hf.getResponseBody(), "token\":\"", "\"");

		// 组织修改用户基本信息请求报文
		ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
		modifyUserInfo.setOid_partner(oid_partner);
		modifyUserInfo.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))) {
			modifyUserInfo.setUser_id(user_id);
		} else {
			modifyUserInfo.setUser_id(datadriven.get("user_id"));
		}
		modifyUserInfo.setToken(token);
		// modifyUserInfo.setName_user(datadriven.get("name_user"));
		// modifyUserInfo.setType_idcard(datadriven.get("type_idcard"));
		// modifyUserInfo.setNo_idcard(datadriven.get("no_idcard"));
		modifyUserInfo.setExp_idcard(datadriven.get("exp_idcard1"));
		// modifyUserInfo.setFlg_sex(datadriven.get("flg_sex"));
		modifyUserInfo.setAddr_conn(datadriven.get("addr_conn1"));
		modifyUserInfo.setAddr_pro(datadriven.get("addr_pro1"));
		modifyUserInfo.setAddr_city(datadriven.get("addr_city1"));
		modifyUserInfo.setAddr_dist(datadriven.get("addr_dist1"));
		modifyUserInfo.setOid_job(datadriven.get("oid_job1"));
		modifyUserInfo.setRisk_item(datadriven.get("risk_item"));
		modifyUserInfo
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUserInfo)), datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(modifyUserInfo);
		String modifyUserInfoUrl = Property.get("llwallet.modifyUserInfo.url");

		// 用户基本信息修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUserInfoUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		if (hf.getResponseBody().contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			Thread.sleep(1000);
			// 个人用户基本信息修改后数据检查
			DbUtils dbUtils = new DbUtils();
			JSONObject jsobj = JSONObject.parseObject(hf.getResponseBody());
			String oid_userno_new = jsobj.getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert datadriven.get("result_acctstat").equals(stat_acct);
		}

		Reporter.log("用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUserInfo1")
	public void modifyUserInfo1(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		// 钱包支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		// pwdAuth.setNum_license(datadriven.get("num_license"));
		pwdAuth.setNo_idcard(datadriven.get("no_idcard"));
		pwdAuth.setFlag_check(datadriven.get("flag_check1"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");

		// 钱包支付密码验证授权请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(pwdAuthUrl);
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		String token = StringUtils.substringBetween(hf.getResponseBody(), "token\":\"", "\"");

		// 组织修改用户基本信息请求报文
		ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
		modifyUserInfo.setOid_partner(datadriven.get("oid_partner"));
		modifyUserInfo.setSign_type(datadriven.get("sign_type"));
		modifyUserInfo.setUser_id(datadriven.get("user_id"));
		modifyUserInfo.setToken(token);
		// modifyUserInfo.setName_user(datadriven.get("name_user"));
		// modifyUserInfo.setType_idcard(datadriven.get("type_idcard1"));
		// modifyUserInfo.setNo_idcard(datadriven.get("no_idcard1"));
		modifyUserInfo.setExp_idcard(datadriven.get("exp_idcard1"));
		// modifyUserInfo.setFlg_sex(datadriven.get("flg_sex1"));
		modifyUserInfo.setAddr_conn(datadriven.get("addr_conn1"));
		modifyUserInfo.setAddr_pro(datadriven.get("addr_pro1"));
		modifyUserInfo.setAddr_city(datadriven.get("addr_city1"));
		modifyUserInfo.setAddr_dist(datadriven.get("addr_dist1"));
		modifyUserInfo.setOid_job(datadriven.get("oid_job1"));
		modifyUserInfo.setRisk_item(datadriven.get("risk_item"));
		modifyUserInfo
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUserInfo)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(modifyUserInfo);
		String modifyUserInfoUrl = Property.get("llwallet.modifyUserInfo.url");

		// 用户基本信息修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUserInfoUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		if (hf.getResponseBody().contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			Thread.sleep(1000);
			// 个人用户基本信息修改后数据检查
			DbUtils dbUtils = new DbUtils();
			JSONObject jsobj = JSONObject.parseObject(hf.getResponseBody());
			String oid_userno_new = jsobj.getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert datadriven.get("result_acctstat").equals(stat_acct);
		}

		Reporter.log("用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(description = "用户基本信息修改接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 直接开户
		DirectOpenUser directOpenUser = new DirectOpenUser();
		Object reqObj = JavaBeanUtil.convertMap(directOpenUser.getClass(), dt.get("baseline"));
		directOpenUser
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", directOpenUser.getSign());
		String reqJson = JSON.toJSONString(map);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);

		DbUtils dbUtils = new DbUtils();
		String mob_bind = JSONObject.parseObject(reqJson).getString("mob_bind");
		String no_idcard = JSONObject.parseObject(reqJson).getString("no_idcard");
		String user_id = JSONObject.parseObject(rsp).getString("user_id");

		// 支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		dt.get("baseline").put("user_id", user_id);
		dt.get("baseline").put("mob_bind", mob_bind);
		dt.get("baseline").put("no_idcard", no_idcard);
		Object reqObj1 = JavaBeanUtil.convertMap(pwdAuth.getClass(), dt.get("baseline"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj1)), dt.get("baseline").get("key")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("sign", pwdAuth.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");
		String rsp1 = HttpRequest.httpPostWithJSON(pwdAuthUrl, reqJson1);
		String token1 = JSONObject.parseObject(rsp1).getString("token");

		// 修改用户基本信息
		ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("baseline").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "modifyuserinfo.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(modifyUserInfo, params);
		if ("get".equals(dt.get("baseline").get("modifyuserinfo.user_id"))) {
			modifyUserInfo.setUser_id(user_id);
		}
		if ("get".equals(dt.get("baseline").get("modifyuserinfo.token"))) {
			modifyUserInfo.setToken(token1);
		}
		modifyUserInfo.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(modifyUserInfo)),
				dt.get("baseline").get("modifyuserinfo.key")));
		String reqJson2 = JSON.toJSONString(modifyUserInfo);
		String modifyUserInfoUrl = Property.get("llwallet.modifyUserInfo.url");
		String rsp2 = HttpRequest.httpPostWithJSON(modifyUserInfoUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//modifyUserInfo.txt",
				"===============================================");

		// 修改成功数据检查
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			Thread.sleep(500);
			String oid_userno_new = JSONObject.parseObject(rsp2).getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert dt.get("baseline").get("expect_acctstat").equals(stat_acct);
		}

		Reporter.log("个人用户开户接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 用户基本信息修改请求返回检查
		assert rsp2.contains(dt.get("baseline").get("expect_return1"));

	}

	@DataProvider(name = "modifyUserInfo")
	public Iterator<Object[]> data4modifyUserInfo() throws IOException {
		return new ExcelProvider(this, "modifyUserInfo", 1111);
	}

	@DataProvider(name = "modifyUserInfo1")
	public Iterator<Object[]> data4modifyUserInfo1() throws IOException {
		return new ExcelProvider(this, "modifyUserInfo1", 111);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3);
	}

	@AfterClass
	public void afterClass() {

	}

}