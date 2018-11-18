package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.UserTransAcct;
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
 * 钱包余额转账接口测试
 */

public class UserTransAcctTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包余额转账接口测试", timeOut = 60000, dataProvider = "userTransAcct")
	public void userTransAcct(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		UserTransAcct userTransAcct = new UserTransAcct();
		userTransAcct.setOid_partner(datadriven.get("oid_partner"));
		userTransAcct.setSign_type(datadriven.get("sign_type"));
		userTransAcct.setBusi_partner(datadriven.get("busi_partner"));
		if ("auto".equals(datadriven.get("no_order"))){
			userTransAcct.setNo_order(no_order);
		}else{
			userTransAcct.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			userTransAcct.setDt_order(dt_order);
		}else{
			userTransAcct.setDt_order(datadriven.get("dt_order"));
		}
		
		userTransAcct.setPay_userid(datadriven.get("pay_userid"));
		userTransAcct.setCol_userid(datadriven.get("col_userid"));
		userTransAcct.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		userTransAcct.setRisk_item(datadriven.get("risk_item"));
		userTransAcct.setInfo_order(datadriven.get("info_order"));
		userTransAcct.setProc_src(datadriven.get("proc_src"));
		if (datadriven.get("pwd_pay").length() != 0) {
			userTransAcct.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			userTransAcct.setPwd_pay(datadriven.get("pwd_pay"));
		}		
		userTransAcct.setName_goods(datadriven.get("name_goods"));
		userTransAcct.setShareing_data(datadriven.get("shareing_data"));
		userTransAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userTransAcct)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userTransAcct);
		String userTransAcctUrl = Property.get("llwallet.userTransAcct.url");

		// 钱包余额转账请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userTransAcctUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包余额转账请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包余额转账接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "钱包余额转账-二维码", timeOut = 60000, dataProvider = "userTransAcct1")
	public void userTransAcct1(Map<String, String> datadriven) throws Exception {

		// 清除缓存
//		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
//		StringBuffer command = new StringBuffer();
//		if (datadriven.get("result_code").contains("7701")){
//			command.append("flush_all");
//			telnetUtil.execute(command.toString());
//		}
//		telnetUtil.disconnect();
		
		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		UserTransAcct userTransAcct = new UserTransAcct();
		userTransAcct.setOid_partner(datadriven.get("oid_partner"));
		userTransAcct.setSign_type(datadriven.get("sign_type"));
		userTransAcct.setBusi_partner(datadriven.get("busi_partner"));
		if ("auto".equals(datadriven.get("no_order"))){
			userTransAcct.setNo_order(no_order);
		}else{
			userTransAcct.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			userTransAcct.setDt_order(dt_order);
		}else{
			userTransAcct.setDt_order(datadriven.get("dt_order"));
		}
		
		userTransAcct.setPay_userid(datadriven.get("pay_userid"));
		userTransAcct.setCol_userid(datadriven.get("col_userid"));
		userTransAcct.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		userTransAcct.setRisk_item(datadriven.get("risk_item"));
		userTransAcct.setInfo_order(datadriven.get("info_order"));
		userTransAcct.setProc_src(datadriven.get("proc_src"));
		if (datadriven.get("pwd_pay").length() != 0) {
			userTransAcct.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			userTransAcct.setPwd_pay(datadriven.get("pwd_pay"));
		}		
		userTransAcct.setName_goods(datadriven.get("name_goods"));
		userTransAcct.setShareing_data(datadriven.get("shareing_data"));
		userTransAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userTransAcct)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userTransAcct);
		String userTransAcctUrl = Property.get("llwallet.userTransAcct.url");

		// 钱包余额转账请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userTransAcctUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包余额转账请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包余额转账接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@Test(description = "钱包余额转账-分账", timeOut = 60000, dataProvider = "userTransAcct2")
	public void userTransAcct2(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		
		UserTransAcct userTransAcct = new UserTransAcct();
		userTransAcct.setOid_partner(datadriven.get("oid_partner"));
		userTransAcct.setSign_type(datadriven.get("sign_type"));
		userTransAcct.setBusi_partner(datadriven.get("busi_partner"));
		if ("auto".equals(datadriven.get("no_order"))){
			userTransAcct.setNo_order(no_order);
		}else{
			userTransAcct.setNo_order(datadriven.get("no_order"));
		}
		if ("auto".equals(datadriven.get("dt_order"))){
			userTransAcct.setDt_order(dt_order);
		}else{
			userTransAcct.setDt_order(datadriven.get("dt_order"));
		}
		
		userTransAcct.setPay_userid(datadriven.get("pay_userid"));
		userTransAcct.setCol_userid(datadriven.get("col_userid"));
		userTransAcct.setMoney_order(Float.parseFloat(datadriven.get("money_order")));
		userTransAcct.setRisk_item(datadriven.get("risk_item"));
		userTransAcct.setInfo_order(datadriven.get("info_order"));
		userTransAcct.setProc_src(datadriven.get("proc_src"));
		if (datadriven.get("pwd_pay").length() != 0) {
			userTransAcct.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			userTransAcct.setPwd_pay(datadriven.get("pwd_pay"));
		}		
		userTransAcct.setName_goods(datadriven.get("name_goods"));
		userTransAcct.setShareing_data(datadriven.get("shareing_data"));
		userTransAcct.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userTransAcct)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userTransAcct);
		String userTransAcctUrl = Property.get("llwallet.userTransAcct.url");

		// 钱包余额转账请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userTransAcctUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包余额转账请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包余额转账接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "userTransAcct")
	public Iterator<Object[]> data4userTransAcct() throws IOException {
		return new ExcelProvider(this, "userTransAcct");
	}

	@DataProvider(name = "userTransAcct1")
	public Iterator<Object[]> data4userTransAcct1() throws IOException {
		return new ExcelProvider(this, "userTransAcct1");
	}
	
	@DataProvider(name = "userTransAcct2")
	public Iterator<Object[]> data4userTransAcct2() throws IOException {
		return new ExcelProvider(this, "userTransAcct2");
	}
	
	@AfterClass
	public void afterClass() {

	}

}
