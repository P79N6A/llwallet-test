package com.account.interfaces.test.api.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.account.interfaces.bean.BalanceQuery;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包刷单金额查询接口测试
 */

public class BalanceQueryTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包刷单金额查询接口测试", timeOut = 60000, dataProvider = "balanceQuery")
	public void balanceQuery(Map<String, String> datadriven) throws Exception {

		BalanceQuery balanceQuery = new BalanceQuery();
		balanceQuery.setOid_partner(datadriven.get("oid_partner"));
		balanceQuery.setSign_type(datadriven.get("sign_type"));
		balanceQuery.setUser_id(datadriven.get("user_id"));
		balanceQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balanceQuery)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(balanceQuery);
		String balanceQueryUrl = Property.get("llwallet.balanceQuery.url");

		// 钱包刷单金额查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(balanceQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包刷单金额查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包刷单金额查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "balanceQuery")
	public Iterator<Object[]> data4balanceQuery() throws IOException {
		return new ExcelProvider(this, "balanceQuery");
	}

	@AfterClass
	public void afterClass() {

	}
}