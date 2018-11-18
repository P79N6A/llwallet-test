package com.llwallet.interfaces.test.api.online.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.CashoutCombineApply;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 用户银行卡提现申请接口测试
 */

public class CashoutCombineApplyOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "用户银行卡提现申请接口测试", timeOut = 60000, dataProvider = "cashoutCombineApply")
	public void cashoutCombineApply(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		CashoutCombineApply cashoutCombineApply = new CashoutCombineApply();
		cashoutCombineApply.setOid_partner(datadriven.get("oid_partner"));
		cashoutCombineApply.setSign_type(datadriven.get("sign_type"));
		cashoutCombineApply.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			cashoutCombineApply.setNo_order(no_order);
		}else{
			cashoutCombineApply.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			cashoutCombineApply.setDt_order(dt_order);
		}else{
			cashoutCombineApply.setDt_order(datadriven.get("dt_order"));
		}		
		if ("".equals(datadriven.get("money_order"))){
			cashoutCombineApply.setMoney_order(null);
		}else {
			cashoutCombineApply.setMoney_order(datadriven.get("money_order"));
		}
		cashoutCombineApply.setCard_no(datadriven.get("card_no"));
		if (datadriven.get("pwd_pay").length() != 0) {
			cashoutCombineApply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			cashoutCombineApply.setPwd_pay(datadriven.get("pwd_pay"));
		}
		cashoutCombineApply.setNo_agree(datadriven.get("no_agree"));
		cashoutCombineApply.setCity_code(datadriven.get("city_code"));
		cashoutCombineApply.setBrabank_name(datadriven.get("brabank_name"));
		cashoutCombineApply.setInfo_order(datadriven.get("info_order"));
		cashoutCombineApply.setNotify_url(datadriven.get("notify_url"));
		cashoutCombineApply.setApi_version(datadriven.get("api_version"));
		cashoutCombineApply.setPrcptcd(datadriven.get("prcptcd"));		
		cashoutCombineApply.setRisk_item(datadriven.get("risk_item"));
		cashoutCombineApply.setFee_data(datadriven.get("fee_data"));		
		cashoutCombineApply.setInfo_order(datadriven.get("info_order"));
		cashoutCombineApply.setPaytime_type(datadriven.get("paytime_type"));
		cashoutCombineApply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutCombineApply)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(cashoutCombineApply);
		String cashoutCombineApplyUrl = Property.get("llwallet.cashoutCombineApply.url.online");

		// 用户银行卡提现申请请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(cashoutCombineApplyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 用户银行卡提现申请请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("用户银行卡提现申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "企业用户提现测试", timeOut = 60000, dataProvider = "cashoutCombineApply1")
	public void cashoutCombineApply1(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		CashoutCombineApply cashoutCombineApply = new CashoutCombineApply();
		cashoutCombineApply.setOid_partner(datadriven.get("oid_partner"));
		cashoutCombineApply.setSign_type(datadriven.get("sign_type"));
		cashoutCombineApply.setUser_id(datadriven.get("user_id"));
		if ("auto".equals(datadriven.get("no_order"))){
			cashoutCombineApply.setNo_order(no_order);
		}else{
			cashoutCombineApply.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			cashoutCombineApply.setDt_order(dt_order);
		}else{
			cashoutCombineApply.setDt_order(datadriven.get("dt_order"));
		}		
		if ("".equals(datadriven.get("money_order"))){
			cashoutCombineApply.setMoney_order(null);
		}else {
			cashoutCombineApply.setMoney_order(datadriven.get("money_order"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			cashoutCombineApply.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			cashoutCombineApply.setPwd_pay(datadriven.get("pwd_pay"));
		}
		cashoutCombineApply.setNotify_url(datadriven.get("notify_url"));
		cashoutCombineApply.setRisk_item(datadriven.get("risk_item"));
		cashoutCombineApply.setFee_data(datadriven.get("fee_data"));		
		cashoutCombineApply.setPaytime_type(datadriven.get("paytime_type"));
		cashoutCombineApply.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(cashoutCombineApply)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(cashoutCombineApply);
		String cashoutCombineApplyUrl = Property.get("llwallet.cashoutCombineApply.url.online");

		// 用户银行卡提现申请请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(cashoutCombineApplyUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();

		// 用户银行卡提现申请请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
        
		Reporter.log("用户银行卡提现申请接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}
	
	@DataProvider(name = "cashoutCombineApply")
	public Iterator<Object[]> data4cashoutCombineApply() throws IOException {
		return new ExcelProvider(this, "cashoutCombineApply",2);
	}

	@DataProvider(name = "cashoutCombineApply1")
	public Iterator<Object[]> data4cashoutCombineApply1() throws IOException {
		return new ExcelProvider(this, "cashoutCombineApply1",4111);
	}
	
	@AfterClass
	public void afterClass() {

	}
}