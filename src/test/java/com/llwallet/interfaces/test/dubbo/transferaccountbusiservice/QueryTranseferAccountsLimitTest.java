package com.llwallet.interfaces.test.dubbo.transferaccountbusiservice;

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
import com.yintong.llwallet.domain.WalletTransferAccountsBean;
import com.yintong.llwallet.service.IWalletTransferAccountsBusiService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class QueryTranseferAccountsLimitTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private IWalletTransferAccountsBusiService iWalletTransferAccountsBusiService;

	@Test(description = "查询转账限额是否超限接口测试", timeOut = 60000, dataProvider = "queryTranseferAccountsLimit")
	public void queryTranseferAccountsLimit(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();

		WalletTransferAccountsBean walletTransferAccountsBean = new WalletTransferAccountsBean();
		walletTransferAccountsBean.setCorrelationID(correlationID);
		walletTransferAccountsBean.setOid_partner(datadriven.get("oid_partner"));
		walletTransferAccountsBean.setOid_userno(datadriven.get("oid_userno"));
		walletTransferAccountsBean.setUser_id(datadriven.get("user_id"));
		walletTransferAccountsBean.setMoney_order(datadriven.get("money_order"));
		walletTransferAccountsBean.setRet_code(datadriven.get("ret_code"));
		walletTransferAccountsBean.setRet_msg(datadriven.get("ret_msg"));
		walletTransferAccountsBean.setRisk_item(datadriven.get("risk_item"));
		walletTransferAccountsBean.setAmt_day_transe(datadriven.get("amt_day_transe"));
		walletTransferAccountsBean.setAmt_month_transe(datadriven.get("amt_month_transe"));
		walletTransferAccountsBean.setAmt_single_transe(datadriven.get("amt_single_transe"));
		walletTransferAccountsBean.setTrans_times(datadriven.get("trans_times"));

		System.out.println("walletTransferAccountsBean： " + JSON.toJSONString(walletTransferAccountsBean));
		String rsp = JSON.toJSONString(
				iWalletTransferAccountsBusiService.queryTranseferAccountsLimit(walletTransferAccountsBean));
		System.out.println("查询转账限额是否超限返回：： " + rsp);
		Reporter.log("查询转账限额是否超限返回：" + rsp);
		// 查询转账限额是否超限请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("查询转账限额是否超限接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "queryTranseferAccountsLimit")
	public Iterator<Object[]> data4queryTranseferAccountsLimit() throws IOException {
		return new ExcelProvider(this, "queryTranseferAccountsLimit");
	}

	@AfterClass
	public void afterClass() {

	}
}