package com.spay.interfaces.test.api.online;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.spay.interfaces.bean.OrderQuery;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 商户支付结果查询接口测试
 */

public class OrderQueryTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "商户支付结果查询接口测试", timeOut = 60000, dataProvider = "orderQuery")
	public void orderQuery(Map<String, String> datadriven) throws Exception {

		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setOid_partner(datadriven.get("oid_partner"));
		orderQuery.setSign_type(datadriven.get("sign_type"));
		orderQuery.setNo_order(datadriven.get("no_order"));
		orderQuery.setDt_order(datadriven.get("dt_order"));
		orderQuery.setOid_paybill(datadriven.get("oid_paybill"));
		orderQuery.setQuery_version(datadriven.get("query_version"));
		orderQuery.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(orderQuery)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(orderQuery);
		String orderQueryUrl = Property.get("spay.orderQuery.url");

		// 商户支付结果查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(orderQueryUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 商户支付结果查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("商户支付结果查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "orderQuery")
	public Iterator<Object[]> data4orderQuery() throws IOException {
		return new ExcelProvider(this, "orderQuery");
	}

	@AfterClass
	public void afterClass() {

	}
}