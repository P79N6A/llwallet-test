package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.ModifyUnitUser;
import com.llwallet.interfaces.bean.enterprise.PwdAuth;
import com.llwallet.interfaces.bean.personal.SingleUserQuery;
import com.llwallet.interfaces.bean.personal.SmsCheck;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpFixture;
import com.tools.http.HttpRequest;
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
 * 企业用户基本信息修改接口测试
 */

public class ModifyUnitUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUser")
	public void modifyUnitUser(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		// 清除用户缓存
		// TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"),
		// Integer.parseInt(Property.get("port")));
		// StringBuffer command = new StringBuffer();
		// command.append("delete
		// ").append("TRADER_").append(datadriven.get("user_id")).append(datadriven.get("oid_partner")).append("llwalltet_userbase");
		// telnetUtil.execute(command.toString());
		// telnetUtil.disconnect();

		// 更新用户测试数据
		// DbUtils dbUtils = new DbUtils();
		// String oid_userno = dbUtils.queryOidUserNo(oid_partner,
		// user_id).getOid_userno().trim();
		// dbUtils.updateUnitUserAuthResult(oid_userno);

		SingleUserQuery singleUserQuery = new SingleUserQuery();
		singleUserQuery.setOid_partner(datadriven.get("oid_partner"));
		singleUserQuery.setSign_type(datadriven.get("sign_type"));
		singleUserQuery.setUser_id(datadriven.get("user_id"));
		singleUserQuery
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(singleUserQuery)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(singleUserQuery);
		String singleUserQueryUrl = Property.get("llwallet.singleUserQuery.url");

		// 钱包用户查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(singleUserQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		String num_license = JSONObject.parseObject(hf.getResponseBody()).getString("num_license");

		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("auto".equals(datadriven.get("num_license"))) {
			pwdAuth.setNum_license(num_license);
		} else {
			pwdAuth.setNum_license(datadriven.get("num_license"));
		}
		pwdAuth.setFlag_check(datadriven.get("flag_check"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");

		// 钱包支付密码验证授权请求提交
		hf.nextRequest();
		hf.setUrl(pwdAuthUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包支付密码验证授权请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		String token1 = JSONObject.parseObject(hf.getResponseBody()).getString("token");

		ModifyUnitUser modifyUnitUser = new ModifyUnitUser();
		modifyUnitUser.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUser.setSign_type(datadriven.get("sign_type"));
		modifyUnitUser.setToken(token1);
		modifyUnitUser.setUser_id(datadriven.get("user_id"));
		modifyUnitUser.setName_user(datadriven.get("name_user"));
		modifyUnitUser.setType_idcard(datadriven.get("type_idcard"));
		modifyUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		modifyUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		modifyUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		modifyUnitUser.setAddr_city(datadriven.get("addr_city"));
		modifyUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		modifyUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		modifyUnitUser.setBusi_user(datadriven.get("busi_user"));
		modifyUnitUser.setName_unit(datadriven.get("name_unit"));
		modifyUnitUser.setNum_license(datadriven.get("num_license1"));
		modifyUnitUser.setExp_license(datadriven.get("exp_license"));
		modifyUnitUser.setType_register(datadriven.get("type_register"));
		modifyUnitUser.setType_explicense(datadriven.get("type_explicense"));
		modifyUnitUser.setRisk_item(datadriven.get("risk_item"));
		modifyUnitUser
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUser)), datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(modifyUnitUser);
		String modifyUnitUserUrl = Property.get("llwallet.modifyUnitUser.url");

		// 企业用户基本信息修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUnitUserUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 企业用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "企业用户基本信息修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUser1")
	public void modifyUnitUser1(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		// 清除用户缓存
		// TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"),
		// Integer.parseInt(Property.get("port")));
		// StringBuffer command = new StringBuffer();
		// command.append("delete
		// ").append("TRADER_").append(user_id).append(oid_partner).append("llwalltet_userbase");
		// telnetUtil.execute(command.toString());
		// StringBuffer command1 = new StringBuffer();
		// command1.append("flush_all");
		// telnetUtil.execute(command1.toString());
		// telnetUtil.disconnect();

		// 更新用户测试数据
		DbUtils dbUtils = new DbUtils();
		// String oid_userno = dbUtils.queryOidUserNo(oid_partner,
		// user_id).getOid_userno().trim();
		// dbUtils.updateUnitUserAuthResult(oid_userno);

		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		pwdAuth.setNum_license(datadriven.get("num_license"));
		pwdAuth.setFlag_check(datadriven.get("flag_check"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");

		// 钱包支付密码验证授权请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(pwdAuthUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包支付密码验证授权请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		String token1 = JSONObject.parseObject(hf.getResponseBody()).getString("token");
		String verify_code1 = dbUtils.queryCdSmscd(datadriven.get("mob_bind")).getTno_smscd();

		SmsCheck smsCheck1 = new SmsCheck();
		smsCheck1.setOid_partner(datadriven.get("oid_partner"));
		smsCheck1.setSign_type(datadriven.get("sign_type"));
		smsCheck1.setUser_id(datadriven.get("user_id"));
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
		String token2 = JSONObject.parseObject(hf.getResponseBody()).getString("token");

		ModifyUnitUser modifyUnitUser = new ModifyUnitUser();
		modifyUnitUser.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUser.setSign_type(datadriven.get("sign_type"));
		modifyUnitUser.setToken(token2);
		modifyUnitUser.setUser_id(datadriven.get("user_id"));
		modifyUnitUser.setName_user(datadriven.get("name_user"));
		modifyUnitUser.setType_idcard(datadriven.get("type_idcard"));
		modifyUnitUser.setNo_idcard(datadriven.get("no_idcard"));
		modifyUnitUser.setAddr_unit(datadriven.get("addr_unit"));
		modifyUnitUser.setAddr_pro(datadriven.get("addr_pro"));
		modifyUnitUser.setAddr_city(datadriven.get("addr_city"));
		modifyUnitUser.setAddr_dist(datadriven.get("addr_dist"));
		modifyUnitUser.setExp_idcard(datadriven.get("exp_idcard"));
		modifyUnitUser.setBusi_user(datadriven.get("busi_user"));
		modifyUnitUser.setName_unit(datadriven.get("name_unit"));
		modifyUnitUser.setNum_license(datadriven.get("num_license1"));
		modifyUnitUser.setExp_license(datadriven.get("exp_license"));
		modifyUnitUser.setType_register(datadriven.get("type_register"));
		modifyUnitUser.setType_explicense(datadriven.get("type_explicense"));
		modifyUnitUser.setRisk_item(datadriven.get("risk_item"));

		modifyUnitUser
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUser)), datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(modifyUnitUser);
		String modifyUnitUserUrl = Property.get("llwallet.modifyUnitUser.url");

		// 企业用户基本信息修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyUnitUserUrl);
		hf.addRequestBody(reqJson2);
		hf.Post();
		// 企业用户基本信息修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户基本信息修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "企业用户基本信息修改接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 查询用户信息
		SingleUserQuery singleUserQuery = new SingleUserQuery();
		Object reqObj = JavaBeanUtil.convertMap(singleUserQuery.getClass(), dt.get("baseline"));
		singleUserQuery
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", singleUserQuery.getSign());
		String reqJson = JSON.toJSONString(map);
		String singleUserQueryUrl = Property.get("llwallet.singleUserQuery.url");
		String rsp = HttpRequest.httpPostWithJSON(singleUserQueryUrl, reqJson);
		String num_license = JSONObject.parseObject(rsp).getString("num_license");

		// 支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		if ("get".equals(dt.get("baseline").get("num_license"))) {
			dt.get("baseline").put("num_license", num_license);
		}
		Object reqObj1 = JavaBeanUtil.convertMap(pwdAuth.getClass(), dt.get("baseline"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj1)), dt.get("baseline").get("key")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("sign", pwdAuth.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");
		String rsp1 = HttpRequest.httpPostWithJSON(pwdAuthUrl, reqJson1);
		String token1 = JSONObject.parseObject(rsp1).getString("token");

		DbUtils dbUtils = new DbUtils();
		if ("1".equals(dt.get("baseline").get("flag_check"))) {

			String verify_code1 = dbUtils.queryCdSmscd(dt.get("baseline").get("mob_bind")).getTno_smscd();
			SmsCheck smsCheck = new SmsCheck();
			dt.get("baseline").put("token", token1);
			dt.get("baseline").put("verify_code", verify_code1);
			Object reqObj4 = JavaBeanUtil.convertMap(smsCheck.getClass(), dt.get("baseline"));
			smsCheck.setSign(
					GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj4)), dt.get("baseline").get("key")));
			Map<String, String> map4 = JSON.parseObject(JSON.toJSONString(reqObj4), Map.class);
			map4.put("sign", smsCheck.getSign());
			String reqJson4 = JSON.toJSONString(map4);
			String smsCheckUrl = Property.get("llwallet.smsCheck.url");
			String rsp4 = HttpRequest.httpPostWithJSON(smsCheckUrl, reqJson4);
			token1 = JSONObject.parseObject(rsp4).getString("token");
		}

		ModifyUnitUser modifyUnitUser = new ModifyUnitUser();
		dt.get("baseline").put("token", token1);
		dt.get("baseline").put("num_license", dt.get("baseline").get("num_license1"));
		Object reqObj2 = JavaBeanUtil.convertMap(modifyUnitUser.getClass(), dt.get("baseline"));
		modifyUnitUser
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj2)), dt.get("baseline").get("key")));
		Map<String, String> map2 = JSON.parseObject(JSON.toJSONString(reqObj2), Map.class);
		map2.put("sign", modifyUnitUser.getSign());
		String reqJson2 = JSON.toJSONString(map2);
		String modifyUnitUserUrl = Property.get("llwallet.modifyUnitUser.url");
		String rsp2 = HttpRequest.httpPostWithJSON(modifyUnitUserUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//modifyUnitUser.txt",
				"===============================================");
		
		Reporter.log("企业用户基本信息修改接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 企业用户基本信息修改请求返回检查
		assert rsp2.contains(dt.get("baseline").get("expect_return1"));
	}

	@DataProvider(name = "modifyUnitUser")
	public Iterator<Object[]> data4modifyUnitUser() throws IOException {
		return new ExcelProvider(this, "modifyUnitUser", 99);
	}

	@DataProvider(name = "modifyUnitUser1")
	public Iterator<Object[]> data4modifyUnitUser1() throws IOException {
		return new ExcelProvider(this, "modifyUnitUser1", 99);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3, 4);
	}

	@AfterClass
	public void afterClass() {

	}

}
