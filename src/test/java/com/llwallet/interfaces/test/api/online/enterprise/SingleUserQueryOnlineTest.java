package com.llwallet.interfaces.test.api.online.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.SingleUserQuery;
import com.tools.dataprovider.ExcelProvider;
//import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包用户查询接口测试
 */

public class SingleUserQueryOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包用户查询接口测试", timeOut = 600000, dataProvider = "singleUserQuery")
	public void singleUserQuery(Map<String, String> datadriven) throws Exception {

		SingleUserQuery singleUserQuery = new SingleUserQuery();
		singleUserQuery.setOid_partner(datadriven.get("oid_partner"));
		singleUserQuery.setSign_type(datadriven.get("sign_type"));
		singleUserQuery.setUser_id(datadriven.get("user_id"));
		singleUserQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(singleUserQuery)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(singleUserQuery);
		String singleUserQueryUrl = Property.get("llwallet.singleUserQuery.url.online");

		// 钱包用户查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(singleUserQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包用户查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包用户查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
   
	@DataProvider(name = "singleUserQuery")
	public Iterator<Object[]> data4singleUserQuery() throws IOException {
		return new ExcelProvider(this, "singleUserQuery",2);
	}

	@AfterClass
	public void afterClass() {

	}

}
