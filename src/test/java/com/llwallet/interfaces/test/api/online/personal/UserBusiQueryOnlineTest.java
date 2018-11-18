package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.UserBusiQuery;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户业务查询接口测试
 */

public class UserBusiQueryOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户业务查询接口测试", timeOut = 600000, dataProvider = "userBusiQuery")
	public void userBusiQuery(Map<String, String> datadriven) throws Exception {

		UserBusiQuery userBusiQuery = new UserBusiQuery();
		userBusiQuery.setOid_partner(datadriven.get("oid_partner"));
		userBusiQuery.setSign_type(datadriven.get("sign_type"));
		userBusiQuery.setUser_id(datadriven.get("user_id"));
		userBusiQuery.setBusi_user(datadriven.get("busi_user"));
		userBusiQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userBusiQuery)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userBusiQuery);
		String userBusiQueryUrl = Property.get("llwallet.userBusiQuery.url.online");

		// 用户业务查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userBusiQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 用户业务查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		
		Reporter.log("用户业务查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "userBusiQuery")
	public Iterator<Object[]> data4userBusiQuery() throws IOException {
		return new ExcelProvider(this, "userBusiQuery");
	}
	
	@AfterClass
	public void afterClass() {

	}

}
