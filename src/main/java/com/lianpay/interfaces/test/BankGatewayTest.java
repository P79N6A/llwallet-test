package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.BankGateway;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * WEB网银支付接口测试
 */

public class BankGatewayTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "WEB网银支付接口测试", timeOut = 60000, dataProvider = "bankGateway")
	public void bankGateway(Map<String, String> datadriven) throws Exception {

		BankGateway bankGateway = new BankGateway();
		bankGateway.setOid_partner(datadriven.get("oid_partner"));
		bankGateway.setSign_type(datadriven.get("sign_type"));
		bankGateway.setVersion(datadriven.get("version"));
		bankGateway.setCharset_name(datadriven.get("charset_name"));
		bankGateway.setUser_id(datadriven.get("user_id"));
		bankGateway.setTimestamp(datadriven.get("timestamp"));
		bankGateway.setBusi_partner(datadriven.get("busi_partner"));		
		bankGateway.setNo_order(datadriven.get("no_order"));
		bankGateway.setDt_order(datadriven.get("dt_order"));
		bankGateway.setName_goods(datadriven.get("name_goods"));
		bankGateway.setInfo_order(datadriven.get("info_order"));
		bankGateway.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		bankGateway.setNotify_url(datadriven.get("notify_url"));
		bankGateway.setUrl_return(datadriven.get("url_return"));
		bankGateway.setUserreq_ip(datadriven.get("userreq_ip"));
		bankGateway.setUrl_order(datadriven.get("url_order"));
		bankGateway.setValid_order(datadriven.get("valid_order"));
		bankGateway.setBank_code(datadriven.get("bank_code"));
		bankGateway.setPay_type(datadriven.get("pay_type"));
		bankGateway.setRisk_item(datadriven.get("risk_item"));
		bankGateway.setCol_oidpartner(datadriven.get("col_oidpartner"));
		bankGateway.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankGateway)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankGateway);
		String bankGatewayUrl = Property.get("llwallet.bankGateway.url");

		// WEB网银支付请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + bankGatewayUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// WEB网银支付请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		// assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("WEB网银支付接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "bankGateway")
	public Iterator<Object[]> data4bankGateway() throws IOException {
		return new ExcelProvider(this, "bankGateway");
	}

	@AfterClass
	public void afterClass() {

	}

}
