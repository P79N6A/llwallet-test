package com.account.interfaces.test.dubbo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.Property;
import com.yintong.llwallet.accounts.excepption.WalletAccountServiceException;
import com.yintong.llwallet.accounts.service.ILLwalletAccountsService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class OrderSingleQryTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ILLwalletAccountsService llwalletAccountsService;

	@Test(description = "订单单条查询接口测试", timeOut = 60000, dataProvider = "orderSingleQry")
	public void orderSingleQry(Map<String, String> datadriven) throws IllegalArgumentException, WalletAccountServiceException {

		String rsp = JSON.toJSONString(llwalletAccountsService.orderSingleQry(datadriven.get("oid_partner"), datadriven.get("no_order")));
		System.out.println("订单单条查询返回：" + rsp);	
		Reporter.log("订单单条查询返回：" + rsp);
		// 订单单条查询接口返回检查
		assert rsp.contains(datadriven.get("result_code"));
		Reporter.log("订单单条查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "orderSingleQry")
	public Iterator<Object[]> data4orderSingleQry() throws IOException {
		return new ExcelProvider(this, "orderSingleQry");
	}

	@AfterClass
	public void afterClass() {

	}
}