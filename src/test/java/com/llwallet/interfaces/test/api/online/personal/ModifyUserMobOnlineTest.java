package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.ModifyUserMob;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户绑定手机修改接口测试
 */

public class ModifyUserMobOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户绑定手机修改接口测试", timeOut = 60000, dataProvider = "modifyUserMob")
	public void modifyUserMob(Map<String, String> datadriven) throws Exception {

		// 组织修改用户绑定手机请求报文
		ModifyUserMob modifyUserMob = new ModifyUserMob();
		modifyUserMob.setOid_partner(datadriven.get("oid_partner"));
		modifyUserMob.setSign_type(datadriven.get("sign_type"));
		modifyUserMob.setUser_id(datadriven.get("user_id"));
		modifyUserMob.setToken(datadriven.get("token"));
		modifyUserMob.setMob_bind(datadriven.get("mob_bind"));
		modifyUserMob.setRisk_item(datadriven.get("risk_item"));
		modifyUserMob.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUserMob)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(modifyUserMob);
		String modifyUserMobUrl = Property.get("llwallet.modifyUserMob.url.online");

		// 用户绑定手机修改请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(modifyUserMobUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 用户绑定手机修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));
		
		Reporter.log("用户绑定手机修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "modifyUserMob")
	public Iterator<Object[]> data4modifyUserMob() throws IOException {
		 return new ExcelProvider(this, "modifyUserMob",1);
	}
	
	@AfterClass
	public void afterClass() {

	}
}