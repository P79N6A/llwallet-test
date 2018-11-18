package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.BalancePayVerify;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 余额支付短信验证接口测试
 */

public class BalancePayVerifyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "余额支付短信验证接口测试", timeOut = 60000, dataProvider = "balancePayVerify")
	public void balancePayVerify(Map<String, String> datadriven) throws Exception {

		BalancePayVerify balancePayVerify = new BalancePayVerify();
		balancePayVerify.setOid_partner(datadriven.get("oid_partner"));
		balancePayVerify.setSign_type(datadriven.get("sign_type"));
		balancePayVerify.setUser_id(datadriven.get("user_id"));
		balancePayVerify.setNo_order(datadriven.get("no_order"));
		balancePayVerify.setToken(datadriven.get("token"));
		balancePayVerify.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		balancePayVerify.setVerify_code(datadriven.get("verify_code"));
		balancePayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePayVerify)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(balancePayVerify);
		String balancePayVerifyUrl = Property.get("llwallet.balancePayVerify.url");

		// 余额支付短信验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + balancePayVerifyUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 余额支付短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		// assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("余额支付短信验证接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "balancePayVerify")
	public Iterator<Object[]> data4balancePayVerify() throws IOException {
		return new ExcelProvider(this, "balancePayVerify",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
