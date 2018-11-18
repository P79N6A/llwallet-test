package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.SecpayRefund;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 担保交易退款测试
 */

public class SecpayRefundOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "担保交易退款测试", timeOut = 60000, dataProvider = "secpayRefund")
	public void secpayRefund(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String dt_refund = ApiUtils.getCurrentDateSecondStr();
		String no_refund = ApiUtils.getCurrentDateMillisecondStr();

		SecpayRefund secpayRefund = new SecpayRefund();
		secpayRefund.setOid_partner(datadriven.get("oid_partner"));
		secpayRefund.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("no_refund"))) {
			secpayRefund.setNo_refund(no_refund);
		} else {
			secpayRefund.setNo_refund(datadriven.get("no_refund"));
		}
		if ("auto".equals(datadriven.get("dt_refund"))) {
			secpayRefund.setDt_refund(dt_refund);
		} else {
			secpayRefund.setDt_refund(datadriven.get("dt_refund"));
		}

		if (datadriven.get("money_refund").length() < 1) {
			secpayRefund.setMoney_refund(null);
		} else {
			secpayRefund.setMoney_refund(Float.parseFloat(datadriven.get("money_refund")));
		}
		secpayRefund.setNo_order(datadriven.get("no_order"));
		secpayRefund.setDt_order(datadriven.get("dt_order"));
		secpayRefund.setOid_paybill(datadriven.get("oid_paybill"));
		secpayRefund.setNotify_url(datadriven.get("notify_url"));
		secpayRefund.setCol_partner(datadriven.get("col_partner"));
		secpayRefund.setCol_custid(datadriven.get("col_custid"));
		secpayRefund.setCol_custtype(datadriven.get("col_custtype"));		
		secpayRefund.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(secpayRefund)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(secpayRefund);
		String secpayRefundUrl = Property.get("llwallet.secpayRefund.url.online");

		// 担保交易退款请求提交
		String rsp = HttpRequest.httpPostWithJSON(secpayRefundUrl, reqJson);

		Reporter.log("组合支付退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		
		// 担保交易退款请求返回检查
		assert rsp.contains(datadriven.get("result_code"));
	}

	@DataProvider(name = "secpayRefund")
	public Iterator<Object[]> data4secpayRefund() throws IOException {
		return new ExcelProvider(this, "secpayRefund",7);
	}

	@AfterClass
	public void afterClass() {

	}

}