package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.CashoutNowApply;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.TelnetUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户实时提现申请接口测试
 */

public class CashoutNowApplyTest {

	DbUtils dbUtils = new DbUtils();
	
	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户实时提现申请接口测试", timeOut = 60000, dataProvider = "cashoutNowApply")
	public void cashoutNowApply(Map<String, String> datadriven) throws Exception {

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		if (datadriven.get("result_code").contains("7701")) {
			String oid_userno = dbUtils.queryOidUserNo(datadriven.get("oid_partner"), datadriven.get("user_id")).getOid_userno().trim();
			String oid_acctno = dbUtils.queryAcctStat(oid_userno).getOid_acctno();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_acctno).append("_payPwdWrongTimesCachedKey\"])");
			telnetUtil.execute(command.toString());
		}
		telnetUtil.disconnect();
		
		dbUtils.updateTraderBusiCtl(datadriven.get("oid_partner"));
		
		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		String balanceUpdate = datadriven.get("balanceUpdate");
		String cashoutNowApplyUrl = Property.get("llwallet.cashoutNowApply.url");
		
		try {
			CashoutNowApply cashoutNowApply = new CashoutNowApply();
			cashoutNowApply.setOid_partner(datadriven.get("oid_partner"));
			cashoutNowApply.setSign_type(datadriven.get("sign_type"));
			cashoutNowApply.setUser_id(datadriven.get("user_id"));
			if ("auto".equals(datadriven.get("no_order"))){
				cashoutNowApply.setNo_order(no_order);
			}else{
				cashoutNowApply.setNo_order(datadriven.get("no_order"));
			}
			if ("auto".equals(datadriven.get("dt_order"))){
				cashoutNowApply.setDt_order(dt_order);
			}else{
				cashoutNowApply.setDt_order(datadriven.get("dt_order"));
			}		
			if ("".equals(datadriven.get("money_order"))){
				cashoutNowApply.setMoney_order(null);
			}else {
				cashoutNowApply.setMoney_order(datadriven.get("money_order"));
			}
			cashoutNowApply.setCard_no(datadriven.get("card_no"));
			if (datadriven.get("pwd_pay").length() != 0) {
				cashoutNowApply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
			} else {
				cashoutNowApply.setPwd_pay(datadriven.get("pwd_pay"));
			}
			if ("auto".equals(datadriven.get("no_agree"))){			
				DbUtils dbUtils = new DbUtils();
				String no_agree = dbUtils.querySigned(datadriven.get("user_id")).getAgreementno();
				cashoutNowApply.setNo_agree(no_agree);
			}else{
				cashoutNowApply.setNo_agree(datadriven.get("no_agree"));
			}
			cashoutNowApply.setCity_code(datadriven.get("city_code"));
			cashoutNowApply.setBrabank_name(datadriven.get("brabank_name"));
			cashoutNowApply.setInfo_order(datadriven.get("info_order"));
			cashoutNowApply.setNotify_url(datadriven.get("notify_url"));
			cashoutNowApply.setApi_version(datadriven.get("api_version"));
			cashoutNowApply.setPrcptcd(datadriven.get("prcptcd"));		
			cashoutNowApply.setRisk_item(datadriven.get("risk_item"));
			cashoutNowApply.setFee_data(datadriven.get("fee_data"));		
			cashoutNowApply.setInfo_order(datadriven.get("info_order"));
			cashoutNowApply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutNowApply)), datadriven.get("key")));
			String reqJson = JSON.toJSONString(cashoutNowApply);

			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], result[1]);
			}
			// 用户实时提现申请请求提交
			HttpFixture hf = new HttpFixture();
			hf.setEncode("UTF-8");
			hf.setUrl(cashoutNowApplyUrl);
			hf.addHeaderValue("Content-Type", "application/json");
			hf.addRequestBody(reqJson);
			hf.Post();

			// 用户实时提现申请请求返回检查
			assert hf.getResponseBody().contains(datadriven.get("result_code"));
	        
			Reporter.log("用户实时提现申请接口测试： " + datadriven.get("comment"), true);
			System.out.println("===============================================");
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!StringUtils.isBlank(balanceUpdate)) {
				String result[] = balanceUpdate.split(",");
				dbUtils.updateAcctBalance(result[0], "1000000000");
			}
		}
	}

	@DataProvider(name = "cashoutNowApply")
	public Iterator<Object[]> data4cashoutNowApply() throws IOException {
		return new ExcelProvider(this, "cashoutNowApply");
	}

	@AfterClass
	public void afterClass() {

	}
}