package com.llwallet.interfaces.test.api.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.chnl.domain.notify.PaymentProcessReq;
import com.lianpay.chnl.service.notify.PaymentResultNotifyService;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.utils.ApiUtils;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 验证签名测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class PaymentResultNotifyTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private PaymentResultNotifyService paymentResultNotifyService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "异步回调处理", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		String bankSerial = ApiUtils.getCurrentDateMillisecondStr();

		PaymentProcessReq paymentprocessreq = new PaymentProcessReq();
		paymentprocessreq.setStatus(dt.get("paymentprocessreq").get("status"));
		paymentprocessreq.setPayRungroupid(dt.get("paymentprocessreq").get("payRungroupid"));
		paymentprocessreq.setSettleDate(dt.get("paymentprocessreq").get("settleDate"));
		paymentprocessreq.setBankSerial(bankSerial);
		paymentprocessreq.setPayAmount(dt.get("paymentprocessreq").get("payAmount"));
		System.out.println("paymentprocessreq： " + JSON.toJSONString(paymentprocessreq));
		String rsp = JSON.toJSONString(paymentResultNotifyService.processAfterNotify(paymentprocessreq));
		System.out.println("异步回调返回：" + rsp);

		Reporter.log("异步回调处理： " + dt.get("paymentprocessreq").get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,4);
	}

	@AfterClass
	public void afterClass() {

	}

}
