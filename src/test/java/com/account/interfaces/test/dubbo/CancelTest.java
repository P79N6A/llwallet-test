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
import com.yintong.llwallet.accounts.domain.request.User;
import com.yintong.llwallet.accounts.service.ILLwalletAccountsService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class CancelTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ILLwalletAccountsService llwalletAccountsService;

	@Test(description = "订单撤销接口测试", timeOut = 60000, dataProvider = "cancel")
	public void cancel(Map<String, String> datadriven) throws Exception {

		String rsp = JSON.toJSONString(llwalletAccountsService.cancel(new User(datadriven.get("oid_userno"), datadriven.get("user_id"), datadriven.get("oid_partner")), datadriven.get("no_order"), datadriven.get("remark")));

		System.out.println("订单撤销返回：" + rsp);	
		Reporter.log("订单撤销返回：" + rsp);
		Thread.sleep(1000);
		// 订单撤销请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("订单撤销接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "cancel")
	public Iterator<Object[]> data4cancel() throws IOException {
		return new ExcelProvider(this, "cancel",1);
	}

	@AfterClass
	public void afterClass() {

	}
}