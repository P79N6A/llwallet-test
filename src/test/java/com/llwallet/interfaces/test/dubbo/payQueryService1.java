package com.llwallet.interfaces.test.dubbo;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.net.MalformedURLException;

import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import com.lianpay.publics.dubbo.service.IPayQueryService;
import com.caucho.hessian.client.HessianProxyFactory;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class payQueryService1 extends AbstractTestNGSpringContextTests {

	private static HessianProxyFactory getFactory() {
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setReadTimeout(10000);
		factory.setOverloadEnabled(true);
		return factory;
	}
	
//	@Autowired
//	private IPayQueryService payQueryService;

	@Test
	public void test() throws MalformedURLException {

		HessianProxyFactory factory = getFactory();
		IPayQueryService payQueryService = (IPayQueryService) factory.create(IPayQueryService.class,"http://192.168.110.20:8088/payQueryService");
		
		String req = "{\"oid_trader\":\"201712041913479351111111111\", \"oid_billno\":\"201504030000006002111111111\",\"order_id\":\"2017012\",\"correlationID\":\"2017012\",\"type_dc\":\"2017012\",\"col_custid\":\"2017012\"}";
		String rsp = payQueryService.paySingleQry(req);
		System.out.println("请求： " + req);
		System.out.println("响应： " + rsp);

	}
}