package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.AccountCancellation;
import com.llwallet.interfaces.bean.custom.DirectOpenUser;
import com.tools.dataprovider.ExcelProvider2;
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
 * 用户注销接口测试
 */

public class AccountCancellationTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户注销接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 直接开户
		DirectOpenUser directOpenUser = new DirectOpenUser();
		AccountCancellation accountCancellation = new AccountCancellation();
		Object reqObj = JavaBeanUtil.convertMap(directOpenUser.getClass(), excel.getParamMap(dt.get("directOpenUser")));
		directOpenUser.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("directOpenUser").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", directOpenUser.getSign());
		String reqJson = JSON.toJSONString(map);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);
		// excel.writeCurrentCell("directOpenUser", "actual_return", rsp);
		Reporter.log("用户直接开户接口测试： " + dt.get("directOpenUser").get("comment"), true);
		assert rsp.contains(dt.get("directOpenUser").get("expect_return"));

		// 用户注销
		Object reqObj1 = JavaBeanUtil.convertMap(accountCancellation.getClass(),
				excel.getParamMap(dt.get("accountCancellation")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		if ("get".equals(dt.get("accountCancellation").get("user_id"))) {
			accountCancellation.setUser_id(JSONObject.parseObject(rsp).getString("user_id"));
			map1.put("user_id", accountCancellation.getUser_id());
		}
		accountCancellation.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map1)),
				dt.get("accountCancellation").get("key")));
		map1.put("sign", accountCancellation.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String accountCancellationUrl = Property.get("llwallet.accountCancellation.url");
		String rsp1 = HttpRequest.httpPostWithJSON(accountCancellationUrl, reqJson1);
		// excel.writeCurrentCell("accountCancellation", "actual_return", rsp1);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt",
				"===============================================");

		Reporter.log("用户注销接口测试： " + dt.get("accountCancellation").get("comment"), true);
		System.out.println("===============================================");

		// 用户注销请求返回检查
		assert rsp1.contains(dt.get("accountCancellation").get("expect_return"));
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
					JSONObject.parseObject(rsp1).getString("sign"));
		}
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户注销接口测试", timeOut = 60000, dataProvider = "provider1")
	public void test1(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		AccountCancellation accountCancellation = new AccountCancellation();
		Object reqObj1 = JavaBeanUtil.convertMap(accountCancellation.getClass(),
				excel.getParamMap(dt.get("accountCancellation1")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		accountCancellation.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map1)),
				dt.get("accountCancellation1").get("key")));
		map1.put("sign", accountCancellation.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String accountCancellationUrl = Property.get("llwallet.accountCancellation.url");
		String rsp1 = HttpRequest.httpPostWithJSON(accountCancellationUrl, reqJson1);
		// excel.writeCurrentCell("accountCancellation1", "actual_return",
		// rsp1);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt",
				"===============================================");

		Reporter.log("用户注销接口测试： " + dt.get("accountCancellation1").get("comment"), true);
		System.out.println("===============================================");

		// 用户注销请求返回检查
		assert rsp1.contains(dt.get("accountCancellation1").get("expect_return"));
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
					JSONObject.parseObject(rsp1).getString("sign"));
		}
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户注销接口测试", timeOut = 60000, dataProvider = "provider2")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		DirectOpenUser directOpenUser = new DirectOpenUser();
		AccountCancellation accountCancellation = new AccountCancellation();

		// 直接开户
		Object reqObj = JavaBeanUtil.convertMap(directOpenUser.getClass(), excel.getParamMap(dt.get("directOpenUser")));
		directOpenUser.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("directOpenUser").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", directOpenUser.getSign());
		String reqJson = JSON.toJSONString(map);
		String directOpenUserUrl = Property.get("llwallet.directOpenUser.url");
		String rsp = HttpRequest.httpPostWithJSON(directOpenUserUrl, reqJson);
		// excel.writeCurrentCell("directOpenUser", "actual_return", rsp);
		Reporter.log("用户直接开户接口测试： " + dt.get("directOpenUser").get("comment"), true);
		assert rsp.contains(dt.get("directOpenUser").get("expect_return"));

		// 用户注销
		Object reqObj1 = JavaBeanUtil.convertMap(accountCancellation.getClass(),
				excel.getParamMap(dt.get("accountCancellation")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		if ("get".equals(dt.get("accountCancellation").get("user_id"))) {
			accountCancellation.setUser_id(JSONObject.parseObject(rsp).getString("user_id"));
			map1.put("user_id", accountCancellation.getUser_id());
		}
		accountCancellation.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map1)),
				dt.get("accountCancellation").get("key")));
		map1.put("sign", accountCancellation.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String accountCancellationUrl = Property.get("llwallet.accountCancellation.url");
		String rsp1 = HttpRequest.httpPostWithJSON(accountCancellationUrl, reqJson1);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//accountCancellation.txt",
				"===============================================");

		// excel.writeCurrentCell("accountCancellation", "actual_return", rsp1);
		Reporter.log("用户注销接口测试： " + dt.get("accountCancellation").get("comment"), true);
		System.out.println("===============================================");

		// 用户注销请求返回检查
		assert rsp1.contains(dt.get("accountCancellation").get("expect_return"));
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)),
					JSONObject.parseObject(rsp1).getString("sign"));
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 3,9);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 99);
	}

	@DataProvider(name = "provider2")
	public Iterator<Object[]> provider2() throws IOException {
		return new ExcelProvider2(this, 399);
	}

	@AfterClass
	public void afterClass() {

	}

}
