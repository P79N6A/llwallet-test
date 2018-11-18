package com.llwallet.interfaces.test.api.test.combination;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.combination.CombinationPayVerify;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 组合支付验证接口测试
 */

public class CombinationPayVerifyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "组合支付验证接口测试", timeOut = 60000, dataProvider = "combinationPayVerify")
//	@Test(description = "组合支付验证接口测试", threadPoolSize = 2, invocationCount = 2, timeOut = 60000, dataProvider = "combinationPayVerify")
	public void combinationPayVerify(Map<String, String> datadriven) throws Exception {
		
		CombinationPayVerify combinationPayVerify = new CombinationPayVerify();
		combinationPayVerify.setOid_partner(datadriven.get("oid_partner"));
		combinationPayVerify.setSign_type(datadriven.get("sign_type"));
		combinationPayVerify.setUser_id(datadriven.get("user_id"));
		combinationPayVerify.setApi_version(datadriven.get("api_version"));
		combinationPayVerify.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
		combinationPayVerify.setToken(datadriven.get("token"));
		combinationPayVerify.setNo_order(datadriven.get("no_order"));
		combinationPayVerify.setVerify_code(datadriven.get("verify_code"));
		combinationPayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPayVerify)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationPayVerify);
		String combinationPayVerifyUrl = Property.get("llwallet.combinationPayVerify.url");

		// 组合支付验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(combinationPayVerifyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 组合支付验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		
		Reporter.log("组合支付验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "combinationPayVerify")
	public Iterator<Object[]> data4combinationPayVerify() throws IOException {
		return new ExcelProvider(this, "combinationPayVerify",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
