package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.UserRefund;
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
 * 用户退款测试
 */

public class UserRefundOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户退款测试", timeOut = 60000, dataProvider = "userRefund")
	public void userRefund(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_refund = ApiUtils.getCurrentDateSecondStr();
		String no_refund = ApiUtils.getCurrentDateMillisecondStr();

		UserRefund userRefund = new UserRefund();
		userRefund.setOid_partner(datadriven.get("oid_partner"));
		userRefund.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("no_refund"))) {
			userRefund.setNo_refund(no_refund);
		} else {
			userRefund.setNo_refund(datadriven.get("no_refund"));
		}
		if ("auto".equals(datadriven.get("dt_refund"))) {
			userRefund.setDt_refund(dt_refund);
		} else {
			userRefund.setDt_refund(datadriven.get("dt_refund"));
		}
		userRefund.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("money_refund").length() < 1) {
			userRefund.setMoney_refund(null);
		} else {
			userRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund")));
		}
		userRefund.setNo_order(datadriven.get("no_order"));
		userRefund.setDt_order(datadriven.get("dt_order"));
		userRefund.setOid_paybill(datadriven.get("oid_paybill"));
		userRefund.setNotify_url(datadriven.get("notify_url"));
		userRefund.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userRefund)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(userRefund);
		String userRefundUrl = Property.get("llwallet.userRefund.url.online");

		// 用户退款请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userRefundUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 用户退款请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("用户退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "userRefund")
	public Iterator<Object[]> data4userRefund() throws IOException {
		return new ExcelProvider(this, "userRefund",4);
	}

	@AfterClass
	public void afterClass() {

	}

}
