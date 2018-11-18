package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.ModifyUnitUserAcct;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 企业用户对公账户修改接口测试
 */

public class ModifyUnitUserAcctTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "企业用户对公账户修改接口测试", timeOut = 60000, dataProvider = "modifyUnitUserAcct")
	public void modifyUnitUserAcct(Map<String, String> datadriven) throws Exception {

		ModifyUnitUserAcct modifyUnitUserAcct = new ModifyUnitUserAcct();
		modifyUnitUserAcct.setOid_partner(datadriven.get("oid_partner"));
		modifyUnitUserAcct.setSign_type(datadriven.get("sign_type"));
		modifyUnitUserAcct.setToken(datadriven.get("token"));
		modifyUnitUserAcct.setUser_id(datadriven.get("user_id"));
		modifyUnitUserAcct.setCity_code(datadriven.get("city_code"));
		modifyUnitUserAcct.setBrabank_name(datadriven.get("brabank_name"));
		modifyUnitUserAcct.setCard_no(datadriven.get("card_no"));
		modifyUnitUserAcct.setBank_code(datadriven.get("bank_code"));
		modifyUnitUserAcct.setPrcptcd(datadriven.get("prcptcd"));
		modifyUnitUserAcct.setAcct_name(datadriven.get("acct_name"));
		modifyUnitUserAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(modifyUnitUserAcct)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(modifyUnitUserAcct);
		String modifyUnitUserAcctUrl = Property.get("llwallet.modifyUnitUserAcct.url");
				
		// 企业用户对公账户修改请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl("" + modifyUnitUserAcctUrl + "");
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 企业用户对公账户修改请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
//		assert hf.getResponseBody().contains(datadriven.get("result_code1"));

		Reporter.log("企业用户对公账户修改接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
   
	@DataProvider(name = "modifyUnitUserAcct")
	public Iterator<Object[]> data4modifyUnitUserAcct() throws IOException {
		return new ExcelProvider(this, "modifyUnitUserAcct");
	}

	@AfterClass
	public void afterClass() {

	}

}
