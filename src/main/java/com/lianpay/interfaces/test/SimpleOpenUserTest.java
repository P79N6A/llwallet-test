package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.SimpleOpenUser;
import com.lianpay.interfaces.dao.DbUtils;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包简易开户接口测试
 */

public class SimpleOpenUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包简易开户接口测试", timeOut = 60000, dataProvider = "simpleOpenUser")
	public void simpleOpenUser(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		//删除用户测试数据
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

		// 钱包简易开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(simpleOpenUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包简易开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包简易开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
   
	@Test(description = "钱包简易开户接口测试", timeOut = 60000, dataProvider = "simpleOpenUser1")
	public void simpleOpenUser1(Map<String, String> datadriven) throws Exception {

		String user_id = datadriven.get("user_id");
		String oid_partner = datadriven.get("oid_partner");
		//删除用户测试数据
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

		// 钱包简易开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(simpleOpenUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包简易开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

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
		
		SimpleOpenUser simpleOpenUser1 = new SimpleOpenUser();
		simpleOpenUser1.setOid_partner(oid_partner1);
		simpleOpenUser1.setSign_type(datadriven.get("sign_type"));
		simpleOpenUser1.setUser_id(user_id1);
		simpleOpenUser1.setMob_bind(datadriven.get("mob_bind1"));
		simpleOpenUser1.setEml_bind(datadriven.get("eml_bind"));
		simpleOpenUser1.setName_user(datadriven.get("name_user"));		
		simpleOpenUser1.setNo_idcard(datadriven.get("no_idcard"));
		simpleOpenUser1.setRisk_item(datadriven.get("risk_item"));
		simpleOpenUser1.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(simpleOpenUser1)),datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(simpleOpenUser1);
		// 钱包简易开户请求提交
		hf.nextRequest();		
		hf.setUrl(simpleOpenUserUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 钱包简易开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));
		
		Reporter.log("钱包简易开户接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
	
	@DataProvider(name = "simpleOpenUser")
	public Iterator<Object[]> data4simpleOpenUser() throws IOException {
		return new ExcelProvider(this, "simpleOpenUser");
	}
	
	@DataProvider(name = "simpleOpenUser1")
	public Iterator<Object[]> data4simpleOpenUser1() throws IOException {
		return new ExcelProvider(this, "simpleOpenUser1");
	}

	@AfterClass
	public void afterClass() {

	}

}
