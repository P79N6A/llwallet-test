package com.llwallet.interfaces.test.dubbo.userbusiqueryservice;

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
import com.lianpay.llwallet.busi.domain.UserBusiBean;
import com.yintong.llwallet.service.ILLWalletUserBusiQueryService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class QueryUserBusiAmtTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private ILLWalletUserBusiQueryService iLLWalletUserBusiQueryService;

	@Test(description = "查询用户业务额度接口测试", timeOut = 60000, dataProvider = "queryUserBusiAmt")
	public void queryUserBusiAmt(Map<String, String> datadriven) throws Exception {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();

		UserBusiBean userBusiBean = new UserBusiBean();
		userBusiBean.setCorrelationID(correlationID);
		userBusiBean.setOid_partner(datadriven.get("oid_partner"));
		userBusiBean.setOid_userno(datadriven.get("oid_userno"));
		userBusiBean.setUser_login(datadriven.get("user_login"));
		userBusiBean.setType_user(datadriven.get("type_user"));
		userBusiBean.setRet_code(datadriven.get("ret_code"));
		userBusiBean.setRet_msg(datadriven.get("ret_msg"));

//		userBusiBean.setAccumulate_day_balancepay(datadriven.get("accumulate_day_balancepay"));
//		userBusiBean.setAccumulate_day_cashout(datadriven.get("accumulate_day_cashout"));
//		userBusiBean.setAccumulate_day_cashout_times(datadriven.get("accumulate_day_cashout_times"));
//		userBusiBean.setAccumulate_day_recharge(datadriven.get("accumulate_day_recharge"));
//		userBusiBean.setAccumulate_day_trans(datadriven.get("accumulate_day_trans"));
//
//		userBusiBean.setAccumulate_month_balancepay(datadriven.get("accumulate_month_balancepay"));
//		userBusiBean.setAccumulate_month_cashout(datadriven.get("accumulate_month_cashout"));
//		userBusiBean.setAccumulate_month_recharge(datadriven.get("accumulate_month_recharge"));
//		userBusiBean.setAccumulate_month_trans(datadriven.get("accumulate_month_trans"));
//		userBusiBean.setAccumulate_trans_times(datadriven.get("accumulate_trans_times"));
//
//		userBusiBean.setAmt_day_balancepay(datadriven.get("amt_day_balancepay"));
//		userBusiBean.setAmt_day_cashout(datadriven.get("amt_day_cashout"));
//		userBusiBean.setAmt_day_recharge(datadriven.get("amt_day_recharge"));
//		userBusiBean.setAmt_day_trans(datadriven.get("amt_day_trans"));
//
//		userBusiBean.setAmt_month_balancepay(datadriven.get("amt_month_balancepay"));
//		userBusiBean.setAmt_month_cashout(datadriven.get("amt_month_cashout"));
//		userBusiBean.setAmt_month_recharge(datadriven.get("amt_month_recharge"));
//		userBusiBean.setAmt_month_trans(datadriven.get("amt_month_trans"));
//
//		userBusiBean.setAmt_single_balancepay(datadriven.get("amt_single_balancepay"));
//		userBusiBean.setAmt_single_cashout(datadriven.get("amt_single_cashout"));
//		userBusiBean.setAmt_single_recharge(datadriven.get("amt_single_recharge"));
//		userBusiBean.setAmt_single_trans(datadriven.get("amt_single_trans"));
//
//		userBusiBean.setCash_times(datadriven.get("cash_times"));
//		userBusiBean.setDay_overload(datadriven.get("day_overload"));
//		userBusiBean.setDt_create(datadriven.get("dt_create"));
//		userBusiBean.setDt_day_limit_update(datadriven.get("dt_day_limit_update"));
//		userBusiBean.setDt_month_limit_update(datadriven.get("dt_month_limit_update"));
//		userBusiBean.setDt_update(datadriven.get("dt_update"));
//		userBusiBean.setInCache(Boolean.parseBoolean(datadriven.get("inCache")));
//		userBusiBean.setMemo(datadriven.get("memo"));
//		userBusiBean.setStat_user(datadriven.get("stat_user"));
//		userBusiBean.setTrader_amt_day_balancepay(datadriven.get("trader_amt_day_balancepay"));
//		userBusiBean.setTrader_amt_day_cashout(datadriven.get("trader_amt_day_cashout"));
//		userBusiBean.setTrader_amt_day_recharge(datadriven.get("trader_amt_day_recharge"));
//		userBusiBean.setTrader_amt_day_trans(datadriven.get("trader_amt_day_trans"));
//
//		userBusiBean.setTrader_amt_month_balancepay(datadriven.get("trader_amt_month_balancepay"));
//		userBusiBean.setTrader_amt_month_cashout(datadriven.get("trader_amt_month_cashout"));
//		userBusiBean.setTrader_amt_month_recharge(datadriven.get("trader_amt_month_recharge"));
//		userBusiBean.setTrader_amt_month_trans(datadriven.get("trader_amt_month_trans"));
//		userBusiBean.setTrader_cash_times(datadriven.get("trader_cash_times"));
//		userBusiBean.setTrader_trans_times(datadriven.get("trader_trans_times"));
//		userBusiBean.setTrans_times(datadriven.get("trans_times"));

		System.out.println("userBusiBean： " + JSON.toJSONString(userBusiBean));
		String rsp = JSON.toJSONString(iLLWalletUserBusiQueryService.queryUserBusiAmt(userBusiBean));
		System.out.println("查询用户业务额度返回：： " + rsp);
		Reporter.log("查询用户业务额度返回：" + rsp);
		// 查询用户业务额度请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("查询用户业务额度接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "queryUserBusiAmt")
	public Iterator<Object[]> data4queryUserBusiAmt() throws IOException {
		return new ExcelProvider(this, "queryUserBusiAmt");
	}

	@AfterClass
	public void afterClass() {

	}
}