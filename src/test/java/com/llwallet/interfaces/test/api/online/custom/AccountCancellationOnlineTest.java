package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.AccountCancellation;
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
 * 用户注销接口测试
 */

public class AccountCancellationOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "用户注销接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		AccountCancellation accountCancellation = new AccountCancellation();

		// 用户注销
		Object reqObj = JavaBeanUtil.convertMap(accountCancellation.getClass(),
				excel.getParamMap(dt.get("accountCancellation")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		accountCancellation.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
				dt.get("accountCancellation").get("key")));
		map.put("sign", accountCancellation.getSign());
		String reqJson = JSON.toJSONString(map);
		String accountCancellationUrl = Property.get("llwallet.accountCancellation.url.online");
		// 用户注销请求提交
		String rsp = HttpRequest.httpPostWithJSON(accountCancellationUrl, reqJson);
		// excel.writeCurrentCell("accountCancellation", "actual_return", rsp);
		Reporter.log("用户注销接口测试： " + dt.get("accountCancellation").get("comment"), true);
		System.out.println("===============================================");
		// 用户注销请求返回检查
		assert rsp.contains(dt.get("accountCancellation").get("expect_return"));
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,3);
	}

	@AfterClass
	public void afterClass() {

	}

}
