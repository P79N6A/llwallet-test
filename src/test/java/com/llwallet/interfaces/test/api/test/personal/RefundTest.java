package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.Refund;
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
 * 商户退款测试
 */

public class RefundTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "商户退款测试", timeOut = 60000, dataProvider = "refund")
	public void refund(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_refund = ApiUtils.getCurrentDateSecondStr();
		String no_refund = ApiUtils.getCurrentDateMillisecondStr();
		
		Refund refund = new Refund();
		refund.setOid_partner(datadriven.get("oid_partner"));
		refund.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("no_refund"))){
			refund.setNo_refund(no_refund);
		}else{
			refund.setNo_refund(datadriven.get("no_refund"));
		}
		if ("auto".equals(datadriven.get("dt_refund"))){
			refund.setDt_refund(dt_refund);
		}else{
			refund.setDt_refund(datadriven.get("dt_refund"));
		}
		refund.setPay_userid(datadriven.get("pay_userid"));		
		refund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund")));
		refund.setNo_order(datadriven.get("no_order"));
		refund.setDt_order(datadriven.get("dt_order"));
		refund.setOid_paybill(datadriven.get("oid_paybill"));
		refund.setNotify_url(datadriven.get("notify_url"));
		refund.setRisk_item(datadriven.get("risk_item"));
		refund.setTrader_refund(datadriven.get("trader_refund"));
		refund.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(refund)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(refund);
		String refundUrl = Property.get("llwallet.refund.url");

		// 商户退款请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(refundUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 商户退款请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("商户退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "refund")
	public Iterator<Object[]> data4refund() throws IOException {
		return new ExcelProvider(this, "refund",1);
	}

	@AfterClass
	public void afterClass() {

	}

}
