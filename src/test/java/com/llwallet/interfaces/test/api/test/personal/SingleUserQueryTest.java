package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.SingleUserQuery;
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
 * 钱包用户查询接口测试
 */

public class SingleUserQueryTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "钱包用户查询接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		SingleUserQuery singleUserQuery = new SingleUserQuery();
		Object reqObj = JavaBeanUtil.convertMap(singleUserQuery.getClass(),
				excel.getParamMap(dt.get("singleUserQuery")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		singleUserQuery.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("singleUserQuery").get("key")));
		map.put("sign", singleUserQuery.getSign());
		String reqJson = JSON.toJSONString(map);
		String singleUserQueryUrl = Property.get("llwallet.singleUserQuery.url");
		// 钱包用户查询请求提交
		String rsp = HttpRequest.httpPostWithJSON(singleUserQueryUrl, reqJson);
		// excel.writeCurrentCell("singleUserQueryUrl", "actual_return", rsp);
		Reporter.log("钱包用户查询接口测试： " + dt.get("singleUserQuery").get("comment"), true);
		System.out.println("===============================================");
		// 钱包用户查询请求返回检查
		assert rsp.contains(dt.get("singleUserQuery").get("expect_return"));
		assert rsp.contains(dt.get("singleUserQuery").get("expect_return1"));
		assert rsp.contains(dt.get("singleUserQuery").get("expect_return2"));
		assert rsp.contains(dt.get("singleUserQuery").get("expect_return3"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,3);
	}

	@AfterClass
	public void afterClass() {

	}

}