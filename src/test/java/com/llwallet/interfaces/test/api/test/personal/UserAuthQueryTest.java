package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.UserAuthQuery;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户认证查询接口测试
 */

public class UserAuthQueryTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户认证查询接口测试", timeOut = 60000, dataProvider = "userAuthQuery")
	public void userAuthQuery(Map<String, String> datadriven) throws Exception {

		UserAuthQuery userAuthQuery = new UserAuthQuery();
		userAuthQuery.setOid_partner(datadriven.get("oid_partner"));
		userAuthQuery.setSign_type(datadriven.get("sign_type"));
		userAuthQuery.setUser_id(datadriven.get("user_id"));
		userAuthQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userAuthQuery)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userAuthQuery);
		String userAuthQueryUrl = Property.get("llwallet.userAuthQuery.url");

		// 用户认证查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userAuthQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 用户认证查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("用户认证查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
   
	@DataProvider(name = "userAuthQuery")
	public Iterator<Object[]> data4userAuthQuery() throws IOException {
		return new ExcelProvider(this, "userAuthQuery");
	}

	@AfterClass
	public void afterClass() {

	}

}
