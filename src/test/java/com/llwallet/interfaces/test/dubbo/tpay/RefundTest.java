package com.llwallet.interfaces.test.dubbo.tpay;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.lianpay.tpay.core.domain.RefundRequest;
import com.lianpay.tpay.core.domain.TpayResponse;
import com.lianpay.tpay.core.service.RefundService;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class RefundTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private RefundService refundService;

	@Test(description = "消费申请接口测试", timeOut = 60000, dataProvider = "refund")
	public void refund(Map<String, String> datadriven) throws Exception {

		RefundRequest refundRequest = new RefundRequest();
		refundRequest.setOid_partner("201608240000207011");
		refundRequest.setNo_refund("21111212132");
		refundRequest.setDt_refund("20180110224420");
		refundRequest.setNo_order("2018013022891592");
//		refundRequest.setOid_paybill("2018013086516045");
		refundRequest.setMoney_refund("3000");
		System.out.println("退款请求报文：" + JSON.toJSONString(refundRequest));
		TpayResponse response = refundService.refund(refundRequest);
		System.out.println("退款返回报文：" + JSON.toJSONString(response));
		// 退款请求返回检查
		assert JSON.toJSONString(response).contains(datadriven.get("result_code"));

		Reporter.log("退款接口测试： " + datadriven.get("comment"), true);
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