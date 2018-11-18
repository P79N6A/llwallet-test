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
import com.llwallet.interfaces.bean.personal.ModifyBaseUserInfo;
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
 * 用户职业住址修改接口测试
 */

public class ModifyBaseUserInfoTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户职业住址修改接口测试", timeOut = 60000, dataProvider = "modifyBaseUserInfo")
	public void modifyBaseUserInfo(Map<String, String> datadriven) throws Exception {
		
		Thread.sleep(1000);
		String user_id = ApiUtils.getCurrentDateMillisecondStr();
		String mob_bind = ApiUtils.getMobStr();
		String eml_bind = user_id+"@yintong.com.cn";
		String oid_partner = datadriven.get("oid_partner");

		// 组织开户请求报文
		DirectOpenUser directOpenUser = new DirectOpenUser();
		directOpenUser.setOid_partner(oid_partner);
		directOpenUser.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))){
			directOpenUser.setUser_id(user_id);
		}else{
			directOpenUser.setUser_id(datadriven.get("user_id"));
		}
		if ("auto".equals(datadriven.get("mob_bind"))){
			directOpenUser.setMob_bind(mob_bind);
		}else{
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
		if ("auto".equals(datadriven.get("eml_bind"))){
			directOpenUser.setEml_bind(eml_bind);
		}else{
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
		directOpenUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(directOpenUser)), datadriven.get("key")));
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

		// 组织修改请求报文
		ModifyBaseUserInfo modifyBaseUserInfo = new ModifyBaseUserInfo();
		modifyBaseUserInfo.setOid_partner(datadriven.get("oid_partner"));
		modifyBaseUserInfo.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))){
			modifyBaseUserInfo.setUser_id(user_id);
		}else{
			modifyBaseUserInfo.setUser_id(datadriven.get("user_id"));
		}
		modifyBaseUserInfo.setOid_job(datadriven.get("oid_job1"));
		modifyBaseUserInfo.setAddr_pro(datadriven.get("addr_pro1"));
		modifyBaseUserInfo.setAddr_city(datadriven.get("addr_city1"));
		modifyBaseUserInfo.setAddr_dist(datadriven.get("addr_dist1"));		
		modifyBaseUserInfo.setAddr_conn(datadriven.get("addr_conn1"));
		modifyBaseUserInfo.setRisk_item(datadriven.get("risk_item"));
		modifyBaseUserInfo.setExp_idcard(datadriven.get("exp_idcard1"));
		modifyBaseUserInfo.setType_expidcard(datadriven.get("type_expidcard1"));		
		modifyBaseUserInfo.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyBaseUserInfo)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(modifyBaseUserInfo);
		String modifyBaseUserInfoUrl = Property.get("llwallet.modifyBaseUserInfo.url");

		// 用户职业住址修改请求提交
		hf.nextRequest();
		hf.setUrl(modifyBaseUserInfoUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 用户职业住址修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		if (hf.getResponseBody().contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			Thread.sleep(1000);
			// 个人用户职业住址修改后数据检查
			DbUtils dbUtils = new DbUtils();
	        JSONObject jsobj = JSONObject.parseObject(hf.getResponseBody());
	        String oid_userno_new = jsobj.getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert datadriven.get("result_acctstat").equals(stat_acct);
		}
		
		Reporter.log("用户职业住址修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@Test(description = "用户职业住址修改接口测试", timeOut = 60000, dataProvider = "modifyBaseUserInfo1")
	public void modifyBaseUserInfo1(Map<String, String> datadriven) throws Exception {
		
		// 组织修改请求报文
		ModifyBaseUserInfo modifyBaseUserInfo = new ModifyBaseUserInfo();
		modifyBaseUserInfo.setOid_partner(datadriven.get("oid_partner"));
		modifyBaseUserInfo.setSign_type(datadriven.get("sign_type"));
		modifyBaseUserInfo.setUser_id(datadriven.get("user_id"));
		modifyBaseUserInfo.setOid_job(datadriven.get("oid_job1"));
		modifyBaseUserInfo.setAddr_pro(datadriven.get("addr_pro1"));
		modifyBaseUserInfo.setAddr_city(datadriven.get("addr_city1"));
		modifyBaseUserInfo.setAddr_dist(datadriven.get("addr_dist1"));		
		modifyBaseUserInfo.setAddr_conn(datadriven.get("addr_conn1"));
		modifyBaseUserInfo.setRisk_item(datadriven.get("risk_item"));
		modifyBaseUserInfo.setExp_idcard(datadriven.get("exp_idcard1"));
		modifyBaseUserInfo.setType_expidcard(datadriven.get("type_expidcard1"));		
		modifyBaseUserInfo.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyBaseUserInfo)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(modifyBaseUserInfo);
		String modifyBaseUserInfoUrl = Property.get("llwallet.modifyBaseUserInfo.url");

		// 用户职业住址修改请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(modifyBaseUserInfoUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 用户职业住址修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		if (hf.getResponseBody().contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			Thread.sleep(1000);
			// 个人用户职业住址修改后数据检查
			DbUtils dbUtils = new DbUtils();
	        JSONObject jsobj = JSONObject.parseObject(hf.getResponseBody());
	        String oid_userno_new = jsobj.getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert datadriven.get("result_acctstat").equals(stat_acct);
		}
		
		Reporter.log("用户职业住址修改接口测试： " + datadriven.get("comment"), true);
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
		String user_id = JSONObject.parseObject(rsp).getString("user_id");

		// 修改用户职业住址信息
		ModifyBaseUserInfo modifyBaseUserInfo = new ModifyBaseUserInfo();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("baseline").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "modifybaseuserinfo.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(modifyBaseUserInfo, params);
		if ("get".equals(dt.get("baseline").get("modifybaseuserinfo.user_id"))) {
			modifyBaseUserInfo.setUser_id(user_id);
		}
		modifyBaseUserInfo.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(modifyBaseUserInfo)),
				dt.get("baseline").get("modifybaseuserinfo.key")));
		String reqJson1 = JSON.toJSONString(modifyBaseUserInfo);
		String modifyBaseUserInfoUrl = Property.get("llwallet.modifyBaseUserInfo.url");
		String rsp1 = HttpRequest.httpPostWithJSON(modifyBaseUserInfoUrl, reqJson1);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//modifyBaseUserInfo.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//modifyBaseUserInfo.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//modifyBaseUserInfo.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//modifyBaseUserInfo.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//modifyBaseUserInfo.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//modifyBaseUserInfo.txt",
				"===============================================");

		// 修改成功数据检查
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			Thread.sleep(500);
			String oid_userno_new = JSONObject.parseObject(rsp1).getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert dt.get("baseline").get("expect_acctstat").equals(stat_acct);
		}

		Reporter.log("个人用户开户接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 用户基本信息修改请求返回检查
		assert rsp1.contains(dt.get("baseline").get("expect_return1"));

	}
	
	@DataProvider(name = "modifyBaseUserInfo")
	public Iterator<Object[]> data4modifyBaseUserInfo() throws IOException {
		 return new ExcelProvider(this, "modifyBaseUserInfo",111);
	}

	@DataProvider(name = "modifyBaseUserInfo1")
	public Iterator<Object[]> data4modifyBaseUserInfo1() throws IOException {
		 return new ExcelProvider(this, "modifyBaseUserInfo1",111);
	}
	
	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3,4);
	}
	
	@AfterClass
	public void afterClass() {

	}
}