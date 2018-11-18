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
public class AccountsQryTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ILLwalletAccountsService llwalletAccountsService;

	@Test(description = "账户查询接口测试", timeOut = 60000, dataProvider = "accountsQry")
	public void accountsQry(Map<String, String> datadriven) throws IllegalArgumentException, WalletAccountServiceException {

		String rsp = JSON.toJSONString(llwalletAccountsService.accountsQry(datadriven.get("oid_acctno")));
		System.out.println("账户查询返回：" + rsp);	
		Reporter.log("账户查询返回：" + rsp);
		// 账户查询接口返回检查
		assert rsp.contains(datadriven.get("result_code"));
		Reporter.log("账户查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "accountsQry")
	public Iterator<Object[]> data4accountsQry() throws IOException {
		return new ExcelProvider(this, "accountsQry");
	}

	@AfterClass
	public void afterClass() {

	}
}