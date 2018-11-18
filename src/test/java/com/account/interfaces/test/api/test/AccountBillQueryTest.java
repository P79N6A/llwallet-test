package com.account.interfaces.test.api.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.account.interfaces.bean.AccountBillQuery;
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
 * 钱包记账单查询接口测试
 */

public class AccountBillQueryTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包记账单查询接口测试", timeOut = 60000, dataProvider = "accountBillQuery")
	public void accountBillQuery(Map<String, String> datadriven) throws Exception {

		AccountBillQuery accountBillQuery = new AccountBillQuery();
		accountBillQuery.setOid_partner(datadriven.get("oid_partner"));
		accountBillQuery.setSign_type(datadriven.get("sign_type"));
		accountBillQuery.setNo_order(datadriven.get("no_order"));
		accountBillQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(accountBillQuery)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(accountBillQuery);
		String accountBillQueryUrl = Property.get("llwallet.accountBillQuery.url");

		// 钱包记账单查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(accountBillQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包记账单查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包记账单查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "accountBillQuery")
	public Iterator<Object[]> data4accountBillQuery() throws IOException {
		return new ExcelProvider(this, "accountBillQuery");
	}

	@AfterClass
	public void afterClass() {

	}
}