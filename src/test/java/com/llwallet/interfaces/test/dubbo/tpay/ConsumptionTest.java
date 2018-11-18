package com.llwallet.interfaces.test.dubbo.tpay;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.lianpay.tpay.core.domain.ConsumptionRequest;
import com.lianpay.tpay.core.domain.TpayResponse;
import com.lianpay.tpay.core.service.ConsumptionService;
import com.lianpay.tpay.share.domain.ConsumptionInfo;
import com.lianpay.tpay.share.util.DateUtil;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class ConsumptionTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ConsumptionService consumptionService;

	@Test(description = "消费申请接口测试", timeOut = 60000, dataProvider = "consumption")
	public void consumption(Map<String, String> datadriven) throws Exception {

		ConsumptionRequest consumptionRequest=new ConsumptionRequest();
		ConsumptionInfo consumptionInfo=new ConsumptionInfo();
		
		consumptionRequest.setOid_partner("201608240000207011");
		consumptionRequest.setNo_order(DateUtil.getCurrentDateTimeStr1());
		consumptionRequest.setNo_qrorder(DateUtil.getCurrentDateTimeStr1());
		consumptionRequest.setDt_order("20180109164520");
		consumptionRequest.setMoney_order("3000");
		consumptionRequest.setCust_ip("192.167.20.11");
		consumptionRequest.setPay_chnl("0");
		consumptionRequest.setBank_serialno("221121112");
		consumptionRequest.setPay_partner("201606150000160004");
		consumptionRequest.setAuth_flag("1");
		consumptionRequest.setAuth_amt("120000");
		consumptionInfo.setTpay_acctno("SQ1G8jD7tzWk");
		consumptionInfo.setTpay_bank("04012900");
		consumptionInfo.setMerchant_name("测试商户号");
		consumptionInfo.setMerchant_type("MCC");
		consumptionInfo.setCard_accptrId("12111111121");
		consumptionInfo.setRisk_param("{\"test\"}");
		consumptionRequest.setConsumptionInfo(consumptionInfo);

		System.out.println("consumptionRequest： " + JSON.toJSONString(consumptionRequest));
		TpayResponse rsp = consumptionService.consumption(consumptionRequest);
		Reporter.log("消费申请返回：" + JSON.toJSONString(rsp));
		// 消费申请请求返回检查
//		assert JSON.toJSONString(rsp).contains(datadriven.get("result_code"));

		Reporter.log("消费申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "consumption")
	public Iterator<Object[]> data4consumption() throws IOException {
		return new ExcelProvider(this, "consumption",1);
	}

	@AfterClass
	public void afterClass() {

	}
}