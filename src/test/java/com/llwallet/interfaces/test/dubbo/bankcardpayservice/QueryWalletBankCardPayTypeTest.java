package com.llwallet.interfaces.test.dubbo.bankcardpayservice;

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
import com.yintong.llwallet.domain.WalletBankCardPayBean;
import com.yintong.llwallet.service.IWalletBankCardPayService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class QueryWalletBankCardPayTypeTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private IWalletBankCardPayService iWalletBankCardPayService;

	@Test(description = "查询银行卡支付验密方式接口测试", timeOut = 60000, dataProvider = "queryWalletBankCardPayType")
	public void queryWalletBankCardPayType(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();

		WalletBankCardPayBean walletBankCardPayBean = new WalletBankCardPayBean();
		walletBankCardPayBean.setCorrelationID(correlationID);
		walletBankCardPayBean.setOid_partner(datadriven.get("oid_partner"));
		walletBankCardPayBean.setOid_userno(datadriven.get("oid_userno"));
		walletBankCardPayBean.setUser_id(datadriven.get("user_id"));
		walletBankCardPayBean.setMoney_order(datadriven.get("money_order"));
		walletBankCardPayBean.setRet_code(datadriven.get("ret_code"));
		walletBankCardPayBean.setRet_msg(datadriven.get("ret_msg"));
		walletBankCardPayBean.setRisk_item(datadriven.get("risk_item"));
		walletBankCardPayBean.setPay_type_bankcardpay(datadriven.get("pay_type_bankcardpay"));

		System.out.println("walletBankCardPayBean： " + JSON.toJSONString(walletBankCardPayBean));
		String rsp = JSON.toJSONString(iWalletBankCardPayService.queryWalletBankCardPayType(walletBankCardPayBean));
		System.out.println("查询银行卡支付验密方式返回：： " + rsp);
		Reporter.log("查询银行卡支付验密方式返回：" + rsp);
		// 查询银行卡支付验密方式请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("查询银行卡支付验密方式接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "queryWalletBankCardPayType")
	public Iterator<Object[]> data4queryWalletBankCardPayType() throws IOException {
		return new ExcelProvider(this, "queryWalletBankCardPayType");
	}

	@AfterClass
	public void afterClass() {

	}
}