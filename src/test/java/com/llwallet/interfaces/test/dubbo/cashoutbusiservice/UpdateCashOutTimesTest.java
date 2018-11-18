package com.llwallet.interfaces.test.dubbo.cashoutbusiservice;

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
import com.yintong.llwallet.domain.WalletCashOutBean;
import com.yintong.llwallet.service.IWalletCashOutBusiService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class UpdateCashOutTimesTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private IWalletCashOutBusiService iWalletCashOutBusiService;

	@Test(description = "更新钱包提现次数接口测试", timeOut = 60000, dataProvider = "updateCashOutTimes")
	public void updateCashOutTimes(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();
		
		WalletCashOutBean walletCashOutBean = new WalletCashOutBean();
		walletCashOutBean.setCorrelationID(correlationID);
		walletCashOutBean.setOid_partner(datadriven.get("oid_partner"));
		walletCashOutBean.setOid_userno(datadriven.get("oid_userno"));
		walletCashOutBean.setUser_id(datadriven.get("user_id"));
		walletCashOutBean.setMoney_order(datadriven.get("money_order"));
		walletCashOutBean.setRet_code(datadriven.get("ret_code"));
		walletCashOutBean.setRet_msg(datadriven.get("ret_msg"));
		walletCashOutBean.setRisk_item(datadriven.get("risk_item"));
		walletCashOutBean.setAdvanceMoney(datadriven.get("advanceMoney"));
		walletCashOutBean.setAmountLimit(Boolean.parseBoolean(datadriven.get("amountLimit")));
		walletCashOutBean.setAmt_day_cashout(datadriven.get("amt_day_cashout"));
		walletCashOutBean.setAmt_month_cashout(datadriven.get("amt_month_cashout"));
		walletCashOutBean.setAmt_single_cashout(datadriven.get("amt_single_cashout"));
		walletCashOutBean.setCash_times(datadriven.get("cash_times"));

		System.out.println("walletCashOutBean： " + JSON.toJSONString(walletCashOutBean));
		String rsp = JSON.toJSONString(iWalletCashOutBusiService.updateCashOutTimes(walletCashOutBean));
		System.out.println("更新钱包提现次数返回：： " + rsp);
		Reporter.log("更新钱包提现次数返回：" + rsp);
		// 更新钱包提现次数请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("更新钱包提现次数接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "updateCashOutTimes")
	public Iterator<Object[]> data4updateCashOutTimes() throws IOException {
		return new ExcelProvider(this, "updateCashOutTimes");
	}

	@AfterClass
	public void afterClass() {

	}
}