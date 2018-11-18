package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.PayPwdVerify;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 银行卡授权验证接口测试
 */

public class PayPwdVerifyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡授权验证接口测试", timeOut = 60000, dataProvider = "payPwdVerify")
	public void payPwdVerify(Map<String, String> datadriven) throws Exception {

		PayPwdVerify payPwdVerify = new PayPwdVerify();
		payPwdVerify.setOid_partner(datadriven.get("oid_partner"));
		payPwdVerify.setSign_type(datadriven.get("sign_type"));
		payPwdVerify.setUser_id(datadriven.get("user_id"));
		payPwdVerify.setToken(datadriven.get("token"));
		payPwdVerify.setVerify_code(datadriven.get("verify_code"));
		payPwdVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(payPwdVerify)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(payPwdVerify);
		String payPwdVerifyUrl = Property.get("llwallet.payPwdVerify.url.online");

		// 银行卡授权验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.setUrl(payPwdVerifyUrl);
		hf.Post();
		// 银行卡授权验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("银行卡授权验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "payPwdVerify")
	public Iterator<Object[]> data4payPwdVerify() throws IOException {
		return new ExcelProvider(this, "payPwdVerify",3);
	}

	@AfterClass
	public void afterClass() {

	}

}
