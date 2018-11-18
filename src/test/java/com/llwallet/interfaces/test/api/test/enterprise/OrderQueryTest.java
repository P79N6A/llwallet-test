package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.OrderQuery;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 订单查询接口测试
 */

public class OrderQueryTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户注销接口测试", timeOut = 60000, dataProvider = "provider")
	public void test1(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		OrderQuery orderQuery = new OrderQuery();
		// 订单查询
		Object reqObj = JavaBeanUtil.convertMap(orderQuery.getClass(), excel.getParamMap(dt.get("orderQuery")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		orderQuery.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("orderQuery").get("key")));
		map.put("sign", orderQuery.getSign());
		String reqJson = JSON.toJSONString(map);
		String orderQueryUrl = Property.get("llwallet.orderQuery.url");
		// 订单查询请求提交
		String rsp = HttpRequest.httpPostWithJSON(orderQueryUrl, reqJson);
		// excel.writeCurrentCell("orderQuery", "actual_return", rsp);
		Reporter.log("订单查询接口测试： " + dt.get("orderQuery").get("comment"), true);
		System.out.println("===============================================");
		// 订单查询请求返回检查
		assert rsp.contains(dt.get("orderQuery").get("expect_return"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,7);
	}

	@AfterClass
	public void afterClass() {

	}

}