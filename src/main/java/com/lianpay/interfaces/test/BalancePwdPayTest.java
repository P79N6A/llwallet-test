package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.BalancePwdPay;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.util.RSAUtil2;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业钱包余额消费接口测试
 */

public class BalancePwdPayTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业钱包余额消费接口测试", timeOut = 60000, dataProvider = "balancePwdPay")
	public void balancePwdPay(Map<String, String> datadriven) throws Exception {

		BalancePwdPay balancePwdPay = new BalancePwdPay();
		balancePwdPay.setOid_partner(datadriven.get("oid_partner"));
		balancePwdPay.setSign_type(datadriven.get("sign_type"));
		balancePwdPay.setUser_id(datadriven.get("user_id"));
		balancePwdPay.setNo_order(datadriven.get("no_order"));
		balancePwdPay.setDt_order(datadriven.get("dt_order"));
		balancePwdPay.setBusi_partner(datadriven.get("busi_partner"));
		balancePwdPay.setName_goods(datadriven.get("name_goods"));
		balancePwdPay.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		balancePwdPay.setInfo_order(datadriven.get("info_order"));
		if (datadriven.get("pwd_pay").length() != 0) {
			balancePwdPay.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			balancePwdPay.setPwd_pay(datadriven.get("pwd_pay"));
		}
		balancePwdPay.setNotify_url(datadriven.get("notify_url"));
		balancePwdPay.setRisk_item(datadriven.get("risk_item"));
		balancePwdPay.setShareing_data(datadriven.get("shareing_data"));
		balancePwdPay.setCol_oidpartner(datadriven.get("col_oidpartner"));
		balancePwdPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(balancePwdPay)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(balancePwdPay);
		String balancePwdPayUrl = Property.get("llwallet.balancePwdPay.url");

		// 企业钱包余额消费请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + balancePwdPayUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业钱包余额消费请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		// assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业钱包余额消费接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "balancePwdPay")
	public Iterator<Object[]> data4balancePwdPay() throws IOException {
		return new ExcelProvider(this, "balancePwdPay");
	}

	@AfterClass
	public void afterClass() {

	}

}
