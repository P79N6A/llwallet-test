package com.account.interfaces.test.dubbo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.Property;
import com.yintong.llwallet.accounts.constants.AccountType;
import com.yintong.llwallet.accounts.constants.CurrencyType;
import com.yintong.llwallet.accounts.constants.OrderType;
import com.yintong.llwallet.accounts.domain.request.User;
import com.yintong.llwallet.accounts.domain.request.WalletOrderInfoRequest;
import com.yintong.llwallet.accounts.service.ILLwalletAccountsService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class ChargeUpTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ILLwalletAccountsService llwalletAccountsService;

	@Test(description = "记账接口测试", timeOut = 60000, dataProvider = "chargeUp")
	public void chargeUp(Map<String, String> datadriven) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date date = cal.getTime();
		String dt_order = sdf.format(date);
		String no_order = sdf.format(date);
		String trader_paybill = sdf.format(date);

		WalletOrderInfoRequest walletOrderInfoRequest = new WalletOrderInfoRequest();

		walletOrderInfoRequest.setCard_key(datadriven.get("card_key"));
		walletOrderInfoRequest.setCard_no(datadriven.get("card_no"));
		walletOrderInfoRequest.setMoney_order(datadriven.get("money_order"));
		walletOrderInfoRequest.setName_goods(datadriven.get("name_goods"));
		walletOrderInfoRequest.setOid_acctno(datadriven.get("oid_acctno"));
		if ("auto".equals(datadriven.get("dt_order"))) {
			walletOrderInfoRequest.setDt_order(dt_order);
		} else {
			walletOrderInfoRequest.setDt_order(datadriven.get("dt_order"));
		}
		if ("auto".equals(datadriven.get("no_order"))) {
			walletOrderInfoRequest.setNo_order(no_order);
		} else {
			walletOrderInfoRequest.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("trader_paybill"))) {
			walletOrderInfoRequest.setTrader_paybill(trader_paybill);
		} else {
			walletOrderInfoRequest.setTrader_paybill(datadriven.get("trader_paybill"));
		}
		// walletOrderInfoRequest.setAcct_type(AccountType.POS);
		// walletOrderInfoRequest.setCurrencyType(CurrencyType.CNY);
		// walletOrderInfoRequest.setOrder_type(OrderType.POS_ACCEPTANCE);
		walletOrderInfoRequest.setAcct_type(AccountType.getAccountTypeInstanseByName(datadriven.get("acct_type")));
		walletOrderInfoRequest
				.setCurrencyType(CurrencyType.getCurrencyTypeInstanceByName(datadriven.get("currencyType")));
		walletOrderInfoRequest.setOrder_type(OrderType.getOrderTypeInstanceByName(datadriven.get("order_type")));

		walletOrderInfoRequest.setUser(
				new User(datadriven.get("oid_userno"), datadriven.get("user_id"), datadriven.get("oid_partner")));
		System.out.println("walletOrderInfoRequest： " + JSON.toJSONString(walletOrderInfoRequest));

		String rsp = JSON.toJSONString(llwalletAccountsService.chargeUp(walletOrderInfoRequest));
		System.out.println("记账返回：" + rsp);	
		Reporter.log("记账返回：" + rsp);
		Thread.sleep(1000);
		// 记账请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("记账接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "chargeUp")
	public Iterator<Object[]> data4chargeUp() throws IOException {
		return new ExcelProvider(this, "chargeUp");
	}

	@AfterClass
	public void afterClass() {

	}
}