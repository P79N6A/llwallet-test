package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BankCardAuthVerfy;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 银行卡绑卡验证接口测试
 */

public class BankCardAuthVerfyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡绑卡验证接口测试", timeOut = 60000, dataProvider = "bankCardAuthVerfy")
	public void bankCardAuthVerfy(Map<String, String> datadriven) throws Exception {

		BankCardAuthVerfy bankCardAuthVerfy = new BankCardAuthVerfy();
		bankCardAuthVerfy.setOid_partner(datadriven.get("oid_partner"));
		bankCardAuthVerfy.setSign_type(datadriven.get("sign_type"));
		bankCardAuthVerfy.setUser_id(datadriven.get("user_id"));
		bankCardAuthVerfy.setVerify_code(datadriven.get("verify_code"));
		bankCardAuthVerfy.setToken(datadriven.get("token"));
		bankCardAuthVerfy.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardAuthVerfy)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardAuthVerfy);
		String bankCardAuthVerfyUrl = Property.get("llwallet.bankCardAuthVerfy.url.online");

		// 银行卡绑卡验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
        hf.nextRequest();
		hf.setUrl(bankCardAuthVerfyUrl);
		hf.addRequestBody(reqJson);
		hf.Post();
		// 银行卡绑卡验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		
		Reporter.log("银行卡绑卡验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "bankCardAuthVerfy")
	public Iterator<Object[]> data4bankCardAuthVerfy() throws IOException {
		return new ExcelProvider(this, "bankCardAuthVerfy",6);
	}

	@AfterClass
	public void afterClass() {

	}

}
