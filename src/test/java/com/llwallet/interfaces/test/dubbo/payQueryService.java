package com.llwallet.interfaces.test.dubbo;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import com.lianpay.publics.dubbo.service.IPayQueryService;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class payQueryService extends AbstractTestNGSpringContextTests {

	@Autowired
	private IPayQueryService payQueryService;

	@Test
	public void test() {

		String req = "{\"oid_trader\":\"201712041913479351111111111\", \"oid_billno\":\"201504030000006002111111111\",\"order_id\":\"2017012\",\"correlationID\":\"2017012\",\"type_dc\":\"2017012\",\"col_custid\":\"2017012\"}";
		String rsp = payQueryService.paySingleQry(req);
		System.out.println("请求： " + req);
		System.out.println("响应： " + rsp);

	}
}