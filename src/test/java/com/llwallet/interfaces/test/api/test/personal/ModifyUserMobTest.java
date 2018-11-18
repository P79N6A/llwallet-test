package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.DirectOpenUser;
import com.llwallet.interfaces.bean.enterprise.PwdAuth;
import com.llwallet.interfaces.bean.personal.BankPayPwdSet;
import com.llwallet.interfaces.bean.personal.ModifyUserMob;
import com.llwallet.interfaces.bean.personal.PayPwdVerify;
import com.llwallet.interfaces.bean.personal.SmsCheck;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户绑定手机修改接口测试
 */

public class ModifyUserMobTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户绑定手机修改接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {
		Thread.sleep(1000);
		// 直接开户
		DirectOpenUser directOpenUser = new DirectOpenUser();
		Object reqObj = JavaBeanUtil.convertMap(directOpenUser.getClass(), excel.getParamMap(dt.get("modifyUserMob")));
		directOpenUser.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("modifyUserMob").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", directOpenUser.getSign());
		String reqJson = JSON.toJSONString(map);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);
		// excel.writeCurrentCell("modifyUserMob", "actual_return", rsp);
		String user_id = JSONObject.parseObject(rsp).getString("user_id");

		// 钱包支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		Object reqObj1 = JavaBeanUtil.convertMap(pwdAuth.getClass(), excel.getParamMap(dt.get("modifyUserMob")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("user_id", user_id);
		map1.put("mob_bind", "");
		map1.put("no_idcard", dt.get("modifyUserMob").get("no_idcard"));
		map1.put("flag_check", dt.get("modifyUserMob").get("flag_check1"));
		pwdAuth.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map1)), dt.get("modifyUserMob").get("key")));
		map1.put("sign", pwdAuth.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");
		String rsp1 = HttpRequest.httpPostWithJSON(pwdAuthUrl, reqJson1);
		// excel.writeCurrentCell("modifyUserMob", "actual_return1", rsp1);
		String token = JSONObject.parseObject(rsp1).getString("token");

		// 修改用户绑定手机
		ModifyUserMob modifyUserMob = new ModifyUserMob();
		Object reqObj2 = JavaBeanUtil.convertMap(modifyUserMob.getClass(), excel.getParamMap(dt.get("modifyUserMob")));
		Map<String, String> map2 = JSON.parseObject(JSON.toJSONString(reqObj2), Map.class);
		map2.put("user_id", user_id);
		map2.put("token", token);
		map2.put("mob_bind", MyConfig.getMobBind(dt.get("modifyUserMob").get("mob_bind2")));
		modifyUserMob.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map2)), dt.get("modifyUserMob").get("key")));
		map2.put("sign", modifyUserMob.getSign());
		String reqJson2 = JSON.toJSONString(map2);
		String modifyUserMobUrl = Property.get("llwallet.modifyUserMob.url");
		String rsp2 = HttpRequest.httpPostWithJSON(modifyUserMobUrl, reqJson2);
		// excel.writeCurrentCell("modifyUserMob", "actual_return2", rsp2);
		Reporter.log("修改用户绑定手机接口测试： " + dt.get("modifyUserMob").get("comment"), true);
		System.out.println("===============================================");
		// 修改用户绑定手机请求返回检查
		assert rsp2.contains(dt.get("modifyUserMob").get("expect_return"));
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户绑定手机修改接口测试", timeOut = 60000, dataProvider = "provider1")
	public void test1(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {
		Thread.sleep(1000);
		// 直接开户
		DirectOpenUser directOpenUser = new DirectOpenUser();
		Object reqObj = JavaBeanUtil.convertMap(directOpenUser.getClass(), excel.getParamMap(dt.get("modifyUserMob1")));
		directOpenUser.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("modifyUserMob1").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", directOpenUser.getSign());
		String reqJson = JSON.toJSONString(map);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);
		// excel.writeCurrentCell("modifyUserMob1", "actual_return", rsp);
		String user_id = JSONObject.parseObject(rsp).getString("user_id");
		String mob_bind = map.get("mob_bind");

		// 钱包支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		Object reqObj1 = JavaBeanUtil.convertMap(pwdAuth.getClass(), excel.getParamMap(dt.get("modifyUserMob1")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("user_id", user_id);
		map1.put("mob_bind", mob_bind);
		map1.put("no_idcard", "");
		map1.put("flag_check", dt.get("modifyUserMob1").get("flag_check1"));
		pwdAuth.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map1)), dt.get("modifyUserMob1").get("key")));
		map1.put("sign", pwdAuth.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");
		String rsp1 = HttpRequest.httpPostWithJSON(pwdAuthUrl, reqJson1);
		// excel.writeCurrentCell("modifyUserMob1", "actual_return1", rsp1);
		String token = JSONObject.parseObject(rsp1).getString("token");

		// 短信验证
		SmsCheck smsCheck = new SmsCheck();
		Object reqObj2 = JavaBeanUtil.convertMap(smsCheck.getClass(), excel.getParamMap(dt.get("modifyUserMob1")));
		Map<String, String> map2 = JSON.parseObject(JSON.toJSONString(reqObj2), Map.class);
		map2.put("user_id", user_id);
		map2.put("token", token);
		DbUtils dbUtils = new DbUtils();
		map2.put("verify_code", dbUtils.queryCdSmscd(mob_bind).getTno_smscd());
		smsCheck.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map2)), dt.get("modifyUserMob1").get("key")));
		map2.put("sign", smsCheck.getSign());
		String reqJson2 = JSON.toJSONString(map2);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url");
		String rsp2 = HttpRequest.httpPostWithJSON(smsCheckUrl, reqJson2);
		// excel.writeCurrentCell("modifyUserMob1", "actual_return2", rsp2);
		String token1 = JSONObject.parseObject(rsp2).getString("token");

		// 修改用户绑定手机
		ModifyUserMob modifyUserMob = new ModifyUserMob();
		Object reqObj3 = JavaBeanUtil.convertMap(modifyUserMob.getClass(), excel.getParamMap(dt.get("modifyUserMob1")));
		Map<String, String> map3 = JSON.parseObject(JSON.toJSONString(reqObj3), Map.class);
		map3.put("user_id", user_id);
		map3.put("token", token1);
		map3.put("mob_bind", MyConfig.getMobBind(dt.get("modifyUserMob1").get("mob_bind2")));
		modifyUserMob.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map3)), dt.get("modifyUserMob1").get("key")));
		map3.put("sign", modifyUserMob.getSign());
		String reqJson3 = JSON.toJSONString(map3);
		String modifyUserMobUrl = Property.get("llwallet.modifyUserMob.url");
		String rsp3 = HttpRequest.httpPostWithJSON(modifyUserMobUrl, reqJson3);
		// excel.writeCurrentCell("modifyUserMob1", "actual_return3", rsp3);
		Reporter.log("修改用户绑定手机接口测试： " + dt.get("modifyUserMob1").get("comment"), true);
		System.out.println("===============================================");
		// 修改用户绑定手机返回检查
		assert rsp3.contains(dt.get("modifyUserMob1").get("expect_return"));
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户绑定手机修改接口测试", timeOut = 60000, dataProvider = "provider2")
	public void test2(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {
		Thread.sleep(1000);
		// 绑卡授权申请
		BankPayPwdSet bankPayPwdSet = new BankPayPwdSet();
		Object reqObj = JavaBeanUtil.convertMap(bankPayPwdSet.getClass(), excel.getParamMap(dt.get("modifyUserMob2")));
		bankPayPwdSet.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("modifyUserMob2").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", bankPayPwdSet.getSign());
		String reqJson = JSON.toJSONString(map);
		String bankPayPwdSetUrl = Property.get("llwallet.bankPayPwdSet.url");
		String rsp = HttpRequest.httpPostWithJSON(bankPayPwdSetUrl, reqJson);
		// excel.writeCurrentCell("modifyUserMob2", "actual_return", rsp);
		String user_id = JSONObject.parseObject(rsp).getString("user_id");
		String token = JSONObject.parseObject(rsp).getString("token");

		// 绑卡授权验证
		PayPwdVerify payPwdVerify = new PayPwdVerify();
		Object reqObj1 = JavaBeanUtil.convertMap(payPwdVerify.getClass(), excel.getParamMap(dt.get("modifyUserMob2")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("user_id", user_id);
		map1.put("token", token);
		DbUtils dbUtils = new DbUtils();
		map1.put("verify_code", dbUtils.queryCdSmscd(dt.get("modifyUserMob2").get("bind_mob")).getTno_smscd());
		payPwdVerify.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map1)), dt.get("modifyUserMob2").get("key")));
		map1.put("sign", payPwdVerify.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String payPwdVerifyUrl = Property.get("llwallet.payPwdVerify.url");
		String rsp1 = HttpRequest.httpPostWithJSON(payPwdVerifyUrl, reqJson1);
		// excel.writeCurrentCell("modifyUserMob2", "actual_return1", rsp1);
		String token1 = JSONObject.parseObject(rsp1).getString("token");

		// 修改用户绑定手机
		ModifyUserMob modifyUserMob = new ModifyUserMob();
		Object reqObj2 = JavaBeanUtil.convertMap(modifyUserMob.getClass(), excel.getParamMap(dt.get("modifyUserMob2")));
		Map<String, String> map2 = JSON.parseObject(JSON.toJSONString(reqObj2), Map.class);
		map2.put("user_id", user_id);
		map2.put("token", token1);
		map2.put("mob_bind", MyConfig.getMobBind(dt.get("modifyUserMob2").get("mob_bind1")));
		modifyUserMob.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map2)), dt.get("modifyUserMob2").get("key")));
		map2.put("sign", modifyUserMob.getSign());
		String reqJson2 = JSON.toJSONString(map2);
		String modifyUserMobUrl = Property.get("llwallet.modifyUserMob.url");
		String rsp2 = HttpRequest.httpPostWithJSON(modifyUserMobUrl, reqJson2);
		// excel.writeCurrentCell("modifyUserMob", "actual_return2", rsp2);
		Reporter.log("修改用户绑定手机接口测试： " + dt.get("modifyUserMob").get("comment"), true);
		System.out.println("===============================================");
		// 修改用户绑定手机返回检查
		assert rsp2.contains(dt.get("modifyUserMob2").get("expect_return"));
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户绑定手机修改接口测试", timeOut = 60000, dataProvider = "provider3")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {
		Thread.sleep(1000);
		// 直接开户
		DirectOpenUser directOpenUser = new DirectOpenUser();
		Object reqObj = JavaBeanUtil.convertMap(directOpenUser.getClass(), excel.getParamMap(dt.get("modifyUserMob")));
		directOpenUser.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("modifyUserMob").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", directOpenUser.getSign());
		String reqJson = JSON.toJSONString(map);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);
		// excel.writeCurrentCell("modifyUserMob", "actual_return", rsp);
		String user_id = JSONObject.parseObject(rsp).getString("user_id");

		// 钱包支付密码验证授权
		PwdAuth pwdAuth = new PwdAuth();
		Object reqObj1 = JavaBeanUtil.convertMap(pwdAuth.getClass(), excel.getParamMap(dt.get("modifyUserMob")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("user_id", user_id);
		map1.put("mob_bind", "");
		map1.put("no_idcard", dt.get("modifyUserMob").get("no_idcard"));
		map1.put("flag_check", dt.get("modifyUserMob").get("flag_check1"));
		pwdAuth.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map1)), dt.get("modifyUserMob").get("key")));
		map1.put("sign", pwdAuth.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");
		String rsp1 = HttpRequest.httpPostWithJSON(pwdAuthUrl, reqJson1);
		// excel.writeCurrentCell("modifyUserMob", "actual_return1", rsp1);
		String token = JSONObject.parseObject(rsp1).getString("token");

		// 修改用户绑定手机
		ModifyUserMob modifyUserMob = new ModifyUserMob();
		Object reqObj2 = JavaBeanUtil.convertMap(modifyUserMob.getClass(), excel.getParamMap(dt.get("modifyUserMob")));
		Map<String, String> map2 = JSON.parseObject(JSON.toJSONString(reqObj2), Map.class);
		map2.put("user_id", user_id);
		map2.put("token", token);
		map2.put("mob_bind", MyConfig.getMobBind(dt.get("modifyUserMob").get("mob_bind2")));
		modifyUserMob.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map2)), dt.get("modifyUserMob").get("key")));
		map2.put("sign", modifyUserMob.getSign());
		String reqJson2 = JSON.toJSONString(map2);
		String modifyUserMobUrl = Property.get("llwallet.modifyUserMob.url");
		String rsp2 = HttpRequest.httpPostWithJSON(modifyUserMobUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//modifyUserMob.txt", "===============================================");

		// excel.writeCurrentCell("modifyUserMob", "actual_return2", rsp2);
		Reporter.log("修改用户绑定手机接口测试： " + dt.get("modifyUserMob").get("comment"), true);
		System.out.println("===============================================");
		// 修改用户绑定手机请求返回检查
		assert rsp2.contains(dt.get("modifyUserMob").get("expect_return"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 31);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 99);
	}

	@DataProvider(name = "provider2")
	public Iterator<Object[]> provider2() throws IOException {
		return new ExcelProvider2(this, 99);
	}

	@DataProvider(name = "provider3")
	public Iterator<Object[]> provider3() throws IOException {
		return new ExcelProvider2(this, 3, 4);
	}

	@AfterClass
	public void afterClass() {

	}
}