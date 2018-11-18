package com.llwallet.interfaces.test.api.online.combination;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.combination.CombinationPayVerify;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 组合支付验证接口测试
 */

public class CombinationPayVerifyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "组合支付验证接口测试", timeOut = 60000, dataProvider = "combinationPayVerify")
	public void combinationPayVerify(Map<String, String> datadriven) throws Exception {
		
		String timestamp = ApiUtils.getCurrentDateSecondStr();
		
		CombinationPayVerify combinationPayVerify = new CombinationPayVerify();
		combinationPayVerify.setOid_partner(datadriven.get("oid_partner"));
		combinationPayVerify.setSign_type(datadriven.get("sign_type"));
		combinationPayVerify.setUser_id(datadriven.get("user_id"));
		combinationPayVerify.setApi_version(datadriven.get("api_version"));
		
		if ("auto".equals(datadriven.get("timestamp"))) {
			combinationPayVerify.setTimestamp(timestamp);
		}else {
			combinationPayVerify.setTimestamp(datadriven.get("timestamp"));			
		}		
		combinationPayVerify.setToken(datadriven.get("token"));
		combinationPayVerify.setNo_order(datadriven.get("no_order"));
		combinationPayVerify.setVerify_code(datadriven.get("verify_code"));
		combinationPayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationPayVerify)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationPayVerify);
		String combinationPayVerifyUrl = Property.get("llwallet.combinationPayVerify.url.online");

		// 组合支付验证请求提交
		String rsp = HttpRequest.httpPostWithJSON(combinationPayVerifyUrl, reqJson);
		// 组合支付验证请求返回检查
		assert rsp.contains(datadriven.get("result_code"));
		
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
