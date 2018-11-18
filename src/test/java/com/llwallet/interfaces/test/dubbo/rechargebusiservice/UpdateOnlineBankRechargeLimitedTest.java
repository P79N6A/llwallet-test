package com.llwallet.interfaces.test.dubbo.rechargebusiservice;

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
import com.yintong.llwallet.domain.WalletRechargeBean;
import com.yintong.llwallet.service.IWalletRechargeBusiService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class UpdateOnlineBankRechargeLimitedTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private IWalletRechargeBusiService iWalletRechargeBusiService;

	@Test(description = "更新网银充值限额接口测试", timeOut = 60000, dataProvider = "updateOnlineBankRechargeLimited")
	public void updateOnlineBankRechargeLimited(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();

		WalletRechargeBean walletRechargeBean = new WalletRechargeBean();
		walletRechargeBean.setCorrelationID(correlationID);
		walletRechargeBean.setOid_partner(datadriven.get("oid_partner"));
		walletRechargeBean.setOid_userno(datadriven.get("oid_userno"));
		walletRechargeBean.setUser_id(datadriven.get("user_id"));
		walletRechargeBean.setMoney_order(datadriven.get("money_order"));
		walletRechargeBean.setRet_code(datadriven.get("ret_code"));
		walletRechargeBean.setRet_msg(datadriven.get("ret_msg"));
		walletRechargeBean.setRisk_item(datadriven.get("risk_item"));
		walletRechargeBean.setAmt_day_recharge(datadriven.get("amt_day_recharge"));
		walletRechargeBean.setAmt_month_recharge(datadriven.get("amt_month_recharge"));
		walletRechargeBean.setAmt_single_recharge(datadriven.get("amt_single_recharge"));
		walletRechargeBean.setId_no(datadriven.get("id_no"));
		walletRechargeBean.setId_type(datadriven.get("id_type"));
		walletRechargeBean.setName_user(datadriven.get("name_user"));
		walletRechargeBean.setPay_type_recharge(datadriven.get("pay_type_recharge"));
		walletRechargeBean.setUserType(datadriven.get("userType"));

		System.out.println("walletRechargeBean： " + JSON.toJSONString(walletRechargeBean));
		String rsp = JSON.toJSONString(iWalletRechargeBusiService.updateOnlineBankRechargeLimited(walletRechargeBean));
		System.out.println("更新网银充值限额返回：： " + rsp);
		Reporter.log("更新网银充值限额返回：" + rsp);
		// 更新网银充值限额请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("更新网银充值限额接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "updateOnlineBankRechargeLimited")
	public Iterator<Object[]> data4updateOnlineBankRechargeLimited() throws IOException {
		return new ExcelProvider(this, "updateOnlineBankRechargeLimited");
	}

	@AfterClass
	public void afterClass() {

	}
}