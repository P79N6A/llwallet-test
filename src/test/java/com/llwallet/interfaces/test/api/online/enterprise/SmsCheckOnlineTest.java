package com.llwallet.interfaces.test.api.online.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.SmsCheck;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包短信验证接口测试
 */

public class SmsCheckOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包短信验证接口测试", timeOut = 60000, dataProvider = "smsCheck")
	public void smsCheck(Map<String, String> datadriven) throws Exception {

		SmsCheck smsCheck = new SmsCheck();
		smsCheck.setOid_partner(datadriven.get("oid_partner"));
		smsCheck.setSign_type(datadriven.get("sign_type"));
		smsCheck.setUser_id(datadriven.get("user_id"));
		smsCheck.setToken(datadriven.get("token"));
		smsCheck.setVerify_code(datadriven.get("verify_code"));
		smsCheck.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(smsCheck);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url.online");

		// 钱包短信验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + smsCheckUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包短信验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "smsCheck")
	public Iterator<Object[]> data4smsCheck() throws IOException {
		return new ExcelProvider(this, "smsCheck",3);
	}

	@AfterClass
	public void afterClass() {

	}

}
