package com.llwallet.interfaces.test.dubbo.balancepaybusiservice;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.ApiUtils;
import com.tools.utils.Property;
import com.yintong.llwallet.domain.WalletBalancePayBean;
import com.yintong.llwallet.service.IWalletBalancePayBusiService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class QueryWalletBalancePayLimitTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private IWalletBalancePayBusiService iWalletBalancePayBusiService;

	@Test(description = "查询余额消费是否超限接口测试", timeOut = 60000, dataProvider = "queryWalletBalancePayLimit")
	public void queryWalletBalancePayLimit(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();

		WalletBalancePayBean walletBalancePayBean = new WalletBalancePayBean();
		walletBalancePayBean.setCorrelationID(correlationID);
		walletBalancePayBean.setOid_partner(datadriven.get("oid_partner"));
		walletBalancePayBean.setOid_userno(datadriven.get("oid_userno"));
		walletBalancePayBean.setUser_id(datadriven.get("user_id"));
		walletBalancePayBean.setMoney_order(datadriven.get("money_order"));
		walletBalancePayBean.setRet_code(datadriven.get("ret_code"));
		walletBalancePayBean.setRet_msg(datadriven.get("ret_msg"));
		walletBalancePayBean.setRisk_item(datadriven.get("risk_item"));
		walletBalancePayBean.setPay_type_balancepay(datadriven.get("pay_type_balancepay"));
		walletBalancePayBean.setAmt_day_balancepay(datadriven.get("amt_day_balancepay"));
		walletBalancePayBean.setAmt_month_balancepay(datadriven.get("amt_month_balancepay"));
		walletBalancePayBean.setAmt_single_balancepay(datadriven.get("amt_single_balancepay"));

		System.out.println("walletBalancePayBean： " + JSON.toJSONString(walletBalancePayBean));
		String rsp = JSON.toJSONString(iWalletBalancePayBusiService.queryWalletBalancePayLimit(walletBalancePayBean));
		System.out.println("查询余额消费是否超限返回：： " + rsp);
		Reporter.log("查询余额消费是否超限返回：" + rsp);
		// 查询余额消费是否超限请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("查询余额消费是否超限接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "queryWalletBalancePayLimit")
	public Iterator<Object[]> data4queryWalletBalancePayLimit() throws IOException {
		return new ExcelProvider(this, "queryWalletBalancePayLimit");
	}

	@AfterClass
	public void afterClass() {

	}
}