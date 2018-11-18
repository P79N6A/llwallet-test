package com.account.interfaces.test.api.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.account.interfaces.bean.FeePay;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包记账扣手续费接口测试
 */

public class FeePayTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包记账扣手续费接口测试", timeOut = 60000, dataProvider = "feePay")
	public void feePay(Map<String, String> datadriven) throws Exception {

		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		FeePay feePay = new FeePay();
		feePay.setOid_partner(datadriven.get("oid_partner"));
		feePay.setSign_type(datadriven.get("sign_type"));
		feePay.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			feePay.setNo_order(no_order);
		}else{
			feePay.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			feePay.setDt_order(dt_order);
		}else{
			feePay.setDt_order(datadriven.get("dt_order"));
		}
		
		if ("".equals(datadriven.get("money_order"))){
			feePay.setMoney_order(null);
		}else {
			feePay.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		}
		
		feePay.setName_goods(datadriven.get("name_goods"));
		feePay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(feePay)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(feePay);
		String feePayUrl = Property.get("llwallet.feePay.url");

		// 钱包记账扣手续费请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(feePayUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		Thread.sleep(1000);
		// 钱包记账扣手续费请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("钱包记账扣手续费接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "feePay")
	public Iterator<Object[]> data4feePay() throws IOException {
		return new ExcelProvider(this, "feePay");
	}

	@AfterClass
	public void afterClass() {

	}
}