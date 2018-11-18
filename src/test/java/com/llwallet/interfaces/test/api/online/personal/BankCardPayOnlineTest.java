package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.BankCardPay;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 担保交易支付验证接口测试
 */

public class BankCardPayOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保交易支付验证接口测试", timeOut = 60000, dataProvider = "bankCardPay1")
	public void bankCardPay1(Map<String, String> datadriven) throws Exception {

		BankCardPay bankCardPay = new BankCardPay();
		bankCardPay.setOid_partner(datadriven.get("oid_partner1"));		
		bankCardPay.setToken(datadriven.get("token1"));
		bankCardPay.setSign_type(datadriven.get("sign_type1"));
		bankCardPay.setNo_order(datadriven.get("no_order1"));
		bankCardPay.setMoney_order(datadriven.get("money_order1"));
		bankCardPay.setCol_oidpartner(datadriven.get("col_oidpartner1"));
		bankCardPay.setVerify_code(datadriven.get("verify_code1"));
		bankCardPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardPay)),datadriven.get("key1")));
		String reqJson1 = JSON.toJSONString(bankCardPay);
		String bankCardPayUrl = Property.get("llwallet.bankCardPay.url.online");
		
		// 担保交易支付验证请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(bankCardPayUrl);
		hf.addRequestBody(reqJson1);
		hf.Post();

		// 担保交易支付验证请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("担保交易支付验证接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "bankCardPay1")
	public Iterator<Object[]> data4bankCardPay1() throws IOException {
		return new ExcelProvider(this, "bankCardPay1",4);
	}
	
	@AfterClass
	public void afterClass() {

	}

}