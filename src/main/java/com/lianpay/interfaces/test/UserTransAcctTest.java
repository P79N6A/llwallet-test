package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.UserTransAcct;
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
 * 钱包余额转账接口测试
 */

public class UserTransAcctTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包余额转账接口测试", timeOut = 60000, dataProvider = "userTransAcct")
	public void userTransAcct(Map<String, String> datadriven) throws Exception {

		UserTransAcct userTransAcct = new UserTransAcct();
		userTransAcct.setOid_partner(datadriven.get("oid_partner"));
		userTransAcct.setSign_type(datadriven.get("sign_type"));
		userTransAcct.setBusi_partner(datadriven.get("busi_partner"));
		userTransAcct.setNo_order(datadriven.get("no_order"));
		userTransAcct.setDt_order(datadriven.get("dt_order"));
		userTransAcct.setPay_userid(datadriven.get("pay_userid"));
		userTransAcct.setCol_userid(datadriven.get("col_userid"));
		userTransAcct.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		userTransAcct.setRisk_item(datadriven.get("risk_item"));
		userTransAcct.setInfo_order(datadriven.get("info_order"));
		userTransAcct.setProc_src(datadriven.get("proc_src"));
		if (datadriven.get("pwd_pay").length() != 0) {
			userTransAcct.setPwd_pay(RSAUtil2.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			userTransAcct.setPwd_pay(datadriven.get("pwd_pay"));
		}		
		userTransAcct.setName_goods(datadriven.get("name_goods"));
		userTransAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userTransAcct)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userTransAcct);
		String userTransAcctUrl = Property.get("llwallet.userTransAcct.url");

		// 钱包余额转账请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + userTransAcctUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包余额转账请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		// assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("钱包余额转账接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "userTransAcct")
	public Iterator<Object[]> data4userTransAcct() throws IOException {
		return new ExcelProvider(this, "userTransAcct");
	}

	@AfterClass
	public void afterClass() {

	}

}
