package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.AuthUser;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包实名认证接口测试
 */

public class AuthUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包实名认证接口测试", timeOut = 60000, dataProvider = "authUser")
	public void authUser(Map<String, String> datadriven) throws Exception {

		AuthUser authUser = new AuthUser();
		authUser.setOid_partner(datadriven.get("oid_partner"));
		authUser.setSign_type(datadriven.get("sign_type"));
		authUser.setUser_id(datadriven.get("user_id"));
		authUser.setCheck_auth(datadriven.get("check_auth"));
		authUser.setRisk_item(datadriven.get("risk_item"));
		authUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(authUser)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(authUser);
		String authUserUrl = Property.get("llwallet.authUser.url");

		// 钱包实名认证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(authUserUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包实名认证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包实名认证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
   
	@DataProvider(name = "authUser")
	public Iterator<Object[]> data4authUser() throws IOException {
		return new ExcelProvider(this, "authUser",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
