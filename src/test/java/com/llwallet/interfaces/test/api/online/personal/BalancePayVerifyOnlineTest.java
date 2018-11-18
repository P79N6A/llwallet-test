package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BalancePayVerify;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 余额支付短信验证接口测试
 */

public class BalancePayVerifyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "余额支付短信验证接口测试", timeOut = 60000, dataProvider = "balancePayVerify1")
	public void balancePayVerify1(Map<String, String> datadriven) throws Exception {

		BalancePayVerify balancePayVerify = new BalancePayVerify();
		balancePayVerify.setOid_partner(datadriven.get("oid_partner1"));
		balancePayVerify.setSign_type(datadriven.get("sign_type1"));
		balancePayVerify.setUser_id(datadriven.get("user_id1"));
		balancePayVerify.setNo_order(datadriven.get("no_order1"));
		balancePayVerify.setToken(datadriven.get("token1"));
		balancePayVerify.setMoney_order(datadriven.get("money_order1"));
		balancePayVerify.setVerify_code(datadriven.get("verify_code1"));
		balancePayVerify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePayVerify)),datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(balancePayVerify);
		String balancePayVerifyUrl = Property.get("llwallet.balancePayVerify.url.online");

		// 余额支付短信验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(balancePayVerifyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson1);
		hf.Post();
		// 余额支付短信验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("余额支付短信验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	
	@DataProvider(name = "balancePayVerify1")
	public Iterator<Object[]> data4balancePayVerify1() throws IOException {
		return new ExcelProvider(this, "balancePayVerify1",1);
	}
	
	@AfterClass
	public void afterClass() {

	}

}