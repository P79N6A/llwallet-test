package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.SimpleOpenUser;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 个人用户简易开户接口测试
 */

public class SimpleOpenUserTest {

	@BeforeClass@Deprecated
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户简易开户接口测试", timeOut = 60000, dataProvider = "simpleOpenUser")
	@Deprecated
	public void simpleOpenUser(Map<String, String> datadriven) throws Exception {

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
		SimpleOpenUser simpleOpenUser = new SimpleOpenUser();
		simpleOpenUser.setOid_partner(oid_partner);
		simpleOpenUser.setSign_type(datadriven.get("sign_type"));
		simpleOpenUser.setUser_id(user_id);
		simpleOpenUser.setMob_bind(datadriven.get("mob_bind"));
		simpleOpenUser.setName_user(datadriven.get("name_user"));
		simpleOpenUser.setNo_idcard(datadriven.get("no_idcard"));
		simpleOpenUser.setEml_bind(datadriven.get("eml_bind"));
		simpleOpenUser.setRisk_item(datadriven.get("risk_item"));
		simpleOpenUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(simpleOpenUser)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(simpleOpenUser);
		String simpleOpenUserUrl = Property.get("llwallet.simpleOpenUser.url");

		// 个人用户简易开户请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(simpleOpenUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 个人用户简易开户请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("个人用户简易开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "simpleOpenUser")@Deprecated
	public Iterator<Object[]> data4simpleOpenUser() throws IOException {
		 return new ExcelProvider(this, "simpleOpenUser");
	}

	@AfterClass@Deprecated
	public void afterClass() {

	}

}